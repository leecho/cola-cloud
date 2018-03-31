package com.honvay.cola.cloud.duplication.repository;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.UUID;

/**
 * 房重复提交标识的存储(通过实现此接口拓展不同的存储方式,可存入redis可存入session等等)
 *
 * @author LIQIU
 * @date 2017-12-06-下午3:52
 */
public class DuplicationVerifyTokenRepository {

    public static final String DUPLICATION_TOKEN_CACHE = "duplicationTokenCache";


    private CacheManager cacheManager;

    /**
     * 判断是否存在防重复提交的标志码
     *
     * @return true 存在
     * @return false 不存在
     */
    public boolean exists(String key){
        Cache cache = cacheManager.getCache(DUPLICATION_TOKEN_CACHE);
        Cache.ValueWrapper valueWrapper = cache.get(key);
        return valueWrapper != null && valueWrapper.get() != null;
    }

    /**
     * 创建keyFlag并存入仓库
     */
    public String push(){
        String key = UUID.randomUUID().toString();
        cacheManager.getCache(DUPLICATION_TOKEN_CACHE).put(key,key);
        return key;
    }

    /**
     * 删除key
     */
    public void delete(String key){
        this.cacheManager.getCache(DUPLICATION_TOKEN_CACHE).evict(key);
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
