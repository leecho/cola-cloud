package com.honvay.cola.cloud.auth.integration.authenticator.webchat.miniapp;

import com.honvay.cola.cloud.auth.integration.IntegrationAuthentication;
import com.honvay.cola.cloud.auth.integration.authenticator.IntegrationAuthenticator;
import com.honvay.cola.cloud.uc.client.UcClient;
import com.honvay.cola.cloud.uc.model.UserVO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 小程序集成认证
 * @author LIQIU
 * @date 2018-3-31
 **/
public class MiniAppIntegrationAuthenticator implements IntegrationAuthenticator {

    @Autowired
    private UcClient ucClient;

    @Override
    public UserVO authenticate(IntegrationAuthentication integrationAuthentication) {
        return null;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
        
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return false;
    }

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication) {

    }
}
