package com.honvay.cola.cloud.notification.service;

import com.honvay.cola.cloud.framework.oauth2.feign.EnableOAuth2ClientFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * 消息通知服务
 *
 * @author LIQIU
 */
@SpringCloudApplication
@EnableResourceServer
@EnableOAuth2Client
@EnableFeignClients("com.honvay.cola")
@EnableScheduling
@EnableSwagger2
@EnableOAuth2ClientFeign
@ComponentScan("com.honvay.cola")
public class MessageApplication extends ResourceServerConfigurerAdapter {


    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v2/api-docs", "/hystrix**").permitAll()
                .and().authorizeRequests()
                .anyRequest().authenticated();
    }
}
