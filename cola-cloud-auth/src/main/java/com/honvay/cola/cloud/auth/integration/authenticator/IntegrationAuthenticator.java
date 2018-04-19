package com.honvay.cola.cloud.auth.integration.authenticator;

import com.honvay.cola.cloud.auth.integration.IntegrationAuthentication;
import com.honvay.cola.cloud.uc.model.SysUserDO;

/**
 * @author LIQIU
 * @date 2018-3-31
 **/
public interface IntegrationAuthenticator {

    /**
     * 处理集成认证
     * @param integrationAuthentication
     * @return
     */
    SysUserDO authenticate(IntegrationAuthentication integrationAuthentication);


    /**
     * 进行预处理
     * @param integrationAuthentication
     */
    void prepare(IntegrationAuthentication integrationAuthentication);

     /**
     * 判断是否支持集成认证类型
     * @param integrationAuthentication
     * @return
     */
    boolean support(IntegrationAuthentication integrationAuthentication);

    /** 认证结束后执行
     * @param integrationAuthentication
     */
    void complete(IntegrationAuthentication integrationAuthentication);

}
