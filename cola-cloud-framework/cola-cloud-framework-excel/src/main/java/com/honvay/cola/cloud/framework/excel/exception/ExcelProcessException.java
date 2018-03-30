package com.honvay.cola.cloud.framework.excel.exception;

/**
 * excel处理异常
 *
 * @author fengshuonan
 * @date 2017年11月29日17:39:50
 */
public class ExcelProcessException extends RuntimeException {

    public ExcelProcessException(String msg) {
        super(msg);
    }

    public ExcelProcessException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
