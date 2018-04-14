package com.honvay.cola.cloud.framework.oauth2.token;

import com.honvay.cola.cloud.framework.security.userdetail.User;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;

import java.util.Map;

/**
 * @author LIQIU
 * @date 2018-4-14
 **/
public class UserPrincipalExtractor implements PrincipalExtractor {

    private FixedPrincipalExtractor fixedPrincipalExtractor = new FixedPrincipalExtractor();

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        Map<String, Object> authentication = (Map<String, Object>) map.get("userAuthentication");
        if (authentication != null) {
           Object principal =authentication.get("principal");
            User user = new User();
            try {
                BeanUtils.populate(user,  (Map<String, Object>) principal);
            } catch (Exception e) {
                throw new InvalidTokenException("");
            }
            return user;
        } else {
            Object principal = this.fixedPrincipalExtractor.extractPrincipal(map);
            return (principal == null ? "unknown" : principal);
        }
    }
}
