package com.honvay.cola.cloud.auth.integration.authenticator;

import com.honvay.cola.cloud.auth.integration.IntegrationAuthentication;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.uc.model.UserVO;
import com.honvay.cola.cloud.vcc.client.VccClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

/**
 * 集成验证码认证
 * @author LIQIU
 * @date 2018-3-31
 **/
@Component
public class VerificationCodeIntegrationAuthenticator extends DefaultAuthenticator implements IntegrationAuthenticator {

    private final static String VERIFICATION_CODE_AUTH_TYPE = "vc";

    @Autowired
    private VccClient vccClient;

    @Override
    public UserVO authenticate(IntegrationAuthentication integrationAuthentication) {

        String vcToken = integrationAuthentication.getAuthParameter("vc_token");
        String vcCode = integrationAuthentication.getAuthParameter("vc_code");
        //验证验证码
        Result<Boolean> result = vccClient.validate(vcToken, vcCode, null);
        if (!result.getData()) {
            throw new BadCredentialsException("验证码错误");
        }
        return super.authenticate(integrationAuthentication);
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return VERIFICATION_CODE_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }
}
