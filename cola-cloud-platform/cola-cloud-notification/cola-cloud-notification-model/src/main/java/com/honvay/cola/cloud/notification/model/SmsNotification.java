package com.honvay.cola.cloud.notification.model;

import lombok.Data;

import java.util.Map;

/**
 * @author LIQIU
 * @date 2018-3-27
 **/
@Data
public class SmsNotification extends Notification{

    private String phoneNumber;

    private String templateCode;

    private Map<String,Object> params;

    private String signName;

}
