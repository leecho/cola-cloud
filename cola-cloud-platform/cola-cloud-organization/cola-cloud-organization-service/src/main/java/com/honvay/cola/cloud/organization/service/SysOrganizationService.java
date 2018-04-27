package com.honvay.cola.cloud.organization.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.organization.entity.SysOrganization;
import com.honvay.cola.cloud.organization.model.SysOrganizationDTO;
import com.honvay.cola.cloud.organization.model.SysOrganizationVO;

import java.io.Serializable;
import java.util.List;

public interface SysOrganizationService extends BaseService<SysOrganization> {

	List<SysOrganizationVO> list(Page page, String name, String code,String status);

	SysOrganizationVO get(Long id);

	/**
	 * 根据父ID获取子部门，如果父ID为空则获取租户下的根节点
	 * @param id
	 * @return
	 */
	List<SysOrganization> getOrganizationListByPid(Long id);

	/**
	 * 删除部门，逻辑删除
	 * @param id
	 */
	void delete(Serializable id);

	void update(SysOrganizationDTO sysOrganizationDTO);

	void insert(SysOrganizationDTO sysOrganizationDTO);
}
