package com.mmren.edu.gerry.rpc.util;

import com.mmren.edu.gerry.rpc.exception.GerryRpcException;

import java.net.InetAddress;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname IPUtil
 * @Description 获取本机IP地址
 * @Date 2020-2-23 20:25
 * @Created by Gerry
 */
public class IPUtil {
    /**
     * 获取本机IP的方法
     * @return
     */
    public static String getLocalHost() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();

            return inetAddress.getHostAddress();
        } catch (Exception e) {
            throw new GerryRpcException("获取本机IP出现异常");
        }
    }
}
