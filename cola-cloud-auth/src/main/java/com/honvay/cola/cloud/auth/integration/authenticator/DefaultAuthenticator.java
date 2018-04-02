package com.honvay.cola.cloud.auth.integration.authenticator;

import com.honvay.cola.cloud.auth.integration.IntegrationAuthentication;
import com.honvay.cola.cloud.framework.util.StringUtils;
import com.honvay.cola.cloud.uc.client.UcClient;
import com.honvay.cola.cloud.uc.model.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 默认登录处理
 * @author LIQIU
 * @date 2018-3-31
 **/
@Component
@Primary
public class DefaultAuthenticator implements IntegrationAuthenticator {

    @Autowired
    private UcClient ucClient;

    @Override
    public UserVO authenticate(IntegrationAuthentication integrationAuthentication) {
        return ucClient.findUserByUsername(integrationAuthentication.getUsername());
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {

    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return StringUtils.isEmpty(integrationAuthentication.getAuthType());
    }
}
