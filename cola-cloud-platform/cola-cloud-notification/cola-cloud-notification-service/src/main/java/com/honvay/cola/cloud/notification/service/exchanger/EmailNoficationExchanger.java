package com.honvay.cola.cloud.notification.service.exchanger;

import com.honvay.cola.cloud.framework.mail.MailSender;
import com.honvay.cola.cloud.notification.model.EmailNotification;
import com.honvay.cola.cloud.notification.model.Notification;

/**
 * @author LIQIU
 * @date 2018-3-27
 **/
public class EmailNoficationExchanger implements NotificationExchanger{

    private MailSender mailSender;

    public EmailNoficationExchanger(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean support(Object notification) {
        return notification.getClass().equals(EmailNotification.class);
    }

    @Override
    public boolean exchange(Notification notification) {
        return false;
    }
}
