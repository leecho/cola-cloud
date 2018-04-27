package com.honvay.cola.cloud.uc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.service.impl.BaseServiceImpl;
import com.honvay.cola.cloud.framework.core.constant.CommonConstant;
import com.honvay.cola.cloud.framework.security.utils.SecurityUtils;
import com.honvay.cola.cloud.framework.util.Assert;
import com.honvay.cola.cloud.framework.util.BeanUtils;
import com.honvay.cola.cloud.framework.util.StringUtils;
import com.honvay.cola.cloud.uc.entity.SysUser;
import com.honvay.cola.cloud.uc.model.SysUserAuthentication;
import com.honvay.cola.cloud.uc.model.SysUserCriteria;
import com.honvay.cola.cloud.uc.model.SysUserDTO;
import com.honvay.cola.cloud.uc.password.PasswordStrategy;
import com.honvay.cola.cloud.uc.password.PasswordValidateResult;
import com.honvay.cola.cloud.uc.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {

    @Autowired
    private PasswordStrategy passwordStrategy;

    @Autowired
    private Validator validator;

    private String geSysUserBizCode() {
        return StringUtils.camelCaseToUnderline(SysUser.class.getSimpleName()).toUpperCase();
    }


    private SysUserAuthentication getSysUserAuthentication(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        SysUserAuthentication sysUserAuthentication = new SysUserAuthentication();
        BeanUtils.copy(sysUser, sysUserAuthentication);
        return sysUserAuthentication;
    }


    @Override
    public void insert(SysUserDTO sysUserDTO) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDTO, sysUser);
        this.insert(sysUser);
        sysUserDTO.setId(sysUser.getId());
    }

    @Override
    public void update(SysUserDTO sysUserDTO) {
        SysUser sysUser = this.selectById(sysUserDTO.getId());
        BeanUtils.copyProperties(sysUserDTO, sysUser);
        this.checkUser(sysUser);
        this.updateById(sysUser);
    }

    @Override
    public boolean insert(SysUser user) {

        this.checkUser(user);

        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(passwordStrategy.getPassword());
        }
        user.setPassword(SecurityUtils.encrypt(user.getPassword()));
        user.setStatus(CommonConstant.USER_STATUS_ENABLED);
        if (StringUtils.isEmpty(user.getAvatar())) {
            user.setAvatar("asset/img/avatar.png");
        }
        user.setDeleted(CommonConstant.COMMON_NO);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return super.insert(user);
    }


    /**
     * 检查是否有重复属性的数据
     *
     * @param property
     * @param value
     * @param excludeId
     * @return
     */
    public boolean checkProperty(String property, Object value, Long excludeId) {
        if (value == null || StringUtils.isEmpty(String.valueOf(value))) {
            return true;
        }
        List<SysUser> users = null;
        Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>();
        wrapper.eq(property, value);
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        users = this.mapper.selectList(wrapper);
        return (users.size() == 0);
    }

    /**
     * 检查用户名
     *
     * @return
     */
    private boolean checkUsername(SysUser sysUser) {
        return this.checkProperty("username", sysUser.getUsername(), sysUser.getId());
    }

    private boolean checkEmail(SysUser sysUser) {
        return this.checkProperty("email", sysUser.getEmail(), sysUser.getId());
    }

    private boolean checkPhoneNumber(SysUser sysUser) {
        return this.checkProperty("phone_number", sysUser.getPhoneNumber(), sysUser.getId());
    }

    @Override
    public List<SysUser> list() {
        return this.selectList();
    }

    /**
     * 检查用户数据重复项
     *
     * @param user
     * @return
     */
    public void checkUser(SysUser user) {
        boolean checkUsername = this.checkUsername(user);
        boolean checkEmail = this.checkEmail(user);
        boolean checkPhoneNumber = this.checkPhoneNumber(user);
        Assert.isTrue(checkUsername, "账号已存在");
        Assert.isTrue(checkEmail, "邮箱已存在");
        Assert.isTrue(checkPhoneNumber, "手机号已存在");
        //验证用户数据
        Set<ConstraintViolation<SysUser>> constraintViolations = validator.validate(user);
        if(constraintViolations.size() > 0){
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    @Override
    public boolean updateById(SysUser entity) {
        this.checkUser(entity);
        return super.updateById(entity);
    }


    @Override
    public void doResetPassword(Long id) {
        Assert.notNull(id, "用户ID错误");
        SysUser sysUser = this.selectById(id);
        sysUser.setPassword(passwordStrategy.getPassword());
        sysUser.setPassword(SecurityUtils.encrypt(sysUser.getPassword()));
        super.updateById(sysUser);
    }

    @Override
    public SysUser doDisable(Long id) {
        SysUser user = this.selectById(id);
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(user.getStatus().equals(CommonConstant.USER_STATUS_ENABLED), "用户未启用");
        user.setStatus(CommonConstant.USER_STATUS_DISABLED);
        this.updateById(user);
        return user;
    }

    @Override
    public SysUser doEnable(Long id) {
        SysUser user = this.selectById(id);
        Assert.notNull(user, "用户不存在");
        //判断是否是异常状态
        Assert.isTrue(!user.getStatus().equals(CommonConstant.USER_STATUS_ENABLED), "用户已启用");
        user.setStatus(CommonConstant.USER_STATUS_ENABLED);
        this.updateById(user);
        return user;
    }

    /**
     * 锁定
     *
     * @param id
     * @return
     */
    @Override
    public SysUser doLock(Long id) {
        SysUser user = this.selectById(id);
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(user.getStatus().equals(CommonConstant.USER_STATUS_ENABLED), "用户未启用");
        user.setStatus(CommonConstant.USER_STATUS_LOCKED);
        this.updateById(user);
        return user;
    }

    /**
     * 解锁
     *
     * @param id
     * @return
     */
    @Override
    public SysUser doUnlock(Long id) {
        SysUser user = this.selectById(id);
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(user.getStatus().equals(CommonConstant.USER_STATUS_LOCKED), "用户未锁定");
        user.setStatus(CommonConstant.USER_STATUS_ENABLED);
        this.updateById(user);
        return user;
    }

    @Override
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        Assert.isTrue(StringUtils.isNotEmpty(oldPassword) && StringUtils.isNotEmpty(newPassword), "新密码和旧密码不能为空");

        PasswordValidateResult passwordValidateResult = this.passwordStrategy.validate(newPassword);
        //如果密码认证没有通过则进行报错
        Assert.isTrue(passwordValidateResult.getPassed(), passwordValidateResult.getMessage());

        SysUser user = this.selectById(id);
        Assert.notNull(user, "用户不存在");

        String encyptedPassword = SecurityUtils.encrypt(oldPassword);
        Assert.isTrue(user.getPassword().equals(encyptedPassword), "旧密码不正确");
        String newEncyptedPassword = SecurityUtils.encrypt(newPassword);

        Assert.isTrue(!encyptedPassword.equals(newEncyptedPassword), "新旧密码不能相同");

        user.setPassword(newEncyptedPassword);
        super.updateById(user);
    }

    @Override
    public SysUserAuthentication findUserByUsername(String username) {
        SysUser sysUser = this.findOneByColumn("username", username);
        return this.getSysUserAuthentication(sysUser);
    }

    @Override
    public SysUserAuthentication findUserById(Long id) {
        return this.getSysUserAuthentication(this.selectById(id));
    }

    @Override
    public SysUserAuthentication findUserByPhoneNumber(String phoneNumber) {
        SysUser sysUser = this.findOneByColumn("phone_number", phoneNumber);
        return this.getSysUserAuthentication(sysUser);
    }

    /**
     * 物理删除用户信息
     */
    @Override
    public boolean deleteSysUser(String username) {
        EntityWrapper<SysUser> wrapper = this.newEntityWrapper();
        wrapper.setSqlSelect("username", username);
        Assert.hasText(username, "用户名不能为空");
        return this.delete(wrapper);
    }
}
