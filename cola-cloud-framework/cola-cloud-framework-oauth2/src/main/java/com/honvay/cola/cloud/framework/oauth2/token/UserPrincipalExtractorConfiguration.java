package com.honvay.cola.cloud.framework.oauth2.token;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LIQIU
 * @date 2018-3-26
 **/
@Configuration
@AutoConfigureBefore(ResourceServerTokenServicesConfiguration.class)
public class UserPrincipalExtractorConfiguration {

    @Bean
    public UserPrincipalExtractor userPrincipalExtractor(){
        return new UserPrincipalExtractor();
    }
}
