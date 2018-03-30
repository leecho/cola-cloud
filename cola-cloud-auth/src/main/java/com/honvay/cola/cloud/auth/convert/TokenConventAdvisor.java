package com.honvay.cola.cloud.auth.convert;

import com.honvay.cola.cloud.framework.core.protocol.Result;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

/**
 * @author LIQIU
 * @date 2018-3-28
 **/
public class TokenConventAdvisor extends StaticMethodMatcherPointcutAdvisor {

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return aClass.equals(TokenEndpoint.class) && method.getName().equals("postAccessToken");
    }

    @PostConstruct
    public void init() {
        this.setAdvice((MethodInterceptor) methodInvocation -> {
            Object object = methodInvocation.proceed();
            if (object != null && object.getClass().equals(ResponseEntity.class)) {
                ResponseEntity<OAuth2AccessToken> entity = (ResponseEntity<OAuth2AccessToken>) object;
                OAuth2AccessToken accessToken = entity.getBody();
                Result result = Result.buildSuccess(accessToken);
                return new ResponseEntity<Result<OAuth2AccessToken>>(result, entity.getHeaders(), entity.getStatusCode());
            }
            return object;
        });
    }
}
