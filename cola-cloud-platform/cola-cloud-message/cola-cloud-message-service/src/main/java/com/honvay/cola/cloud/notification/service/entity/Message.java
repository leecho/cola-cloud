package com.honvay.cola.cloud.notification.service.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 站内信表
 * </p>
 *
 * @author LIQIU
 * @since 2018-03-13
 */
@Data
@TableName("cola_sys_message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
    /**
     * 用户id
     */
	@TableField("sys_user_id")
	private Long sysUserId;

	/**
	 * 标题
	 */
	private String title;

    /**
     * 内容
     */
	private String content;
    /**
     * 是否发送邮件：-1不发送 0 待发送，1已发送
     */
	@TableField("email_status")
	private String emailStatus;
    /**
     * 是否发送短信：-1不发送 0 待发送，1已发送
     */
	@TableField("sms_status")
	private String smsStatus;

	/**
	 * 短息模板
	 */
	@TableField("template_code")
	private String templateCode;
	@TableField("sms_params")
	private String smsParams;
    /**
     * 读标志：N未读，Y已读
     */
	@TableField("read_flag")
	private String readFlag;
    /**
     * 删除标志：N未删除，Y已删除
     */
	@TableField("delete_flag")
	private String deleteFlag;
    /**
     * 扩展字段
     */
	private String extend;
	@TableField("create_time")
	private Date createTime;
	@TableField("create_by")
	private Long createBy;

    /**
     * 业务类型
     */
	private String bizType;

}
