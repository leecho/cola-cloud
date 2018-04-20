package com.honvay.cola.cloud.auth.integration.authenticator.sms;

import com.honvay.cola.cloud.auth.integration.IntegrationAuthentication;
import com.honvay.cola.cloud.auth.integration.authenticator.AbstractPreparableIntegrationAuthenticator;
import com.honvay.cola.cloud.auth.integration.authenticator.sms.event.SmsAuthenticateBeforeEvent;
import com.honvay.cola.cloud.auth.integration.authenticator.sms.event.SmsAuthenticateSuccessEvent;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.uc.client.SysUserClient;
import com.honvay.cola.cloud.uc.model.SysUserAuthentication;
import com.honvay.cola.cloud.vcc.client.VerificationCodeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

/**
 * 短信验证码集成认证
 *
 * @author LIQIU
 * @date 2018-3-31
 **/
@Component
public class SmsIntegrationAuthenticator extends AbstractPreparableIntegrationAuthenticator implements  ApplicationEventPublisherAware {

    @Autowired
    private SysUserClient sysUserClient;

    @Autowired
    private VerificationCodeClient verificationCodeClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ApplicationEventPublisher applicationEventPublisher;

    private final static String SMS_AUTH_TYPE = "sms";

    @Override
    public SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication) {

        //获取密码，实际值是验证码
        String password = integrationAuthentication.getAuthParameter("password");
        //获取用户名，实际值是手机号
        String username = integrationAuthentication.getUsername();
        //发布事件，可以监听事件进行自动注册用户
        this.applicationEventPublisher.publishEvent(new SmsAuthenticateBeforeEvent(integrationAuthentication));
        //通过手机号码查询用户
        SysUserAuthentication sysUserAuthentication = this.sysUserClient.findUserByPhoneNumber(username);
        if (sysUserAuthentication != null) {
            //将密码设置为验证码
            sysUserAuthentication.setPassword(passwordEncoder.encode(password));
            //发布事件，可以监听事件进行消息通知
            this.applicationEventPublisher.publishEvent(new SmsAuthenticateSuccessEvent(integrationAuthentication));
        }
        return sysUserAuthentication;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
        String smsToken = integrationAuthentication.getAuthParameter("sms_token");
        String smsCode = integrationAuthentication.getAuthParameter("password");
        String username = integrationAuthentication.getAuthParameter("username");
        Result<Boolean> result = verificationCodeClient.validate(smsToken, smsCode, username);
        if (!result.getData()) {
            throw new OAuth2Exception("验证码错误或已过期");
        }
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return SMS_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
