package com.honvay.cola.cloud.tenancy.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.tenancy.entity.SysTenant;
import com.honvay.cola.cloud.tenancy.model.SysTenantDTO;
import com.honvay.cola.cloud.tenancy.model.SysTenantVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统管理-租户管理
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
public interface SysTenantService extends BaseService<SysTenant> {


    /**
     * 保存租户
     * @param sysTenantDTO
     */
    void save(SysTenantDTO sysTenantDTO);

    /**
     * 修改租户
     * @param sysTenantVO
     */
    void update(SysTenantVO sysTenantVO);

    /**
     * 删除租户，逻辑删除
     * @param id
     */
    void delete(Long id);

    /**
     * 禁用租户
     * @param id
     * @return
     */
    SysTenant disable(Long id);

    /**
     * 启用租户
     * @param id
     * @return
     */
    SysTenant enable(Long id);

    /**
     * 设置系统管理员
     * @param tenantId
     * @param userId
     */
    void setAdministrator(Long tenantId, Long userId);

    /**
     * 查询租户列表
     * @param page
     * @param code
     * @param name
     * @return
     */
    Page<List<SysTenantVO>> getTenantList(Page page, String code, String name);
}
