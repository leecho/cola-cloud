package com.honvay.cola.cloud.framework.starter.mail;


import com.honvay.cola.cloud.framework.mail.MailSender;
import com.honvay.cola.cloud.framework.starter.mail.properties.MailProperties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Properties;

/**
 * 邮件发送配置
 *
 * @author LIQIU
 * @Date 2017/12/12 下午4:53
 */
@Configuration
@AutoConfigureAfter({MailSenderAutoConfiguration.class})
@EnableConfigurationProperties({MailProperties.class})
public class MailAutoConfiguration {

    private Logger log= Logger.getLogger(MailAutoConfiguration.class);

    @Autowired
    private MailProperties mailProperties;

    @Bean
    @ConditionalOnBean(JavaMailSender.class)
    public MailSender adiMailSender1(JavaMailSender sender, FreeMarkerConfigurer freeMarkerConfigurer) {
        if (mailProperties.getPreferIPv4Stack()) {
            System.setProperty("java.net.preferIPv4Stack", "true");
        }

        JavaMailSenderImpl js= (JavaMailSenderImpl) sender;
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.ssl.enable", true);
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.timeout", 25000);
        js.setJavaMailProperties(props);
        MailSender mailSender = new MailSender();
        mailSender.setJavaMailSender(js);
        mailSender.setFreeMarkerConfigurer(freeMarkerConfigurer);
        mailSender.setName(mailProperties.getName());
        mailSender.setFrom(mailProperties.getFrom());
        return mailSender;
    }

}
