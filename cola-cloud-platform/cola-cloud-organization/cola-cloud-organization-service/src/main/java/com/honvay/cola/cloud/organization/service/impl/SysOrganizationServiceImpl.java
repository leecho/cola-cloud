package com.honvay.cola.cloud.organization.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.service.impl.TenancyServiceImpl;
import com.honvay.cola.cloud.framework.core.constant.CommonConstant;
import com.honvay.cola.cloud.framework.util.Assert;
import com.honvay.cola.cloud.framework.util.BeanUtils;
import com.honvay.cola.cloud.organization.entity.SysOrganization;
import com.honvay.cola.cloud.organization.mapper.SysOrganizationMapper;
import com.honvay.cola.cloud.organization.model.SysOrganizationDTO;
import com.honvay.cola.cloud.organization.model.SysOrganizationVO;
import com.honvay.cola.cloud.organization.service.SysEmployeeService;
import com.honvay.cola.cloud.organization.service.SysOrganizationService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 组织架构服务类
 * @author LIQIU
 * @date 2018-1-3
 */
@Service
public class SysOrganizationServiceImpl extends TenancyServiceImpl<SysOrganization>
		implements SysOrganizationService {

    @Autowired
    private SysEmployeeService sysEmployeeService;

    /**
     * 分页查询组织架构列表
     *
     * @param page
     * @param name
     * @param code
     * @return
     */
    @Override
    public List<SysOrganizationVO> list(Page page, String name, String code, String status) {
        return this.getMapper(SysOrganizationMapper.class).getOrganizationList(page, name, code, status);
    }

    /**
     * 获取组织架构信息
     *
     * @param id
     * @return
     */
    @Override
    public SysOrganizationVO get(Long id) {
        return this.getMapper(SysOrganizationMapper.class).getOrganization(id);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<SysOrganization> getOrganizationListByPid(Long id) {
        EntityWrapper<SysOrganization> wrapper = this.newEntityWrapper();
        //添加租户查询条件
        this.addTenantCondition(wrapper);
        if (id == null) {
            wrapper.isNull("parent");
        } else {
            wrapper.eq("parent", id);
        }
        wrapper.andNew().eq("deleted", CommonConstant.COMMON_NO).or().isNull("deleted");
        return this.selectList(wrapper);
    }


    /**
     * 判断是否有子部门
     *
     * @param id
     * @return
     */
    private void assertHasChild(Serializable id) {
        Assert.isTrue(CollectionUtils.isEmpty(this.selectList("parent", id)), "存在子部门无法删除");
    }

    /**
     * 判断是否有员工
     *
     * @param id
     * @return
     */
    private void assertHasEmployee(Serializable id) {
        Assert.isTrue(CollectionUtils.isEmpty(this.sysEmployeeService.selectList("sys_org_id", id)), "存在员工无法删除");
    }


    @Override
    public void delete(Serializable id) {
        this.assertHasChild(id);
        this.assertHasEmployee(id);
        SysOrganization sysOrganization = this.selectById(id);
        sysOrganization.setDeleted(CommonConstant.COMMON_YES);
        super.updateById(sysOrganization);
    }

    @Override
    public void update(SysOrganizationDTO dto) {
        this.checkOrganization(dto);
        SysOrganization sysOrganization = super.selectById(dto.getId());
        org.springframework.beans.BeanUtils.copyProperties(dto, sysOrganization);
        Long current = sysOrganization.getId();
        Long pid = current;

        if (sysOrganization.getId() != null) {
            Assert.isTrue(!sysOrganization.getParent().equals(sysOrganization.getId()), "不能将自己设置为上级部门");
        }

        while (pid != null) {
            pid = this.selectById(pid).getParent();
            Assert.isTrue(pid.equals(current), "不能设置下级部门为上级");
        }
        super.updateById(sysOrganization);
    }

    @Override
    public void insert(SysOrganizationDTO dto) {
        SysOrganization sysOrganization = new SysOrganization();
        org.springframework.beans.BeanUtils.copyProperties(dto, sysOrganization);
        this.checkOrganization(dto);
        sysOrganization.setDeleted(CommonConstant.COMMON_NO);
        super.insert(sysOrganization);
    }

    /**
     * 判断重复
     *
     * @param dto
     */
    private void checkOrganization(SysOrganizationDTO dto) {
        EntityWrapper<SysOrganization> wrapper = this.newEntityWrapper();
        wrapper.eq("code", dto.getCode());
        Assert.isTrue(CollectionUtils.isEmpty(this.selectList(wrapper)), "部门编号已存在");
    }
}
