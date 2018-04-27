package com.honvay.cola.cloud.upm.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author LIQIU
 * @date 2018-4-27
 **/
@Data
@ApiModel("系統资源")
public class SysResourceDTO {

    @ApiModelProperty("资源ID")
    private Long id;
    /**
     * 权限的标识
     */
    @ApiModelProperty("编号")
    @NotBlank(message = "编号不能为空")
    private String code;
    /**
     * 父id
     */
    @ApiModelProperty("父节点")
    private Long parent;
    /**
     * 权限名称
     */
    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    private String name;
    /**
     * 权限请求路径
     */
    @ApiModelProperty("URL")
    private String url;
    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String icon;
    /**
     * 排序
     */
    @ApiModelProperty("排序号")
    private Integer sort;
    /**
     * 是否可用：Y-可用；N-不可用
     */
    @ApiModelProperty("是否可用，Y-可用；N-不可用")
    private String status;
    /**
     * 加载类型 0：普通加载，1、IFRAME加载
     */
    @ApiModelProperty("加载类型 0：普通加载，1、IFRAME加载")
    private String loadType;

    /**
     * 类型 0：目录，1：菜单，2：功能
     */
    @ApiModelProperty("类型 0：目录，1：菜单，2：功能")
    private Integer type;

    @ApiModelProperty("备注")
    private String description;

    /**
     * 路由
     */
    @ApiModelProperty("页面路由")
    private String route;
    /**
     * 所属服务
     */
    @ApiModelProperty("服务ID")
    private String serviceId;
}
