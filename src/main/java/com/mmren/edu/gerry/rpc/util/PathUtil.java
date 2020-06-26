package com.mmren.edu.gerry.rpc.util;

import java.io.File;

/**
 * 欢迎同学们来到牧码人教育
 *
 * @Classname PathUtil
 * @Description TODO
 * @Date 2020-2-20 20:45
 * @Created by Gerry
 */
public class PathUtil {
    /**
     * 根据文件路径转换为一个类的路径
     * @param file
     * @return
     */
    public static String obtainClassPathString(File file) {
        // 获取文件的绝对路径
        String absolutePath = file.getAbsolutePath();
        // 获取classes字符串位置
        int index = absolutePath.lastIndexOf("classes")+"classes".length() + 1;
        String substring = absolutePath.substring(index);
        String classPath = substring.replace("\\", ".").replaceAll("\\.class", "");

        return classPath;
    }
}
