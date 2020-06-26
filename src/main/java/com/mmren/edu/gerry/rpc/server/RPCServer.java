package com.mmren.edu.gerry.rpc.server;

import com.mmren.edu.gerry.rpc.data.RequestParameter;
import com.mmren.edu.gerry.rpc.exception.GerryRpcException;
import com.mmren.edu.gerry.rpc.registry.ServiceRegistry;
import com.mmren.edu.gerry.rpc.util.AnnotationUtil;
import com.mmren.edu.gerry.rpc.util.IPUtil;
import com.mmren.edu.gerry.rpc.util.PathUtil;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname RPCServer
 * @Description RPC服务端核心实现类
 * @Date 2020-2-19 16:01
 * @Created by Gerry
 */
public class RPCServer {
    // 创建存储发布服务的Map集合
    private Map<String, Object> serverMap = new ConcurrentHashMap<>(32);
    // 获取根路径
    private final URL rootPath = this.getClass().getClassLoader().getResource("");

    // 创建一个线程池对象
    private final ThreadPoolExecutor executor =
            new ThreadPoolExecutor(8,20,200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));

    public RPCServer() {
        // 注册暴露服务信息到服务列表中
        scanPublishService(rootPath.getFile());
    }

    /**
     * 注册暴露服务的方法
     * @param path
     */
    private void scanPublishService(String path) {
        File file = new File(path);

        try {
            File[] files = file.listFiles();
            for (File childFile: files) {
                if (childFile.isDirectory()) {
                    scanPublishService(path.concat("/")+childFile.getName());
                } else {
                    // 获取文件名称
                    String fileName = childFile.getName();
                    if (fileName.endsWith(".class")) {
                        String classPathString = PathUtil.obtainClassPathString(childFile);
                        // 根据类路径创建一个类对象
                        Class<?> loadClass = this.getClass().getClassLoader().loadClass(classPathString);
                        if (AnnotationUtil.hasAnnotation(loadClass)) {
                            // 获取到当前类上面实现接口
                            Class<?>[] interfaces = loadClass.getInterfaces();
                            if (interfaces.length == 0) {
                                continue;
                            }
                            // 完成服务注解
                            publishService(interfaces[0],loadClass.newInstance());
                            System.out.println("====>"+interfaces[0]+"," +loadClass.newInstance());
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布服务的方法
     * @param cls 需要发布服务的API对应Class
     * @param instance API对应实现类的实例
     */
    public void publishService(Class<?> cls, Object instance) {
        this.serverMap.put(cls.getName(), instance);
    }

    /**
     * 启动服务的方法
     * @param port
     */
    public void start(int port) {
        // 注册发布的服务到注册中心
        registryService(port);
        try {
            // 创建网络服务端
            ServerSocket server = new ServerSocket();
            // 绑定端口
            server.bind(new InetSocketAddress(port));
            System.out.println("服务启动成功，端口号为："+port);
            while (true) {
                // 每个请求过来我们都会开启一个线程来处理
                executor.submit(new ServerTask(server.accept()));
            }
        } catch (IOException e) {
            throw new GerryRpcException("创建服务端失败");
        }
    }

    /**
     * 把启动服务注册到注册中心
     * @param port
     */
    private void registryService(int port) {
        // 本机IP地址
        String host = IPUtil.getLocalHost();
        InetSocketAddress address = new InetSocketAddress(host, port);
        // 调用注册组件的注册服务的方法
        new ServiceRegistry().registryService(address);
    }

    /**
     * 创建一个连接请求出来线程类
     */
    private class ServerTask implements Runnable {

        private final Socket client;

        public ServerTask(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try (
                    ObjectInputStream deSerializer = new ObjectInputStream(client.getInputStream());
                    ObjectOutputStream serializer = new ObjectOutputStream(client.getOutputStream())
                    ){
                // 反序列化获取客户端传入数据
                RequestParameter requestParameter = (RequestParameter) deSerializer.readObject();
                // 获取到客户端调用API对应实现类的实例
                Object instance = RPCServer.this.serverMap.get(requestParameter.getInterfaceName());
                // 通过反射创建Method对象
                Method method = instance.getClass()
                        .getDeclaredMethod(requestParameter.getMethodName(), requestParameter.getParameterTypes());
                // 反射调用目标对象中对应方法
                Object retVal = method.invoke(instance, requestParameter.getParameterValues());
                // 序列化化结果到网络中
                serializer.writeObject(retVal);
            } catch (Exception e) {
                throw new GerryRpcException("服务端处理客户端连接请求出现异常");
            }
        }
    }
}
