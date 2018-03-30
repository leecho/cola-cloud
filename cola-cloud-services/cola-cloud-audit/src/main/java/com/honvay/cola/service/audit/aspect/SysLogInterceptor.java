package com.honvay.cola.service.audit.aspect;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LIQIU
 * @date 2018-2-8
 **/
@Component
public class SysLogInterceptor implements MethodInterceptor {

    @Autowired
    private SysLogAspectService sysLogAspectService;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 执行业务
        long start = System.currentTimeMillis();
        Object result = invocation.proceed();
        long time = System.currentTimeMillis() - start;
        // 异步记录日志
        sysLogAspectService.createLog(invocation, time);
        return result;
    }
}
