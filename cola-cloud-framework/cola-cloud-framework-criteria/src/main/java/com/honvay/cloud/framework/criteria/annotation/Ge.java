package com.honvay.cloud.framework.criteria.annotation;

import com.honvay.cloud.framework.criteria.annotation.group.GeGroup;

import java.lang.annotation.*;

/**
 * @author LIQIU
 * @date 2018-4-17
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(GeGroup.class)
public @interface Ge {

    /**
     * 允许为空
     * @return
     */
    boolean allowEmpty() default false;

    /**
     * 字段名称
     * @return
     */
    String[] columns() default {};

    /**
     * 查询分组
     * @return
     */
    String group() default "";

    /**
     * 默认值
     * @return
     */
    String defaultValue() default "";

    /**
     * 是否启用
     * @return
     */
    boolean enable() default true;
}
