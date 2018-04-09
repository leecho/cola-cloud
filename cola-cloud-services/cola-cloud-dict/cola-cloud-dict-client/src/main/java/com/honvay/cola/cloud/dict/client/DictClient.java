package com.honvay.cola.cloud.dict.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.honvay.cola.cloud.dict.client.jackson.JacksonDictJsonModifier;
import com.honvay.cola.cloud.dict.model.CacheConstants;
import com.honvay.cola.cloud.dict.model.DictVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author LIQIU
 * @date 2018-3-14
 **/
@Component
public class DictClient {

    @Autowired
    private CacheManager cacheManager;

    /**
     * 通过数据字典代码获取数据字典值
     * @param parent
     * @return
     */
    public Map<String,DictVO> getByParent(String parent) {
        Cache cache = cacheManager.getCache(CacheConstants.DICT_ITEM_CACHE_NAME);
        Cache.ValueWrapper valueWrapper = cache.get(parent);
        if(valueWrapper != null){
            return (Map) valueWrapper.get();
        }
        return null;
    }

    /**
     * 通过编号和名称获取数据字典项
     * @param parent
     * @param code
     * @return
     */
    public String getNameByCode(String parent,String code) {
        Map<String,DictVO> dicts = this.getByParent(parent);
        if(dicts != null){
            DictVO dictVO = dicts.get(code);
            if(dictVO != null){
                return dictVO.getName();
            }
        }
        return null;
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new JacksonDictJsonModifier());
        DictVO dictItem = new DictVO();
        dictItem.setCode("1231231");
        dictItem.setParent("1231231");
        dictItem.setName("1231231");
        objectMapper.registerModule(module);
        System.out.println(objectMapper.writeValueAsString(dictItem));
    }
}
