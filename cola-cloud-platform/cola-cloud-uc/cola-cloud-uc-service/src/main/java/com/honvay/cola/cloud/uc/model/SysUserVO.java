package com.honvay.cola.cloud.uc.model;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author LIQIU
 * @date 2018-1-8
 **/
@Data
public class SysUserVO {

    private static final long serialVersionUID = 1L;

    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 登录账号
     */
    @NotNull(message = "用户名不能为空")
    @Length(max = 20,message = "用户名长度必须在{max}个字符内")
    private String username;
    /**
     * 名称
     */
    @NotNull(message = "姓名不能为空")
    @Length(max = 20,message = "姓名长度必须为{max}个字符内")
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码盐
     */
    private String salt;
    /**
     * 电话号码
     */
    @TableField("phone_number")
    private String phoneNumber;
    /**
     * 电子邮箱
     */
    @Email(message = "邮箱格式错误")
    private String email;
    /**
     * 状态:  Y-可用  N-不可用
     */
    private String status;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 删除状态: Y-已删除,N-未删除
     */
    private String deleted;
    private String avatar;

    /**
     * 用户类型
     */
    private String type;
}
