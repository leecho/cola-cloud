package com.honvay.cola.cloud.dict.service;

import com.honvay.cola.cloud.dict.entity.Dict;
import com.honvay.cola.cloud.framework.base.service.BaseService;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author liqiu
 * @since 2017-12-22
 */
public interface DictService extends BaseService<Dict> {

    /**
     * 批量删除
     * @param ids
     */
    void batchDelete(Long[] ids);

    void enable(Long id);

    void disable(Long id);
}
