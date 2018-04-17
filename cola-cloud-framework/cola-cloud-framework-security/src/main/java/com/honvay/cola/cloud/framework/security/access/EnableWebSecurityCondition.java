package com.honvay.cola.cloud.framework.security.access;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * 判断是否启用了安全控制
 * @author LIQIU
 * @date 2018-4-17
 **/
public class EnableWebSecurityCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String[] enablers = context.getBeanFactory()
                .getBeanNamesForAnnotation(EnableWebSecurity.class);
        ConditionMessage.Builder message = ConditionMessage
                .forCondition("@EnableWebSecurity Condition");
        if (enablers != null && enablers.length > 0) {
            return ConditionOutcome.match(message
                    .found("@EnableWebSecurity annotation on Application")
                    .items(enablers));
        }

        return ConditionOutcome.noMatch(message.didNotFind(
                "@EnableWebSecurity annotation " + "on Application")
                .atAll());
    }
}
