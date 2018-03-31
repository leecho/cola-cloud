package com.honvay.cola.cloud.auth.integration.token;

import lombok.Data;

/**
 * @author LIQIU
 * @date 2018-3-31
 **/
@Data
public class SmsAuthenticationToken extends IntegrationAuthenticationToken{
    private String code;
    private String token;
}
