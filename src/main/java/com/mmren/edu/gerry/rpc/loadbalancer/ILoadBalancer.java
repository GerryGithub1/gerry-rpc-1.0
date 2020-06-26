package com.mmren.edu.gerry.rpc.loadbalancer;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname ILoadBalancer
 * @Description TODO
 * @Date 2020-2-23 21:20
 * @Created by Gerry
 */
public interface ILoadBalancer {
    InetSocketAddress doSelect(List<String> serviceNodes, String serviceUrl);
}
