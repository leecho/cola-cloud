package com.honvay.cola.cloud.auth.service;

import com.honvay.cola.cloud.framework.security.userdetail.User;
import com.honvay.cola.cloud.uc.client.UcClient;
import com.honvay.cola.cloud.uc.model.UserVO;
import com.honvay.cola.cloud.upm.client.UpmClient;
import com.honvay.cola.cloud.upm.model.Authorize;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author LIQIU
 * @date 2018-3-7
 **/
@Service
public class UcUserDetailsService implements UserDetailsService {

    @Autowired
    private UcClient ucClient;

    @Autowired
    private UpmClient upmClient;


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVO userVo = this.ucClient.findUserByUsername(username);
        if(userVo == null){
            throw new UsernameNotFoundException(username);
        }
        User user = new User();
        BeanUtils.copyProperties(userVo,user);
        Authorize authorize = this.upmClient.getAuthorize(user.getId());
        user.setRoles(authorize.getRoles());
        user.setResources(authorize.getResources());
        return user;
    }

    public static void main(String[] args){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("111111"));
    }
}
