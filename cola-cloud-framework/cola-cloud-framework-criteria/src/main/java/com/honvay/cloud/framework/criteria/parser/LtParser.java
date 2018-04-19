package com.honvay.cloud.framework.criteria.parser;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cloud.framework.criteria.CriteriaContext;
import com.honvay.cloud.framework.criteria.annotation.Lt;
import com.honvay.cola.cloud.framework.util.StringUtils;

/**
 * 大于等于条件解析
 *
 * @author LIQIU
 * @date 2018-4-17
 **/
public class LtParser extends AbstractGenericParser<Lt> {

    @Override
    public Object getDefaultValue(CriteriaContext<Lt> criteriaContext) {
        return criteriaContext.getType().defaultValue();
    }

    @Override
    public void doParse(CriteriaContext<Lt> criteriaContext, EntityWrapper entityWrapper, String column) {
        entityWrapper.ge(column, criteriaContext.getValue());
    }

    @Override
    public String[] getColumns(CriteriaContext<Lt> criteriaContext) {
        return criteriaContext.getType().columns();
    }

    @Override
    public String getGroup(Lt lt) {
        return lt.group();
    }

    @Override
    public boolean allowEmpty(Lt lt) {
        return lt.allowEmpty() || StringUtils.isNotEmpty(lt.defaultValue());
    }

    @Override
    public boolean isEnable(Lt lt) {
        return lt.enable();
    }
}
