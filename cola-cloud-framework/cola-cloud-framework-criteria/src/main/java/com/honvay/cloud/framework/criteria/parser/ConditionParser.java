package com.honvay.cloud.framework.criteria.parser;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cloud.framework.criteria.CriteriaContext;

import java.lang.annotation.Annotation;

/**
 * @author LIQIU
 **/
public interface ConditionParser<S extends Annotation> {

    /**
     * 解析条件带分组
     * @param criteriaContext
     * @param entityWrapper
     * @return
     */
    void parse(CriteriaContext<S> criteriaContext, EntityWrapper entityWrapper);

    /**
     * 获取分组
     * @param s
     * @return
     */
    String getGroup(S s);

    /**
     * 是否允许为空
     * @param s
     * @return
     */
    boolean allowEmpty(S s);

    /**
     * 是否启用
     * @param s
     * @return
     */
    boolean isEnable(S s);
}
