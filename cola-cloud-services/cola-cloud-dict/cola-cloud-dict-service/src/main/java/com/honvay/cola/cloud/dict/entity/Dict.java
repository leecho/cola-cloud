package com.honvay.cola.cloud.dict.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.honvay.cola.cloud.client.jackson.annotation.JsonDict;
import com.honvay.cola.cloud.client.jackson.annotation.JsonDictName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotEmpty;

import java.beans.Transient;
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
@TableName("cola_dict")
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 排序
     */
    @NotEmpty(message = "数据字典代码不能为空")
    private String code;
    /**
     * 名称
     */
    @NotEmpty(message = "数据字典名称不能为空")
    private String name;
    /**
     * 提示
     */
    private String description;
    /**
     * 是否启用
     */
    private String enable;

    private String enableText;

    /**
     * 排序号
     */
    private Integer orderNo;
    /**
     * 父节点，通过Code关联
     */
    private String parent;
}
