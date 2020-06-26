package com.mmren.edu.gerry.rpc.loadbalancer;

import com.alibaba.fastjson.JSON;
import com.mmren.edu.gerry.rpc.util.ZKClient;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname RandomLoadBalancer
 * @Description 实现随机负载均衡算法
 * @Date 2020-2-23 21:21
 * @Created by Gerry
 */
public class RandomLoadBalancer implements ILoadBalancer {
    @Override
    public InetSocketAddress doSelect(List<String> serviceNodes, String serviceUrl) {
        try {
            String nodeString = serviceNodes.get(new Random().nextInt(serviceNodes.size()));
            String nodeUrl = serviceUrl + "/" + nodeString;
            String nodeValue = new String(ZKClient.getZkClient().getData().forPath(nodeUrl));
            System.out.println("选择的负载均衡的服务为："+nodeValue);

            return JSON.parseObject(nodeValue, InetSocketAddress.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
