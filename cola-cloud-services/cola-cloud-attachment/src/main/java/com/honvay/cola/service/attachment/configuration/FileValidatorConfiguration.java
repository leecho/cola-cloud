package com.honvay.cola.service.attachment.configuration;

import com.honvay.cola.service.attachment.validator.FileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.util.Arrays;

/**
 * @author LIQIU
 * @date 2018-4-3
 **/
@Configuration
@EnableConfigurationProperties({FileUploadProperties.class})
public class FileValidatorConfiguration {
    private static long DEFUALT_MAX_SIZE = 1024 * 1024 * 500;

    @Autowired
    private FileUploadProperties fileUploadProperties;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        long maxSize = fileUploadProperties.getMaxSize() != null ? fileUploadProperties.getMaxSize() : DEFUALT_MAX_SIZE;
        factory.setMaxFileSize(maxSize);
        factory.setMaxRequestSize(maxSize);
        return factory.createMultipartConfig();
    }

    @Bean
    public FileValidator defaultFileValidation() {
        if (fileUploadProperties != null) {
            FileValidator fileValidator = new FileValidator();
            if(fileUploadProperties.getMaxSize() != null){
                fileValidator.setMaxBytesSize(fileUploadProperties.getMaxSize());
            }
            if (fileUploadProperties.getContentType() != null) {
                fileValidator.setContentTypes(Arrays.asList(fileUploadProperties.getContentType()));
            }
            if (fileUploadProperties.getExcludeSuffix() != null) {
                fileValidator.setExcludeSuffix(Arrays.asList(fileUploadProperties.getExcludeSuffix()));
            }
            if (fileUploadProperties.getIncludeSuffix() != null) {
                fileValidator.setIncludeSuffix(Arrays.asList(fileUploadProperties.getIncludeSuffix()));
            }
            return fileValidator;
        }
        return null;
    }
}
