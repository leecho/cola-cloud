package com.honvay.cola.cloud.uc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cola.cloud.framework.base.service.impl.BaseServiceImpl;
import com.honvay.cola.cloud.uc.entity.SysSocial;
import com.honvay.cola.cloud.uc.service.SysSocialService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liqiu
 * @date 2018-01-04
 */
@Service
public class SysSocialServiceImpl extends BaseServiceImpl<SysSocial> implements SysSocialService {

    @Override
    public SysSocial getSocialByTokenAndType(String token, String type) {
        EntityWrapper<SysSocial> wrapper = new EntityWrapper<>();
        wrapper.eq("token",token);
        wrapper.eq("type",type);
        return this.selectOne(wrapper);
    }
}
