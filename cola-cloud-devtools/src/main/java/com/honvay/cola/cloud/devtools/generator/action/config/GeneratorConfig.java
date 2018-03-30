package com.honvay.cola.cloud.devtools.generator.action.config;


import com.honvay.cola.cloud.devtools.generator.config.rules.DbType;
import com.honvay.cola.cloud.devtools.generator.config.rules.NamingStrategy;

/**
 * 默认的代码生成的配置
 *
 * @author fengshuonan
 * @date 2017-10-28-下午8:27
 */
public class GeneratorConfig extends AbstractGeneratorConfig {

    protected void globalConfig() {
        globalConfig.setOutputDir("");//写自己项目的绝对路径,注意具体到java目录
        globalConfig.setFileOverride(true);
        globalConfig.setEnableCache(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setOpen(false);
        globalConfig.setAuthor("fengshuonan");
    }

    protected void dataSourceConfig() {
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("root");
        dataSourceConfig.setUrl("");
    }

    protected void strategyConfig() {
        strategyConfig.setTablePrefix(new String[]{""});// 此处可以修改为您的表前缀
        strategyConfig.setInclude(new String[]{""});//这里限制需要生成的表,不写则是生成所有表
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
    }

    protected void packageConfig() {
        packageConfig.setParent(null);
        packageConfig.setEntity("com.honvay.cola.admin.modular.system.persistence.model");
        packageConfig.setMapper("com.honvay.cola.admin.modular.system.persistence.mapper");
        packageConfig.setXml("com.honvay.cola.admin.modular.system.persistence.mapper.mapping");
    }

    protected void contextConfig() {
        contextConfig.setProPackage("com.honvay.cola.admin");
        contextConfig.setCoreBasePackage("com.honvay.cola.common");
        contextConfig.setBizChName("字典管理");
        contextConfig.setBizEnName("sysDict");
        contextConfig.setModuleName("system");
        contextConfig.setProjectPath("");//写自己项目的绝对路径
        contextConfig.setEntityName("SysDict");
        sqlConfig.setParentMenuName(null);//这里写已有菜单的名称,当做父节点

        /**
         * mybatis-plus 生成器开关
         */
        contextConfig.setEntitySwitch(true);
        contextConfig.setDaoSwitch(true);
        contextConfig.setServiceSwitch(true);

        /**
         * cola 生成器开关
         */
        contextConfig.setControllerSwitch(true);
        contextConfig.setIndexPageSwitch(true);
        contextConfig.setAddPageSwitch(true);
        contextConfig.setEditPageSwitch(true);
        contextConfig.setJsSwitch(true);
        contextConfig.setInfoJsSwitch(true);
        contextConfig.setSqlSwitch(true);
    }

    @Override
    protected void config() {
        globalConfig();
        dataSourceConfig();
        strategyConfig();
        packageConfig();
        contextConfig();
    }
}
