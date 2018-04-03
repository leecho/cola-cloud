package com.honvay.cola.cloud.auth.integration;

import com.honvay.cola.cloud.auth.integration.authenticator.IntegrationAuthenticator;
import com.honvay.cola.cloud.framework.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author LIQIU
 * @date 2018-3-30
 **/
@Component
public class IntegrationAuthenticationFilter extends GenericFilterBean implements ApplicationContextAware {

    private static final String AUTH_TYPE_PARM_NAME = "auth_type";

    private Collection<IntegrationAuthenticator> authenticators;

    private ApplicationContext applicationContext;

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
                //预处理
                this.prepare(integrationAuthentication);
                IntegrationAuthenticationContext.set(integrationAuthentication);
                filterChain.doFilter(request,response);
            }finally {
                IntegrationAuthenticationContext.clear();
            }
        }else{
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 进行预处理
     * @param integrationAuthentication
     */
    private void prepare(IntegrationAuthentication integrationAuthentication) {

        //延迟加载认证器
        if(this.authenticators == null){
            synchronized (this){
                Map<String,IntegrationAuthenticator> integrationAuthenticatorMap = applicationContext.getBeansOfType(IntegrationAuthenticator.class);
                if(integrationAuthenticatorMap != null){
                    this.authenticators = integrationAuthenticatorMap.values();
                }
            }
        }

        if(this.authenticators == null){
            this.authenticators = new ArrayList<>();
        }

        for (IntegrationAuthenticator authenticator: authenticators) {
            if(authenticator.support(integrationAuthentication)){
                authenticator.prepare(integrationAuthentication);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
