package com.honvay.cola.cloud.organization.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.organization.entity.SysEmployee;
import com.honvay.cola.cloud.organization.model.SysEmployeeAddDTO;
import com.honvay.cola.cloud.organization.model.SysEmployeeDTO;
import com.honvay.cola.cloud.organization.model.SysEmployeeVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liqiu
 * @date 2018-01-04
 */
public interface SysEmployeeService extends BaseService<SysEmployee> {

    /**
     * 添加员工
     * @param sysEmployeeAddDTO
     */
    void add(SysEmployeeAddDTO sysEmployeeAddDTO);

    /**
     * 创建员工
     * @param sysEmployeeDTO
     */
    void create(SysEmployeeDTO sysEmployeeDTO);

    /**
     * 修改员工信息
     * @param sysEmployeeDTO
     */
    void update(SysEmployeeDTO sysEmployeeDTO);

    /**
     * 获取员工详细信息
     * @param id
     * @return
     */
    SysEmployeeVO getEmployeeById(Long id);

    /**
     * 通过部门ID获取员工列表
     * @param page
     * @param name
     * @param username
     * @param status
     * @return
     */
    Page<SysEmployeeVO> getEmployeeListByOrgId(Page page, String name, String username, Integer status);
}
