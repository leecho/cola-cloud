package com.honvay.cola.cloud.organization.model;

import com.honvay.cloud.framework.criteria.Criteria;
import com.honvay.cloud.framework.criteria.annotation.Eq;
import com.honvay.cloud.framework.criteria.annotation.Like;
import com.honvay.cola.cloud.framework.base.pagination.PageableCriteria;
import com.honvay.cola.cloud.framework.base.pagination.Pagination;
import com.honvay.cola.cloud.organization.entity.SysOrganization;
import com.honvay.cola.cloud.organization.entity.SysPost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author LIQIU
 * @date 2018-4-27
 **/
@Data
@ApiModel("组织架构查询条件")
public class SysPostCriteria extends PageableCriteria<SysPost> {

    @Like
    @ApiModelProperty("岗位名称")
    private String name;

    @Like
    @ApiModelProperty("岗位编号")
    private String code;
}
