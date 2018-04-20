package com.honvay.cola.cloud.auth.service;

import com.honvay.cola.cloud.auth.integration.IntegrationAuthentication;
import com.honvay.cola.cloud.auth.integration.IntegrationAuthenticationContext;
import com.honvay.cola.cloud.auth.integration.authenticator.IntegrationAuthenticator;
import com.honvay.cola.cloud.framework.security.userdetail.User;
import com.honvay.cola.cloud.uc.model.SysUserAuthentication;
import com.honvay.cola.cloud.uc.model.SysUserDTO;
import com.honvay.cola.cloud.upm.client.SysAuthorizeClient;
import com.honvay.cola.cloud.upm.model.Authorize;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 集成认证用户服务
 *
 * @author LIQIU
 * @date 2018-3-7
 **/
@Service
public class IntegrationUserDetailsService implements UserDetailsService {

    @Autowired
    private SysAuthorizeClient sysAuthorizeClient;

    private List<IntegrationAuthenticator> authenticators;

    @Autowired(required = false)
    public void setIntegrationAuthenticators(List<IntegrationAuthenticator> authenticators) {
        this.authenticators = authenticators;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        IntegrationAuthentication integrationAuthentication = IntegrationAuthenticationContext.get();
        //判断是否是集成登录
        if (integrationAuthentication == null) {
            integrationAuthentication = new IntegrationAuthentication();
        }
        integrationAuthentication.setUsername(username);
        SysUserAuthentication sysUserAuthentication = this.authenticate(integrationAuthentication);

        if(sysUserAuthentication == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        User user = new User();
        BeanUtils.copyProperties(sysUserAuthentication, user);
        this.setAuthorize(user);
        return user;

    }

    /**
     * 设置授权信息
     *
     * @param user
     */
    public void setAuthorize(User user) {
        Authorize authorize = this.sysAuthorizeClient.getAuthorize(user.getId());
        user.setRoles(authorize.getRoles());
        user.setResources(authorize.getResources());
    }

    private SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication) {
        if (this.authenticators != null) {
            for (IntegrationAuthenticator authenticator : authenticators) {
                if (authenticator.support(integrationAuthentication)) {
                    return authenticator.authenticate(integrationAuthentication);
                }
            }
        }
        return null;
    }
}
