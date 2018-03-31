package com.honvay.cola.cloud.dict.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cola.cloud.dict.entity.DictItem;
import com.honvay.cola.cloud.dict.model.CacheConstants;
import com.honvay.cola.cloud.dict.model.DictItemVO;
import com.honvay.cola.cloud.dict.service.DictItemService;
import com.honvay.cola.cloud.framework.base.service.impl.BaseSerivceImpl;
import com.honvay.cola.cloud.framework.core.constant.CommonConstant;
import com.honvay.cola.cloud.framework.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author liqiu
 * @since 2017-12-22
 */
@Service
@EnableCaching
@CacheConfig(cacheNames = CacheConstants.DICT_ITEM_CACHE_NAME)
public class DictItemServiceImpl extends BaseSerivceImpl<DictItem> implements DictItemService, InitializingBean {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public Collection<DictItemVO> listByCode(String code) {
        Cache cache = cacheManager.getCache(CacheConstants.DICT_ITEM_CACHE_NAME);
        Cache.ValueWrapper valueWrapper = cache.get(code);
        if(valueWrapper != null){
            return (Collection<DictItemVO>) valueWrapper.get();
        }
        return null;
    }

    @Override
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

    @Override
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

    private void assertDuplication(DictItem dictItem) {
        EntityWrapper<DictItem> wrapper = this.newEntityWrapper();
        wrapper.eq("value", dictItem.getValue());
        wrapper.eq("name", dictItem.getName());
        wrapper.eq("code", dictItem.getCode());
        if (dictItem.getId() != null) {
            wrapper.ne("id", dictItem.getId());
        }
        Assert.isTrue(this.selectList(wrapper).size() == 0, "存在相同的名称和值");
    }

    @Override
    public boolean insert(DictItem entity) {
        assertDuplication(entity);
        boolean result = super.insert(entity);
        this.refreshCache(entity.getCode());
        return result;
    }

    @Override
    public boolean updateById(DictItem entity) {
        assertDuplication(entity);
        boolean result = super.updateById(entity);
        this.refreshCache(entity.getCode());
        return result;
    }

    @Override
    public void enable(Long id) {
        DictItem item = this.selectById(id);
        Assert.isTrue(CommonConstant.COMMON_NO.equals(item.getEnable()), "字典项已启用");
        item.setEnable(CommonConstant.COMMON_YES);
        this.updateById(item);
    }

    @Override
    public void disable(Long id) {
        DictItem item = this.selectById(id);
        Assert.isTrue(CommonConstant.COMMON_YES.equals(item.getEnable()), "字典项被禁用");
        item.setEnable(CommonConstant.COMMON_NO);
        //刷新缓存
        this.updateById(item);
    }

    @Override
    public boolean deleteById(Serializable id) {
        DictItem item = this.selectById(id);
        boolean result = super.deleteById(id);
        this.refreshCache(item.getCode());
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.cacheAll();
    }

    /**
     * 缓存所有的数据字典
     */
    private void cacheAll() {
        EntityWrapper wrapper = this.newEntityWrapper();
        wrapper.eq("enable",CommonConstant.COMMON_YES);
        List<DictItem> items = this.selectList(wrapper);
        Map<String, List<DictItemVO>> dictItemMap = new HashMap<>();
        for (DictItem item : items) {
            if(StringUtils.isEmpty(item.getCode())){
                continue;
            }
            List<DictItemVO> itemsOfCode = dictItemMap.get(item.getCode());
            if(itemsOfCode == null){
                itemsOfCode = new ArrayList<>();
                dictItemMap.put(item.getCode(),itemsOfCode);
            }
            DictItemVO vo = new DictItemVO();
            BeanUtils.copyProperties(item,vo);
            itemsOfCode.add(vo);
        }

        Cache cache = cacheManager.getCache(CacheConstants.DICT_ITEM_CACHE_NAME);
        for(Map.Entry<String,List<DictItemVO>> entry : dictItemMap.entrySet()){
            List<DictItemVO> items1 = entry.getValue();
            items1.sort((o1, o2) -> {
                if(o1.getOrderNo() == null){
                    return 1;
                }
                if(o2.getOrderNo() == null){
                    return -1;
                }
                return o1.getOrderNo() - o2.getOrderNo();
            });
            cache.put(entry.getKey(),entry.getValue());
        }
    }

    /**
     * 刷新缓存
     * @param code
     */
    private void refreshCache(String code) {
        EntityWrapper wrapper = this.newEntityWrapper();
        wrapper.eq("enable",CommonConstant.COMMON_YES);
        wrapper.eq("code",code);
        wrapper.orderBy("order_no");
        Cache cache = cacheManager.getCache(CacheConstants.DICT_ITEM_CACHE_NAME);
        Collection items = CollectionUtils.collect(this.selectList(wrapper).iterator(),(input) -> {
            DictItemVO vo = new DictItemVO();
            BeanUtils.copyProperties(input,vo);
            return vo;
        });
        cache.put(code,items);
    }
}
