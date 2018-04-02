package com.honvay.cola.cloud.nofitcation.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author LIQIU
 * @date 2018-3-31
 **/
@SpringBootApplication
@ComponentScan("com.honvay.cola.cloud.nofitcation.test")
public class NotificationTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationTestApplication.class, args);
    }
}
