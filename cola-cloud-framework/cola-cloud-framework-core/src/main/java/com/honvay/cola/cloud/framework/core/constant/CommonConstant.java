package com.honvay.cola.cloud.framework.core.constant;

/**
 * 平台常量
 * @author LIQIU
 * @date 2018-1-5
 **/
public class CommonConstant {

    /**
     * 平台菜单级别，0:平台级，1:租户级 2:运营级
     */
    public static final Integer RESOURCE_LEVEL_PLATFORM = 0;
    public static final Integer RESOURCE_LEVEL_TENANT = 1;
    public static final Integer RESOURCE_LEVEL_OPERATOR = 2;
    public static final String RESOURCE_CACHE_NAME = "resourceCache";

    public static final String COMMON_YES = "Y";
    public static final String COMMON_NO = "N";
    /**
     * 平台资源类型：目录
     */
    public static final Integer RESOURCE_TYPE_DIRECTORY = 0;
    /**
     * 平台资源类型：菜单
     */
    public static final Integer RESOURCE_TYPE_MENU = 1;
    /**
     * 平台资源类型：功能
     */
    public static final Integer RESOURCE_TYPE_FUNCTION = 2;

    /**
     * 系统管理员角色代码
     */
    public static final String ROLE_ADMIN = "admin";
    /**
     * 租户管理员角色代码
     */
    public static final String ROLE_TENANT_ADMIN = "tenant_admin";

    /**
     * 授权对象为用户
     */
    public static final String AUTHORIZE_TYPE_USER = "0";

    /**
     * 授权对象为租户
     */
    public static final String AUTHORIZE_TYPE_TENANT = "1";

    /**
     * 用户状态，1：正常，0：禁用，-1锁定
     */
    public static final String USER_STATUS_ENABLED = "1";
    public static final String USER_STATUS_DISABLED = "0";
    public static final String USER_STATUS_LOCKED = "-1";

    public static final Integer TENANT_STATUS_ENABLED = 1;
    public static final Integer TENANT_STATUS_DISABLE = 0;
}
