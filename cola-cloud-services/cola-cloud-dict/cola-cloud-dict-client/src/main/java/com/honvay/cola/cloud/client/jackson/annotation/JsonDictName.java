package com.honvay.cola.cloud.client.jackson.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.honvay.cola.cloud.client.jackson.serializer.JsonDictNameSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LIQIU
 * @date 2018-4-9
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = JsonDictNameSerializer.class,nullsUsing = JsonDictNameSerializer.class)
public @interface JsonDictName {
    String valueField() default "";
    String dictCode();
}
