package com.honvay.cola.cloud.upm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cola.cloud.framework.base.service.impl.TenancyServiceImpl;
import com.honvay.cola.cloud.framework.core.constant.CommonConstant;
import com.honvay.cola.cloud.framework.util.Assert;
import com.honvay.cola.cloud.upm.entity.SysAuthority;
import com.honvay.cola.cloud.upm.entity.SysAuthorize;
import com.honvay.cola.cloud.upm.entity.SysRole;
import com.honvay.cola.cloud.upm.mapper.SysAuthorityMapper;
import com.honvay.cola.cloud.upm.mapper.SysAuthorizeMapper;
import com.honvay.cola.cloud.upm.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户权限吧 服务实现类
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
@Service
public class SysRoleServiceImpl extends TenancyServiceImpl<SysRole> implements SysRoleService {
	
	@Autowired
	private SysAuthorityMapper sysAuthorityMapper;
	
	@Autowired
	private SysAuthorizeMapper sysAuthorizeMapper;

    /**
     * 获取角色列表
     * @return
     */
    @Override
	public List<SysRole> list(){
        EntityWrapper<SysRole> wrapper = this.newEntityWrapper();
        //添加租户条件
        this.addTenantCondition(wrapper);
        return this.selectList(wrapper);
    }

    /**
     * 检查配置项是否有重复
     * @param sysRole
     * @return
     */
    private boolean check(SysRole sysRole){
        EntityWrapper<SysRole> wrapper = new EntityWrapper<SysRole>();
        wrapper.eq("code",sysRole.getCode());
        if(sysRole.getId() != null){
            wrapper.ne("id",sysRole.getId());
        }
        return this.selectList(wrapper).size() == 0;
    }


    @Override
    public boolean insert(SysRole entity) {
        Assert.isTrue(check(entity),"编号已存在");
        return super.insert(entity);
    }

    /**
     * 判断是否为管理员角色
     * @param role
     * @return
     */
    public boolean isAdminRole(SysRole role){
        if(role == null){
            return false;
        }
        return role.getCode().equals(CommonConstant.ROLE_ADMIN) || role.getCode().equals(CommonConstant.ROLE_TENANT_ADMIN);
    }

    @Override
    public boolean updateById(SysRole entity) {
        Assert.isTrue(check(entity),"编号已存在");
        SysRole originRole = this.selectById(entity.getId());
        if(isAdminRole(originRole) && !originRole.getCode().equals(entity.getCode())){
            throw new IllegalArgumentException("管理员角色编号不能修改");
        }
        return super.updateById(entity);
    }

    @Override
	public void delete(Long id) {

        SysRole originRole = this.selectById(id);
        Assert.isTrue(!isAdminRole(originRole),"管理员角色不能删除");

		this.deleteById(id);
		//删除权限
		sysAuthorityMapper.delete(new EntityWrapper<SysAuthority>().eq("sys_role_id", id));
		//删除授权
		sysAuthorizeMapper.delete(new EntityWrapper<SysAuthorize>().eq("sys_role_id", id));
	}
}
