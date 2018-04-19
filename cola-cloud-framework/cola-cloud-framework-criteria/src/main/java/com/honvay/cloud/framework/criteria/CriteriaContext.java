package com.honvay.cloud.framework.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.lang.annotation.Annotation;

/**
 * @author LIQIU
 * @date 2018-4-18
 **/
@Data
@AllArgsConstructor
@ToString
public class CriteriaContext<S extends Annotation> {

    /**
     * 条件类型
     */
    private S type;

    /**
     * 字段的值
     */
    private Object value;

    /**
     * 字段名称
     */
    private String property;

    /**
     * 查询对象
     */
    private Criteria criteria;

    /**
     * 字段数据类型
     */
    private Class<?> dataType;
}
