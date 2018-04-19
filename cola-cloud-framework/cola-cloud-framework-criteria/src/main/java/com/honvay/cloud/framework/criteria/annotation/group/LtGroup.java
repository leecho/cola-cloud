package com.honvay.cloud.framework.criteria.annotation.group;

import com.honvay.cloud.framework.criteria.annotation.Lt;

import java.lang.annotation.*;

/**
 * @author LIQIU
 * @date 2018-4-18
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LtGroup {

    Lt[] value();
}
