package com.honvay.cola.cloud.auth.sms;

import com.honvay.cola.cloud.framework.security.userdetail.User;
import com.honvay.cola.cloud.framework.util.BeanUtils;
import com.honvay.cola.cloud.uc.client.UcClient;
import com.honvay.cola.cloud.uc.model.UserVO;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author lengleng
 * @date 2018/1/9
 * 手机号登录校验逻辑
 */
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private UcClient ucClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken mobileAuthenticationToken = (SmsAuthenticationToken) authentication;
        UserVO userVO = ucClient.findUserByPhoneNumber((String) mobileAuthenticationToken.getPrincipal());
        if(userVO == null){
            throw new UsernameNotFoundException("手机号不存在:" + mobileAuthenticationToken.getPrincipal());

        }
        User user = new User();
        BeanUtils.copyProperties(userVO,user);
        SmsAuthenticationToken authenticationToken = new SmsAuthenticationToken(user, user.getAuthorities());
        authenticationToken.setDetails(mobileAuthenticationToken.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UcClient getUcClient() {
        return ucClient;
    }

    public void setUcClient(UcClient ucClient) {
        this.ucClient = ucClient;
    }
}
