package com.honvay.cola.cloud.organization.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cola.cloud.framework.base.service.impl.TenancyServiceImpl;
import com.honvay.cola.cloud.framework.util.Assert;
import com.honvay.cola.cloud.organization.entity.SysEmployee;
import com.honvay.cola.cloud.organization.entity.SysPost;
import com.honvay.cola.cloud.organization.mapper.SysEmployeeMapper;
import com.honvay.cola.cloud.organization.service.SysPostService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 * 岗位管理 服务实现类
 * </p>
 *
 * @author liqiu
 * @date 2018-01-04
 */
@Service
public class SysPostServiceImpl extends TenancyServiceImpl<SysPost> implements SysPostService {

    @Autowired
    private SysEmployeeMapper sysEmployeeMapper;

    /**
     * 检查配置项是否有重复
     *
     * @param sysPost
     * @return
     */
    private boolean check(SysPost sysPost) {
        EntityWrapper<SysPost> wrapper = new EntityWrapper<SysPost>();
        wrapper.eq("code", sysPost.getCode());
        if (sysPost.getId() != null) {
            wrapper.ne("id", sysPost.getId());
        }
        return this.selectList(wrapper).size() == 0;
    }

    @Override
    public boolean insert(SysPost entity) {
        Assert.isTrue(check(entity), "编号已存在");
        return super.insert(entity);
    }

    @Override
    public boolean updateById(SysPost entity) {
        Assert.isTrue(check(entity), "编号已存在");
        return super.updateById(entity);
    }

    /**
     * 判断是否分配给员工
     *
     * @param id
     */
    private void assertUsedByEmployee(Serializable id) {
        EntityWrapper<SysEmployee> wrapper = new EntityWrapper<>();
        wrapper.eq("sys_post_id", id);
        Assert.isTrue(CollectionUtils.isEmpty(this.sysEmployeeMapper.selectList(wrapper)), "岗位已分配给员工，无法删除");
    }

    @Override
    public boolean deleteById(Serializable id) {
        this.assertUsedByEmployee(id);
        return super.deleteById(id);
    }
}
