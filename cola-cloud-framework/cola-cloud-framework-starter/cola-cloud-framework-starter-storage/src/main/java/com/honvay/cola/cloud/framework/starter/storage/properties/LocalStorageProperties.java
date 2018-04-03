package com.honvay.cola.cloud.framework.starter.storage.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 本地存储属性配置
 *
 * @author LIQIU
 * @date 2017-12-19
 */
@ConfigurationProperties(prefix = "cola.storage.local" )
public class LocalStorageProperties {
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
