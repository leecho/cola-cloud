package com.honvay.cola.cloud.framework.base.pagination;

import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.util.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author LIQIU
 * @date 2018-4-17
 **/
@Data
public class Pagination {

    /**
     * 升序
     */
    public final static String ASC = "asc";
    /**
     * 降序
     */
    public final static String DESC = "desc";

    @ApiModelProperty("每页数据条数")
    private Integer pageSize;
    @ApiModelProperty("分页页码")
    private Integer pageNumber;
    @ApiModelProperty("排序字段")
    private String orderBy;
    @ApiModelProperty("排序顺序")
    private String orderDirection ;

    public <T> Page getPage() {
        Integer pageSize = this.pageSize != null ? this.pageSize : 20;
        Integer pageNumber = this.pageNumber != null ? this.pageNumber : 1;
        Page<T> page = new Page<T>(pageNumber, pageSize);
        if (StringUtils.isNotEmpty(orderBy)) {
            page.setOpenSort(true);
            page.setOrderByField(orderBy);
            page.setAsc(!DESC.equals(orderDirection));
        }
        return page;
    }
}
