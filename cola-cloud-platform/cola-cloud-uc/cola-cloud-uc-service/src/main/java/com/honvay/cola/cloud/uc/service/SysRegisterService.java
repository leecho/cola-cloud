package com.honvay.cola.cloud.uc.service;

import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.uc.entity.SysRegister;
import com.honvay.cola.cloud.uc.model.SysRegisterDTO;

/**
 * <p>
 * 注册信息表 服务类
 * </p>
 *
 * @author bojieshen
 * @date 2018-03-14
 */
public interface SysRegisterService extends BaseService<SysRegister> {

  void register(SysRegisterDTO sysRegisterDTO);
}
