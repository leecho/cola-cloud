package com.honvay.cola.cloud.upm.service;


import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.upm.entity.SysRole;

import java.util.List;

/**
 * <p>
 * 系统角色 服务类
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
public interface SysRoleService extends BaseService<SysRole> {


    /**
     * 获取系统角色
     * @return
     */
    List<SysRole> list();

    /**
     * 删除角色
     * @param id
     */
    void delete(Long id);
	
}
