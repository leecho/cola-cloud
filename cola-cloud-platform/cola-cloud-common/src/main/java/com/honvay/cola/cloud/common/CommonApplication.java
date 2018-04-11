package com.honvay.cola.cloud.common;

import com.honvay.cola.cloud.framework.security.access.EnableSecurityAccess;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author LIQIU
 * @date 2018-3-13
 **/
@Configuration
@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer
@EnableOAuth2Client
@ComponentScan(basePackages = {"com.honvay.cola"})
@MapperScan(basePackages = { "com.honvay.cola.**.mapper" })
@EnableCircuitBreaker
@EnableSecurityAccess
public class CommonApplication extends ResourceServerConfigurerAdapter{

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //TODO 需要加权限校验
        /*http.authorizeRequests()
                .antMatchers("/v2/api-docs").permitAll()
                .and().authorizeRequests().anyRequest().authenticated();*/
        http.authorizeRequests().anyRequest().permitAll();
    }

    public static  void main(String[] args){
        SpringApplication.run(CommonApplication.class,args);
    }
}
