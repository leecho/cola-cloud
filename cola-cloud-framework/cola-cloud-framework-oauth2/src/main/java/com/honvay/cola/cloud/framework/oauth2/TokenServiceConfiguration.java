package com.honvay.cola.cloud.framework.oauth2;

import com.honvay.cola.cloud.framework.oauth2.token.UserInfoTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author LIQIU
 * @date 2018-3-26
 **/
@Configuration
public class TokenServiceConfiguration {

    @Autowired
    private ResourceServerProperties sso;

    @Bean
    public ResourceServerTokenServices tokenServices() {
        return new UserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
    }
}
