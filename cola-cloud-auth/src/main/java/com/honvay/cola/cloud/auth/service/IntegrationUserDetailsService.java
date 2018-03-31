package com.honvay.cola.cloud.auth.service;

import com.honvay.cola.cloud.auth.integration.IntegrationAuthentication;
import com.honvay.cola.cloud.auth.integration.IntegrationAuthenticationContext;
import com.honvay.cola.cloud.auth.integration.authenticator.DefaultAuthenticator;
import com.honvay.cola.cloud.auth.integration.authenticator.IntegrationAuthenticator;
import com.honvay.cola.cloud.framework.security.userdetail.User;
import com.honvay.cola.cloud.uc.client.UcClient;
import com.honvay.cola.cloud.uc.model.UserVO;
import com.honvay.cola.cloud.upm.client.UpmClient;
import com.honvay.cola.cloud.upm.model.Authorize;
import com.honvay.cola.cloud.vcc.client.VccClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 集成认证用户服务
 * @author LIQIU
 * @date 2018-3-7
 **/
@Service
public class IntegrationUserDetailsService implements UserDetailsService {

    @Autowired
    private UcClient ucClient;

    @Autowired
    private UpmClient upmClient;

    @Autowired
    private VccClient vccClient;

    @Autowired
    private DefaultAuthenticator defaultAuthenticator;

    private List<IntegrationAuthenticator> authenticators;

    @Autowired(required = false)
    public void setIntegrationAuthenticators(List<IntegrationAuthenticator> authenticators){
        this.authenticators = authenticators;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        UserVO userVo = null;
        IntegrationAuthentication integrationAuthentication = IntegrationAuthenticationContext.get();
        //判断是否是集成登录
        if(integrationAuthentication == null){
            integrationAuthentication = new IntegrationAuthentication();

        }

        integrationAuthentication.setUsername(username);
        userVo = this.authenticate(integrationAuthentication);

        if(userVo == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        User user = new User();
        BeanUtils.copyProperties(userVo,user);
        this.setAuthorize(user);
        return user;
    }

    /**
     * 设置授权信息
     * @param user
     */
    public void setAuthorize(User user){
        Authorize authorize = this.upmClient.getAuthorize(user.getId());
        user.setRoles(authorize.getRoles());
        user.setResources(authorize.getResources());
    }

    private  UserVO authenticate(IntegrationAuthentication integrationAuthentication){
        if(this.authenticators != null){
            for (IntegrationAuthenticator authenticator: authenticators) {
                if(authenticator.support(integrationAuthentication)){
                    return authenticator.authenticate(integrationAuthentication);
                }
            }
        }
        return null;
    }
}
