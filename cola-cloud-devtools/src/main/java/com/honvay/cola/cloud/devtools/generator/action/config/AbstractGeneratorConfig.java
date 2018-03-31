package com.honvay.cola.cloud.devtools.generator.action.config;

import com.honvay.cola.cloud.devtools.generator.AutoGenerator;
import com.honvay.cola.cloud.devtools.generator.config.DataSourceConfig;
import com.honvay.cola.cloud.devtools.generator.config.GlobalConfig;
import com.honvay.cola.cloud.devtools.generator.config.PackageConfig;
import com.honvay.cola.cloud.devtools.generator.config.StrategyConfig;
import com.honvay.cola.cloud.devtools.generator.config.po.TableInfo;
import com.honvay.cola.cloud.devtools.generator.engine.SimpleTemplateEngine;
import com.honvay.cola.cloud.devtools.generator.engine.base.TemplateEngine;
import com.honvay.cola.cloud.devtools.generator.engine.config.ContextConfig;
import com.honvay.cola.cloud.devtools.generator.engine.config.SqlConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 代码生成的抽象配置
 *
 * @author LIQIU
 * @date 2017-10-28-下午8:22
 */
public abstract class AbstractGeneratorConfig {

    /**
     * mybatis-plus代码生成器配置
     */

    GlobalConfig globalConfig = new GlobalConfig();

    DataSourceConfig dataSourceConfig = new DataSourceConfig();

    StrategyConfig strategyConfig = new StrategyConfig();

    PackageConfig packageConfig = new PackageConfig();

    TableInfo tableInfo = null;

    /**
     * Cola代码生成器配置
     */
    ContextConfig contextConfig = new ContextConfig();

    SqlConfig sqlConfig = new SqlConfig();

    protected abstract void config();

    public void init() {
        config();

        packageConfig.setService(contextConfig.getProPackage() + "." + contextConfig.getModuleName() + ".service");
        packageConfig.setServiceImpl(contextConfig.getProPackage() + "." + contextConfig.getModuleName() + ".service.impl");
        strategyConfig.setEntityLombokModel(true);

        //controller没用掉,生成之后会自动删掉
        packageConfig.setController("TTT");

        if (!contextConfig.getEntitySwitch()) {
            packageConfig.setEntity("TTT");
        }

        if (!contextConfig.getDaoSwitch()) {
            packageConfig.setMapper("TTT");
            packageConfig.setXml("TTT");
        }

        if (!contextConfig.getServiceSwitch()) {
            packageConfig.setService("TTT");
            packageConfig.setServiceImpl("TTT");
        }

    }

    /**
     * 删除不必要的代码
     */
    public void destory() {
        String outputDir = globalConfig.getOutputDir() + "/TTT";
        try {
			FileUtils.deleteDirectory(new File(outputDir));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public AbstractGeneratorConfig() {
    }

    public void doMpGeneration() {
        init();
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(globalConfig);
        autoGenerator.setDataSource(dataSourceConfig);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setPackageInfo(packageConfig);
        autoGenerator.execute();
        destory();

        //获取table信息,用于guns代码生成
        List<TableInfo> tableInfoList = autoGenerator.getConfig().getTableInfoList();
        if (tableInfoList != null && tableInfoList.size() > 0) {
            this.tableInfo = tableInfoList.get(0);
        }
    }

    public void doGeneration() {
        TemplateEngine templateEngine = new SimpleTemplateEngine();
        templateEngine.setContextConfig(contextConfig);
        sqlConfig.setConnection(dataSourceConfig.getConn());
        templateEngine.setSqlConfig(sqlConfig);
        templateEngine.setTableInfo(tableInfo);
        templateEngine.start();
    }
}
