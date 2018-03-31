package com.honvay.cola.cloud.notification.service;

import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.notification.model.EmailNotification;
import com.honvay.cola.cloud.notification.model.SmsNotification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LIQIU
 * @date 2018-3-27
 **/
@RestController("/notification")
@Api(value="notification",tags = "通知中心")
public class NotificationController{

    @Autowired
    private NotificationDispatcher dispatcher;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ApiOperation("/短信通知")
    @PostMapping("/send/sms")
    public Result<String> sendSms(SmsNotification smsNotification){
        this.dispatcher.dispatch(smsNotification);
        return Result.buildSuccess("");
    }

    @ApiOperation("/邮件通知")
    @PostMapping("/send/email")
    public Result<String> sendEmail(EmailNotification emailNotification){
        this.dispatcher.dispatch(emailNotification);
        return Result.buildSuccess("");
    }

}
