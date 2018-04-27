package com.honvay.cola.cloud.upm.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author cola
 * @since 2017-12-11
 */
@Data
@Accessors
@TableName("cola_sys_resource")
public class SysResource implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long    id;
    /**
     * 权限的标识
     */
    private String  code;
    /**
     * 父id
     */
    private Long parent;
    /**
     * 权限名称
     */
    private String  name;
    /**
     * 权限请求路径
     */
    private String  url;
    /**
     * 图标
     */
    private String  icon;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否可用：Y-可用；N-不可用
     */
    private String  status;
    /**
     * 加载类型 0：普通加载，1、IFRAME加载
     */
    @TableField("load_type")
    private String  loadType;

    /**
     * 类型 0：目录，1：菜单，2：功能
     */
    private Integer type;

    private String description;

    /**
     * 路由
     */
    private String route;

    /**
     * 所属服务
     */
    private String serviceId;
}
