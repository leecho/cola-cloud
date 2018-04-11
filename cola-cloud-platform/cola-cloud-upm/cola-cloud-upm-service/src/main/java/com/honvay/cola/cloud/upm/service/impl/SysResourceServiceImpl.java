package com.honvay.cola.cloud.upm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cola.cloud.framework.base.service.impl.BaseSerivceImpl;
import com.honvay.cola.cloud.framework.security.utils.SecurityUtils;
import com.honvay.cola.cloud.framework.util.Assert;
import com.honvay.cola.cloud.upm.cache.SysResourceCacheService;
import com.honvay.cola.cloud.upm.entity.SysAuthority;
import com.honvay.cola.cloud.upm.entity.SysResource;
import com.honvay.cola.cloud.upm.model.SysMenuVO;
import com.honvay.cola.cloud.upm.service.SysAuthorityService;
import com.honvay.cola.cloud.upm.service.SysAuthorizeService;
import com.honvay.cola.cloud.upm.service.SysResourceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
@Service
public class SysResourceServiceImpl extends BaseSerivceImpl<SysResource> implements SysResourceService {

    private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private SysAuthorityService sysAuthorityService;

	@Autowired
    private SysAuthorizeService sysAuthorizeService;

	@Autowired
    private SysResourceCacheService sysResourceCacheService;

    @Override
    public List<SysMenuVO> getResourceListByPid(Long id) {
        EntityWrapper<SysResource> wrapper = this.newEntityWrapper();
        if (id == null) {
            wrapper.isNull("pid");
        } else {
            wrapper.eq("pid", id);
        }
        wrapper.orderBy("sort");

        List<SysMenuVO> menus = new ArrayList<>();

        Long tenantId = SecurityUtils.getTenantId();
        List<SysResource> resources = this.selectList(wrapper);
        List<SysResource> noAuhorized = new ArrayList<>();
        if(tenantId != null){
            //如果是租户的话则判断权限
            for(SysResource resource : resources){
                //TODO 需要验证权限
                if(!SecurityUtils.hasResource(resource.getCode())){
                    noAuhorized.add(resource);
                }
            }
            resources.removeAll(noAuhorized);
        }

        for(SysResource resource : resources){
            SysMenuVO sysMenuVO = new SysMenuVO();
            BeanUtils.copyProperties(resource,sysMenuVO);
            sysMenuVO.setChildren(this.getResourceListByPid(sysMenuVO.getId()));
            menus.add(sysMenuVO);
        }

        return menus;
    }

    /**
     * 检查配置项是否有重复
     * @param sysResource
     * @return
     */
    private boolean check(SysResource sysResource){
        EntityWrapper<SysResource> wrapper = new EntityWrapper<SysResource>();
        wrapper.eq("code",sysResource.getCode());
        if(sysResource.getId() != null){
            wrapper.ne("id",sysResource.getId());
        }
        return this.selectList(wrapper).size() == 0;
    }

    @Override
    public boolean insert(SysResource entity) {
        Assert.isTrue(check(entity),"资源编号已存在");
        boolean result = super.insert(entity);
        return result;
    }

    @Override
    public boolean updateById(SysResource entity) {
        Assert.isTrue(check(entity),"资源编号已存在");
        boolean result = super.updateById(entity);
        return result;
    }

    @Override
    public void delete(Long id) {
    	Assert.isTrue(CollectionUtils.isEmpty(this.listByProperty("pid", id)),"存在子资源无法删除");
    	this.deleteById(id);
    	this.sysAuthorityService.delete(new EntityWrapper<SysAuthority>().eq("sys_resource_id", id));
    }
    
    @Override
	public List<SysMenuVO> getAuthorizedResource(){
		return this.getAuthorizedResource(-1L);
	}

    private List<SysMenuVO> getAuthorizedResource(Long pid){
        List<SysMenuVO> menus = new ArrayList<SysMenuVO>();
        List<SysResource> children = this.sysResourceCacheService.getChildrenCacheByResourceId(pid);
        for (SysResource child : children){
            //判断是否有权限
            if(SecurityUtils.hasResource(child.getCode()) ){
                SysMenuVO menu = new SysMenuVO();
                BeanUtils.copyProperties(child,menu);
                //遍历子节点
                menu.setChildren(this.getAuthorizedResource(child.getId()));
                menus.add(menu);
            }
        }
        menus.sort((o1, o2) -> {
            if(o1.getSort() == null){
                return 0;
            }
            if(o2.getSort() == null){
                return -1;
            }
            return o1.getSort() - o2.getSort();
        });
        return menus;
    }
}
