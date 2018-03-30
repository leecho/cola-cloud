package com.honvay.cola.service.audit.aspect;

import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author LIQIU
 * @date 2018-2-8
 **/
//@Component
public class SysLogAdvisor extends StaticMethodMatcherPointcutAdvisor implements InitializingBean {

    @Autowired
    private SysLogInterceptor sysLogInterceptor;

    /**
     *
     */
    private static final long serialVersionUID = 545158457213842666L;

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return (AnnotationUtils.findAnnotation(targetClass, EnableAudit.class) != null
                || AnnotationUtils.findAnnotation(method,EnableAudit.class) != null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setAdvice(sysLogInterceptor);
    }


}