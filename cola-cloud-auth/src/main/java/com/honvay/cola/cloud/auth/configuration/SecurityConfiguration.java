package com.honvay.cola.cloud.auth.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author LIQIU
 * @date 2018-4-12
 **/
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .and().logout().permitAll()
                .and().formLogin().permitAll()
                .and().authorizeRequests().anyRequest().authenticated();
    }
}
