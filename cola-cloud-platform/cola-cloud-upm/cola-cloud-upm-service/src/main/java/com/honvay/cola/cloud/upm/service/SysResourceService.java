package com.honvay.cola.cloud.upm.service;


import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.upm.entity.SysResource;
import com.honvay.cola.cloud.upm.model.SysMenuVO;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
public interface SysResourceService extends BaseService<SysResource> {

    /**
     * 根据资源id获取所有子资源
     */
    List<SysMenuVO> getResourceListByPid(Long id);

	/**
	 * 获取已授权的资源
	 * @return
	 */
	List<SysMenuVO> getAuthorizedResource();


	void delete(Long id);

}
