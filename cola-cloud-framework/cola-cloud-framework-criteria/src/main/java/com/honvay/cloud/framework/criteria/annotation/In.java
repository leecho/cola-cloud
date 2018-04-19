package com.honvay.cloud.framework.criteria.annotation;

import com.honvay.cloud.framework.criteria.annotation.group.InGroup;

import java.lang.annotation.*;

/**
 * @author LIQIU
 * @date 2018-4-17
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(InGroup.class)
public @interface In {

    /**
     * 是否允许为空
     *
     * @return
     */
    boolean allowEmpty() default false;

    /**
     * 字段
     * @return
     */
    String[] columns() default {};

    /**
     * 分组
     * @return
     */
    String group() default "";

    /**
     * 字符串分隔符
     * @return
     */
    String delimiter() default ",";

    /**
     * 数据类型
     * @return
     */
    Class<?> dataType() default String.class;

    /**
     * 数据格式
     * @return
     */
    String format() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 是否允许为空
     * @return
     */
    String defaultValue() default "";

    /**
     * 是否启用
     * @return
     */
    boolean enable() default true;

    /**
     * 反转，反转为 not in
     * @return
     */
    boolean reverse() default false;
}
