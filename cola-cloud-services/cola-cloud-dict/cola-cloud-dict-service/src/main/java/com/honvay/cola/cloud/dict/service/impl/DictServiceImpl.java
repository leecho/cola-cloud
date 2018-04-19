package com.honvay.cola.cloud.dict.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.honvay.cola.cloud.dict.entity.Dict;
import com.honvay.cola.cloud.dict.model.CacheConstants;
import com.honvay.cola.cloud.dict.model.DictVO;
import com.honvay.cola.cloud.dict.service.DictService;
import com.honvay.cola.cloud.framework.base.service.impl.BaseServiceImpl;
import com.honvay.cola.cloud.framework.core.constant.CommonConstant;
import com.honvay.cola.cloud.framework.util.Assert;
import com.honvay.cola.cloud.framework.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {

    @Autowired
    private CacheManager cacheManager;

    private boolean check(Dict dict) {
        EntityWrapper<Dict> wrapper = new EntityWrapper<Dict>();
        wrapper.eq("code", dict.getCode());
        if (dict.getId() != null) {
            wrapper.ne("id", dict.getId());
        }
        return this.selectList(wrapper).size() == 0;
    }

    @Override
    public boolean insert(Dict dict) {
        Assert.isTrue(check(dict), "编号已经存在");
        return super.insert(dict);
    }

    @Override
    public boolean updateById(Dict dict) {
        Assert.isTrue(check(dict), "编号已经存在");
        return super.updateById(dict);
    }

    @Override
    public boolean deleteById(Serializable id) {
        return super.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        this.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void enable(Long id) {
        Wrapper<Dict> wrapper = this.newEntityWrapper();
        wrapper.eq("id", id);
        wrapper.eq("enable", CommonConstant.COMMON_NO);
        Dict dict = new Dict();
        dict.setEnable(CommonConstant.COMMON_YES);
        Boolean result = this.update(dict, wrapper);
        Assert.isTrue(result, "数据字典已启用");
    }

    @Override
    public void disable(Long id) {
        Wrapper<Dict> wrapper = this.newEntityWrapper();
        wrapper.eq("id", id);
        wrapper.eq("enable", CommonConstant.COMMON_YES);
        Dict dict = new Dict();
        dict.setEnable(CommonConstant.COMMON_NO);
        Boolean result = this.update(dict, wrapper);
        Assert.isTrue(result, "数据字典已禁用");
    }

    /**
     * 缓存所有的数据字典
     */
    @PostConstruct
    public void cacheDict() {
        EntityWrapper wrapper = this.newEntityWrapper();
        wrapper.eq("enable", CommonConstant.COMMON_YES);
        List<Dict> dicts = this.selectList(wrapper);
        Map<String, List<DictVO>> dictMap = new HashMap<>();
        for (Dict dict : dicts) {
            if (StringUtils.isEmpty(dict.getParent())) {
                continue;
            }
            //找到目录
            List<DictVO> subDictMap = dictMap.get(dict.getParent());
            if (subDictMap == null) {
                subDictMap = new ArrayList<>();
                dictMap.put(dict.getParent(), subDictMap);
            }
            DictVO vo = new DictVO();
            BeanUtils.copyProperties(dict, vo);
            subDictMap.add(vo);
        }

        Cache cache = cacheManager.getCache(CacheConstants.DICT_ITEM_CACHE_NAME);
        for (Map.Entry<String, List<DictVO>> entry : dictMap.entrySet()) {
            List<DictVO> children = entry.getValue();
            //排序处理
            children.sort((o1, o2) -> {
                if (o1.getOrderNo() == null) {
                    return 1;
                }
                if (o2.getOrderNo() == null) {
                    return -1;
                }
                return o1.getOrderNo() - o2.getOrderNo();
            });

            //添加到Mapper中
            HashMap<String,DictVO> subMap = new LinkedHashMap<>();
            for (DictVO dictVO : children) {
                subMap.put(dictVO.getCode(),dictVO);
            }

            cache.put(entry.getKey(), subMap);
        }
    }
}
