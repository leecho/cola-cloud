package com.honvay.cola.cloud.dict.service;

import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.dict.model.DictItemVO;
import com.honvay.cola.cloud.dict.entity.DictItem;

import java.util.Collection;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author liqiu
 * @since 2017-12-22
 */
public interface DictItemService extends BaseService<DictItem> {


    /**
     * 通过数据字典代码获取数据字典值
     * @param code
     * @return
     */
    Collection<DictItemVO> listByCode(String code);


    /**
     * 通过编号和值获取数据字典项
     * @param code
     * @param value
     * @return
     */
    DictItemVO getByCodeAndValue(String code, String value);

    /**
     * 通过编号和名称获取数据字典项
     * @param code
     * @param name
     * @return
     */
    DictItemVO getByCodeAndName(String code, String name);

    /**
     * 启用数据字典项
     * @param id
     */
    void enable(Long id);

    /**
     * 禁用数据字典项
     * @param id
     */
    void disable(Long id);
}
