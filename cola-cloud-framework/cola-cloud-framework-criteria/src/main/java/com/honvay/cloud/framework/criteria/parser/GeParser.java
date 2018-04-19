package com.honvay.cloud.framework.criteria.parser;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cloud.framework.criteria.CriteriaContext;
import com.honvay.cloud.framework.criteria.annotation.Ge;
import com.honvay.cola.cloud.framework.util.StringUtils;

/**
 * 大于等于条件解析
 *
 * @author LIQIU
 * @date 2018-4-17
 **/
public class GeParser extends AbstractGenericParser<Ge> {

    @Override
    public Object getDefaultValue(CriteriaContext<Ge> criteriaContext) {
        return criteriaContext.getType().defaultValue();
    }

    @Override
    public void doParse(CriteriaContext<Ge> criteriaContext, EntityWrapper entityWrapper, String column) {
        entityWrapper.ge(column, criteriaContext.getValue());
    }

    @Override
    public String[] getColumns(CriteriaContext<Ge> criteriaContext) {
        return criteriaContext.getType().columns();
    }

    @Override
    public String getGroup(Ge ge) {
        return ge.group();
    }

    @Override
    public boolean allowEmpty(Ge ge) {
        return ge.allowEmpty() || StringUtils.isNotEmpty(ge.defaultValue());
    }

    @Override
    public boolean isEnable(Ge ge) {
        return ge.enable();
    }
}
