package com.honvay.cola.cloud.framework.security.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author LIQIU
 * @date 2018-4-14
 **/
@ConditionalOnBean(ResourceServerConfiguration.class)
public class ResourceServiceSecurityAccessConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    @Qualifier("securityAccessInterceptor")
    private FilterSecurityInterceptor securityAccessInterceptor;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(securityAccessInterceptor,FilterSecurityInterceptor.class);
    }

}
