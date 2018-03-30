package com.honvay.cola.cloud.upm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.honvay.cola.cloud.framework.base.entity.TenancyEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户权限吧
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
@Data
@Accessors
@ToString
@TableName("cola_sys_role")
public class SysRole extends TenancyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租户用户的角色
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long   id;
    /**
     * 角色名称
     */
    @NotNull(message = "角色名称不能为空")
    @Length(max = 50, message = "角色名称长度必须{max}个字符内")
    private String name;
    /**
     * 角色编码
     */
    @NotNull(message = "角色编号不能为空")
    @Length(max = 20, message = "角色编号长度必须在{max}个字符内")
    private String code;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date   createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date   updateTime;
    /**
     * 状态: Y-激活状态    N-锁定状态
     */
    private String status;
    /**
     * 删除状态: Y-已删除,N-未删除
     */
    private String deleted;
    /**
     * 备注
     */
    private String description;
}
