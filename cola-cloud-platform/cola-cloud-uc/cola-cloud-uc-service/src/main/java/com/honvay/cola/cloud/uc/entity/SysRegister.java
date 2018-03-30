package com.honvay.cola.cloud.uc.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> 注册信息表 </p>
 *
 * @author bojieshen
 * @since 2018-03-14
 */
@Data
@Accessors(chain = true)
@TableName("cola_sys_register")
public class SysRegister implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  @TableId(value="id", type= IdType.AUTO)
  private Long id;

  /**
   * 注册来源编号
   */
  @TableField("source_code")
  private String sourceCode;

  /**
   * 注册人主键
   */
  @TableField("register_sys_user_id")
  private Long registerSysUserId;

  /**
   * 推荐人主键
   */
  @TableField("recommend_sys_user_id")
  private Long recommendSysUserId;

  /**
   * 创建时间
   */
  @TableField("create_time")
  private Date createTime;

  /**
   * 更新时间
   */
  @TableField("update_time")
  private Date updateTime;


}
