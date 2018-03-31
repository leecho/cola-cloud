package com.honvay.cola.cloud.upm.controller;

import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.upm.entity.SysRole;
import com.honvay.cola.cloud.upm.model.Authorize;
import com.honvay.cola.cloud.upm.model.SysAuthorizeBatchDTO;
import com.honvay.cola.cloud.upm.service.SysAuthorizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 授权控制类，管理用户角色授权信息
 *
 * @author LIQIU
 * @date 2017-12-20
 */
@EnableAudit
@RestController
@RequestMapping("/authorize")
@Api(value = "/authorize", tags = "系统授权管理")
public class SysAuthorizeController extends BaseController {

    @Autowired
    private SysAuthorizeService authorizeService;

    /**
     * 通过用户ID获取角色列表
     *
     * @param target
     * @param type
     * @return
     */
    @GetMapping("/listRole")
    @ApiOperation("通过用户ID获取角色列表")
    public Result<List<SysRole>> listRoleByUser(Long target, String type) {
        return this.success(authorizeService.getRoleListByTargetAndType(target, type));
    }

    @GetMapping("/get")
    public Authorize get(Long userId){
        return this.authorizeService.getAuthorize(userId);
    }


    /**
     * 批量授权
     * @return
     */
    @PostMapping("/batch")
    @ApiOperation(("批量授权对象角色"))
    public Result<String> authorizeBatchTarget(SysAuthorizeBatchDTO sysAuthorizeBatchDTO) {
        this.authorizeService.batchAuthorize(sysAuthorizeBatchDTO);
        return this.success();
    }

    /**
     * 删除授权，通过用户ID和角色ID删除授权
     *
     * @param userId
     * @param roleId
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation("删除授权")
    public Result<String> delete(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) {
        this.authorizeService.delete(userId, roleId);
        return this.success();
    }
}
