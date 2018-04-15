package com.honvay.cola.cloud.framework.security.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author LIQIU
 * @date 2018-4-10
 **/
public class SecurityAccessAutoConfiguration {

    @Autowired
    private CacheManager cacheManager;

    @Value("${spring.application.name:}")
    private String serviceId;

    @Bean("securityAccessMetadataSource")
    public FilterInvocationSecurityMetadataSource securityAccessMetadataSource() {
        SecurityAccessMetadataSource securityAccessMetadataSource = new SecurityAccessMetadataSource();
        securityAccessMetadataSource.setCacheManager(cacheManager);
        securityAccessMetadataSource.setServiceId(serviceId);
        return securityAccessMetadataSource;
    }

    @Bean
    public FilterSecurityInterceptor securityAccessInterceptor() {
        FilterSecurityInterceptor securityInterceptor = new FilterSecurityInterceptor();
        securityInterceptor.setAccessDecisionManager(new SecurityAccessDecisionManager());
        securityInterceptor.setSecurityMetadataSource(this.securityAccessMetadataSource());
        return securityInterceptor;
    }

    @Configuration
    @ConditionalOnClass(ResourceServerConfiguration.class)
    @ConditionalOnBean(ResourceServerConfiguration.class)
    private class ResourceServerSecurityAccessConfiguration extends ResourceServerConfigurerAdapter {

        public ResourceServerSecurityAccessConfiguration() {

        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            //排除Swagger文档
            http.authorizeRequests().antMatchers("/v2/api-docs").permitAll().and().csrf().disable()
                    .authorizeRequests().anyRequest().authenticated().filterSecurityInterceptorOncePerRequest(false)
                    .and().addFilterAfter(securityAccessInterceptor(), FilterSecurityInterceptor.class);
        }

    }

    @Configuration
    @ConditionalOnClass(WebSecurityConfiguration.class)
    @ConditionalOnMissingBean(ResourceServerConfiguration.class)
    @ConditionalOnBean(WebSecurityConfiguration.class)
    private class WebSecurityAccessConfiguration extends WebSecurityConfigurerAdapter {

        public WebSecurityAccessConfiguration() {

        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //排除Swagger文档
            http.authorizeRequests().antMatchers("/v2/api-docs").permitAll().and().csrf().disable()
                    .authorizeRequests().anyRequest().authenticated().filterSecurityInterceptorOncePerRequest(false)
                    .and().addFilterAfter(securityAccessInterceptor(), FilterSecurityInterceptor.class);
        }
    }

}
