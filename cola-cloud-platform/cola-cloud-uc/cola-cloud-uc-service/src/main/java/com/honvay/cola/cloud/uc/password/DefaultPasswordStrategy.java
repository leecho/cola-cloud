package com.honvay.cola.cloud.uc.password;

import lombok.ToString;

/**
 * 默认密码策略，密码为默认值
 * @author LIQIU
 * @date 2018-1-10
 **/
@ToString
public class DefaultPasswordStrategy extends AbstractValidatorPasswordStrategy{

    private String defaultPassword;

    public String getDefaultPassword() {
        return this.defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    @Override
    public String getPassword() {
        return this.defaultPassword;
    }

    @Override
    public String getPassword(Object param) {
        throw  new UnsupportedOperationException("不支持通过参数生成密码");
    }
}
