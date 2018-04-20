package com.honvay.cola.cloud.organization;

import com.honvay.cola.cloud.framework.oauth2.feign.EnableOAuth2ClientFeign;
import com.honvay.cola.cloud.framework.security.access.EnableSecurityAccess;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author LIQIU
 * @date 2018-3-8
 **/
@EnableOAuth2Client
@EnableOAuth2ClientFeign
@EnableResourceServer
@EnableFeignClients("com.honvay")
@ComponentScan("com.honvay")
@EnableCaching
@EnableSecurityAccess
@SpringCloudApplication
public class OrganizationApplication  extends ResourceServerConfigurerAdapter {

    public static void main(String[] args){
        SpringApplication.run(OrganizationApplication.class, args);

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v2/api-docs").permitAll().and().authorizeRequests().anyRequest().authenticated();
    }
}
