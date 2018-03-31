package com.honvay.cola.cloud.devtools.generator.engine.config;

/**
 * Dao模板生成的配置
 *
 * @author LIQIU
 * @date 2017-05-07 22:12
 */
public class DaoConfig {

    private ContextConfig contextConfig;

    private String daoPathTemplate;
    private String xmlPathTemplate;

    private String packageName;

    public void init() {
        this.daoPathTemplate = "\\src\\main\\java\\" + contextConfig.getProPackage().replaceAll("\\.", "\\\\") + "\\modular\\" + contextConfig.getModuleName() + "\\mapper\\{}Dao.java";
        this.xmlPathTemplate = "\\src\\main\\java\\" + contextConfig.getProPackage().replaceAll("\\.", "\\\\") + "\\modular\\" + contextConfig.getModuleName() + "\\mapper\\mapping\\{}Dao.xml";
        this.packageName = contextConfig.getProPackage() + "." + contextConfig.getModuleName() + ".mapper";
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDaoPathTemplate() {
        return daoPathTemplate;
    }

    public void setDaoPathTemplate(String daoPathTemplate) {
        this.daoPathTemplate = daoPathTemplate;
    }

    public String getXmlPathTemplate() {
        return xmlPathTemplate;
    }

    public void setXmlPathTemplate(String xmlPathTemplate) {
        this.xmlPathTemplate = xmlPathTemplate;
    }

    public ContextConfig getContextConfig() {
        return contextConfig;
    }

    public void setContextConfig(ContextConfig contextConfig) {
        this.contextConfig = contextConfig;
    }
}
