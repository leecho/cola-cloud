package com.honvay.cola.cloud.uc.password;

/**
 * 密码策略接口
 * @author LIQIU
 * @date 2018-1-10
 **/
public interface PasswordStrategy {
    /**
     * 校验密码
     * @param password
     * @return
     */
    PasswordValidateResult validate(String password);

    /**
     * 获取密码
     * @return
     */
    String getPassword();

    /**
     * 获取密码
     * @param param
     * @return
     */
    String getPassword(Object param);
}
