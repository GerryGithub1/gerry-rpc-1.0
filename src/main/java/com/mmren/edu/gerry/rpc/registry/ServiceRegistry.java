package com.mmren.edu.gerry.rpc.registry;

import com.alibaba.fastjson.JSON;
import com.mmren.edu.gerry.rpc.util.ZKClient;
import com.mmren.edu.gerry.rpc.util.ZKConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname ServiceRegistry
 * @Description 实现组成服务的类
 * @Date 2020-2-23 20:24
 * @Created by Gerry
 */
public class ServiceRegistry implements ZKConfig {
    /**
     * 注册服务逻辑
     * @param address
     */
    public void registryService(InetSocketAddress address) {
        try {
            // 获取ZK操作对象
            CuratorFramework zkClient = ZKClient.getZkClient();
            // 拼接注册的rootUrl
            String rootUrl = "/" + ROOT_NAME + "/" + SERVICE_NAME;
            // 判断这个rootUrl是否已经存在
            if (null == zkClient.checkExists().forPath(rootUrl)) {
                // 在zk中创建rootUrl节点
                zkClient.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(rootUrl,"".getBytes());
            }

            String addressUrl = rootUrl + "/" + UUID.randomUUID();
            String serviceValue = JSON.toJSONString(address);
            // 在zk对应路径下面创建服务节点
            zkClient.create().withMode(CreateMode.EPHEMERAL).forPath(addressUrl, serviceValue.getBytes());
            System.out.println("服务注册成功 serviceNode:"+serviceValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
