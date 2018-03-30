package com.honvay.cola.cloud.auth.config;

import com.honvay.cola.cloud.auth.sms.SmsAuthenticationSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author LIQIU
 * @date 2018-3-2
 **/
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Autowired
    private SmsAuthenticationSecurityConfigurer smsAuthenticationSecurityConfigurer;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v2/api-docs","/app/token","/sms/token").permitAll()
                .and().apply(smsAuthenticationSecurityConfigurer)
                .and().authorizeRequests().anyRequest().authenticated();
    }
}
