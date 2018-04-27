package com.honvay.cola.cloud.uc.controller;

import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.base.pagination.Pagination;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.framework.security.userdetail.User;
import com.honvay.cola.cloud.framework.security.utils.SecurityUtils;
import com.honvay.cola.cloud.uc.entity.SysSocial;
import com.honvay.cola.cloud.uc.entity.SysUser;
import com.honvay.cola.cloud.uc.model.SysUserAuthentication;
import com.honvay.cola.cloud.uc.model.SysUserCriteria;
import com.honvay.cola.cloud.uc.model.SysUserDTO;
import com.honvay.cola.cloud.uc.service.SysSocialService;
import com.honvay.cola.cloud.uc.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统用户控制类
 *
 * @author LIQIU
 * @date 2017.12.10
 */
@EnableAudit
@RestController
@RequestMapping("/user")
@Api(value = "/user", tags = "系统用户管理")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysSocialService sysSocialService;

    @GetMapping("/findUserBySocial/{type}/{token}")
    public SysUserAuthentication findUserBySocial(@PathVariable("type") String type, @PathVariable("token") String token) {
        SysSocial sysSocial = this.sysSocialService.getSocialByTokenAndType(token, type);
        if (sysSocial != null) {
            return this.sysUserService.findUserById(sysSocial.getSysUserId());
        }
        return null;
    }

    /**
     * 通过用户名查询用户及其角色信息
     *
     * @param username 用户名
     * @return UseVo 对象
     */
    @GetMapping("/findUserByUsername/{username}")
    public SysUserAuthentication findUserByUsername(@PathVariable String username) {
        return this.sysUserService.findUserByUsername(username);
    }


    /**
     * 通过用户名查询用户及其角色信息
     *
     * @param phoneNumber 手机号
     * @return UseVo 对象
     */
    @GetMapping("/findUserByPhoneNumber/{phoneNumber}")
    public SysUserAuthentication findUserByPhoneNumber(@PathVariable String phoneNumber) {
        return this.sysUserService.findUserByPhoneNumber(phoneNumber);
    }


    /**
     * 获取用户列表
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("获取用户列表")
    public Object list(SysUserCriteria sysUserCriteria) {
        return this.success(this.sysUserService.selectPage(sysUserCriteria));
    }

    /**
     * 添加用户
     *
     * @param sysUserDTO
     * @return
     */
    @PostMapping("/save")
    @ApiOperation("添加用户")
    public Result<SysUserDTO> save(@RequestBody SysUserDTO sysUserDTO) {
        this.sysUserService.insert(sysUserDTO);
        return this.success(sysUserDTO);
    }

    /**
     * 获取用户
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @ApiOperation("获取用户")
    public Result<SysUser> get(Long id) {
        return this.success(this.sysUserService.selectById(id));
    }

    /**
     * 修改用户
     *
     * @param sysUserDTO
     * @return
     */
    @PostMapping("/update")
    @ApiOperation("/修改用户")
    public Object update(SysUserDTO sysUserDTO) {
        this.sysUserService.update(sysUserDTO);
        return this.success();
    }

    /**
     * 重置用户密码
     *
     * @param id
     * @return
     */
    @PostMapping("/resetPassword")
    @ApiOperation("重置用户密码")
    public Object reset(Long id) {
        this.sysUserService.doResetPassword(id);
        return this.success();
    }

    @ApiOperation("修改用户密码")
    @PostMapping(value = "/updatePassword")
    public Object changePassword(String oldPassword, String password) {
        User user = SecurityUtils.getPrincipal();
        this.sysUserService.updatePassword(user.getId(), oldPassword, password);
        return this.success();
    }

    /**
     * 禁用用户
     *
     * @param id
     * @return
     */
    @PostMapping("/disable")
    public Object disable(Long id) {
        return this.success(this.sysUserService.doDisable(id).getStatus());
    }

    /**
     * 启用用户
     *
     * @param id
     * @return
     */
    @PostMapping("/enable")
    public Object enable(Long id) {
        return this.success(this.sysUserService.doEnable(id).getStatus());
    }

    /**
     * 锁定用户
     *
     * @param id
     * @return
     */
    @PostMapping("/lock")
    public Object lock(Long id) {
        return this.success(this.sysUserService.doLock(id).getStatus());
    }

    /**
     * 解锁用户
     *
     * @param id
     * @return
     */
    @PostMapping("/unlock")
    public Object unlock(Long id) {
        return this.success(this.sysUserService.doUnlock(id).getStatus());
    }

    /**
     * 创建保险员业务异常,物理删除用户数据
     */
    @GetMapping("/delete/{username}")
    public Object deleteSysUser(@PathVariable String username) {
        this.sysUserService.deleteSysUser(username);
        return this.success();
    }

    ;
}
