package com.honvay.cola.cloud.framework.util;

import java.net.URL;

/**
 * Classes工具类
 *
 * @author YRain
 * @createtime 2017-12-25
 */
public class ClasseUtils {

    /**
     * 获取类的绝对路径
     *
     * @param clazz 指定类
     * @return 指定类的绝对路径
     */
    public static String getClassPath(Class<?> clazz) {
        return getClassPath() + getClassRelativePath(clazz);
    }

    /**
     * 获取类的相对路径
     *
     * @param clazz 指定类
     * @return 指定类的相对路径
     */
    public static String getClassRelativePath(Class<?> clazz) {
        return clazz.getPackage().getName().replace(".", "/");
    }


    /**
     * 获取当前classpath路径
     *
     * @return 当前classpath路径
     */
    public static String getClassPath() {
        String classpath = "";
        URL resource = getURL("");
        if (resource != null) {
            classpath = resource.getPath();
        }
        return classpath;
    }


    /**
     * 获得资源URL
     *
     * @param resource:指定资源
     * @return 获得资源URL
     */
    public static URL getURL(String resource) {
        return getClassLoader().getResource(resource);
    }


    /**
     * 获取类加载器
     *
     * @return 当前类加载器
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    
}
