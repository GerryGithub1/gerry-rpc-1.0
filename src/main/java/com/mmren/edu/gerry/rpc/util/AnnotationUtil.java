package com.mmren.edu.gerry.rpc.util;

import com.mmren.edu.gerry.rpc.annotation.GRPC;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname AnnotationUtil
 * @Description TODO
 * @Date 2020-2-20 20:53
 * @Created by Gerry
 */
public class AnnotationUtil {
    /**
     * 判断是否标注了@GRPC注解
     * @param cls
     * @return
     */
    public static boolean hasAnnotation(Class<?> cls) {
        return cls.isAnnotationPresent(GRPC.class);
    }
}
