package com.honvay.cola.cloud.auth.exception;

import com.honvay.cola.cloud.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * 异常转换器
 */
@Component
@Slf4j
public class WebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    public static final String BAD_MSG = "凭证错误";

    /**
     * @param e spring security内部异常
     * @return 经过处理的异常信息
     * @throws Exception 通用异常
     */
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.error("Grant Error : " + e);
        e.printStackTrace();
        OAuth2Exception oAuth2Exception;
        if (e instanceof InternalAuthenticationServiceException) {
            oAuth2Exception = new InvalidGrantException("系统内部错误", e);
        } else if(e instanceof UsernameNotFoundException){
            oAuth2Exception = new InvalidGrantException("用户名或密码错误", e);
        }else if(e instanceof BadClientCredentialsException){
            oAuth2Exception = new InvalidGrantException("用户名或密码错误", e);
        }  else{
            oAuth2Exception = new UnsupportedResponseTypeException("服务处理异常", e);
        }
        return super.translate(oAuth2Exception);
    }
}
