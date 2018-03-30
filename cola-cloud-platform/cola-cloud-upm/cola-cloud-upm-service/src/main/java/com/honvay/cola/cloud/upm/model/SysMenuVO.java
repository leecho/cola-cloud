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
public class SysMenuVO implements Serializable {

    private Long id;
    private String code;
    private String name;
    private String icon;
    private String url;
    private String loadType;
    private Integer sort;
    private String portalUrl;
    private Integer type;
    private Long pid;
    private String status;

    private List<SysMenuVO> children;

}
