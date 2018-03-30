package com.honvay.cola.cloud.upm.event;

import org.springframework.context.ApplicationEvent;

/**
 * 系统资源缓存刷新事件
 * @author LIQIU
 * @date 2018-1-8
 **/
public class SysCacheRefreshEvent extends ApplicationEvent{

    public SysCacheRefreshEvent(Object source) {
        super(source);
    }
}
