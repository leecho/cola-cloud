package com.honvay.cola.cloud.auth.integration;

import com.honvay.cola.cloud.auth.integration.authenticator.IntegrationAuthenticator;
import com.honvay.cola.cloud.framework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

/**
 * @author LIQIU
 * @date 2018-3-30
 **/
@Component
public class IntegrationAuthenticationFilter extends GenericFilterBean {

    private static final String AUTH_TYPE_PARM_NAME = "auth_type";

    private List<IntegrationAuthenticator> authenticators;

    @Autowired(required = false)
    public void setIntegrationAuthenticators(List<IntegrationAuthenticator> authenticators){
        this.authenticators = authenticators;
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
        if(this.authenticators != null){
            for (IntegrationAuthenticator authenticator: authenticators) {
                if(authenticator.support(integrationAuthentication)){
                    authenticator.prepare(integrationAuthentication);
                }
            }
        }
    }
}
