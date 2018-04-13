package com.honvay.cola.cloud.auth.configuration;

import com.honvay.cola.cloud.auth.exception.WebResponseExceptionTranslator;
import com.honvay.cola.cloud.auth.integration.IntegrationAuthenticationFilter;
import com.honvay.cola.cloud.auth.service.DatabaseCachableClientDetailsService;
import com.honvay.cola.cloud.auth.service.IntegrationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author LIQIU
 * @date 2018-3-7
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IntegrationUserDetailsService integrationUserDetailsService;

    @Autowired
    private WebResponseExceptionTranslator webResponseExceptionTranslator;

    @Autowired
    private IntegrationAuthenticationFilter integrationAuthenticationFilter;

    @Autowired
    private DatabaseCachableClientDetailsService redisClientDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // TODO persist clients details
        clients.withClientDetails(redisClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(new RedisTokenStore(redisConnectionFactory))
//                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                .exceptionTranslator(webResponseExceptionTranslator)
                .reuseRefreshTokens(false)
                .userDetailsService(integrationUserDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("permitAll()")
                .addTokenEndpointAuthenticationFilter(integrationAuthenticationFilter);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("cola-cloud");
        return jwtAccessTokenConverter;
    }
}
