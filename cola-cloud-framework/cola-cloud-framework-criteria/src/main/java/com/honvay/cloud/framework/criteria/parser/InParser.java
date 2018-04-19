package com.honvay.cloud.framework.criteria.parser;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cloud.framework.criteria.CriteriaContext;
import com.honvay.cloud.framework.criteria.annotation.In;
import com.honvay.cola.cloud.framework.util.ConvertUtils;
import com.honvay.cola.cloud.framework.util.ObjectUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Date;

/**
 * @author LIQIU
 * @date 2018-4-19
 **/
public class InParser extends AbstractParser<In> {

    private void doParse(CriteriaContext<In> criteriaContext, EntityWrapper entityWrapper, String[] columns, Object[] values) {
        boolean reverse = criteriaContext.getType().reverse();
        for (String column : columns) {
            if (reverse) {
                entityWrapper.notIn(column, values);
            } else {
                entityWrapper.in(column, values);
            }
        }
    }

    private void doParse(CriteriaContext<In> criteriaContext, EntityWrapper entityWrapper, String[] columns, Collection<?> values) {
        boolean reverse = criteriaContext.getType().reverse();
        for (String column : columns) {
            if (reverse) {
                entityWrapper.notIn(column, values);
            } else {
                entityWrapper.in(column, values);
            }
        }
    }

    /**
     * 获取值
     *
     * @param criteriaContext
     * @return
     */
    public Object getValue(CriteriaContext<In> criteriaContext) {
        Object value = criteriaContext.getValue();
        String defaultValue = criteriaContext.getType().defaultValue();
        if (ObjectUtils.isNull(value) && StringUtils.isNotEmpty(defaultValue)) {
            value = defaultValue;
        }
        return value;
    }

    @Override
    public void parse(CriteriaContext<In> criteriaContext, EntityWrapper entityWrapper) {

        Object value = this.getValue(criteriaContext);
        In in = criteriaContext.getType();

        if (criteriaContext.getValue() == null && !this.allowEmpty(criteriaContext.getType())) {
            throw new IllegalArgumentException("The condition is not allowed to be empty but the value is empty");
        }

        String[] columns = in.columns();
        if (ArrayUtils.isEmpty(columns)) {
            columns = new String[]{this.getDefaultColumn(criteriaContext)};
        }

        //判断是否为数组
        if (value.getClass().isArray()) {
            this.doParse(criteriaContext, entityWrapper, columns, (Object[]) value);
        } else if (value instanceof Collection) {
            //是否为集合
            this.doParse(criteriaContext, entityWrapper, columns, (Collection) value);
        } else {
            //将字符串进行转换
            String[] stringValues = String.valueOf(value).split(in.delimiter());
            if (in.dataType().equals(String.class)) {
                this.doParse(criteriaContext, entityWrapper, columns, stringValues);
            } else {
                Object[] values = (Object[]) ConvertUtils.convert(stringValues, in.dataType());
                this.doParse(criteriaContext, entityWrapper, columns, values);
            }
        }
    }


    @Override
    public String getGroup(In in) {
        return in.group();
    }

    @Override
    public boolean allowEmpty(In in) {
        return in.allowEmpty() || StringUtils.isNotEmpty(in.defaultValue());
    }

    @Override
    public boolean isEnable(In in) {
        return in.enable();
    }


    public static void main(String[] args) {
        String dates = "2017-12-10,2018-19-10,2020-10-07";
        Object[] values = (Object[]) ConvertUtils.convert(dates, Date.class);
        System.out.println(values);
    }
}
