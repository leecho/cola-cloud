package com.honvay.cola.cloud.organization.builder;

import com.honvay.cola.cloud.framework.core.constant.CommonConstant;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.Objects;

/**
 * @author LIQIU
 * @date 2018-1-10
 **/
public class SysOrganizationBuilder {

    /**
     * 构建获取员工列表SQL语句
     *
     * @param params
     * @return
     */
    public String buildGetEmployeeListByOrgIdSql(Map<String, Object> params) {
        return new SQL() {{
            SELECT("ase.id,asu.name,asu.username,asu.status,asu.email");
            SELECT("asu.phone_number as phoneNumber,ase.sys_org_id as orgId");
            SELECT("asp.name as postName,asu.id as userId");
            FROM("cola_sys_employee ase");
            LEFT_OUTER_JOIN("cola_sys_user asu on asu.id = ase.sys_user_id");
            LEFT_OUTER_JOIN("cola_sys_post asp on asp.id = ase.sys_post_id");
            if (Objects.nonNull(params.get("name"))) {
                WHERE("asu.name like #{name}");
            }
            if (Objects.nonNull(params.get("username"))) {
                WHERE("asu.username  like #{username}");
            }
            if (Objects.nonNull(params.get("status"))) {
                WHERE("asu.status  = #{status}");
            }
        }}.toString();
    }

    /**
     * 构建组织机构列表SQL语句
     *
     * @param params
     * @return
     */
    public String buildListOrganizationSql(Map<String, Object> params) {
        return new SQL() {{
            SELECT("ado.id,ado.name,ado.description,ado.code,ado.parent,ado.status");
            SELECT("adop.code as pcode,adop.name as pname");
            FROM("cola_sys_organization ado");
            LEFT_OUTER_JOIN("cola_sys_organization adop on adop.id = ado.parent");
            WHERE("ado.deleted <> '" + CommonConstant.COMMON_YES + "'");
            if (Objects.nonNull(params.get("name"))) {
                WHERE("ado.name like #{name}");
            }
            if (Objects.nonNull(params.get("code"))) {
                WHERE("ado.code  like #{code}");
            }
            if (Objects.nonNull(params.get("status"))) {
                WHERE("ado.status = #{status}");
            }
        }}.toString();
    }

    /**
     * 构建组织机构详情SQL语句
     *
     * @param params
     * @return
     */
    public String buildGetOrganizationSql(Map<String, Object> params) {
        return new SQL() {{
            SELECT("ado.id,ado.name,ado.description,ado.code,ado.parent,ado.deleted,ado.status");
            SELECT("adop.code as pcode,adop.name as pname");
            FROM("cola_sys_organization ado");
            LEFT_OUTER_JOIN("cola_sys_organization adop on adop.id = ado.parent");
            WHERE("ado.id = " + params.get("id"));
        }}.toString();
    }
}
