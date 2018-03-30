package com.honvay.cola.cloud.organization.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author LIQIU
 * @date 2018-1-4
 **/
@Data
public class SysEmployeeAddDTO implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotNull(message = "部门ID不能为空")
    private Long orgId;
    private Long postId;
}
