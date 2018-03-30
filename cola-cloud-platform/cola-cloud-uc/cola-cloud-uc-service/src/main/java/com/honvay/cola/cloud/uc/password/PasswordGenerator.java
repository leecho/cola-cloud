package com.honvay.cola.cloud.uc.password;

/**
 * @author LIQIU
 * @date 2018-1-10
 **/
public interface PasswordGenerator {

    /**
     * 生成密码
     * @param param
     * @return
     */
    String generate(Object param);
}
