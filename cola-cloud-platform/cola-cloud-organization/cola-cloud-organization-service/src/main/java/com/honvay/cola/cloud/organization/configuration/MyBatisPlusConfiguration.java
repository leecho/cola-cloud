package com.honvay.cola.cloud.organization.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author LIQIU
 * @date 2018-3-8
 **/
@Configuration
@MapperScan(basePackages = { "com.honvay.cola.**.mapper" })
public class MyBatisPlusConfiguration {

}
