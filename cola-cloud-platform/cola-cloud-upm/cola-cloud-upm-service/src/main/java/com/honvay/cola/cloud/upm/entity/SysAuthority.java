package com.honvay.cola.cloud.upm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 权限-资源中间表
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("cola_sys_authority")
public class SysAuthority implements Serializable {
    
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("sys_role_id")
    @NotNull(message = "角色ID不能为空")
    private Long sysRoleId;

    @TableField("sys_resource_id")
    @NotNull(message = "资源ID不能为空")
    private Long sysResourceId;
    
}

