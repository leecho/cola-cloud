package com.honvay.cola.cloud.organization.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.organization.builder.SysOrganizationBuilder;
import com.honvay.cola.cloud.organization.entity.SysOrganization;
import com.honvay.cola.cloud.organization.model.SysOrganizationVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 组织架构Mapper
 * @author LIQIU
 */
public interface SysOrganizationMapper extends BaseMapper<SysOrganization>{

    /**
     * 获取组织机构列表
     * @param page
     * @param name
     * @param code
     * @return
     */

    @SelectProvider(type = SysOrganizationBuilder.class,method ="buildListOrganizationSql")
    List<SysOrganizationVO> getOrganizationList(Page page, @Param("name") String name, @Param("code") String code, @Param("status") String status) ;

    /**
     * 获取组织机构详情
     * @param id
     * @return
     */
    @SelectProvider(type = SysOrganizationBuilder.class,method ="buildGetOrganizationSql")
    SysOrganizationVO getOrganization(@Param("id") Long id) ;

}
