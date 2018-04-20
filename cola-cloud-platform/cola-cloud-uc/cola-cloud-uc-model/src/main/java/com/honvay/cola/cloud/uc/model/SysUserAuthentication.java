package com.honvay.cola.cloud.uc.model;


import lombok.Data;

import java.io.Serializable;

/**
 * 认证主体
 *
 * @author LIQIU
 * @date 2017-12-20
 */
@Data
public class SysUserAuthentication implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String status;

    private String name;

    private String type;
}
