package com.honvay.cola.cloud.auth.integration.authenticator.sms.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author LIQIU
 * @date 2018-3-31
 **/
public class SmsAuthenticateSuccessEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public SmsAuthenticateSuccessEvent(Object source) {
        super(source);
    }
}
