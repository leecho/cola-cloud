package com.honvay.cola.cloud.organization.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.honvay.cola.cloud.framework.base.entity.TenancyEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 组织架构
 *
 * @author LIQIU
 */
@Data
@Accessors
@TableName("cola_sys_organization")
public class SysOrganization extends TenancyEntity implements Serializable {

    @TableId(type = IdType.AUTO, value = "id")
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
    @NotNull(message = "上级组织架构不能为空")
    private Long parent;

    /**
     * 组织代码
     */
    @NotNull(message = "组织编号不能为空")
    @Length(max = 50, message = "组织编号长度必须{max}个字符内")
    private String code;

    private String deleted;

    /**
     * 状态：10：停用，20：启用
     */
    private String status;
}
