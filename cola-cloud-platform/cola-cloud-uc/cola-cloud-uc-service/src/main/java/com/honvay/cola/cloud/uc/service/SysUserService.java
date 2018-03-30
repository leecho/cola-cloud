package com.honvay.cola.cloud.uc.service;


import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.uc.entity.SysUser;
import com.honvay.cola.cloud.uc.model.UserVO;

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
     * 查询系统用户
     *
     * @return
     */
    List<SysUser> list();

    /**
     * 更新用户
     */
    void update(SysUser user);

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

    void updatePassword(Long id, String oldPassword, String newPassword);

    UserVO findUserByUsername(String username);

    UserVO findUserByPhoneNumber(String phoneNumber);
    
    boolean deleteSysUser(String username);
}
