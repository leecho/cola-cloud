package com.honvay.cola.cloud.upm.service;


import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.upm.entity.SysAuthority;
import com.honvay.cola.cloud.upm.entity.SysResource;
import com.honvay.cola.cloud.upm.entity.SysRole;
import com.honvay.cola.cloud.upm.model.SysAuthorityBatchDTO;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 权限-资源中间表 服务类
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
public interface SysAuthorityService extends BaseService<SysAuthority> {

    /**
     * 根据资源ID获取角色列表
     * @param resourceId
     * @return
     */
	List<SysRole> getRoleListByResourceId(Long resourceId);

    /**
     * 根据角色获取资源ID列表
     * @param roleId
     * @return
     */
    List<SysResource> getResourcesByRoleId(Long roleId);


    List<SysResource> getResourcesByAuthorizeTargetAndType(Long target, String type);

    Collection<String> getResourceCodesByAuthorizeTargetAndType(Long target, String type);

    /**
     * 批量添加权限
     * @param sysAuthorityBatchDTO
     */
    void batch(SysAuthorityBatchDTO sysAuthorityBatchDTO);

    /**
     * 判断是否存在
     * @param resourceId
     * @param roleId
     * @return
     */
    boolean exists(Long resourceId, Long roleId);

    /**
     * 删除授权
     * @param resourceId
     * @param roleId
     */
	void delete(Long resourceId, Long roleId);

}
