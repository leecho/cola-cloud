package com.honvay.cola.cloud.framework.core.constant;

/**
 * @author LIQIU
 * @date 2018-1-12
 **/
public enum ErrorStatus {


    // 1xx Informational

    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR(10000, "系统错误"),
    /**
     * 参数错误
     */
    ILLEGAL_ARGUMENT(10001, "参数错误"),
    /**
     * 业务错误
     */
    SERVICE_EXCEPTION(10002, "业务错误"),
    /**
     * 非法的数据格式，参数没有经过校验
     */
    ILLEGAL_DATA(10003, "数据错误"),

    MULTIPART_TOO_LARGE(1004,"文件太大"),
    /**
     * 非法状态
     */
    ILLEGAL_STATE(10005, "非法状态"),
    /**
     * 缺少参数
     */
    MISSING_ARGUMENT(10006, "缺少参数"),
    /**
     * 非法访问
     */
    ILLEGAL_ACCESS(10007, "非法访问,没有认证"),
    /**
     * 权限不足
     */
    UNAUTHORIZED(10008, "权限不足"),

    /**
     * 错误的请求
     */
    METHOD_NOT_ALLOWED(10009, "不支持的方法"),


    /**
     * 参数错误
     */
    ILLEGAL_ARGUMENT_TYPE(10010, "参数类型错误");

    private final int value;

    private final String message;


    ErrorStatus(int value, String message) {
        this.value = value;
        this.message = message;
    }


    /**
     * Return the integer value of this status code.
     */
    public int value() {
        return this.value;
    }

    /**
     * Return the reason phrase of this status code.
     */
    public String getMessage() {
        return this.message;
    }

}
