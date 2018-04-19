package com.honvay.cola.cloud.framework.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cola.cloud.framework.base.entity.TenancyEntity;
import com.honvay.cola.cloud.framework.security.utils.SecurityUtils;
import org.springframework.util.Assert;

import java.io.Serializable;


/**
 * @author LIQIU
 * @date 2018-1-3
 * @param <T>
 */
public class TenancyServiceImpl<T extends TenancyEntity> extends BaseServiceImpl<T> {

	@Override
	public boolean insert(T entity) {
		if(entity.getTenantId() == null){
			Long tenantId = SecurityUtils.getTenantId();
			entity.setTenantId(tenantId);
		}
		return super.insert(entity);
	}

	@Override
	public boolean deleteById(Serializable id) {

		Long tenantId = SecurityUtils.getTenantId();
		if(tenantId != null){
		    T t = this.selectById(id);
		    Assert.isTrue(t.getTenantId().longValue() == tenantId.longValue(),"操作失败，所属租户错误");
        }
		return super.deleteById(id);
	}

	@Override
	public boolean updateById(T entity) {

		Long tenantId = SecurityUtils.getTenantId();
        if(tenantId != null && entity.getTenantId() != null){
            Assert.isTrue(entity.getTenantId().longValue() == tenantId.longValue(),"操作失败，所属租户错误");
        }
		return super.updateById(entity);
	}

    /**
     * 自动添加租户ID字段参数
     * @param wrapper
     */
	protected EntityWrapper addTenantCondition(EntityWrapper wrapper){

	    if(wrapper == null){
	        return null;
        }

	    Long tenantId = SecurityUtils.getTenantId();
	    if(tenantId != null){
	        wrapper.eq("tenant_id",tenantId);
        }else{
	        wrapper.isNull("tenant_id");
        }
        return wrapper;
    }
}
