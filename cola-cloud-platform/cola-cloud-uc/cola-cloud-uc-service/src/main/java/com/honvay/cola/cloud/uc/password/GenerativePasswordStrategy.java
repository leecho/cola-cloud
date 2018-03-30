package com.honvay.cola.cloud.uc.password;

import org.springframework.util.Assert;

/**
 * 密码可生成的密码策略
 * @author LIQIU
 * @date 2018-1-10
 **/
public class GenerativePasswordStrategy extends AbstractValidatorPasswordStrategy{

    /**
     * 密码生成器
     */
    private PasswordGenerator passwordGenerator;

    @Override
    public String getPassword() {
        throw new UnsupportedOperationException("不支持的操作，请传递参数生成密码");
    }

    @Override
    public String getPassword(Object param) {
        Assert.isTrue(passwordGenerator != null,"密码生成器没有配置");
        return passwordGenerator.generate(param);
    }

    public PasswordGenerator getPasswordGenerator() {
        return passwordGenerator;
    }

    public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }
}
