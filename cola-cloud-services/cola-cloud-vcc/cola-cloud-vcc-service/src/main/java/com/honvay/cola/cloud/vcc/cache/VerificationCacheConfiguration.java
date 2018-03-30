package com.honvay.cola.cloud.vcc.cache;

import com.honvay.cola.cloud.vcc.cache.impl.GeneralVerificationCodeCache;
import com.honvay.cola.cloud.vcc.cache.impl.RedisVerificationCodeCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author LIQIU
 * @date 2018-3-16
 **/
public class VerificationCacheConfiguration {

    @Bean
    @ConditionalOnBean(RedisOperations.class)
    public RedisVerificationCodeCache redisVerificationCodeCache(StringRedisTemplate stringRedisTemplate){
        return new RedisVerificationCodeCache(stringRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(VerificationCodeCache.class)
    public GeneralVerificationCodeCache generalVerificationCodeCache(CacheManager cacheManager){
        return new GeneralVerificationCodeCache(cacheManager);
    }
}
