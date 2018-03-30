package com.honvay.cola.cloud.uc.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liqiu
 * @since 2018-01-04
 */
@Data
@Accessors(chain = true)
@TableName("cola_sys_social")
public class SysSocial implements Serializable {

    private static final long serialVersionUID = 1L;

	private Long id;
	private String type;
	private String token;
	@TableField("sys_user_id")
	private Long sysUserId;


}
