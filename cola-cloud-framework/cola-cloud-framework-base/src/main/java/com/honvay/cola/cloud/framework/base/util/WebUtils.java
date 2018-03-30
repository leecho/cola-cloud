package com.honvay.cola.cloud.framework.base.util;

import com.honvay.cola.cloud.framework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Web服务工具类
 * @author LIQIU
 * @date 2018-1-18
 **/
public class WebUtils extends org.springframework.web.util.WebUtils {

    private final static String REAL_IP_HEADER = "X-Real-IP";

    /**
     * 获取客户端IP
     * @return
     */
    public static String getClientIp(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if(requestAttributes != null){
            request = ((ServletRequestAttributes)requestAttributes).getRequest();
        }

        if(StringUtils.isNotEmpty(request.getHeader(REAL_IP_HEADER))){
            return request.getHeader(REAL_IP_HEADER);
        }

        if(request != null){
            return request.getRemoteAddr();
        }
        return null;
    }
}
