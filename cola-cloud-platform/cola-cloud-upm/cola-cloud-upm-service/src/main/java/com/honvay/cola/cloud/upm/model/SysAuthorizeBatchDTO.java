package com.honvay.cola.cloud.upm.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 批量添加角色授权对象
 * @author LIQIU
 * @date 2018-1-5
 **/
@Data
@Accessors(chain = true)
@ApiModel("批量授权")
public class SysAuthorizeBatchDTO implements Serializable {
    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty("角色ID")
    private Long[] roleIds;
    @NotNull(message = "授权对象ID不能为空")
    @ApiModelProperty("授权对象(用户或者租户)ID字符串，多个使用,拼接")
    private Long[] targets;
    @NotNull(message = "授权类型不能为空")
    @ApiModelProperty("租户授权为1，用户授权为0")
    private String type;
}
