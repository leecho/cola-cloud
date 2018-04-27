package com.honvay.cola.cloud.upm.service;


import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.upm.entity.SysResource;
import com.honvay.cola.cloud.upm.model.SysResourceDTO;
import com.honvay.cola.cloud.upm.model.SysResourceTreeNode;
import com.honvay.cola.cloud.upm.model.SysResourceVO;

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


	void delete(Long id);

	/**
	 * 根据资源id获取所有子资源
	 */
	List<SysResourceTreeNode> getChildren(Long id);

	/**
	 * 获取已授权的资源树
	 * @return
	 */
	List<SysResourceTreeNode> getResourceTree();

	/**
	 * 获取已授权的资源列表
	 * @return
	 */
	List<SysResourceVO> getResourceList();

	/**
	 * 修改资源
	 * @param sysResourceDTO
	 */
    void update(SysResourceDTO sysResourceDTO);

	/**
	 * 新增资源
	 * @param sysResourceDTO
	 */
	void insert(SysResourceDTO sysResourceDTO);
}
