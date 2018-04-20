package com.honvay.cola.cloud.tenancy.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cola.cloud.client.SysMemberDTO;
import com.honvay.cola.cloud.framework.base.service.impl.TenancyServiceImpl;
import com.honvay.cola.cloud.tenancy.entity.SysMember;
import com.honvay.cola.cloud.tenancy.service.SysMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liqiu
 * @date 2018-01-04
 */
@Service
public class SysMemberServiceImpl extends TenancyServiceImpl<SysMember> implements SysMemberService {

    @Override
    public List<SysMember> getMembersByUserId(Long userId) {
        EntityWrapper<SysMember> wrapper = this.newEntityWrapper();
        wrapper.eq("sys_user_id",userId);
        return this.selectList(wrapper);
    }

    @Override
    public SysMember getMembers(SysMemberDTO sysMemberDTO) {
        EntityWrapper<SysMember> wrapper = this.newEntityWrapper();
        wrapper.eq("sys_user_id",sysMemberDTO.getSysUserId());
        wrapper.eq("tenant_id",sysMemberDTO.getTenantId());
        return this.selectOne(wrapper);
    }

    @Override
    public void delete(SysMemberDTO sysMemberDTO) {
        EntityWrapper<SysMember> wrapper = this.newEntityWrapper();
        wrapper.eq("sys_user_id",sysMemberDTO.getSysUserId());
        wrapper.eq("tenant_id",sysMemberDTO.getTenantId());
        this.delete(wrapper);
    }
}
