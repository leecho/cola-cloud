package com.honvay.cola.cloud.auth;

import com.honvay.cola.cloud.framework.oauth2.feign.EnableOAuth2ClientFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/** 
 * @author LIQIU
 */
@SpringCloudApplication
@EnableResourceServer
@EnableOAuth2Client
@EnableDiscoveryClient
@EnableOAuth2ClientFeign
@EnableFeignClients("com.honvay")
@ComponentScan("com.honvay")
public class AuthApplication    {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
