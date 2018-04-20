package com.honvay.cola.cloud.auth;

import com.honvay.cola.cloud.framework.oauth2.feign.EnableOAuth2ClientFeign;
import com.honvay.cola.cloud.framework.security.access.EnableSecurityAccess;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/** 
 * @author LIQIU
 */
@SpringCloudApplication
@EnableResourceServer
@EnableCaching
@EnableOAuth2Client
@EnableOAuth2ClientFeign
@EnableFeignClients("com.honvay")
@ComponentScan("com.honvay")
@EnableSecurityAccess
public class AuthApplication    {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
