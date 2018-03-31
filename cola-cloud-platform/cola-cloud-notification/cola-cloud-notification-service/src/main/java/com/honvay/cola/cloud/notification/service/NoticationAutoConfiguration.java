package com.honvay.cola.cloud.notification.service;

import com.honvay.cola.cloud.framework.mail.MailSender;
import com.honvay.cola.cloud.framework.sms.SmsSender;
import com.honvay.cola.cloud.framework.starter.mail.MailAutoConfiguration;
import com.honvay.cola.cloud.framework.starter.sms.SmsAutoConfiguration;
import com.honvay.cola.cloud.notification.service.exchanger.EmailNoficationExchanger;
import com.honvay.cola.cloud.notification.service.exchanger.SmsNotificationExchanger;
import com.honvay.cola.cloud.notification.service.exchanger.WebSocketNotificationExchanger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LIQIU
 * @date 2018-3-27
 **/
@Configuration
@EnableAutoConfiguration
@AutoConfigureAfter({SmsAutoConfiguration.class, MailAutoConfiguration.class})
public class NoticationAutoConfiguration {

    @Autowired(required = false)
    private SmsSender smsSender;

    @Autowired(required = false)
    private MailSender mailSender;

    @Bean
    public SmsNotificationExchanger smsNotifcationExchanger(){
        return new SmsNotificationExchanger(smsSender);
    }

    @Bean
    public EmailNoficationExchanger emailNoficationExchanger(){
        return new EmailNoficationExchanger(mailSender);
    }

    @Bean
    @ConditionalOnProperty(name="cola.notification.websocket.enable",matchIfMissing = false)
    public WebSocketNotificationExchanger webSocketNotificationExchanger(){
        return new WebSocketNotificationExchanger();
    }
}
