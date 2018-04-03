package com.honvay.cola.service.attachment.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author @author SKL
 * @since 2017-12-14
 */
@Data
@Accessors
@TableName("cola_attachment")
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 文件上传时的名称
     */
	@TableField("file_name")
	private String fileName;
    /**
     * 文件mime类型
     */
	@TableField("content_type")
	private String contentType;
    /**
     * 文件唯一标识
     */
	private String attachmentKey;
    /**
     * 文件大小,单位字节
     */
	private Long size;
    /**
     * 上传人
     */
	@TableField("create_by")
	private String createBy;
    /**
     * 上传时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 业务类型
     */
	@TableField("biz_id")
	private String bizId;
    /**
     * 业务标识
     */
	@TableField("biz_code")
	private String bizCode;
}
