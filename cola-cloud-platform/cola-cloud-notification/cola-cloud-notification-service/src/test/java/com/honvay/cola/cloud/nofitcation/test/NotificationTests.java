package com.honvay.cola.cloud.nofitcation.test;

import com.honvay.cola.cloud.notification.model.SmsNotification;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LIQIU
 * @date 2018-3-31
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotificationTestApplication.class,webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
public class NotificationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private int number = 6000;

    private CountDownLatch countDownLatch = new CountDownLatch(number);

    private AtomicInteger count = new AtomicInteger(0);

    @Test
    public void concurrencyTest() throws InterruptedException {
        for (int i = 0; i < number ; i++) {
            NotificationTestTask task = new NotificationTestTask();
            task.start();
            countDownLatch.countDown();
        }
        Thread.currentThread().join();
        log.info("发送完毕");
    }

    private class NotificationTestTask extends Thread{

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SmsNotification smsNotification = new SmsNotification();
            try{
                rabbitTemplate.convertAndSend("notification-queue",smsNotification);
                log.info("发送消息:" + count.incrementAndGet());
            }catch (Exception e){
                log.info("发送消息失败：" + e);
            }

        }
    }

}
