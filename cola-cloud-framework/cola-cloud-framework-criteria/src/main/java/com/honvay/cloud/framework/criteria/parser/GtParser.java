package com.honvay.cloud.framework.criteria.parser;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cloud.framework.criteria.CriteriaContext;
import com.honvay.cloud.framework.criteria.annotation.Gt;
import com.honvay.cola.cloud.framework.util.StringUtils;

/**
 * 大于等于条件解析
 *
 * @author LIQIU
 * @date 2018-4-17
 **/
public class GtParser extends AbstractGenericParser<Gt> {

    @Override
    public Object getDefaultValue(CriteriaContext<Gt> criteriaContext) {
        return criteriaContext.getType().defaultValue();
    }

    @Override
    public void doParse(CriteriaContext<Gt> criteriaContext, EntityWrapper entityWrapper, String column) {
        entityWrapper.gt(column, criteriaContext.getValue());
    }

    @Override
    public String[] getColumns(CriteriaContext<Gt> criteriaContext) {
        return criteriaContext.getType().columns();
    }

    @Override
    public String getGroup(Gt gt) {
        return gt.group();
    }

    @Override
    public boolean allowEmpty(Gt gt) {
        return gt.allowEmpty() || StringUtils.isNotEmpty(gt.defaultValue());
    }

    @Override
    public boolean isEnable(Gt gt) {
        return gt.enable();
    }
}
