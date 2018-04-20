package com.honvay.cola.cloud.tenancy.builder;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;
import java.util.Objects;

/**
 * @author LIQIU
 * @date 2018-1-10
 **/
public class SysTenantSqlBuilder {

    public String buildTenantListSql(Map<String,Object> params){
       return new SQL(){{
           SELECT("t.id,t.`name`,t.code,t.description,t.status,su.username as administrator");
           FROM("cola_sys_tenant t");
           LEFT_OUTER_JOIN("cola_sys_user su on su.id = t.administrator");
           if (Objects.nonNull(params.get("name"))) {
               WHERE("t.name like #{name}");
           }
           if (Objects.nonNull(params.get("code"))) {
               WHERE("t.code  like #{code}");
           }
       }}.toString();
    }
}
