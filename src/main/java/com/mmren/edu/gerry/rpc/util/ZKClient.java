package com.mmren.edu.gerry.rpc.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname ZKClient
 * @Description 连接ZK的工具类
 * @Date 2020-2-23 20:31
 * @Created by Gerry
 */
public class ZKClient implements ZKConfig {

    private static CuratorFramework curatorFramework;

    static {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STRING)
                .sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();
    }

    public static CuratorFramework getZkClient() {
        return curatorFramework;
    }
}
