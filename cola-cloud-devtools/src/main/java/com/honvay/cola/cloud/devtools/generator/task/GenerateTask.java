package com.honvay.cola.cloud.devtools.generator.task;

import lombok.Data;

/**
 * @author LIQIU
 * @date 2018-4-3
 **/
@Data
public class GenerateTask {

    private String projectLocation;

    private String tablePrefix;

    private String packageName;

    private String tableName;

    private String entityName;

    private String module;

    private String developer;

    private Boolean onlyGenerateEntity;
}
