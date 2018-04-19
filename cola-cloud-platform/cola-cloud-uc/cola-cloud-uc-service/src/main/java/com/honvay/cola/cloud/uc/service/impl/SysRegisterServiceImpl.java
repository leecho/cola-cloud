package com.honvay.cola.cloud.uc.service.impl;

import com.honvay.cola.cloud.framework.base.service.impl.BaseServiceImpl;
import com.honvay.cola.cloud.uc.entity.SysRegister;
import com.honvay.cola.cloud.uc.entity.SysUser;
import com.honvay.cola.cloud.uc.model.SysRegisterDTO;
import com.honvay.cola.cloud.uc.service.SysRegisterService;
import com.honvay.cola.cloud.uc.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p> 注册信息表 服务实现类 </p>
 *
 * @author bojieshen
 * @date 2018-03-14
 */
@Service
public class SysRegisterServiceImpl extends BaseServiceImpl<SysRegister> implements
        SysRegisterService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void register(SysRegisterDTO sysRegisterDTO) {

        // 新增手机注册用户
        SysUser sysUser = new SysUser();
        sysUser.setPhoneNumber(sysRegisterDTO.getPhoneNumber());
        String name = "colauser" + System.currentTimeMillis();
        sysUser.setUsername(name);
        sysUser.setName(name);
        Date date = new Date();
        sysUser.setCreateTime(date);
        sysUser.setUpdateTime(date);
        sysUserService.insert(sysUser);

        // 新增注册信息
        SysRegister sysRegister = new SysRegister();
        BeanUtils.copyProperties(sysRegisterDTO, sysRegister);
        sysRegister.setRegisterSysUserId(sysUser.getId());
        sysRegister.setCreateTime(date);
        sysRegister.setUpdateTime(date);
        this.insert(sysRegister);
    }
}
