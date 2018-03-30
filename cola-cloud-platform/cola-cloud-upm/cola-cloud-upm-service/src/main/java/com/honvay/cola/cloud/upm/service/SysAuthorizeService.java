package com.honvay.cola.cloud.upm.service;


import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.upm.entity.SysAuthorize;
import com.honvay.cola.cloud.upm.entity.SysRole;
import com.honvay.cola.cloud.upm.model.Authorize;
import com.honvay.cola.cloud.upm.model.SysAuthorizeBatchDTO;

import java.util.List;

/**
 * 系统授权服务类
 * @author LIQIU
 * @date 2017-12-20
 */
public interface SysAuthorizeService extends BaseService<SysAuthorize> {

    Authorize getAuthorize(Long userId);

    /**
     * 根据授权对象和类型获取授权列表
     * @param target
     * @param type
     * @return
     */
    List<SysAuthorize> getAuthorizesByTargetAndType(Long target, String type);

    /**
     * 批量添加角色授权
     * @param authorizeBatchTargetDTO
     */
    void batchAuthorize(SysAuthorizeBatchDTO authorizeBatchTargetDTO);

	/**
	 * 根据授权对象和授权类型获取角色列表
	 * @param target
	 * @param type
	 * @return
	 */
    List<SysRole> getRoleListByTargetAndType(Long target, String type);

    /**
	 * 根据用户，角色删除权限
	 * @param userId
	 * @param roleId
	 */
	void delete(Long userId, Long roleId);

}
