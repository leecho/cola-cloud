package com.honvay.cola.cloud.auth.integration;

import com.honvay.cola.cloud.framework.util.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LIQIU
 * @date 2018-3-30
 **/
@Component
public class IntegrationAuthenticationFilter extends GenericFilterBean implements Ordered {

    private static final String OAUTH_TOKEN_URL = "/oauth/token";

    private static final String AUTH_TYPE_PARM_NAME = "auth_type";

    private static final String SMS_AUTH_TYPE = "sms";

    private AntPathMatcher matcher;

    public IntegrationAuthenticationFilter() {
        this.matcher = new AntPathMatcher();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authType = request.getParameter(AUTH_TYPE_PARM_NAME);
        if (StringUtils.isNotEmpty(authType)) {
            IntegrationAuthentication integrationAuthentication = new IntegrationAuthentication();
            integrationAuthentication.setAuthType(authType);
            integrationAuthentication.setAuthParameters(request.getParameterMap());
            try{
                IntegrationAuthenticationContext.set(integrationAuthentication);
                filterChain.doFilter(request,response);
            }finally {
                IntegrationAuthenticationContext.clear();
            }
        }else{
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
