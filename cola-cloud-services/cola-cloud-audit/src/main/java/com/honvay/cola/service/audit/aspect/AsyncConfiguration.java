package com.honvay.cola.service.audit.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer {

    @Value("${cola.async.minSize:5}")
    private Integer minSize;

    @Value("${app.async.maxSize:50}")
    private Integer maxSize;

    @Value("${app.async.queueSize:1000}")
    private Integer queueSize;

    @Value("${app.async.thread.name.prefix:cola}")
    private String threadNamePrefix;

    @Override
    @Bean(name = "executor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(this.minSize);
        executor.setMaxPoolSize(this.maxSize);
        executor.setQueueCapacity(this.queueSize);
        executor.setThreadNamePrefix(this.threadNamePrefix + "-");
        return new AsyncExceptionHandlingAsyncTaskExecutor(executor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

}
