package com.honvay.cola.cloud.devtools.generator.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 代码生成配置
 *
 * @author LIQIU
 * @date 2017-12-13-下午4:53
 */
@Component
@ConfigurationProperties(prefix = "cola.devtools")
public class DevtoolsProperties {

    /**
     * 项目路径
     */
    private String projectLocation;

    /**
     * 开发者
     */
    private String developer;

    /**
     * 表的前缀
     */
    private String tablePrefix;

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }
}
