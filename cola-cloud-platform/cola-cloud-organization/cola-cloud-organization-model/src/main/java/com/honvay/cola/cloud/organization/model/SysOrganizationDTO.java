package com.honvay.cola.cloud.organization.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;


/**
 * 组织架构
 *
 * @author LIQIU
 */
@Data
public class SysOrganizationDTO  {

    private Long id;

    /**
     * 组织名称
     */
    @NotNull(message = "组织名称不能为空")
    @Length(max = 100, message = "组织名称长度必须{max}个字符内")
    private String name;

    /**
     * 组织描述
     */
    private String description;

    /**
     * 父组织编号
     */
    @NotNull(message = "上级部门不能为空")
    private Long parent;

    /**
     * 组织代码
     */
    @NotNull(message = "组织编号不能为空")
    @Length(max = 50, message = "组织编号长度必须{max}个字符内")
    private String code;

    private Long tenantId;

    private String deleted;
}
