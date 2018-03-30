package com.honvay.cola.cloud.uc.password.validator;


import com.honvay.cola.cloud.uc.password.PasswordValidateResult;

/**
 * 密码校验器
 * @author LIQIU
 * @date 2018-1-10
 **/
public interface PasswordValidator {

    /**
     * 校验密码
     * @param password
     * @return
     */
    PasswordValidateResult validate(String password);
}
