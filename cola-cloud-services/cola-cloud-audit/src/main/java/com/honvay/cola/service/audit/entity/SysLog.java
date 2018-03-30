package com.honvay.cola.service.audit.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * EnableAudit
 *
 * @author YRain
 */
@Data
@TableName("cola_sys_log")
public class SysLog implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;                    // 流水号

    private String type;                // 日志类型:登录日志;系统日志;
    private String name;                // 业务名称

    private String requestUrl;          // 请求地址
    private String requestType;         // 请求方法类型
    private String requestClass;        // 调用类
    private String requestMethod;       // 调用方法
    private String requestParameter;    // 调用参数
    private String requestIp;           // 请求IP

    private Long    userId;             // 创建用户ID
    private String  userName;           // 创建用户名称
    private Date    createTime;         // 创建时间
    private Long    time;               // 消耗时间
    private boolean success;            // 是否成功
    private String  exception;          // 异常信息

}