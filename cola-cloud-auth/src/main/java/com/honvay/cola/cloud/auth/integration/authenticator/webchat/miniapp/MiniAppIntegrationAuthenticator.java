package com.honvay.cola.cloud.auth.integration.authenticator.webchat.miniapp;

import com.honvay.cola.cloud.auth.integration.IntegrationAuthentication;
import com.honvay.cola.cloud.auth.integration.authenticator.IntegrationAuthenticator;
import com.honvay.cola.cloud.uc.model.UserVO;

/**
 * 小程序集成认证
 * @author LIQIU
 * @date 2018-3-31
 **/
public class MiniAppIntegrationAuthenticator implements IntegrationAuthenticator {
    @Override
    public UserVO authenticate(IntegrationAuthentication integrationAuthentication) {
        return null;
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return false;
    }
}
