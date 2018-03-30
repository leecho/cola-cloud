package com.honvay.cola.cloud.duplication.configuration;

import com.honvay.cola.cloud.duplication.repository.DuplicationVerifyTokenRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 防重复验证TOKEN缓存配置
 * @author LIQIU
 * @date 2017-12-28
 */
@Configuration
@EnableCaching
@CacheConfig(cacheNames = {DuplicationVerifyTokenRepository.DUPLICATION_TOKEN_CACHE})
public class DuplicationTokenCacheConfiguration {

    @Bean
    public DuplicationVerifyTokenRepository duplicationVerifyTokenRepository(CacheManager cacheManager){
        DuplicationVerifyTokenRepository repository = new DuplicationVerifyTokenRepository();
        repository.setCacheManager(cacheManager);
        return repository;
    }
}
