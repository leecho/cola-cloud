package com.honvay.cola.cloud.organization.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LIQIU
 * @date 2018-1-4
 **/
@Data
public class SysEmployeeVO implements Serializable {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private String postName;
    private String orgId;
    private String userId;
    private String status;
    private Long postId;
}
