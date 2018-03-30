package com.honvay.cola.cloud.vcc.cache;

/**
 * @author LIQIU
 * @date 2018-3-16
 **/
public interface VerificationCodeCache {

    /**
     * 设置值
     *
     * @param cacheName
     * @param key
     * @param value
     */
    void set(String cacheName, String key, String value, long expire);

    void remove(String cacheName, String key);

    /**
     * 获取值
     *
     * @param cacheName
     * @param key
     * @return
     */
    String get(String cacheName, String key);

    /**
     * 验证是否匹配
     * @param cacheName
     * @param key
     * @param value
     * @return
     */
    boolean validate(String cacheName, String key, String value);

    /**
     * 判断是否过期
     * @param cacheName
     * @param key
     * @return
     */
    boolean isExpire(String cacheName, String key);

}