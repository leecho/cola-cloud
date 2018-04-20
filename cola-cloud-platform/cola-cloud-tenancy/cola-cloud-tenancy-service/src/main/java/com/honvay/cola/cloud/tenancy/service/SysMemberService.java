package com.honvay.cola.cloud.tenancy.service;

import com.honvay.cola.cloud.client.SysMemberDTO;
import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.tenancy.entity.SysMember;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liqiu
 * @date 2018-01-04
 */
public interface SysMemberService extends BaseService<SysMember> {

    /**
     * 根据用户ID获取成员列表
     * @param userId
     * @return
     */
   List<SysMember> getMembersByUserId(Long userId);

    /**
     * 根据租户ID和用户ID获取成员
     * @param sysMemberDTO
     * @return
     */
   SysMember getMembers(SysMemberDTO sysMemberDTO);

    /**
     * 根据租户ID和用户ID删除用户
     * @param sysMemberDTO
     */
    void delete(SysMemberDTO sysMemberDTO);
}
