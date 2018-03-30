package com.honvay.cola.cloud.uc.configuration;

import com.honvay.cola.cloud.framework.util.StringUtils;
import com.honvay.cola.cloud.uc.password.DefaultPasswordStrategy;
import com.honvay.cola.cloud.uc.password.PasswordStrategy;
import com.honvay.cola.cloud.uc.password.validator.LengthValidator;
import com.honvay.cola.cloud.uc.password.validator.StrengthValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LIQIU
 * @date 2018-1-10
 **/
@Configuration
@ConditionalOnMissingBean(PasswordStrategy.class)
@EnableConfigurationProperties(PasswordStragtegyProperties.class)
public class PasswordStrategyAutoConfiguration {

    private Logger logger  = Logger.getLogger(this.getClass());

    private static final String DEFAULT_PASSWORD = "111111";

    @Autowired
    PasswordStragtegyProperties platformPasswordStragtegyProperties;

    @Bean
    public PasswordStrategy passwordStrategy(){
        DefaultPasswordStrategy passwordStrategy = new DefaultPasswordStrategy();
        //长度验证器
        if(platformPasswordStragtegyProperties.getMinLength() != null || platformPasswordStragtegyProperties.getMaxLength() != null){
            LengthValidator lengthValidator = new LengthValidator();
            lengthValidator.setMinLength(platformPasswordStragtegyProperties.getMinLength());
            lengthValidator.setMinLength(platformPasswordStragtegyProperties.getMinLength());
            passwordStrategy.addValidator(lengthValidator);
            logger.info("Password Strategy Register Validator :" + lengthValidator);
        }
        //强度验证器
        if(platformPasswordStragtegyProperties.getStrength() != null){
            StrengthValidator strengthValidator = new StrengthValidator();
            strengthValidator.setStrength(platformPasswordStragtegyProperties.getStrength());
            passwordStrategy.addValidator(strengthValidator);
            logger.info("Password Strategy Register Validator :" + strengthValidator);
        }

        //默认密码配置
        String defaultPassword = platformPasswordStragtegyProperties.getDefaultPassword();
        defaultPassword = StringUtils.isNotEmpty(defaultPassword) ? defaultPassword : DEFAULT_PASSWORD;
        passwordStrategy.setDefaultPassword(defaultPassword);
        logger.info("Password Strategy configured :" + passwordStrategy);

        return passwordStrategy;
    }
}