package com.honvay.cola.cloud.framework.security.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author LIQIU
 * @date 2018-4-10
 **/
@Import({ResourceServiceSecurityAccessConfiguration.class,WebSecurityAccessConfiguration.class})
public class SecurityAccessAutoConfiguration{

    @Autowired
    private CacheManager cacheManager;

    @Value("${spring.application.name:}")
    private String serviceId;

    @Bean("securityAccessMetadataSource")
    public FilterInvocationSecurityMetadataSource securityAccessMetadataSource(){
        SecurityAccessMetadataSource securityAccessMetadataSource = new SecurityAccessMetadataSource();
        securityAccessMetadataSource.setCacheManager(cacheManager);
        securityAccessMetadataSource.setServiceId(serviceId);
        return securityAccessMetadataSource;
    }

    @Bean
    public FilterSecurityInterceptor securityAccessInterceptor(){
        FilterSecurityInterceptor securityInterceptor = new FilterSecurityInterceptor();
        securityInterceptor.setAccessDecisionManager(new SecurityAccessDecisionManager());
        securityInterceptor.setSecurityMetadataSource(this.securityAccessMetadataSource());
        return securityInterceptor;
    }
}
