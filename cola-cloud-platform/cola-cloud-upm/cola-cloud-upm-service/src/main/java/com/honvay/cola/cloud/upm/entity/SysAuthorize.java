package com.honvay.cola.cloud.upm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 权限-资源中间表
 * </p>
 *
 * @author LIQIU
 */
@Data
@Accessors(chain = true)
@TableName("cola_sys_authorize")
public class SysAuthorize implements Serializable {
    @TableId("id")
    private Long   id;
    @NotNull(message = "授权对象不能为空")
    @TableField("authorize_target")
    private Long   authorizeTarget;
    @NotNull(message = "角色ID不能为空")
    @TableField("sys_role_id")
    private Long   sysRoleId;
    @TableId("authorize_type")
    @NotNull(message = "授权对象类型不能为空")
    private String authorizeType;

}
