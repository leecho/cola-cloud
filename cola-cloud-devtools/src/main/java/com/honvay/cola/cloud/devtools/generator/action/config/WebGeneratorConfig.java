package com.honvay.cola.cloud.devtools.generator.action.config;

import com.honvay.cola.cloud.framework.util.StringUtils;
import com.honvay.cola.cloud.devtools.generator.action.model.GenerateConfig;
import com.honvay.cola.cloud.devtools.generator.config.rules.DbType;
import com.honvay.cola.cloud.devtools.generator.config.rules.NamingStrategy;

import java.io.File;

/**
 * 默认的代码生成的配置
 *
 * @author fengshuonan
 * @date 2017-10-28-下午8:27
 */
public class WebGeneratorConfig extends AbstractGeneratorConfig {

    private GenerateConfig genQo;

    public WebGeneratorConfig(GenerateConfig genQo) {
        this.genQo = genQo;
    }

    @Override
    protected void config() {
        /**
         * 数据库配置
         */
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername(genQo.getUserName());
        dataSourceConfig.setPassword(genQo.getPassword());
        dataSourceConfig.setUrl(genQo.getUrl());

        /**
         * 全局配置
         */
        globalConfig.setOutputDir(genQo.getProjectPath() + File.separator + "src" + File.separator + "main" + File.separator + "java");
        globalConfig.setFileOverride(true);
        globalConfig.setEnableCache(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setOpen(false);
        globalConfig.setActiveRecord(false);
        globalConfig.setAuthor(genQo.getAuthor());
        contextConfig.setProPackage(genQo.getProjectPackage());
        contextConfig.setCoreBasePackage(genQo.getCorePackage());

        /**
         * 生成策略
         */
        if (genQo.getIgnoreTabelPrefix() != null) {
            strategyConfig.setTablePrefix(new String[]{genQo.getIgnoreTabelPrefix()});
        }
        strategyConfig.setInclude(new String[]{genQo.getTableName()});
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        packageConfig.setParent(null);
        packageConfig.setEntity(genQo.getProjectPackage() + "." + genQo.getModuleName() + ".entity");
        packageConfig.setMapper(genQo.getProjectPackage() + "." + genQo.getModuleName() + ".mapper");
        packageConfig.setXml(genQo.getProjectPackage() + "." + genQo.getModuleName() + ".mapper.mapping");

        /**
         * 业务代码配置
         */
        contextConfig.setBizChName(genQo.getBizName());
        contextConfig.setModuleName(genQo.getModuleName());
        //写自己项目的绝对路径
        contextConfig.setProjectPath(genQo.getProjectPath());
        contextConfig.setProPackage(genQo.getProjectPackage());

        String tableName = genQo.getTableName();
        if(!StringUtils.isEmpty(genQo.getIgnoreTabelPrefix())){
            tableName = StringUtils.removePrefix(tableName, genQo.getIgnoreTabelPrefix());
        }
        String entityName = StringUtils.underlineToCamelCase(tableName);
        contextConfig.setEntityName(entityName);
        contextConfig.setBizEnName(StringUtils.uncapitalize(entityName));

        String bizPath = StringUtils.camelCaseToPath(entityName);
        if(StringUtils.isNotEmpty(genQo.getModuleName())){
            bizPath = StringUtils.trimToEmpty(genQo.getModuleName() )+ "/" + bizPath;
        }

        contextConfig.setBizPath(bizPath);

        //这里写已有菜单的名称,当做父节点
        sqlConfig.setParentMenuName(genQo.getParentMenuName());

        /**
         * mybatis-plus 生成器开关
         */
        contextConfig.setEntitySwitch(genQo.getEntitySwitch());
        contextConfig.setDaoSwitch(genQo.getDaoSwitch());
        contextConfig.setServiceSwitch(genQo.getServiceSwitch());

        /**
         * cola 生成器开关
         */
        contextConfig.setControllerSwitch(genQo.getControllerSwitch());
        contextConfig.setIndexPageSwitch(genQo.getIndexPageSwitch());
        contextConfig.setAddPageSwitch(genQo.getAddPageSwitch());
        contextConfig.setEditPageSwitch(genQo.getEditPageSwitch());
        contextConfig.setJsSwitch(genQo.getJsSwitch());
        contextConfig.setInfoJsSwitch(genQo.getInfoJsSwitch());
        contextConfig.setSqlSwitch(genQo.getSqlSwitch());
    }
}
