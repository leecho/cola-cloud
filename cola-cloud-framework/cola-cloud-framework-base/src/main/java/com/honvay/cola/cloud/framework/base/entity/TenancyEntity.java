package com.honvay.cola.cloud.framework.base.entity;

import com.baomidou.mybatisplus.annotations.TableField;

import java.io.Serializable;

/**
 * 租户类实体
 *
 * @author LIQIU
 * @date 2017-12-20
 */
public class TenancyEntity implements Serializable {

    @TableField("tenant_id")
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
    
}
