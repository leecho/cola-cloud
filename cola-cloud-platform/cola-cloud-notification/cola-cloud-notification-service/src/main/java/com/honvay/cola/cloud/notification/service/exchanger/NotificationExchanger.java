package com.honvay.cola.cloud.notification.service.exchanger;

import com.honvay.cola.cloud.notification.model.Notification;

/**
 * @author LIQIU
 * @date 2018-3-27
 **/
public interface NotificationExchanger {

    boolean support(Object notification);

    boolean exchange(Notification notification);
}
