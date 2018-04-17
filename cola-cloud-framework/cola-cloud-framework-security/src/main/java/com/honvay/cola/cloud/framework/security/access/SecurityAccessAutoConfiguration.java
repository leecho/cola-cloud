package com.honvay.cola.cloud.framework.security.access;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author LIQIU
 * @date 2018-4-10
 **/
@Slf4j
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
    @Conditional(EnableResourceServerCondition.class)
    private class ResourceServerSecurityAccessConfiguration extends ResourceServerConfigurerAdapter {

        public ResourceServerSecurityAccessConfiguration() {

        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            //排除Swagger文档
            http.authorizeRequests().antMatchers("/v2/api-docs").permitAll().and().csrf().disable()
                    .authorizeRequests().anyRequest().authenticated().filterSecurityInterceptorOncePerRequest(false)
                    .and().addFilterAfter(securityAccessInterceptor(), FilterSecurityInterceptor.class);
            log.info("Security Access Control is enabled on Resource Server Application");
        }

    }

    @Configuration
    @Conditional(EnableWebSecurityCondition.class)
    private class WebSecurityAccessConfiguration extends WebSecurityConfigurerAdapter {

        public WebSecurityAccessConfiguration() {

        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //排除Swagger文档
            http.authorizeRequests().antMatchers("/v2/api-docs").permitAll().and().csrf().disable()
                    .authorizeRequests().anyRequest().authenticated().filterSecurityInterceptorOncePerRequest(false)
                    .and().addFilterAfter(securityAccessInterceptor(), FilterSecurityInterceptor.class);
            log.info("Security Access Control is enabled on Web Application");
        }
    }

}
