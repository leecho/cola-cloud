package com.honvay.cola.cloud.upm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.honvay.cola.cloud.framework.base.mapper.CommonMapper;
import com.honvay.cola.cloud.framework.base.service.impl.BaseServiceImpl;
import com.honvay.cola.cloud.framework.core.constant.CommonConstant;
import com.honvay.cola.cloud.framework.security.utils.SecurityUtils;
import com.honvay.cola.cloud.framework.util.ValidationUtils;
import com.honvay.cola.cloud.upm.entity.SysAuthorize;
import com.honvay.cola.cloud.upm.entity.SysRole;
import com.honvay.cola.cloud.upm.mapper.SysRoleMapper;
import com.honvay.cola.cloud.upm.model.Authorize;
import com.honvay.cola.cloud.upm.model.SysAuthorizeBatchDTO;
import com.honvay.cola.cloud.upm.service.SysAuthorizeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 授权服务类
 * @author LIQIU
 * @date 2017-12-20
 */
@Service
public class SysAuthorizeServiceImpl extends BaseServiceImpl<SysAuthorize> implements SysAuthorizeService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private CommonMapper commonMapper;

    @Override
    public Authorize getAuthorize(Long userId){
        Authorize authorize = new Authorize();
        List<SysRole> roles = this.getRoleListByTargetAndType(userId, CommonConstant.AUTHORIZE_TYPE_USER);
        Collection<String>  roleCodes  = CollectionUtils.collect(roles.iterator(), input -> input.getCode());
        Collection<Object> resourceCodes;
        if (roleCodes.contains(CommonConstant.ROLE_ADMIN)) {
            String sql = "SELECT CODE FROM cola_sys_resource t";
            resourceCodes = commonMapper.selectObjs(sql);
        } else {
            String sql = "SELECT CODE FROM cola_sys_resource t" + "				 WHERE t.id in  ( " + "						SELECT b.sys_resource_id FROM cola_sys_authority b" + "						 WHERE b.sys_role_id in (" + "							SELECT a.sys_role_id FROM cola_sys_authorize a" + "									WHERE a.authorize_target = {0} " + "                                     AND a.authorize_type = {1} )" + "						)";
                //普通用户给其所对应的权限
            resourceCodes = commonMapper.selectObjs(sql, userId, CommonConstant.AUTHORIZE_TYPE_USER);
        }
        Collection<String> resources = new ArrayList<>();
        if (resourceCodes != null) {
            resources = CollectionUtils.collect(resourceCodes.iterator(), input -> input.toString());
        }
        authorize.setRoles(roleCodes);
        authorize.setResources(resources);
        return authorize;
    }


    @Override
    public List<SysAuthorize> getAuthorizesByTargetAndType(Long target,String type){
        EntityWrapper<SysAuthorize> wrapper = this.newEntityWrapper();
        wrapper.eq("authorize_target",target);
        wrapper.eq("authorize_type",type);
        return this.selectList(wrapper);
    }

    /**
     * 批量设置对象的角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAuthorize(SysAuthorizeBatchDTO sysAuthorizeBatchDTO){
        ///List<Long> roleIds = authorizeBatchTargetDTO.getRoleIds();
        for (Long roldId : sysAuthorizeBatchDTO.getRoleIds()){
            Long[] targets = sysAuthorizeBatchDTO.getTargets();
            for(Long target : targets){
                SysAuthorize authorize = new SysAuthorize();
                authorize.setSysRoleId(roldId);
                authorize.setAuthorizeTarget(target);
                authorize.setAuthorizeType(sysAuthorizeBatchDTO.getType());
                this.insert(authorize);
            }
        }
    }

    @Override
    public List<SysRole> getRoleListByTargetAndType(Long target, String type) {
        EntityWrapper<SysRole> wrapper = new EntityWrapper<>();
        wrapper.andNew("id in (select sys_role_id from cola_sys_authorize t " +
                "where t.authorize_target = {0} and authorize_type = {1})",target,type);
        return this.sysRoleMapper.selectList(wrapper);
    }

    /**
     * 验证角色是否合法
     *
     * @param roleId
     */
    private void validateRole(Long roleId) {
        SysRole role = this.sysRoleMapper.selectById(roleId);
        Long tenantId = SecurityUtils.getTenantId();
        Assert.notNull(role,"角色不存在");
        if (tenantId != null) {
            Assert.isTrue(tenantId != null && tenantId.equals(role.getTenantId()), "该角色不属于当前租户");
        } else {
            Assert.isTrue(tenantId == null && role.getTenantId() == null, "该角色为租户角色");
        }
    }

    @Override
    public boolean insert(SysAuthorize entity) {
        ValidationUtils.validate(entity,true);
        //验证角色
        this.validateRole(entity.getSysRoleId());
        //验证是否存在
        if (this.notExists(entity)) {
            return super.insert(entity);
        }
        return false;
    }

    private boolean notExists(SysAuthorize authorize) {
        Wrapper<SysAuthorize> wrapper = new EntityWrapper<SysAuthorize>();
        wrapper.eq("authorize_target", authorize.getAuthorizeTarget());
        wrapper.eq("sys_role_id", authorize.getSysRoleId());
        wrapper.eq("authorize_type", authorize.getAuthorizeType());
        return CollectionUtils.isEmpty(this.selectList(wrapper));
    }

    @Override
    public void delete(Long userId, Long roleId) {
        this.validateRole(roleId);
        Wrapper<SysAuthorize> wrapper = new EntityWrapper<SysAuthorize>();
        wrapper.eq("authorize_target", userId);
        wrapper.eq("sys_role_id", roleId);
        SysAuthorize authority = this.selectOne(wrapper);
        if (authority != null) {
            this.deleteById(authority.getId());
        }
    }
}
