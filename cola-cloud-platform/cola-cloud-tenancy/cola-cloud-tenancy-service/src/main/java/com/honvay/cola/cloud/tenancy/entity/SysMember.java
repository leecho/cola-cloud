package com.honvay.cola.cloud.tenancy.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.honvay.cola.cloud.framework.base.entity.TenancyEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author liqiu
 * @since 2018-01-04
 */
@Data
@Accessors(chain = true)
@TableName("cola_sys_member")
public class SysMember extends TenancyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("sys_user_id")
    private Long sysUserId;
    @TableField("created_by")
    private Long createdBy;
    @TableField("creation_date")
    private Date creationDate;


}
