package com.honvay.cola.cloud.framework.base.service;

import com.baomidou.mybatisplus.service.IService;

import java.util.List;

public interface BaseService<T> extends IService<T>{

    /**
     * 根据列获取唯一对象
     * @param column
     * @param value
     * @return
     */
    public T findOneByColumn(String column, String value);

    /**
     * 根据列获取列表
     * @param column
     * @param value
     * @return
     */
    public List<T> selectListByColumn(String column, Object value);

    public List<T> selectList();
}
