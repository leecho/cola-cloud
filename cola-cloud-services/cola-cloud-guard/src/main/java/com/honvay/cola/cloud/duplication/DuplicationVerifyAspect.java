package com.honvay.cola.cloud.duplication;

import com.honvay.cola.cloud.duplication.repository.DuplicationVerifyTokenRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * 防重复提交验证,渲染页面之前的操作
 *
 * @author LIQIU
 * @date 2017-12-06-下午2:51
 */
@Aspect
@Component
@Order(10)
public class DuplicationVerifyAspect {
	
	private static final String TOKEN_PARAM = "dv_token";

    @Autowired
    DuplicationVerifyTokenRepository repeatKeyRepository;

    @Pointcut(value = "@annotation(com.honvay.cola.cloud.duplication.annotion.DuplicationVerify)")
    public void duplicationVerifyPointcut() {
    }

    @Around("duplicationVerifyPointcut()")
    public Object verify(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getParameter(TOKEN_PARAM);
        boolean exists = repeatKeyRepository.exists(token);
        if (exists) {
            Object proceed = point.proceed();
            repeatKeyRepository.delete(token);
            return proceed;
        } else {
            throw new IllegalArgumentException("表单已经提交或者已超时");
        }

    }
}
