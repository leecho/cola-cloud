package com.honvay.cola.cloud.framework.starter.mail.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件服务的配置
 *
 * @author LIQIU
 * @date 2017-12-13-上午11:15
 */
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {

    private String name;
    
    private String from;

    private boolean preferIPv4Stack;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getPreferIPv4Stack() {
		return preferIPv4Stack;
	}

	public void setPreferIPv4Stack(boolean preferIPv4Stack) {
		this.preferIPv4Stack = preferIPv4Stack;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}
