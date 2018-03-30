package com.honvay.cola.cloud.uc.model;


import com.honvay.cola.cloud.framework.base.entity.UserEntity;

/**
 * @author LIQIU
 * @date 2018-1-8
 **/
public class SysUserVO implements UserEntity {

    private Long id;
    /**
     * 登录账号
     */
    private String username;
    /**
     * 名称
     */
    private String name;
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 电子邮箱
     */
    private String email;
    private String avatar;
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setUsername(String loginName) {
        this.username = username;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String fullName) {
        this.name = fullName;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void setPassword(String password) {

    }

    @Override
    public String getSalt() {
        return null;
    }

    @Override
    public void setSalt(String salt) {

    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
