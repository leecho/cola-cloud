package com.honvay.cola.cloud.uc.model;

import com.honvay.cloud.framework.criteria.Criteria;
import com.honvay.cloud.framework.criteria.annotation.BetweenAnd;
import com.honvay.cloud.framework.criteria.annotation.Eq;
import com.honvay.cloud.framework.criteria.annotation.Like;
import com.honvay.cola.cloud.uc.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author LIQIU
 * @date 2018-4-17
 **/
@Data
@ApiModel
public class SysUserCriteria implements Criteria<SysUser> {

    @Like
    @ApiModelProperty("名称")
    private String name;

    @Like
    @ApiModelProperty("用户名")
    private String username;

    @Like
    @ApiModelProperty("手机号")
    private String phonenNumber;

    @Eq
    @ApiModelProperty("状态")
    private String status;

    @BetweenAnd(columns="create_time",end = "createTimeEnd")
    private Date createTimeStart;

    private Date createTimeEnd;

    @Like
    @ApiModelProperty("邮箱")
    private String email;
}
