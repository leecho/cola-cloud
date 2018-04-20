package com.honvay.cola.cloud.organization.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.organization.builder.SysOrganizationBuilder;
import com.honvay.cola.cloud.organization.entity.SysEmployee;
import com.honvay.cola.cloud.organization.model.SysEmployeeVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author liqiu
 * @date 2018-01-04
 */
public interface SysEmployeeMapper extends BaseMapper<SysEmployee> {

    /**
     * 通过账号查询员工列表
     * @param page
     * @param name
     * @param username
     * @param status
     * @return
     */

    @SelectProvider(type = SysOrganizationBuilder.class,method ="buildGetEmployeeListByOrgIdSql")
    List<SysEmployeeVO> getEmployeeListByOrgId(Page page, @Param("name") String name, @Param("username") String username, @Param("status") Integer status) ;

    /**
     * 通过
     * @param employeeId
     * @return
     */
    @Select("select ase.id,asu.name,asu.username,asu.status,asu.email,asu.phone_number as phoneNumber,asp.name as postName" +
            " from cola_sys_employee ase,cola_sys_user asu,cola_sys_post asp" +
            "where ase.sys_user_id = asu.id" +
            "  and ase.sys_post_id = asp.id" +
            "  and ase.id = #{employeeId}")
    Map<String,Object> getEmployeeById(@Param("employeeId") Long employeeId) ;
}