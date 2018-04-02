package com.honvay.cola.cloud.notification.service;

import com.honvay.cola.cloud.notification.model.Notification;
import com.honvay.cola.cloud.notification.service.exchanger.NotificationExchanger;

import java.util.concurrent.Callable;

/**
 * @author LIQIU
 * @date 2018-3-31
 **/
public class NotificationTask implements Callable<Boolean> {

    private NotificationExchanger notificationExchanger;

    private Notification notification;

    public NotificationTask(NotificationExchanger notificationExchanger,Notification notification){
        this.notificationExchanger = notificationExchanger;
        this.notification = notification;
    }

    @Override
    public Boolean call() throws Exception {
        return notificationExchanger.exchange(notification);
    }
}
