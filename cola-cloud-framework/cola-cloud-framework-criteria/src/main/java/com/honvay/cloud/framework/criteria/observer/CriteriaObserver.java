package com.honvay.cloud.framework.criteria.observer;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cloud.framework.criteria.CriteriaContext;

/**
 * @author LIQIU
 * @date 2018-4-18
 **/
public class CriteriaObserver<T> implements Observer<T>{

    @Override
    public void beforeParse(EntityWrapper<T> wrapper) {

    }

    @Override
    public void afterParse(EntityWrapper<T> wrapper) {

    }

    @Override
    public boolean onParse(CriteriaContext<?> criteriaContext, EntityWrapper<T> wrapper) {
        return false;
    }
}
