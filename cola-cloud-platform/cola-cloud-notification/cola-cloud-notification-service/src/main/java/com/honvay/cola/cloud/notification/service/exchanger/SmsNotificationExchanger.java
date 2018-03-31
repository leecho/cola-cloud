package com.honvay.cola.cloud.notification.service.exchanger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honvay.cola.cloud.framework.sms.SmsParameter;
import com.honvay.cola.cloud.framework.sms.SmsSendResult;
import com.honvay.cola.cloud.framework.sms.SmsSender;
import com.honvay.cola.cloud.framework.util.StringUtils;
import com.honvay.cola.cloud.notification.model.Notification;
import com.honvay.cola.cloud.notification.model.SmsNotification;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * 短信通知转发器，可以配置<b>cola.notification.sms.sign-name</b>设置默认的短信签名
 * @author LIQIU
 * @date 2018-3-27
 **/
public class SmsNotificationExchanger implements NotificationExchanger {

    private Logger logger = Logger.getLogger(this.getClass());

    private SmsSender smsSender;

    private final static String STATUS_OK = "OK";

    public SmsNotificationExchanger(SmsSender smsSender) {
        if (smsSender != null) {
            logger.info("初始化短信通知组件");
        }
        this.smsSender = smsSender;
    }

    @Value("cola.notification.sms.sign-name")
    private String signName;

    @Override
    public boolean support(Object notification) {
        return notification.getClass().equals(SmsNotification.class);
    }

    @Override
    public boolean exchange(Notification notification) {

        Assert.notNull(smsSender, "短信接口没有初始化");

        SmsNotification smsNotification = (SmsNotification) notification;
        SmsParameter parameter = new SmsParameter();
        parameter.setPhoneNumbers(Arrays.asList(smsNotification.getPhoneNumber()));
        parameter.setTemplateCode(smsNotification.getTemplateCode());

        if(StringUtils.isEmpty(smsNotification.getSignName())){
            smsNotification.setSignName(this.signName);
        }

        Assert.notNull(smsNotification.getSignName(),"短信签名不能为空");

        parameter.setSignName(smsNotification.getSignName());
        if (smsNotification.getParams() != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                parameter.setParams(mapper.writeValueAsString(smsNotification.getParams()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("格式化短信参数失败");
            }
        }
        SmsSendResult smsSendResult = smsSender.send(parameter);
        return STATUS_OK.equals(smsSendResult.getCode());
    }
}
