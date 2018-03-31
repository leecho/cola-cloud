package com.honvay.cola.framework.storage;

import java.io.InputStream;

/**
 * 文件上传接口
 *
 * @author LIQIU
 * @date 2017-12-14-上午10:45
 */
public interface FileStorage {

    /**
     * 上传文件
     *
     * @param fileBytes 文件的字节数组
     * @param key 文件名
     * @return 文件标识的唯一id
     * @author LIQIU
     * @Date 2017/12/14 上午11:28
     */
    void store(byte[] fileBytes, String key);

    /**
     * 存储输入流
     * @param input
     * @param key
     */
    void store(InputStream input,String key);

    /**
     * 下载文件
     *
     * @param key 文件名(带后缀名的)
     * @author LIQIU
     * @Date 2017/12/14 上午11:28
     */
    byte[] getBytes(String key);

    /**
     * 通过KEY删除文件
     * @param key
     */
    void remove(String key);

    /**
     * 过去输入流
     * @param key
     * @return
     */
    InputStream getInputStream(String key);

    /**
     * 获取下载链接
     * @param key
     * @return
     */
    String getDownloadUrl(String key);
}
