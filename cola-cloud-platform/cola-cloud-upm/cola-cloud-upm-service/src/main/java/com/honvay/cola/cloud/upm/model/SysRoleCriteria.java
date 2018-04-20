package com.honvay.cola.cloud.upm.model;

import com.honvay.cloud.framework.criteria.Criteria;
import com.honvay.cloud.framework.criteria.annotation.Eq;
import com.honvay.cloud.framework.criteria.annotation.Like;
import com.honvay.cola.cloud.upm.entity.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author LIQIU
 * @date 2018-4-20
 **/
@Data
@ApiModel
public class SysRoleCriteria implements Criteria<SysRole> {

    @Like
    @ApiModelProperty("角色编号")
    private String code;

    @Like
    @ApiModelProperty("角色名称")
    private String name;

    @Eq
    @ApiModelProperty("状态")
    private String status;
}
