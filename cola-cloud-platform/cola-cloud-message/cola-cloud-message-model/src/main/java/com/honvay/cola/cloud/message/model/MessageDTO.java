package com.honvay.cola.cloud.message.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Map;

/**
 * @author LIQIU
 * @date 2018-3-26
 **/
@Data
public class MessageDTO {

    /**
     * 用户id
     */
    @NotEmpty(message = "接收人不能为空")
    private Long sysUserId;

    /**
     * 标题
     */
    @NotEmpty(message = "消息标题不能为空")
    private String title;

    /**
     * 内容
     */
    @NotEmpty(message = "消息内容不能为空")
    private String content;
    /**
     * 是否发送邮件：-1不发送 0 待发送，1已发送
     */
    private Boolean sendSms;
    /**
     * 是否发送短信：-1不发送 0 待发送，1已发送
     */
    private Boolean sendEmail;

    /**
     * 短息模板
     */
    private String templateCode;
    private Map<String,Object> smsParams;
    private String signName;
    /**
     * 读标志：N未读，Y已读
     */
    private String readFlag;
    /**
     * 删除标志：N未删除，Y已删除
     */
    private String deleteFlag;
    /**
     * 扩展字段
     */
    private String extend;
    /**
     * 业务类型
     */
    private String bizType;
}
