package com.honvay.cola.cloud.uc.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.uc.entity.SysUser;
import com.honvay.cola.cloud.uc.model.SysUserAuthentication;
import com.honvay.cola.cloud.uc.model.SysUserCriteria;
import com.honvay.cola.cloud.uc.model.SysUserDTO;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
public interface SysUserService extends BaseService<SysUser> {


    /**
     * 修改用户信息
     * @param sysUserDTO
     */
    void update(SysUserDTO sysUserDTO);

    /**
     * 查询系统用户
     *
     * @return
     */
    List<SysUser> list();

    /**
     * 重置用户密码
     *
     * @param id
     */
    void doResetPassword(Long id);

    /**
     * 禁用用户
     *
     * @param id
     * @return
     */
    SysUser doDisable(Long id);

    /**
     * 启用用户
     *
     * @param id
     * @return
     */
    SysUser doEnable(Long id);


    /**
     * 锁定用户
     *
     * @param id
     * @return
     */
    SysUser doLock(Long id);

    /**
     * 解锁账号
     *
     * @param id
     * @return
     */
    SysUser doUnlock(Long id);

    /**
     * 修改密码
     * @param id
     * @param oldPassword
     * @param newPassword
     */
    void updatePassword(Long id, String oldPassword, String newPassword);

    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    SysUserAuthentication findUserByUsername(String username);

    /**
     * 通过用户ID获取用户
     * @param id
     * @return
     */
    SysUserAuthentication findUserById(Long id);

    /**
     * 通过手机号获取用户
     * @param phoneNumber
     * @return
     */
    SysUserAuthentication findUserByPhoneNumber(String phoneNumber);
    
    boolean deleteSysUser(String username);

    /**
     * 创建用户
     * @param sysUserDTO
     */
    void insert(SysUserDTO sysUserDTO);
}
