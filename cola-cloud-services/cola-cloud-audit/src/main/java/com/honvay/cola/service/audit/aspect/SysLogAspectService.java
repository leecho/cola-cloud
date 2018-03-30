package com.honvay.cola.service.audit.aspect;

import com.honvay.cola.cloud.framework.util.DateUtils;
import com.honvay.cola.cloud.framework.util.ReflectionUtils;
import com.honvay.cola.service.audit.entity.SysLog;
import com.honvay.cola.service.audit.service.SysLogService;
import io.swagger.annotations.ApiOperation;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * SysLogAspectService
 *
 * @author YRain
 */
@Service
public class SysLogAspectService {

    @Autowired
    private SysLogService sysLogService;

    public void createLog(MethodInvocation invocation,long time){
        this.createAsync(this.generateSysLog(invocation.getThis().getClass(),invocation.getMethod(),invocation.getArguments(),time));
    }

    /*public void createLogoutLog(UserEntity userEntity){
        this.createAsync(this.newSysLogForLogin(userEntity, Kind.LOGOUT));
    }

    public void createLoginLog(UserEntity userEntity){
        this.createAsync(this.newSysLogForLogin(userEntity, Kind.LOGIN_SUCCUSS));
    }*/

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void createAsync(SysLog sysLog) {
        this.sysLogService.insert(sysLog);
    }

    public enum Type {
        LOGIN, SYSTEM
    }

    public enum Kind {

        LOGIN_SUCCUSS("登录"), LOGOUT("登出");

        private String value;

        private Kind(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /*public EnableAudit newSysLogForLogin(UserEntity userEntity, Kind kind) {
        EnableAudit sysLog = new EnableAudit();
        sysLog.setType(Type.LOGIN.name());
        sysLog.setName(kind.getValue());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != userEntity) {
            sysLog.setUserId(userEntity.getId());
            sysLog.setUserName(userEntity.getName());
        }
        sysLog.setCreateTime(DateUtils.currentDate());
        sysLog.setSuccess(true);
        sysLog.setException(null); // 暂无
        return sysLog;
    }*/

    public SysLog generateSysLog(Class<?> clazz ,Method method,Object[] args, long time) {
        SysLog sysLog = new SysLog();
        // 记录日志
        String operationName = "";
        ApiOperation operation = method.getAnnotation(ApiOperation.class);
        if (null != operation) {
            operationName = operation.value();
        }
        sysLog.setName(operationName);
        sysLog.setType(SysLogAspectService.Type.SYSTEM.name());
        sysLog.setRequestClass(clazz.getSimpleName());
        sysLog.setRequestMethod(method.getName());
        sysLog.setRequestParameter(this.getParams(method,args));
        sysLog.setTime(time);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            HttpServletRequest request = attributes.getRequest();
            if (null != request) {
                sysLog.setRequestUrl(request.getRequestURL().toString());
                sysLog.setRequestType(request.getMethod());
                sysLog.setRequestIp(request.getRemoteAddr());
            }
        }

        /*UserEntity userEntity = SecurityUtils.currentUser();
        if (null != userEntity) {
            sysLog.setUserId(userEntity.getId());
            sysLog.setUserName(userEntity.getName());
        }*/
        sysLog.setCreateTime(DateUtils.currentDate());
        sysLog.setSuccess(true);
        sysLog.setException(null);
        return sysLog;
    }

    public  String getParams(Method method,Object[] args) throws RuntimeException {
        String msg;
        try {
            return ReflectionUtils.getParams(method,args);
        } catch (Exception ex) {
            msg = "获取参数错误";
        }
        return msg;
    }


}