package com.honvay.cola.cloud.upm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.honvay.cola.cloud.framework.base.mapper.CommonMapper;
import com.honvay.cola.cloud.framework.base.service.impl.BaseServiceImpl;
import com.honvay.cola.cloud.framework.security.utils.SecurityUtils;
import com.honvay.cola.cloud.framework.util.ValidationUtils;
import com.honvay.cola.cloud.upm.entity.SysAuthority;
import com.honvay.cola.cloud.upm.entity.SysResource;
import com.honvay.cola.cloud.upm.entity.SysRole;
import com.honvay.cola.cloud.upm.mapper.SysResourceMapper;
import com.honvay.cola.cloud.upm.mapper.SysRoleMapper;
import com.honvay.cola.cloud.upm.model.SysAuthorityBatchDTO;
import com.honvay.cola.cloud.upm.service.SysAuthorityService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 权限-资源中间表 服务实现类
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
@Service
public class SysAuthorityServiceImpl extends BaseServiceImpl<SysAuthority> implements SysAuthorityService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Autowired
    private CommonMapper commonMapper;

    @Override
    public List<SysRole> getRoleListByResourceId(Long resourceId) {

        EntityWrapper<SysRole> wrapper = new EntityWrapper<>();
        wrapper.andNew("id in (select sys_role_id from cola_sys_authority t where t.sys_resource_id = {0})",resourceId);
        Long tenantId = SecurityUtils.getTenantId();
        if (tenantId != null) {
            wrapper.eq("tenant_id", tenantId);
        } else {
            wrapper.isNull("tenant_id");
        }
        return this.sysRoleMapper.selectList(wrapper);

        /*String sql = "select sys_role_id from cola_sys_authority t where t.sys_resource_id = {0}";
        List<Object> roleIds = commonMapper.selectObjs(sql, resourceId);
        if (CollectionUtils.isNotEmpty(roleIds)) {
            EntityWrapper<SysRole> wrapper = new EntityWrapper<>();
            wrapper.in("id", roleIds);
            //如果有租户则只显示租户下的角色
            Long tenantId = SecurityUtils.getTenantId();
            if (tenantId != null) {
                wrapper.eq("tenant_id", tenantId);
            } else {
                wrapper.isNull("tenant_id");
            }
            return this.sysRoleMapper.selectList(wrapper);
        }
        return null;*/
    }

    @Override
    public List<SysResource> getResourcesByRoleId(Long roleId) {
        EntityWrapper<SysResource> wrapper = new EntityWrapper<>();
        wrapper.andNew("id in (select sys_resource_id from cola_sys_authority t where t.sys_role_id = {0})",roleId);
        return this.sysResourceMapper.selectList(wrapper);
    }

    @Override
    public List<SysResource> getResourcesByAuthorizeTargetAndType(Long target, String type){
        EntityWrapper<SysResource> wrapper = new EntityWrapper<>();
        wrapper.andNew("id in (select sys_resource_id from cola_sys_authority t where t.sys_role_id " +
                " in (select sys_role_Id from cola_sys_authorize a where a.authorize_target={0} and a.authorize_type={1}))",target,type);
        return this.sysResourceMapper.selectList(wrapper);
    }

    @Override
    public Collection<String> getResourceCodesByAuthorizeTargetAndType(Long target, String type){
        List<SysResource> sysResources = this.getResourcesByAuthorizeTargetAndType(target,type);
        return CollectionUtils.collect(sysResources.iterator(),(input) -> input.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batch(SysAuthorityBatchDTO sysAuthorityBatchDTO){
        for (Long roleId : sysAuthorityBatchDTO.getRoleIds()) {
            SysAuthority authority = new SysAuthority();
            for(Long resourceId : sysAuthorityBatchDTO.getResourceIds()){
                //已经存在的不能保存
                if(this.exists(resourceId, roleId)) {
                    authority.setSysRoleId(roleId);
                    authority.setSysResourceId(resourceId);
                    this.insert(authority);
                }
            }
        }
    }

    @Override
    public boolean exists(Long resourceId, Long roleId) {
        Wrapper<SysAuthority> wrapper = new EntityWrapper<SysAuthority>();
        wrapper.eq("sys_resource_id", resourceId);
        wrapper.eq("sys_role_id", roleId);
        return CollectionUtils.isEmpty(this.selectList(wrapper));
    }

    /**
     * 验证角色是否合法
     *
     * @param roleId
     */
    private void validateRole(Long roleId) {
        SysRole role = this.sysRoleMapper.selectById(roleId);
        Long tenantId = SecurityUtils.getTenantId();
        if(tenantId != null){
            Assert.isTrue(tenantId != null && tenantId.equals(role.getTenantId()), "该角色不属于当前租户");
        }else{
            Assert.isTrue(tenantId == null && role.getTenantId() == null, "该角色为租户角色");
        }
    }


    /**
     * 判断是否存在
     *
     * @param roleId
     * @param resourceId
     * @return
     */
    private boolean notExists(Long roleId, Long resourceId) {
        Wrapper<SysAuthority> wrapper = new EntityWrapper<SysAuthority>();
        wrapper.eq("sys_role_id", roleId);
        wrapper.eq("sys_resource_id", resourceId);
        return CollectionUtils.isEmpty(this.selectList(wrapper));
    }


    @Override
    public boolean insert(SysAuthority entity) {
        ValidationUtils.validate(entity,true);
        //验证角色
        this.validateRole(entity.getSysRoleId());
        //判断是否存在
        if (this.notExists(entity.getSysRoleId(), entity.getSysResourceId())) {
            return super.insert(entity);
        }
        return false;
    }

    @Override
    public void delete(Long resourceId, Long roleId) {
        this.validateRole(roleId);
        Wrapper<SysAuthority> wrapper = new EntityWrapper<SysAuthority>();
        wrapper.eq("sys_resource_id", resourceId);
        wrapper.eq("sys_role_id", roleId);
        SysAuthority authority = this.selectOne(wrapper);
        if (authority != null) {
            this.deleteById(authority.getId());
        }
    }
}
