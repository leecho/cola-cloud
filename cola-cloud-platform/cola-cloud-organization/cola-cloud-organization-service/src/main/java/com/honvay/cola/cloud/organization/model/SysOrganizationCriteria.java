package com.honvay.cola.cloud.organization.model;

import com.honvay.cloud.framework.criteria.annotation.Eq;
import com.honvay.cloud.framework.criteria.annotation.Like;
import com.honvay.cola.cloud.framework.base.pagination.PageableCriteria;
import com.honvay.cola.cloud.organization.entity.SysOrganization;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author LIQIU
 * @date 2018-4-27
 **/
@Data
@ApiModel("组织架构查询条件")
public class SysOrganizationCriteria extends PageableCriteria<SysOrganization> {

    @Like
    @ApiModelProperty("组织名称")
    private String name;

    @Like
    @ApiModelProperty("组织编号")
    private String code;

    @Eq
    @ApiModelProperty("状态")
    private String status;
}
