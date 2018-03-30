package com.honvay.cola.cloud.notification.service;

import com.honvay.cola.cloud.notification.model.Notification;
import com.honvay.cola.cloud.notification.service.exchanger.NotificationExchanger;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * @author LIQIU
 * @date 2018-3-27
 **/
@Component
@RabbitListener(queues = "${cola.notification.queue.name:notification-queue}")
public class NotificationDispatcher implements ApplicationContextAware{

    private Logger logger = Logger.getLogger(this.getClass());

    private Collection<NotificationExchanger> exchangers;

    @Value("${cola.notification.queue.name:notification-queue}")
    public String notificationQueueName;

    @Bean
    public Queue notificationQueue(){
        return new Queue(notificationQueueName);
    }

    @Autowired
    private RabbitTemplate template;

    @RabbitHandler
    public void dispatch(@Payload Notification notification){
        if(notification != null && exchangers != null){
            Notification finalNotification = notification;
            exchangers.forEach((exchanger) -> {
                if(exchanger.support(finalNotification)){
                    exchanger.exchange(finalNotification);
                }
            });
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,NotificationExchanger> beansOfType = applicationContext.getBeansOfType(NotificationExchanger.class);
        this.exchangers = beansOfType.values();
    }
}
