package com.honvay.cola.cloud.framework.security.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author LIQIU
 * @date 2018-4-10
 **/
public class SecurityAccessAutoConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CacheManager cacheManager;

    @Value("${spring.application.name:}")
    private String serviceId;

    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource(){
        SecurityAccessMetadataSource securityMetadataSource = new SecurityAccessMetadataSource();
        securityMetadataSource.setCacheManager(cacheManager);
        securityMetadataSource.setServiceId(serviceId);
        return securityMetadataSource;
    }

    @Bean
    public FilterSecurityInterceptor securityInterceptor(){
        FilterSecurityInterceptor securityInterceptor = new FilterSecurityInterceptor();
        securityInterceptor.setAccessDecisionManager(new SecurityAccessDecisionManager());
        securityInterceptor.setSecurityMetadataSource(this.securityMetadataSource());
        return securityInterceptor;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(this.securityInterceptor(),FilterSecurityInterceptor.class);
    }
}
