package com.honvay.cola.cloud.gateway.configuration;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LIQIU
 * @date 2018-3-8
 **/
@Component
@Primary
public class SwaggerDocumentConfiguration implements SwaggerResourcesProvider {

    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
        resources.add(swaggerResource("认证中心", "/uaa/v2/api-docs", "2.0"));
        resources.add(swaggerResource("用户中心", "/uc/v2/api-docs", "2.0"));
        resources.add(swaggerResource("权限系统", "/upm/v2/api-docs", "2.0"));
        resources.add(swaggerResource("组织架构", "/organization/v2/api-docs", "2.0"));
        resources.add(swaggerResource("消息中心", "/message/v2/api-docs", "2.0"));
        resources.add(swaggerResource("通知中心", "/notification/v2/api-docs", "2.0"));
        resources.add(swaggerResource("公共服务", "/common/v2/api-docs", "2.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
