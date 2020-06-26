package com.mmren.edu.gerry.rpc.exception;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname GerryRpcException
 * @Description RPC框架的自定义异常类
 * @Date 2020-2-19 15:52
 * @Created by Gerry
 */
public class GerryRpcException extends RuntimeException {
    public GerryRpcException() {
        super();
    }

    public GerryRpcException(String message) {
        super(message);
    }

    public GerryRpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public GerryRpcException(Throwable cause) {
        super(cause);
    }

    protected GerryRpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
