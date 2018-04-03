package com.honvay.cola.service.attachment.validator;

import java.util.List;


/**
 * 默认的文件合法性校验器
 *
 * @author LIQIU
 * @date 2017-12-14-下午1:56
 */

public class FileValidator {

    /**
     * 文件最大大小(默认10MB)
     */
    private Long maxBytesSize = 10 * 1024 * 1024L;

    /**
     * 上传文件的后缀,多个用逗号隔开
     */
    private List<String> includeSuffix;

    /**
     * 上传文件排除的后缀,多个用逗号隔开
     */
    private List<String> excludeSuffix;

    /**
     * 文件头
     */
    private List<String> contentTypes;

    public void validate(Long size, String fileName, String contentType) {
        if (size == null) {
            throw new RuntimeException("上传文件为空");
        }

        //验证大小
        if (size > maxBytesSize) {
            throw new RuntimeException("文件大小超过限制");
        }

        if (!fileName.contains(".")) {
            throw new RuntimeException("文件类型不合法");
        }

        //验证文件后缀合法性
        String postfix = fileName.substring(fileName.lastIndexOf("."));
        if (includeSuffix != null) {
            boolean contains = includeSuffix.contains(postfix);
            if (!contains) {
                throw new RuntimeException("文件类型不合法");
            }
        }
        if (excludeSuffix != null) {
            boolean contains = excludeSuffix.contains(postfix);
            if (contains) {
                throw new RuntimeException("文件类型不合法");
            }
        }

        //验证文件
        if (contentTypes != null) {
            boolean contains = contentTypes.contains(contentType);
            if (!contains) {
                throw new RuntimeException("文件mime不合法");
            }
        }
    }

    public Long getMaxBytesSize() {
        return maxBytesSize;
    }

    public void setMaxBytesSize(Long maxBytesSize) {
        this.maxBytesSize = maxBytesSize;
    }

    public List<String> getIncludeSuffix() {
        return includeSuffix;
    }

    public void setIncludeSuffix(List<String> includeSuffix) {
        this.includeSuffix = includeSuffix;
    }

    public List<String> getExcludeSuffix() {
        return excludeSuffix;
    }

    public void setExcludeSuffix(List<String> excludeSuffix) {
        this.excludeSuffix = excludeSuffix;
    }

    public List<String> getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(List<String> contentTypes) {
        this.contentTypes = contentTypes;
    }

    public static void main(String[] args) {
        String postfix = "abc.jpg".substring("abc.jpg".lastIndexOf("."));
        System.out.println(postfix);

    }

}
