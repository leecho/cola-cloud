package com.honvay.cola.cloud.uc;

import com.honvay.cola.cloud.framework.oauth2.EnableCustomTokenService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
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
@SpringBootApplication
@EnableEurekaClient
@EnableOAuth2Client
@EnableCustomTokenService
@EnableResourceServer
@EnableFeignClients
@EnableCircuitBreaker
@ComponentScan("com.honvay")
@EnableCaching
public class UcApplication extends ResourceServerConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(UcApplication.class, args);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v2/api-docs").permitAll()
                .and().authorizeRequests().anyRequest().authenticated();
    }

}
