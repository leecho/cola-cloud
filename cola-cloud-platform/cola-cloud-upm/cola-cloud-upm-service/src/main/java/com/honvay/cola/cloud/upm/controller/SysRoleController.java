package com.honvay.cola.cloud.upm.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.base.pagination.Pagination;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.upm.entity.SysRole;
import com.honvay.cola.cloud.upm.model.SysRoleCriteria;
import com.honvay.cola.cloud.upm.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色控制类
 *
 * @author LIQIU
 * @date 2017.12.10
 */
@EnableAudit
@RestController
@RequestMapping("/role")
@Api(value = "/role", tags = "系统角色管理")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 获取系统角色列表
     *
     * @return
     */
    @ApiOperation("获取角色列表")
    @GetMapping("/list")
    public Result<Page<SysRole>> list(SysRoleCriteria sysRoleCriteria) {
        return this.success(this.sysRoleService.selectPage(sysRoleCriteria));
    }

    /**
     * 添加系统角色
     *
     * @param t
     * @return
     */
    @PostMapping("/save")
    @ApiOperation("添加角色")
    public Result save(@Validated SysRole t) {
        this.sysRoleService.insert(t);
        return this.success();
    }

    /**
     * 修改角色
     *
     * @param t
     * @return
     */
    @ApiOperation("修改角色")
    @PostMapping("/update")
    public Result update(@Validated SysRole t) {
        SysRole sysRole = sysRoleService.selectById(t.getId());
        sysRole.setCode(t.getCode());
        sysRole.setName(t.getName());
        sysRole.setDescription(t.getDescription());
        sysRoleService.updateById(t);
        return this.success();
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation("删除角色")
    public Result delete(@RequestParam Long id) {
        this.sysRoleService.delete(id);
        return this.success();
    }

    /**
     * 获取角色
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @ApiOperation("获取角色")
    public Object get(@RequestParam Long id) {
        Result result = this.success(this.sysRoleService.selectById(id));
        return result;
    }
}
