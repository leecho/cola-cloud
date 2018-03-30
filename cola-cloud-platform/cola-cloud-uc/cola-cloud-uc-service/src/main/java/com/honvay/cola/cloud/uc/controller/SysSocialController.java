package com.honvay.cola.cloud.uc.controller;

import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.uc.entity.SysSocial;
import com.honvay.cola.cloud.uc.service.SysSocialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 社会化接入控制器
 *
 * @author Cola Generated
 * @Date 14:30:12
 */
@RestController
@RequestMapping("/sys/social")
@Api(value = "/sys/social",tags = "社会化接入")
public class SysSocialController extends BaseController {


    @Autowired
    private SysSocialService sysSocialService;

    /**
     * 获取社会化登录列表
     */
    @GetMapping(value = "/list")
    @ApiOperation("获取社会化接入列表")
    public Result<List<SysSocial>> list() {
        return this.success(sysSocialService.selectList());
    }

    /**
     * 新增社会化登录
     */
    @PostMapping(value = "/add")
    @ApiOperation("新增社会化接入")
    public Result<String> add(SysSocial sysSocial) {
        sysSocialService.insert(sysSocial);
        return this.success();
    }

    /**
     * 删除社会化登录
     */
    @PostMapping(value = "/delete/{id}")
    @ApiOperation("删除社会化接入")
    public Result<String> delete(@PathVariable("id") Long id) {
        sysSocialService.deleteById(id);
        return this.success();
    }

    /**
     * 修改社会化登录
     */
    @PostMapping(value = "/update")
    @ApiOperation("修改社会化接入")
    public Result<String> update(SysSocial sysSocial) {
        sysSocialService.updateById(sysSocial);
        return this.success();
    }

    /**
     * 获取社会化登录
     */
    @GetMapping(value = "/get/{id}")
    @ApiOperation("获取社会化接入")
    public Result<SysSocial> get(@PathVariable("id") Long id) {
        return this.success(sysSocialService.selectById(id));
    }
}
