package com.mmren.edu.gerry.rpc.util;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname ZKConfig
 * @Description zookeeper基本的配置信息常量
 * @Date 2020-2-23 20:32
 * @Created by Gerry
 */
public interface ZKConfig {
    String CONNECT_STRING = "127.0.0.1:2181";
    String ROOT_NAME = "services";
    String SERVICE_NAME = "app";
}
