package com.honvay.cola.cloud.tenancy.controller;

import com.honvay.cola.cloud.client.SysMemberDTO;
import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.tenancy.entity.SysMember;
import com.honvay.cola.cloud.tenancy.service.SysMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户成员控制器
 *
 * @author Cola Generated
 * @Date 17:19:32
 */
@EnableAudit
@RestController
@RequestMapping("/member")
@Api(value = "/member",tags = "租户成员管理")
public class SysMemberController extends BaseController {

    @Autowired
    private SysMemberService sysMemberService;

    /**
     * 获取租户成员列表
     */
    @GetMapping(value = "/list")
    @ApiOperation("获取租户成员列表")
    public Result<List<SysMember>> list() {
        return this.success(sysMemberService.selectList());
    }

    /**
     * 新增租户成员
     */
    @PostMapping(value = "/add")
    @ApiOperation("新增租户成员")
    public Result<String> add(SysMember sysMember) {
        sysMemberService.insert(sysMember);
        return this.success();
    }

    /**
     * 删除租户成员
     */
    @PostMapping(value = "/delete")
    @ApiOperation("删除租户成员")
    public Result<String> delete(SysMemberDTO sysMemberDTO) {
        sysMemberService.delete(sysMemberDTO);
        return this.success();
    }

    /**
     * 修改租户成员
     */
    @PostMapping(value = "/update")
    @ApiOperation("修改租户成员")
    public Result<String> update(SysMember sysMember) {
        sysMemberService.updateById(sysMember);
        return this.success();
    }

    /**
     * 获取租户成员
     */
    @GetMapping(value = "/get/{id}")
    @ApiOperation("获取租户成员")
    public Result<SysMember> get(@PathVariable("id") Long id) {
        return this.success(sysMemberService.selectById(id));
    }
}
