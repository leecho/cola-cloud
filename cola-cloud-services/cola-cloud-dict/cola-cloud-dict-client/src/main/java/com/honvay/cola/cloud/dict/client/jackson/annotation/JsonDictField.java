package com.honvay.cola.cloud.dict.client.jackson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author LIQIU
 * @date 2018-4-9
 **/
@Target({ElementType.TYPE})
public @interface JsonDictField {

    String dictCode();

    String valueField();

    String name();
}
