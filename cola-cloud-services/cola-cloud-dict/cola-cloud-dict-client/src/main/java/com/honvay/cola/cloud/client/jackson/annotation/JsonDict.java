package com.honvay.cola.cloud.client.jackson.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LIQIU
 * @date 2018-4-9
 **/
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE,
        ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
public @interface JsonDict {
    String[] value();
}
