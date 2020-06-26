package com.mmren.edu.gerry.rpc.client;

import com.mmren.edu.gerry.rpc.data.RequestParameter;
import com.mmren.edu.gerry.rpc.exception.GerryRpcException;
import com.mmren.edu.gerry.rpc.loadbalancer.RandomLoadBalancer;
import com.mmren.edu.gerry.rpc.util.ZKClient;
import com.mmren.edu.gerry.rpc.util.ZKConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname RPCClient
 * @Description RPC框架Client端实现类
 * @Date 2020-2-19 15:43
 * @Created by Gerry
 */
public class RPCClient implements ZKConfig {

    private static List<String> serviceNodes = new CopyOnWriteArrayList<>();

    /**
     * 获取远程代理的对象
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getRemoteProxy(Class<T> cls, String serviceName) {
        return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 获取连接IP和Port， 可以在服务发现组件中获取注册服务列表并使用负载均衡算法获取其中一个服务的IP和Port【类似于Ribbon】
                InetSocketAddress address = chooseService(serviceName);
                // 建立与服务端连接【BIO】
                Socket client = new Socket();
                try {
                    // 连接服务端
                    client.connect(address);
                    try(
                            ObjectOutputStream serializer = new ObjectOutputStream(client.getOutputStream());
                            ObjectInputStream deSerializer = new ObjectInputStream(client.getInputStream())

                    ) {
                        // 构建一个数据请求对象
                        RequestParameter requestParameter = new RequestParameter();
                        requestParameter.setInterfaceName(cls.getName());
                        requestParameter.setMethodName(method.getName());
                        requestParameter.setParameterTypes(method.getParameterTypes());
                        requestParameter.setParameterValues(args);
                        // 将其封装数据对象进行序列化
                        serializer.writeObject(requestParameter);

                        // 反序列化服务处理结果
                        return deSerializer.readObject();
                    } catch (Exception e) {
                        throw new GerryRpcException("客户和服务数据交换出异常");
                    }
                } catch (Exception e) {
                    throw new GerryRpcException("客户端与服务端建立连接失败");
                }
            }
        });
    }


    /**
     * 根据提供服务名称在服务注册中心获取注册服务列表并使用配置负载均衡算法获取其中一台服务进行处理客户端请求
     * @param serviceName
     * @return
     */
    private static InetSocketAddress chooseService(String serviceName) {
        try {
            CuratorFramework zkClient = ZKClient.getZkClient();
            String serviceUrl = "/" + ROOT_NAME + "/" + serviceName;
            serviceNodes = zkClient.getChildren().forPath(serviceUrl);
            System.out.println("更新前的服务列表:"+serviceNodes);
            // 监听serviceName下面子节点是否有变化
            registryWatcher(serviceUrl, zkClient);


            return new RandomLoadBalancer().doSelect(serviceNodes, serviceUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 服务发现的逻辑
     * @param serviceUrl
     * @param zkClient
     */
    private static void registryWatcher(String serviceUrl, CuratorFramework zkClient) {
        try {
            PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, serviceUrl, true);
            PathChildrenCacheListener listener = new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                    if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED ||
                            pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                        // 如果子节点发生变化更新服务列表
                        serviceNodes = curatorFramework.getChildren().forPath(serviceUrl);
                        System.out.println("更新后的服务列表:"+serviceNodes);
                    }
                }
            };
            pathChildrenCache.getListenable().addListener(listener);
            pathChildrenCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
