package com.honvay.cola.cloud.upm.cache;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.honvay.cola.cloud.framework.util.StringUtils;
import com.honvay.cola.cloud.upm.entity.SysAuthority;
import com.honvay.cola.cloud.upm.entity.SysResource;
import com.honvay.cola.cloud.upm.entity.SysRole;
import com.honvay.cola.cloud.upm.event.SysCacheRefreshEvent;
import com.honvay.cola.cloud.upm.mapper.SysAuthorityMapper;
import com.honvay.cola.cloud.upm.mapper.SysResourceMapper;
import com.honvay.cola.cloud.upm.mapper.SysRoleMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.DependsOn;
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
@CacheConfig(cacheNames = {UpmCacheService.RESOURCE_ROLE_CACHE,
        UpmCacheService.ROLE_RESOURCE_CACHE,
        UpmCacheService.RESOURCE_HIERARCHY_CACHE})
public class UpmCacheService implements InitializingBean,ApplicationEventPublisherAware {

    public static final String RESOURCE_ROLE_CACHE = "resourceRoleCache";
    public static final String ROLE_RESOURCE_CACHE = "roleResourceCache";
    public static final String RESOURCE_HIERARCHY_CACHE = "resourceHierarchyCache";
    public static final String RESOURCE_PATH_ROLE_CACHE = "resourcePathRoleCache";

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
     * 资源路径对应角色列表Map，用于Shiro认证
     */
    private Map<String, Set<String>> resourcePathRoleMap = new HashMap<>();

    /**
     * 获取资源对应角色列表缓存
     * @return
     */
    public Cache getResourceRoleCache(){
        return cacheManager.getCache(RESOURCE_ROLE_CACHE);
    }

    /**
     * 获取资源对应角色列表缓存
     * @return
     */
    public Cache getRoleResourceCache(){
        return cacheManager.getCache(ROLE_RESOURCE_CACHE);
    }

    /**
     * 将数据库的数据放在缓存中，用于计算
     */
    public  void  cacheAll() {
        List<SysResource> resources = sysResourceMapper.selectList(null);
        List<SysAuthority> authorities = sysAuthorityMapper.selectList(null);
        List<SysRole> roles = sysRoleMapper.selectList(null);

        Map<Long, List<SysAuthority>> resourceAuthorityMap = new HashMap<>();

        //将权限数据进行分解为资源对应权限Map和角色对应权限Map
        for (SysAuthority sysAuthority : authorities) {
            List<SysAuthority> resourceAuthorities = resourceAuthorityMap.get(sysAuthority.getSysResourceId());
            if (resourceAuthorities == null) {
                resourceAuthorities = new ArrayList<>();
                resourceAuthorityMap.put(sysAuthority.getSysResourceId(),resourceAuthorities);
            }
            resourceAuthorities.add(sysAuthority);
        }

        Map<Long, SysResource> resourceMap = new HashMap<>();
        for (SysResource resource : resources) {
            resourceMap.put(resource.getId(), resource);
        }

        Map<Long, SysRole> roleMap = new HashMap<>();
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
     * @return
     */
    private void buildResourceRoleCache(Map<Long, List<SysAuthority>> resourceAuthorityMap, Map<Long, SysResource> resourceMap, Map<Long, SysRole> roleMap) {

        Cache resourceRoleCache = cacheManager.getCache(RESOURCE_ROLE_CACHE);
        resourceRoleCache.clear();
        //角色所拥有的资源Map
        Map<String, Set<String>> roleResourceMap = new HashMap<>();
        //资源层级Map
        Map<Long,List<SysResource>> resourceHierarchyMap = Maps.newHashMap();

        for (Map.Entry<Long, SysResource> entry : resourceMap.entrySet()) {
            SysResource resource = entry.getValue();
            //通过系统资源ID获取权限列表
            List<SysAuthority> authorities = resourceAuthorityMap.get(resource.getId());
            if (authorities != null) {
                //角色代码列表
                Set<String> roleCodes = new HashSet<>();
                for (SysAuthority authority : authorities) {
                    //获取角色
                    SysRole role = roleMap.get(authority.getSysRoleId());
                    //如果没有角色，则退出
                    if (role == null) {
                        continue;
                    }
                    //将角色添加到资源对应的角色列表中
                    roleCodes.add(role.getCode());

                    //获取角色对应的资源列表
                    Set<String> resourceCodes = roleResourceMap.get(role.getCode());
                    if (resourceCodes == null) {
                        resourceCodes = new HashSet<>();
                        roleResourceMap.put(role.getCode(), resourceCodes);
                    }
                    //将资源添加到角色对应的资源列表中
                    resourceCodes.add(resource.getCode());
                }

                //如果有URL则添加Path对应的角色列表
                if (StringUtils.isNotEmpty(resource.getUrl())) {
                    resourcePathRoleMap.put(resource.getUrl(), roleCodes);
                }
                //资源对应角色将数据放入缓存
                resourceRoleCache.put(entry.getKey(), roleCodes);
            }
            //将子节点放入到父节拥有的子节点集合中
            Long pid = resource.getPid() != null ? resource.getPid() : -1L;
            List<SysResource> children = resourceHierarchyMap.get(pid);
            if(children == null){
                children = new ArrayList<>();
                resourceHierarchyMap.put(pid,children);
            }
            children.add(resource);
        }

        //角色对应的资源放入缓存中
        Cache roleResourceCache = cacheManager.getCache(ROLE_RESOURCE_CACHE);
        roleResourceCache.clear();
        for (Map.Entry<String, Set<String>> entry : roleResourceMap.entrySet()) {
            roleResourceCache.put(entry.getKey(),entry.getValue());
        }

        //将资源层级放入缓存
        Cache cache = cacheManager.getCache(RESOURCE_HIERARCHY_CACHE);
        //清楚缓存
        cache.clear();
        for(Map.Entry<Long,List<SysResource>> entry : resourceHierarchyMap.entrySet()){
            cache.put(entry.getKey(),entry.getValue());
        }

        //路径角色权限放入缓存
        Cache resourcePathRoleCache = cacheManager.getCache(RESOURCE_PATH_ROLE_CACHE);
        resourcePathRoleCache.put(RESOURCE_PATH_ROLE_CACHE,resourcePathRoleMap);
    }

    /**
     * 获取资源路径对应角色Map
     * @return
     */
    public Map<String,Set<String>> getResoucePathRoleMap(){
        Cache resourcePathRoleCache = cacheManager.getCache(RESOURCE_PATH_ROLE_CACHE);
        Cache.ValueWrapper valueWrapper = resourcePathRoleCache.get(RESOURCE_PATH_ROLE_CACHE);
        if(valueWrapper != null){
            return (Map<String,Set<String>>)valueWrapper.get();
        }
        return null;
    }

    /**
     * 刷新缓存
     * @param id
     */
    public void refreshParentResouceCache(Long id){
        List<SysResource> resources = this.sysResourceMapper.selectList(new EntityWrapper<SysResource>().eq("pid",id));
        Cache cache = cacheManager.getCache(RESOURCE_HIERARCHY_CACHE);
        cache.evict(id);
        cache.put(id,resources);
    }

    /**
     * 获取缓存中的子资源
     * @param id
     * @return
     */
    public List<SysResource> getChildrenCacheByResourceId(Long id){
        Cache cache = cacheManager.getCache(RESOURCE_HIERARCHY_CACHE);
        if(cache != null){
            Cache.ValueWrapper value = cache.get(id);
            if(value != null && value.get() != null){
                return (List<SysResource>)value.get();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
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
            this.applicationEventPublisher.publishEvent(new SysCacheRefreshEvent(this));
        }
        logger.info("Cache load finished");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
