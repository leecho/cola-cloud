package com.honvay.cola.cloud.dict.client.jackson;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解之后会自动配置SpringMVC的MappingJackson2HttpMessageConverter用户转换数据字典文本
 *
 * @author LIQIU
 * @date 2018-4-9
 **/
@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(JacksonDictConfiguration.class)
public @interface EnableJsonDict {
}
