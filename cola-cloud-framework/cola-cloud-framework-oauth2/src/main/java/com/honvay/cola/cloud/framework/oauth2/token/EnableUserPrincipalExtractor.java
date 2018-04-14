package com.honvay.cola.cloud.framework.oauth2.token;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 提取OAuth2中返回的数据转成User对象
 * @author LIQIU
 * @date 2018-3-26
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(UserPrincipalExtractorConfiguration.class)
public @interface EnableUserPrincipalExtractor {

}
