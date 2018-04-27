package com.honvay.cola.cloud.upm.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单对象VO
 * @author LIQIU
 * @date 2018-1-2
 */
@Data
@Accessors
public class SysResourceTreeNode implements Serializable {

    private Long id;
    private String code;
    private String name;
    private String icon;
    private String url;
    private String loadType;
    private Integer sort;
    private String route;
    private Integer type;
    private Long parent;
    private String status;
    private String serviceId;

    private List<SysResourceTreeNode> children;

}
