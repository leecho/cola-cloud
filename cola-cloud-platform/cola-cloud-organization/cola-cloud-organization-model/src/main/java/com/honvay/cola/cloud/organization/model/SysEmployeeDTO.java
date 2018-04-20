package com.honvay.cola.cloud.organization.model;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author LIQIU
 * @date 2018-1-4
 **/
@Data
public class SysEmployeeDTO implements Serializable {
    private Long   id;
    @Email(message = "邮箱格式不正确")
    private String email;
    @NotNull(message = "名称不能为空")
    private String name;
    @NotNull(message = "手机号不能为空")
    private String phoneNumber;
    @NotNull(message = "部门ID不能为空")
    private Long   orgId;
    private Long   postId;
    @NotNull(message = "用户名不能为空")
    private String username;

}
