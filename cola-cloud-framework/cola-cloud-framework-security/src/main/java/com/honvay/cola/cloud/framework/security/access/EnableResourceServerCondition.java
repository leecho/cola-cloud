package com.honvay.cola.cloud.framework.security.access;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * 判断是否启用了资源服务器
 * @author LIQIU
 * @date 2018-4-17
 **/
public class EnableResourceServerCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String[] enablers = context.getBeanFactory()
                .getBeanNamesForAnnotation(EnableResourceServer.class);
        ConditionMessage.Builder message = ConditionMessage
                .forCondition("@EnableResourceServer Condition");
        if (enablers != null && enablers.length > 0) {
            return ConditionOutcome.match(message
                    .found("@EnableResourceServer annotation on Application")
                    .items(enablers));
        }

        return ConditionOutcome.noMatch(message.didNotFind(
                "@EnableResourceServer annotation " + "on Application")
                .atAll());
    }
}
