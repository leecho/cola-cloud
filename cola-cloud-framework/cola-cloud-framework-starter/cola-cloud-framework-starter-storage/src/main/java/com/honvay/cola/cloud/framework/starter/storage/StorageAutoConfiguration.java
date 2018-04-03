package com.honvay.cola.cloud.framework.starter.storage;


import com.aliyun.oss.OSSClient;
import com.honvay.cola.cloud.framework.starter.storage.properties.LocalStorageProperties;
import com.honvay.cola.cloud.framework.starter.storage.properties.OssProperties;
import com.honvay.cola.framework.storage.FileStorage;
import com.honvay.cola.framework.storage.impl.AliyunFileStorage;
import com.honvay.cola.framework.storage.impl.LocalFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 文件上传自动配置
 *
 * @author LIQIU
 * @Date 2017/12/12 下午4:53
 */
@Configuration
@EnableConfigurationProperties({OssProperties.class, LocalStorageProperties.class})
public class StorageAutoConfiguration {

    @Autowired
    private OssProperties ossProperties;

    @Autowired
    private LocalStorageProperties localStorageProperties;

    /**
     * 根据配置文件自动装配阿里云SSO存储组件
     *
     * @return AliyunFileStorage.class
     */
    @Bean
    @ConditionalOnProperty(prefix = "cola.storage", name = "type", havingValue = "aliyun" )
    @ConditionalOnMissingBean(FileStorage.class)
    @Order(0)
    public AliyunFileStorage aliyunFileStorage() {
        OSSClient ossClient = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        AliyunFileStorage aliyunFileUpload = new AliyunFileStorage(ossClient);
        aliyunFileUpload.setBucketName(ossProperties.getBucketName());
        return aliyunFileUpload;
    }

    /**
     * 根据配置文件自动装配本地存储组件
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "cola.storage", name = "type", havingValue = "local" )
    @ConditionalOnMissingBean(FileStorage.class)
    @Order(1)
    public LocalFileStorage localFileStorage() {
        return new LocalFileStorage(localStorageProperties.getLocation());
    }

    /**
     * 默认配置用户目录作为存储目录
     *
     * @return
     */
    @Bean
    @Order(2)
    @ConditionalOnMissingBean(FileStorage.class)
    public LocalFileStorage defaultLocalStorage() {
        return new LocalFileStorage(System.getProperty("user.home" ));
    }
}
