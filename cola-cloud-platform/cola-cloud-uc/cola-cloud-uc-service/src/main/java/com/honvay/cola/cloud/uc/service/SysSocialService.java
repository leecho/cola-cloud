package com.honvay.cola.cloud.uc.service;

import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.uc.entity.SysSocial;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liqiu
 * @date 2018-01-04
 */
public interface SysSocialService extends BaseService<SysSocial> {

    /**
     * 通过TOKEN和Type获取第三方登录配置
     * @param token
     * @param type
     * @return
     */
    SysSocial getSocialByTokenAndType(String token, String type);
}
