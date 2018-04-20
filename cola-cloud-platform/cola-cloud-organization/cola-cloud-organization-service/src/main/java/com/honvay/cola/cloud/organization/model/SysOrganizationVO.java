package com.honvay.cola.cloud.organization.model;

import lombok.Data;

/**
 * @author LIQIU
 * @date 2018-3-20
 **/
@Data
public class SysOrganizationVO {


    private Long id;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织描述
     */
    private String description;

    /**
     * 父组织编号
     */
    private Long pid;

    /**
     * 组织代码
     */
    private String code;

    private String deleted;

    private String pcode;

    private String pname;

    private String status;
}
