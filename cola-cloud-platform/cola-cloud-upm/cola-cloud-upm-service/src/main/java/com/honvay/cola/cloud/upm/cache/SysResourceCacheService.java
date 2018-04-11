package com.honvay.cola.cloud.upm.cache;

import com.google.common.collect.Maps;
import com.honvay.cola.cloud.framework.util.StringUtils;
import com.honvay.cola.cloud.upm.constant.ResourceCacheConstant;
import com.honvay.cola.cloud.upm.entity.SysAuthority;
import com.honvay.cola.cloud.upm.entity.SysResource;
import com.honvay.cola.cloud.upm.entity.SysRole;
import com.honvay.cola.cloud.upm.event.SysResourceCacheRefreshEvent;
import com.honvay.cola.cloud.upm.mapper.SysAuthorityMapper;
import com.honvay.cola.cloud.upm.mapper.SysResourceMapper;
import com.honvay.cola.cloud.upm.mapper.SysRoleMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 平台资源权限配置
 *
 * @author LIQIU
 * @date 2018-1-8
 **/
@Component
@EnableCaching
@CacheConfig(cacheNames = {
        ResourceCacheConstant.RESOURCE_HIERARCHY_CACHE,
        ResourceCacheConstant.URL_ROLE_MAPPING_CACHE})
public class SysResourceCacheService implements InitializingBean,ApplicationEventPublisherAware {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Autowired
    private SysAuthorityMapper sysAuthorityMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private CacheManager cacheManager;

    private ApplicationEventPublisher applicationEventPublisher ;

    /**
     * 刷新缓存次数
     */
    private int cacheCount = 0;

    /**
     * 资源路径对应角色列表Map，归类到ServiceId下
     */
    private Map<String,Map<String, Set<String>>> urlRoleMappingMap = new HashMap<>();

    /**
     * 将数据库的数据放在缓存中，用于计算
     */
    public  void  cacheAll() {
        List<SysResource> resources = sysResourceMapper.selectList(null);
        List<SysAuthority> authorities = sysAuthorityMapper.selectList(null);
        List<SysRole> roles = sysRoleMapper.selectList(null);

        Map<Long, List<SysAuthority>> resourceAuthorityMap = new HashMap<>(resources.size());

        //将权限数据进行分解为资源对应权限Map和角色对应权限Map
        for (SysAuthority sysAuthority : authorities) {
            List<SysAuthority> resourceAuthorities = resourceAuthorityMap.computeIfAbsent(sysAuthority.getSysResourceId(), k -> new ArrayList<>());
            resourceAuthorities.add(sysAuthority);
        }

        Map<Long, SysResource> resourceMap = new HashMap<>(resources.size());
        for (SysResource resource : resources) {
            resourceMap.put(resource.getId(), resource);
        }

        Map<Long, SysRole> roleMap = new HashMap<>(resources.size());
        for (SysRole role : roles) {
            roleMap.put(role.getId(), role);
        }

        this.buildResourceRoleCache(resourceAuthorityMap,resourceMap,roleMap);
    }

    /**
     * 构建缓存，并生成资源对应路径的角色列表Map
     * @param resourceAuthorityMap
     * @param resourceMap
     * @param roleMap
     */
    private void buildResourceRoleCache(Map<Long, List<SysAuthority>> resourceAuthorityMap, Map<Long, SysResource> resourceMap, Map<Long, SysRole> roleMap) {
        //资源层级Map
        Map<Long,List<SysResource>> resourceHierarchyMap = Maps.newHashMap();
        for (Map.Entry<Long, SysResource> entry : resourceMap.entrySet()) {
            SysResource resource = entry.getValue();
            //通过系统资源ID获取权限列表
            List<SysAuthority> authorities = resourceAuthorityMap.get(resource.getId());
            if (authorities != null) {
                //角色代码列表
                Set<String> urlMappedRoles = new HashSet<>();
                for (SysAuthority authority : authorities) {
                    //获取角色
                    SysRole role = roleMap.get(authority.getSysRoleId());
                    //如果没有角色，则退出
                    if (role == null) {
                        continue;
                    }
                    //将角色添加到资源对应的角色列表中
                    urlMappedRoles.add(role.getCode());
                }

                //如果有URL则添加Path对应的角色列表
                if (StringUtils.isNotEmpty(resource.getUrl())) {
                    Map<String, Set<String>> urlRoleMapping = urlRoleMappingMap.computeIfAbsent(resource.getServiceId(), k -> new HashMap<>());
                    urlRoleMapping.put(resource.getUrl(), urlMappedRoles);
                }
            }
            //将子节点放入到父节拥有的子节点集合中
            Long pid = resource.getPid() != null ? resource.getPid() : -1L;
            List<SysResource> children = resourceHierarchyMap.computeIfAbsent(pid, k -> new ArrayList<>());
            children.add(resource);
        }

        //将资源层级放入缓存
        Cache cache = cacheManager.getCache(ResourceCacheConstant.RESOURCE_HIERARCHY_CACHE);
        //清楚缓存
        cache.clear();
        for(Map.Entry<Long,List<SysResource>> entry : resourceHierarchyMap.entrySet()){
            cache.put(entry.getKey(),entry.getValue());
        }

        //路径角色权限放入缓存
        Cache urlRoleMappingCache = cacheManager.getCache(ResourceCacheConstant.URL_ROLE_MAPPING_CACHE);
        for (Map.Entry<String, Map<String, Set<String>>> entry : urlRoleMappingMap.entrySet()) {
            urlRoleMappingCache.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取缓存中的子资源
     * @param id 父ID
     * @return
     */
    public List<SysResource> getChildrenCacheByResourceId(Long id){
        Cache cache = cacheManager.getCache(ResourceCacheConstant.RESOURCE_HIERARCHY_CACHE);
        if(cache != null){
            Cache.ValueWrapper value = cache.get(id);
            if(value != null && value.get() != null){
                return (List<SysResource>)value.get();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void afterPropertiesSet() {
        this.doCacheAll();
    }

    /**
     * 加载系统缓存
     */
    public void doCacheAll(){
        logger.info("Start load Resource,Role,Authority Cache, CacheManager:" + this.cacheManager);
        this.cacheAll();
        cacheCount ++;
        if(cacheCount > 1){
            this.applicationEventPublisher.publishEvent(new SysResourceCacheRefreshEvent(this));
        }
        logger.info("Cache load finished");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
