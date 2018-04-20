package com.honvay.cola.cloud.tenancy.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.base.pagination.Pagination;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.tenancy.entity.SysTenant;
import com.honvay.cola.cloud.tenancy.model.SysTenantDTO;
import com.honvay.cola.cloud.tenancy.model.SysTenantVO;
import com.honvay.cola.cloud.tenancy.service.SysTenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统管理-租户管理
 *
 * @author LIQIU
 */
@EnableAudit
@RestController
@RequestMapping("/tenant")
@Api(value = "/tenant/", tags = "系统租户管理")
public class SysTenantController extends BaseController {

    @Autowired
    private SysTenantService sysTenantService;

    /**
     * 创建租户
     */
    @PostMapping("/save")
    @ApiOperation("创建租户")
    public Result<String> save(SysTenantDTO sysTenantDTO) {
        this.sysTenantService.save(sysTenantDTO);
        return this.success();
    }

    /**
     * 修改租户
     */
    @PostMapping("/update")
    @ApiOperation("修改租户")
    public Result<String> update(SysTenantVO sysTenantVO) {
        this.sysTenantService.update(sysTenantVO);
        return this.success();
    }

    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation("获取租户列表")
    public Result<Page<SysTenantVO>> list(Pagination pagination, String name, String code) {
        return this.success(this.sysTenantService.getTenantList(pagination.getPage(), code, name));
    }

    /**
     * 查询列表
     */
    @PostMapping("/get")
    @ApiOperation("获取租户信息")
    public Result<SysTenantVO> get(Long id) {
        SysTenant sysTenant = this.sysTenantService.selectById(id);
        SysTenantVO sysTenantVO = new SysTenantVO();
        BeanUtils.copyProperties(sysTenant, sysTenantVO);
        return this.success(sysTenantVO);
    }

    /**
     * 修改租户状态
     */
    @PostMapping("/disable")
    @ApiOperation("禁用租户")
    public Result<String> disable(Long id) {
        SysTenant sysTenant = this.sysTenantService.disable(id);
        return this.success(sysTenant.getStatus());
    }

    /**
     * 修改租户状态
     */
    @PostMapping("/enable")
    @ApiOperation("启用租户")
    public Result<String> enable(Long id) {
        SysTenant sysTenant = this.sysTenantService.enable(id);
        return this.success(sysTenant.getStatus());
    }

    /**
     * 删除租户
     */
    @PostMapping("/delete/{id}")
    @ApiOperation("删除租户")
    public Result<String> delete(@PathVariable Long id) {
        this.sysTenantService.delete(id);
        return this.success();
    }

}
