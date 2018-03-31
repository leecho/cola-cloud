package com.honvay.cola.cloud.tenancy.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.honvay.cola.cloud.framework.base.entity.TenantEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 租户表
 * </p>
 *
 * @author LIQIU
 * @since 2017-11-23
 */
@Data
@Accessors
@TableName("cola_sys_tenant")
public class SysTenant implements TenantEntity {

    /**
     * 租户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long    id;
    /**
     * 租户名称
     */
    private String  name;
    /**
     * 简介
     */
    private String  description;
    /**
     * logo图片地址
     */
    private String  logo;
    /**
     * 租户编码
     */
    private String  code;
    /**
     * 地址
     */
    private String  address;
    /**
     * 租户生效时间
     */
    @TableField("begin_time")
    private Date    beginTime;
    /**
     * 租户到期时间
     */
    @TableField("end_time")
    private Date    endTime;
    /**
     * 审核详情
     */
    @TableField("check_info")
    private String  checkInfo;
    /**
     * 审核通过时间
     */
    @TableField("check_time")
    private Date    checkTime;
    /**
     * 最大用户数
     */
    @TableField("max_user")
    private Integer maxUser;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date    createTime;
    /**
     * 状态: 1 启用, 2 禁用
     */
    private Integer status;
    /**
     * 电话
     */
    private String  tel;
    /**
     * 审核管理员id
     */
    @TableField("checker_id")
    private Long    checkerId;
    /**
     * 创建管理员id
     */
    @TableField("creator_id")
    private Long    creatorId;
    /**
     * 域名
     */
    private String  domain;
    /**
     * 网址
     */
    private String  url;
    /**
     * 删除状态: Y-已删除,N-未删除
     */
    @TableField("deleted")
    private String  deleted;

    private Long administrator;
}
