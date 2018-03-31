package com.honvay.cola.cloud.duplication.annotion;

import java.lang.annotation.*;

/**
 * 验证防重复提交
 *
 * @author LIQIU
 * @date 2017-12-06-下午2:55
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DuplicationVerify {

}
