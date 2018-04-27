package com.honvay.cola.cloud.framework.base.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.honvay.cloud.framework.criteria.Criteria;
import com.honvay.cola.cloud.framework.base.pagination.PageableCriteria;

import java.util.List;
import java.util.Map;

/**
 * 基础服务类
 * @author LIQIU
 * @param <T>
 */
public interface BaseService<T> extends IService<T>{

    /**
     * 根据列获取唯一对象
     * @param column
     * @param value
     * @return
     */
    T findOneByColumn(String column, String value);

    /**
     * 根据列获取列表
     * @param column
     * @param value
     * @return
     */
    List<T> selectList(String column, Object value);

    /**
     * 查询里欸包
     * @return
     */
    List<T> selectList();

    T selectOne(EntityWrapper<T> wrapper, Criteria<T> criteria, String group);

    T selectOne(EntityWrapper<T> wrapper, Criteria<T> criteria);

    T selectOne(Criteria<T> criteria);

    Map<String, Object> selectMap(EntityWrapper<T> wrapper, Criteria<T> criteria);

    Map<String, Object> selectMap(Criteria<T> criteria);

    Object selectObj(EntityWrapper<T> wrapper, Criteria<T> criteria, String group);

    Object selectObj(EntityWrapper<T> wrapper, Criteria<T> criteria);

    Object selectObj(Criteria<T> criteria);

    int selectCount(EntityWrapper<T> wrapper, Criteria<T> criteria, String group);

    int selectCount(EntityWrapper<T> wrapper, Criteria<T> criteria);

    int selectCount(Criteria<T> criteria);

    List<Object> selectObjs(EntityWrapper<T> wrapper, Criteria<T> criteria, String group);

    List<Object> selectObjs(EntityWrapper<T> wrapper, Criteria<T> criteria);

    List<Object> selectObjs(Criteria<T> criteria);

    Page<Map<String, Object>> selectMapsPage(Page page, EntityWrapper<T> wrapper, Criteria<T> criteria, String group);

    Page<Map<String, Object>> selectMapsPage(Page page, EntityWrapper<T> wrapper, Criteria<T> criteria);

    Page<Map<String, Object>> selectMapsPage(Page page, Criteria<T> criteria);

    List<T> selectList(EntityWrapper<T> wrapper, Criteria<T> criteria, String group);

    List<T> selectList(EntityWrapper<T> wrapper, Criteria<T> criteria);

    List<T> selectList(Criteria<T> criteria);

    Page<T> selectPage(Page<T> page, EntityWrapper<T> wrapper, Criteria<T> criteria, String group);

    Page<T> selectPage(Page<T> page, EntityWrapper<T> wrapper, Criteria<T> criteria);

    Page<T> selectPage(Page<T> page, Criteria<T> criteria);

    Page<T> selectPage(PageableCriteria pageableCriteria);
}
