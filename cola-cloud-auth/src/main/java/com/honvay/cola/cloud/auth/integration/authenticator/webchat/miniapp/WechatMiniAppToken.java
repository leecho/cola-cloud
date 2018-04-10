package com.honvay.cola.cloud.auth.integration.authenticator.webchat.miniapp;

import lombok.Data;

/**
 * @author LIQIU
 * @date 2018-4-9
 **/
@Data
public class WechatMiniAppToken {

    private String openId;
    private String unionId;
    private String sessionKey;
    private String username;
    private String encryptedData;
    private String iv;

    public WechatMiniAppToken(String openId, String unionId, String sessionKey) {
        this.openId = openId;
        this.unionId = unionId;
        this.sessionKey = sessionKey;
    }
}
