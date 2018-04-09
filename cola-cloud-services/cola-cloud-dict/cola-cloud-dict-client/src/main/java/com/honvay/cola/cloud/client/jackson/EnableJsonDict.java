package com.honvay.cola.cloud.client.jackson;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LIQIU
 * @date 2018-4-9
 **/
@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(JacksonDictConfiguration.class)
public @interface EnableJsonDict {
}
