package com.honvay.cola.cloud.client;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author LIQIU
 * @date 2018-4-20
 **/
@Data
public class SysMemberDTO {

    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @NotNull(message = "用户ID不能为空")
    private Long sysUserId;
}
