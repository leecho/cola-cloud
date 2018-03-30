package com.honvay.cola.cloud.devtools.generator.engine.config;

import com.honvay.cola.cloud.framework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制器模板生成的配置
 *
 * @author fengshuonan
 * @date 2017-05-07 22:12
 */
public class ControllerConfig {

    private ContextConfig contextConfig;

    private String       controllerPathTemplate;
    private String       packageName;//包名称
    private List<String> imports;//所引入的包

    public void init() {
        ArrayList<String> imports = new ArrayList<>();
        imports.add(contextConfig.getCoreBasePackage() + ".controller.BaseController");
        imports.add("org.springframework.web.bind.annotation.*");
        imports.add("org.springframework.web.bind.annotation.PathVariable");
        imports.add("org.springframework.beans.factory.annotation.Autowired");
        imports.add(contextConfig.getModelPackageName() + "." + StringUtils.firstCharToUpperCase(contextConfig.getEntityName()));
        imports.add(contextConfig.getProPackage() + "." + contextConfig.getModuleName() + ".service" + "." + StringUtils.firstCharToUpperCase(contextConfig.getEntityName()) + "Service");
        this.imports = imports;
        this.packageName = contextConfig.getProPackage() + "." + contextConfig.getModuleName() + ".controller";
        this.controllerPathTemplate = "\\src\\main\\java\\" + contextConfig.getProPackage().replaceAll("\\.", "\\\\") + "\\" + contextConfig.getModuleName() + "\\controller\\{}Controller.java";
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public String getControllerPathTemplate() {
        return controllerPathTemplate;
    }

    public void setControllerPathTemplate(String controllerPathTemplate) {
        this.controllerPathTemplate = controllerPathTemplate;
    }

    public ContextConfig getContextConfig() {
        return contextConfig;
    }

    public void setContextConfig(ContextConfig contextConfig) {
        this.contextConfig = contextConfig;
    }

}
