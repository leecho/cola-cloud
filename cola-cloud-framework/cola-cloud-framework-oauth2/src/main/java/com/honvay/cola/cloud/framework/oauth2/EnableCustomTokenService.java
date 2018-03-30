package com.honvay.cola.cloud.framework.oauth2;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 客户端获取认证信息时匹配协议
 * @author LIQIU
 * @date 2018-3-26
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(TokenServiceConfiguration.class)
public @interface EnableCustomTokenService {

}
