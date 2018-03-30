package com.honvay.cola.cloud.framework.base.audit;

import java.lang.annotation.*;

/**
 * 类SysLog的功能描述:
 * 系统日志注解
 *
 * @auther hxy
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableAudit {

    String value() default "";

}