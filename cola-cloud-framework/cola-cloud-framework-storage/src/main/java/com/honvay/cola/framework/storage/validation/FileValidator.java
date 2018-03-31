package com.honvay.cola.framework.storage.validation;

/**
 * 文件合法性校验接口
 *
 * @author LIQIU
 * @date 2017-12-14-下午1:55
 */
public interface FileValidator {

    /**
     * 校验文件的合法性
     * @param size 大小
     * @param fileName 文件名称
     * @param contentType 媒体类型
     * @author LIQIU
     * @throws RuntimeException
     */
    void validate(Long size, String fileName, String contentType) throws RuntimeException;
}
