package com.honvay.cola.cloud.framework.starter.sms;

import com.honvay.cola.cloud.framework.sms.SmsSender;
import com.honvay.cola.cloud.framework.sms.sender.AliyunSmsSender;
import com.honvay.cola.cloud.framework.starter.sms.config.AliyunSmsProperties;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
@EnableConfigurationProperties({AliyunSmsProperties.class})
public class SmsAutoConfiguration {
	
	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private AliyunSmsProperties aliyunSmsProperties;

	@Bean
	@ConditionalOnClass({ AliyunSmsSender.class })
	@ConditionalOnProperty(name = "cola.sms.type", havingValue = "aliyun")
	public SmsSender smsSender() {
        AliyunSmsSender sender = new AliyunSmsSender();
		BeanUtils.copyProperties(aliyunSmsProperties, sender);
		return sender;
	}

}
