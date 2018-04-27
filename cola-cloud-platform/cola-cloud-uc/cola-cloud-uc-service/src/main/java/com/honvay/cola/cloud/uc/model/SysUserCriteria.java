package com.honvay.cola.cloud.uc.model;

import com.honvay.cloud.framework.criteria.Criteria;
import com.honvay.cloud.framework.criteria.annotation.BetweenAnd;
import com.honvay.cloud.framework.criteria.annotation.Eq;
import com.honvay.cloud.framework.criteria.annotation.Like;
import com.honvay.cola.cloud.framework.base.pagination.PageableCriteria;
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
public class SysUserCriteria extends PageableCriteria<SysUser> {

    @Like
    @ApiModelProperty("名称")
    private String name;

    @Like
    @ApiModelProperty("用户名")
    private String username;

    @Like
    @ApiModelProperty("手机号")
    private String phoneNumber;

    @Eq
    @ApiModelProperty("状态")
    private String status;

    @BetweenAnd(columns="create_time",end = "createTimeEnd")
    @ApiModelProperty("起始时间")
    private Date createTimeStart;

    @ApiModelProperty("结束时间")
    private Date createTimeEnd;

    @Like
    @ApiModelProperty("邮箱")
    private String email;
}
