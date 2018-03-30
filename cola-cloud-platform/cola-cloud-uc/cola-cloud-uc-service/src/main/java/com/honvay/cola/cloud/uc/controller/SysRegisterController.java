package com.honvay.cola.cloud.uc.controller;

import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.uc.entity.SysRegister;
import com.honvay.cola.cloud.uc.model.SysRegisterDTO;
import com.honvay.cola.cloud.uc.service.SysRegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 注册信息表控制器
 *
 * @author Cola Generated
 * @Date 10:20:55
 */
@RestController
@RequestMapping("/register")
@Api(value = "/register", tags = "注册信息表")
public class SysRegisterController extends BaseController {

    @Autowired
    private SysRegisterService sysRegisterService;
    
    /**
     * 新用户注册
     */
    @PostMapping(value = "/add")
    @ApiOperation("注册")
    public Result<String> add(SysRegisterDTO sysRegisterDTO) {
        sysRegisterService.register(sysRegisterDTO);
        return this.success();
    }

    /**
     * 获取注册信息表列表
     */
    @GetMapping(value = "/list")
    @ApiOperation("获取注册信息表列表")
    public Result<List<SysRegister>> list() {
        return this.success(sysRegisterService.selectList());
    }

    /**
     * 删除注册信息表
     */
    @PostMapping(value = "/delete/{id}")
    @ApiOperation("删除注册信息表")
    public Result<String> delete(@PathVariable("id") Long id) {
        sysRegisterService.deleteById(id);
        return this.success();
    }

    /**
     * 修改注册信息表
     */
    @PostMapping(value = "/update")
    @ApiOperation("修改注册信息表")
    public Result<String> update(SysRegister sysRegister) {
        sysRegisterService.updateById(sysRegister);
        return this.success();
    }

    /**
     * 获取注册信息表
     */
    @GetMapping(value = "/get/{id}")
    @ApiOperation("获取注册信息表")
    public Result<SysRegister> get(@PathVariable("id") Long id) {
        return this.success(sysRegisterService.selectById(id));
    }
    
}
