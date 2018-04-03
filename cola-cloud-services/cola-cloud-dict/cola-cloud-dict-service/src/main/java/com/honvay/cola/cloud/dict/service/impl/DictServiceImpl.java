package com.honvay.cola.cloud.dict.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cola.cloud.dict.entity.Dict;
import com.honvay.cola.cloud.dict.entity.DictItem;
import com.honvay.cola.cloud.dict.mapper.DictItemMapper;
import com.honvay.cola.cloud.dict.service.DictService;
import com.honvay.cola.cloud.framework.base.service.impl.BaseSerivceImpl;
import com.honvay.cola.cloud.framework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author liqiu
 * @since 2017-12-22
 */
@Service
public class DictServiceImpl extends BaseSerivceImpl<Dict> implements DictService {

    @Autowired
    private DictItemMapper dictItemMapper;

    private boolean check(Dict dict){
        EntityWrapper<Dict> wrapper = new EntityWrapper<Dict>();
        wrapper.eq("code",dict.getCode());
        if(dict.getId() != null){
            wrapper.ne("id",dict.getId());
        }
        return this.selectList(wrapper).size() == 0;
    }

    @Override
    public boolean insert(Dict dict) {
        Assert.isTrue(check(dict),"编号已经存在");
        return super.insert(dict);
    }

    @Override
    public boolean updateById(Dict dict) {
        Assert.isTrue(check(dict),"编号已经存在");
        return super.updateById(dict);
    }

    /**
     * 判断是否含有数据项
     * @param id
     */
    private void assertHaveItem(Serializable id){
        List<DictItem> dictItems = this.dictItemMapper.selectList(new EntityWrapper<DictItem>().eq("code",id));
        org.springframework.util.Assert.isTrue(dictItems.size() == 0,"数字字典含有配置项无法删除");
    }

    @Override
    public boolean deleteById(Serializable id) {
        //删除数据字典值
        Dict dict = this.selectById(id);
        assertHaveItem(dict.getCode());
        return super.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids){
        for(Long id : ids){
            this.assertHaveItem(id);
            this.deleteById(Long.valueOf(id));
        }
    }
}
