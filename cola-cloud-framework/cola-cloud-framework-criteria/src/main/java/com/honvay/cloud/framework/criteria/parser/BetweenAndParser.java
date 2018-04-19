package com.honvay.cloud.framework.criteria.parser;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cloud.framework.criteria.CriteriaContext;
import com.honvay.cloud.framework.criteria.annotation.BetweenAnd;
import com.honvay.cola.cloud.framework.util.ConvertUtils;
import com.honvay.cola.cloud.framework.util.ObjectUtils;
import com.honvay.cola.cloud.framework.util.StringUtils;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author LIQIU
 * @date 2018-4-17
 **/
public class BetweenAndParser extends AbstractParser<BetweenAnd> {

    @Override
    public void parse(CriteriaContext<BetweenAnd> criteriaContext, EntityWrapper entityWrapper) {
        Object startValue = this.getStartValue(criteriaContext);
        Object endValue = this.getEndValue(criteriaContext);

        if (startValue == null || endValue == null) {
            throw new IllegalArgumentException(String.format("The value of start and end must not to be null"));
        }

        if (!startValue.getClass().equals(endValue.getClass())) {
            throw new IllegalArgumentException(String.format("The type of start and end  do not match , start value type : %s, end value type : %s", startValue.getClass(), endValue.getClass()));
        }

        String[] columns = criteriaContext.getType().columns();
        if (columns != null && columns.length > 0) {
            for (String column : columns) {
                //拼写条件
                this.doParse(criteriaContext, entityWrapper, column, startValue, endValue);
            }
        } else {
            this.doParse(criteriaContext, entityWrapper, this.getDefaultColumn(criteriaContext), startValue, endValue);
        }
    }

    private void doParse(CriteriaContext<BetweenAnd> criteriaContext, EntityWrapper entityWrapper, String column, Object startValue, Object endValue) {
        if (criteriaContext.getType().reverse()) {
            entityWrapper.notBetween(column, startValue, endValue);
        } else {
            entityWrapper.between(column, startValue, endValue);
        }
    }

    private Object getStartValue(CriteriaContext<BetweenAnd> criteriaContext) {
        Object value = criteriaContext.getValue();
        if (ObjectUtils.isNull(value) && criteriaContext.getType().defaultStartValue() != null) {
            value = ConvertUtils.convert(criteriaContext.getType().defaultStartValue(), criteriaContext.getDataType());
        }
        return value;
    }

    private Object getEndValue(CriteriaContext<BetweenAnd> criteriaContext) {
        Object value;
        try {
            value = PropertyUtils.getProperty(criteriaContext.getCriteria(), criteriaContext.getType().end());
        } catch (Exception e) {
            throw new IllegalArgumentException("无法获取BetweenAnd中的end值", e);
        }
        if (ObjectUtils.isNull(value) && StringUtils.isNotEmpty(criteriaContext.getType().defaultEndValue())) {
            value = ConvertUtils.convert(criteriaContext.getType().defaultEndValue(), criteriaContext.getDataType());
        }
        return value;
    }

    @Override
    public String getGroup(BetweenAnd betweenAnd) {
        return betweenAnd.group();
    }

    @Override
    public boolean allowEmpty(BetweenAnd betweenAnd) {
        return betweenAnd.allowEmpty() ||
                (StringUtils.isNotEmpty(betweenAnd.defaultStartValue()) && StringUtils.isNotEmpty(betweenAnd.defaultEndValue()));
    }

    @Override
    public boolean isEnable(BetweenAnd betweenAnd) {
        return betweenAnd.enable();
    }
}
