package com.honvay.cola.cloud.framework.security.utils;

import com.honvay.cola.cloud.framework.security.userdetail.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * @author LIQIU
 * @date 2018-3-8
 **/
public class SecurityUtils {

    public static Long getTenantId() {
        User user = getPrincipal();
        if (user != null) {
            return user.getTenantId();
        }
        return null;
    }

    public static String getUsername() {
        User user = getPrincipal();
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    public static User getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null) {
            if (principal instanceof User) {
                return (User) principal;
            } else {
                throw new IllegalStateException("获取用户数据失败");
            }
        }
        return null;
    }

    public static boolean hasRole(String role) {
        User user = getPrincipal();
        if (user != null) {
            if (user.getRoles().contains(role)) {
                return true;
            }

        }
        return false;
    }

    public static Long getUserId() {
        User user = getPrincipal();
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    public static boolean hasAuthority(String authority) {
        User user = getPrincipal();
        if (user != null) {
            if (user.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasResource(String resource) {
        User user = getPrincipal();
        if (user != null) {
            if (user.getResources().contains(resource)) {
                return true;
            }
        }
        return false;
    }

    public static String encrypt(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

}
