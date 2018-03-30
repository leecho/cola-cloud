package com.honvay.cola.cloud.uc.model;

import lombok.Data;

/**
 * Created by bojieshen on 2018/3/14 0014.
 */
@Data
public class SysRegisterDTO {

  private String sourceCode;

  /**
   * 推荐人主键
   */
  private Long recommendSysUserId;

  /**
   * 电话号码
   */
  private String phoneNumber;

    /**
     * 注册用户类型
     */
  private String type;

}
