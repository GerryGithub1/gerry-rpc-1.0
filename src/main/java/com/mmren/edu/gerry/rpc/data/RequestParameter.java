package com.mmren.edu.gerry.rpc.data;

import java.io.Serializable;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname RequestParameter
 * @Description RPC请求的通用数据模型
 * @Date 2020-2-19 15:40
 * @Created by Gerry
 */
public class RequestParameter implements Serializable {

    private String interfaceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameterValues;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }
}
