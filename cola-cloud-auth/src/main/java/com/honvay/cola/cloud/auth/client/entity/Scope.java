package com.honvay.cola.cloud.auth.client.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * OAuth2客户端授权范围
 * </p>
 *
 * @author LIQIU
 * @since 2018-04-13
 */
@Data
@Accessors(chain = true)
@TableName("cola_oauth_client_scope")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Scope implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户端ID
     */
    @TableField("client_id")
    private Long clientId;

    /**
     * 授权范围
     */
    private String scope;

    @TableField("auto_approve")
    private Boolean autoApprove;



}
