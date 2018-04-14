package com.honvay.cola.cloud.auth.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * @author LIQIU
 * @date 2018-3-2
 **/
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.anonymous().disable().csrf().disable()
            .authorizeRequests().antMatchers("/v2/api-docs", "/sms/token").permitAll()
            .and().logout().permitAll()
            .and().formLogin().permitAll()
            .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
        super.configure(http);
    }
}
