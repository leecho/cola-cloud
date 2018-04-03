package com.honvay.cola.service.attachment.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件上传属性配置
 *
 * @author LIQIU
 * @date 2017年12月14日10:29:37
 */
@ConfigurationProperties(prefix = "cola.uploader.constraint" )
public class FileUploadProperties {

    /**
     * 文件最大大小
     */
    private Long maxSize;

    /**
     * 上传文件的后缀
     */
    private String[] includeSuffix;

    /**
     * 上传文件排除的后缀
     */
    private String[] excludeSuffix;

    /**
     * 可用文件头
     */
    private String[] contentType;

    /**
     * 文件上传路径
     */
    private String uploadPath;

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

    public String[] getIncludeSuffix() {
        return includeSuffix;
    }

    public void setIncludeSuffix(String[] includeSuffix) {
        this.includeSuffix = includeSuffix;
    }

    public String[] getExcludeSuffix() {
        return excludeSuffix;
    }

    public void setExcludeSuffix(String[] excludeSuffix) {
        this.excludeSuffix = excludeSuffix;
    }

    public String[] getContentType() {
        return contentType;
    }

    public void setContentType(String[] contentType) {
        this.contentType = contentType;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}
