package com.honvay.cola.cloud.organization.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.framework.util.Assert;
import com.honvay.cola.cloud.organization.entity.SysOrganization;
import com.honvay.cola.cloud.organization.model.SysOrganizationCriteria;
import com.honvay.cola.cloud.organization.model.SysOrganizationDTO;
import com.honvay.cola.cloud.organization.model.SysOrganizationVO;
import com.honvay.cola.cloud.organization.service.SysOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 组织架构管理
 * </p>
 *
 * @Author LIQIU
 * @Date 2017-12-09
 */
@EnableAudit
@RestController
@RequestMapping("/organization")
@Api(value = "/organization", tags = "组织架构管理")
public class SysOrganizationController extends BaseController {

    @Autowired
    private SysOrganizationService sysOrganizationService;

    @ApiOperation(value = "获取机构列表")
    @GetMapping("/list")
    public Result<Page<SysOrganization>> list(SysOrganizationCriteria sysOrganizationCriteria) {
        return this.success(this.sysOrganizationService.selectPage(sysOrganizationCriteria));
    }

    @ApiOperation(value = "查询机构树")
    @GetMapping("/getByParent")
    public Object getByParentId(Long id) {
        return this.sysOrganizationService.getOrganizationListByPid(id);
    }

    @ApiOperation(value = "删除机构")
    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Long id) {
        sysOrganizationService.delete(id);
        return this.success();
    }

    @ApiOperation(value = "新增/修改机构")
    @PostMapping("/save")
    public Result<String> save(@Validated SysOrganizationDTO sysOrganizationDTO) {
        if (sysOrganizationDTO.getId() != null) {
            this.sysOrganizationService.update(sysOrganizationDTO);
        } else {
            Assert.notNull(sysOrganizationDTO.getParent(), "所属部门不能为空");
            this.sysOrganizationService.insert(sysOrganizationDTO);
        }
        return this.success();
    }

    @ApiOperation(value = "查询机构")
    @GetMapping(value = "/get")
    public Result<SysOrganizationVO> get(@RequestParam Long id) {
        return this.success(this.sysOrganizationService.get(id));
    }

}
