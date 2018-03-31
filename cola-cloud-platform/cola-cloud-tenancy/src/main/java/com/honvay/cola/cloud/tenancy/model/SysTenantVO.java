package com.honvay.cola.cloud.tenancy.model;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 租户表
 * </p>
 *
 * @author LIQIU
 * @since 2017-11-23
 */
@Data
public class SysTenantVO implements Serializable {

    /**
     * 租户id
     */
    private Long   id;
    /**
     * 租户名称
     */
    private String name;
    /**
     * 简介
     */
    private String description;
    /**
     * 租户编码
     */
    private String code;

    private String administrator;

    private String status;

}
