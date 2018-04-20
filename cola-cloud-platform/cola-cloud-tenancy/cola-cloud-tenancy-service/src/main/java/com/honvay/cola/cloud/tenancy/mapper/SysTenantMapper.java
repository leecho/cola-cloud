package com.honvay.cola.cloud.tenancy.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.tenancy.builder.SysTenantSqlBuilder;
import com.honvay.cola.cloud.tenancy.entity.SysTenant;
import com.honvay.cola.cloud.tenancy.model.SysTenantVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * <p>
 * 租户表 Mapper 接口
 * </p>
 *
 * @author LIQIU
 */
public interface SysTenantMapper extends BaseMapper<SysTenant> {

    @SelectProvider(type = SysTenantSqlBuilder.class, method = "buildTenantListSql")
    List<SysTenantVO> findTenantList(Page page, @Param("code") String code, @Param("name") String name);

}