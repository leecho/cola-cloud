package com.honvay.cola.cloud.dict.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.honvay.cola.cloud.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author liqiu
 * @since 2017-12-22
 */
@Data
@Accessors(chain = true)
@TableName("adi_dict_item")
public class DictItem extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

    @NotEmpty(message = "数据字典项名称不能为空")
	private String name;
    /**
     * 名称
     */
    @NotEmpty(message = "数据字典项的值不能为空")
	private String value;
    /**
     * 提示
     */
	private String description;
	private String enable;

	private Integer orderNo;

	@NotEmpty(message = "数据字典项编号不能为空")
	private String code;

}
