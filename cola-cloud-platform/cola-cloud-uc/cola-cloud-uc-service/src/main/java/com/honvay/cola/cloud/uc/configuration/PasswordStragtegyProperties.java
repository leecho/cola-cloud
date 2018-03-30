package com.honvay.cola.cloud.uc.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 密码策略配置项
 * @author LIQIU
 * @date 2018-1-10
 **/
@ConfigurationProperties(prefix = "cola.password")
public class PasswordStragtegyProperties {

    private Integer minLength;

    private Integer maxLength;

    private Integer strength;

    private String defaultPassword;

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }
}
