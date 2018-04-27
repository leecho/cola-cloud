package com.honvay.cola.cloud.organization.controller;

import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.organization.entity.SysPost;
import com.honvay.cola.cloud.organization.model.SysPostCriteria;
import com.honvay.cola.cloud.organization.service.SysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位控制器
 *
 * @author Cola Generated
 * @Date 17:20:28
 */
@EnableAudit
@RestController
@RequestMapping("/post")
@Api(value = "/post",tags = "系统岗位管理")
public class SysPostController extends BaseController {


    @Autowired
    private SysPostService sysPostService;

    /**
     * 获取岗位列表
     */
    @GetMapping(value = "/list")
    @ApiOperation("获取岗位列表")
    public Result<List<SysPost>> list(SysPostCriteria sysPostCriteria) {
        return this.success(sysPostService.selectPage(sysPostCriteria));
    }

    /**
     * 新增岗位
     */
    @PostMapping(value = "/add")
    @ApiOperation("新增岗位")
    public Result<String> add(@Validated  SysPost sysPost) {
        sysPostService.insert(sysPost);
        return this.success();
    }

    /**
     * 删除岗位
     */
    @PostMapping(value = "/delete/{id}")
    @ApiOperation("删除岗位")
    public Result<String> delete(@PathVariable("id") Long id) {
        sysPostService.deleteById(id);
        return this.success();
    }

    /**
     * 修改岗位
     */
    @PostMapping(value = "/update")
    @ApiOperation("修改岗位")
    public Result<String> update(@Validated SysPost sysPost) {
        sysPostService.updateById(sysPost);
        return this.success();
    }

    /**
     * 获取岗位
     */
    @GetMapping(value = "/get/{id}")
    @ApiOperation("获取岗位")
    public Result<SysPost> get(@PathVariable("id") Long id) {
        return this.success(sysPostService.selectById(id));
    }
}
