package com.honvay.cola.cloud.organization.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 *
 * @author liqiu
 * @since 2018-01-04
 */
@Data
@Accessors(chain = true)
@TableName("cola_sys_employee")
public class SysEmployee implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long sysUserId;
    private Long sysOrgId;
    private Long createBy;
    private Date createDate;
    private Long modifyBy;
    private Date modifyDate;
    private Long sysPostId;
    private String status;
}
