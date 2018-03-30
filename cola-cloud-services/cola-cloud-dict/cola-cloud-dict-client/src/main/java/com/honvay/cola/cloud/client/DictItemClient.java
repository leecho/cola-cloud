package com.honvay.cola.cloud.client;

import com.honvay.cola.cloud.dict.model.CacheConstants;
import com.honvay.cola.cloud.dict.model.DictItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author LIQIU
 * @date 2018-3-14
 **/
@Service
public class DictItemClient {

    @Autowired
    private CacheManager cacheManager;

    /**
     * 通过数据字典代码获取数据字典值
     * @param code
     * @return
     */
    public Collection<DictItemVO> listByCode(String code) {
        Cache cache = cacheManager.getCache(CacheConstants.DICT_ITEM_CACHE_NAME);
        Cache.ValueWrapper valueWrapper = cache.get(code);
        if(valueWrapper != null){
            return (Collection<DictItemVO>) valueWrapper.get();
        }
        return null;
    }

    /**
     * 通过编号和值获取数据字典项
     * @param code
     * @param value
     * @return
     */
    public DictItemVO getByCodeAndValue(String code,String value) {
        Collection<DictItemVO> items = this.listByCode(code);
        if(items == null){
            return null;
        }

        for(DictItemVO item : items){
            if(item.getValue().equals(value)){
                return item;
            }
        }
        return null;
    }

    /**
     * 通过编号和名称获取数据字典项
     * @param code
     * @param name
     * @return
     */
    public DictItemVO getByCodeAndName(String code,String name) {
        Collection<DictItemVO> items = this.listByCode(code);
        if(items == null){
            return null;
        }

        for(DictItemVO item : items){
            if(item.getName().equals(name)){
                return item;
            }
        }
        return null;
    }
}
