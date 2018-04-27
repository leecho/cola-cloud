package com.honvay.cola.cloud.organization.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.honvay.cola.cloud.framework.base.entity.TenancyEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 岗位
 * </p>
 *
 * @author liqiu
 * @since 2018-01-04
 */
@Data
@Accessors(chain = true)
@TableName("cola_sys_post")
@ApiModel("系统岗位")
public class SysPost extends TenancyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@TableId(value="id", type= IdType.AUTO)
    @ApiModelProperty("主键ID")
	private Long id;
    /**
     * 名称
     */
    @NotNull(message = "岗位名称不能为空")
    @Length(max = 50,message = "岗位名称长度必须{max}个字符内")
    @ApiModelProperty("岗位名称")
	private String name;
    /**
     * 代码
     */
    @NotNull(message = "岗位编号不能为空")
    @Length(max = 20,message = "岗位编号长度必须在{max}个字符内")
    @ApiModelProperty("岗位编号")
	private String code;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
	private String description;


}
