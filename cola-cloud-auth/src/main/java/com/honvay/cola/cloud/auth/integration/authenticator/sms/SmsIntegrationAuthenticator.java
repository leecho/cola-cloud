package com.honvay.cola.cloud.auth.integration.authenticator.sms;

import com.honvay.cola.cloud.auth.exception.BadVerificatioinCodeException;
import com.honvay.cola.cloud.auth.integration.IntegrationAuthentication;
import com.honvay.cola.cloud.auth.integration.authenticator.IntegrationAuthenticator;
import com.honvay.cola.cloud.auth.integration.authenticator.sms.event.SmsAuthenticateBeforeEvent;
import com.honvay.cola.cloud.auth.integration.authenticator.sms.event.SmsAuthenticateSuccessEvent;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.uc.client.UcClient;
import com.honvay.cola.cloud.uc.model.UserVO;
import com.honvay.cola.cloud.vcc.client.VccClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author LIQIU
 * @date 2018-3-31
 **/
@Component
public class SmsIntegrationAuthenticator implements IntegrationAuthenticator,ApplicationEventPublisherAware {

    @Autowired
    private UcClient ucClient;

    @Autowired
    private VccClient vccClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ApplicationEventPublisher applicationEventPublisher;

    private final static String SMS_AUTH_TYPE = "sms";

    @Override
    public UserVO authenticate(IntegrationAuthentication integrationAuthentication) {

      String smsToken = integrationAuthentication.getAuthParameter("sms_token");
      String smsCode = integrationAuthentication.getAuthParameter("password");
      String username = integrationAuthentication.getUsername();

      Result<Boolean> result = vccClient.validate(smsToken,smsCode,username);
        if(!result.getData()){
            throw new BadVerificatioinCodeException("验证码错误或已过期");
        }
        //发布事件，可以监听事件进行自动注册用户
        this.applicationEventPublisher.publishEvent(new SmsAuthenticateBeforeEvent(integrationAuthentication));
        //通过手机号码查询用户
        UserVO userVo = this.ucClient.findUserByPhoneNumber(username);
        if(userVo != null){
            //将密码设置为验证码
            userVo.setPassword(passwordEncoder.encode(smsCode));
            //发布事件，可以监听事件进行消息通知
            this.applicationEventPublisher.publishEvent(new SmsAuthenticateSuccessEvent(integrationAuthentication));
        }
        return userVo;
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
