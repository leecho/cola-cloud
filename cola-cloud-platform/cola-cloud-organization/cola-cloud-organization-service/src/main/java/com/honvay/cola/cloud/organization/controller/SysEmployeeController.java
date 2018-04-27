package com.honvay.cola.cloud.organization.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.organization.model.SysEmployeeAddDTO;
import com.honvay.cola.cloud.organization.model.SysEmployeeDTO;
import com.honvay.cola.cloud.organization.model.SysEmployeeVO;
import com.honvay.cola.cloud.organization.service.SysEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门成员控制器
 *
 * @author Cola Generated
 * @Date 17:19:58
 */
@EnableAudit
@RestController
@RequestMapping("/employee")
@Api(value = "/employee",tags = "组织员工管理")
public class SysEmployeeController extends BaseController {


    @Autowired
    private SysEmployeeService sysEmployeeService;

    /**
     * 获取部门成员列表
     */
    @GetMapping(value = "/list")
    @ApiOperation("获取部门成员列表")
    public Result<Page<SysEmployeeVO>> list(String name, String username, Integer status) {
        return this.success(sysEmployeeService.getEmployeeListByOrgId(this.getPagination(),name,username,status));
    }

    /**
     * 新增部门成员
     */
    @PostMapping(value = "/create")
    @ApiOperation("新增部门成员")
    public Result<String> create(@Validated SysEmployeeDTO sysEmployeeDTO) {
        sysEmployeeService.create(sysEmployeeDTO);
        return this.success();
    }

    @PostMapping("/add")
    @ApiOperation("添加部门成员")
    public Result<String> add(SysEmployeeAddDTO sysEmployeeAddDTO){
        sysEmployeeService.add(sysEmployeeAddDTO);
        return this.success();
    }

    /**
     * 删除部门成员
     */
    @PostMapping(value = "/delete/{id}")
    @ApiOperation("删除部门成员")
    public Result<String> delete(@PathVariable("id") Long id) {
        sysEmployeeService.deleteById(id);
        return this.success();
    }

    /**
     * 修改部门成员
     */
    @PostMapping(value = "/update")
    @ApiOperation("修改部门成员")
    public Result<String> update(SysEmployeeDTO sysEmployeeDTO) {
        sysEmployeeService.update(sysEmployeeDTO);
        return this.success();
    }

    /**
     * 获取部门成员
     */
    @GetMapping(value = "/get/{id}")
    @ApiOperation("获取部门成员")
    public Result<SysEmployeeVO> get(@PathVariable("id") Long id) {
        return this.success(sysEmployeeService.getEmployeeById(id));
    }
}
