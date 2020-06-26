package com.mmren.edu.gerry.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname GRPC
 * @Description 标注需要暴露服务的具体实现类
 * @Date 2020-2-20 20:33
 * @Created by Gerry
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GRPC {
}
