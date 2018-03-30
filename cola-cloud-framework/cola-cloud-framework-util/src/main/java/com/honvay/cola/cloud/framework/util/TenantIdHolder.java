package com.honvay.cola.cloud.framework.util;

import org.springframework.core.NamedThreadLocal;

/**
 * 用于保存用户需要登录的租户ID
 * @author LIQIU
 * @date 2018-1-5
 **/
public class TenantIdHolder {
    private static final ThreadLocal<Long> tenantHaolder = new NamedThreadLocal<>("TenantIdHolder");

    public static void set(Long tenantId){
        tenantHaolder.set(tenantId);
    }

    public static void remove(){
        tenantHaolder.remove();
    }

    public static Long get(){
        return tenantHaolder.get();
    }
}
