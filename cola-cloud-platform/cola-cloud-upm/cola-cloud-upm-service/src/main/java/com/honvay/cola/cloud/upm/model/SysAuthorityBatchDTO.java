package com.honvay.cola.cloud.upm.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author LIQIU
 * @date 2018-3-31
 **/
@ApiModel("批量添加权限")
@Data
public class SysAuthorityBatchDTO {

    @NotNull(message = "资源ID不能为空")
    @ApiModelProperty("资源ID")
    private Long[] resourceIds;

    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty("角色ID")
    private Long[] roleIds;
}
