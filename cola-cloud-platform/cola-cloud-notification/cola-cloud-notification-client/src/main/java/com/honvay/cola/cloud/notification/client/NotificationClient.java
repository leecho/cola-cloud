package com.honvay.cola.cloud.notification.client;

import com.honvay.cola.cloud.notification.model.Notification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author LIQIU
 * @date 2018-3-27
 **/
@Component
public class NotificationClient {

    @Autowired
    private RabbitTemplate template;

    @Value("${cola.notification.queue.name:notification-queue}")
    private String notificationQueue;

    public void send(Notification notification){
        template.convertAndSend(notificationQueue,notification);
    }

}
