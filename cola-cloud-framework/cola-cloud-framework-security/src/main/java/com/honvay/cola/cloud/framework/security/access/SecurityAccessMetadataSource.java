package com.honvay.cola.cloud.framework.security.access;

import com.honvay.cola.cloud.upm.constant.ResourceCacheConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 安全访问的数据源
 * @author LIQIU
 * @date 2018-4-10
 **/
public class SecurityAccessMetadataSource implements FilterInvocationSecurityMetadataSource {

    private Map<String, Collection<ConfigAttribute>> metadata;

    private CacheManager cacheManager;

    private String serviceId;

    public void loadUrlRoleMapping() {
        if(metadata != null){
            metadata.clear();
        }else{
            this.metadata = new HashMap<>();
        }
        //从缓存中获取数据
        Cache cache = cacheManager.getCache(ResourceCacheConstant.URL_ROLE_MAPPING_CACHE);
        Cache.ValueWrapper valueWrapper = cache.get(serviceId);
        Map<String, Set<String>> urlRoleMapping = null;
        if (valueWrapper != null) {
            urlRoleMapping = (Map<String, Set<String>>) valueWrapper.get();
        }
        //组装SpringSecurrty的数据
        if (urlRoleMapping != null) {
            for (Map.Entry<String, Set<String>> entry : urlRoleMapping.entrySet()) {
                Set<String> roleCodes = entry.getValue();
                Collection<ConfigAttribute> configs = CollectionUtils.collect(roleCodes.iterator(), input -> new SecurityConfig(input));
                this.metadata.put(entry.getKey(), configs);
            }
        }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        /**
         * TODO 目前这里的实现有问题，存在缓存一致性和实时性的的问题，需要处理
         * 解决方案有以下3种：
         * 1、实时去Redis获取数据，这样可以保证一致性和实时性，但是这样会造成频繁的访问Reids，Redis容易成为瓶颈
         * 2、使用第三方二级缓存框架来进行本地缓存，目前正在评估哪个二级缓存可以使用，二级缓存也要解决缓存一致性和实时性的问题
         * 3、使用消息队列异步通知，资源的缓存刷新之后，通知各个节点刷新本地缓存。这样可以保证缓存的最终一致性，但是这样也会造成系统的复杂度会上升
         * */

        if(this.metadata == null){
            loadUrlRoleMapping();
        }
        //object 中包含用户请求的request 信息
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        AntPathRequestMatcher matcher;
        String resUrl;
        for (Map.Entry<String, Collection<ConfigAttribute>> entry : metadata.entrySet()) {
            matcher = new AntPathRequestMatcher(entry.getKey());
            if (matcher.matches(request)) {
                return metadata.get(entry.getValue());
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
