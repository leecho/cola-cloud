package com.honvay.cola.cloud.upm.event;

import org.springframework.context.ApplicationEvent;

/**
 * 系统资源缓存刷新事件
 * @author LIQIU
 * @date 2018-1-8
 **/
public class SysResourceCacheRefreshEvent extends ApplicationEvent{

    public SysResourceCacheRefreshEvent(Object source) {
        super(source);
    }
}
