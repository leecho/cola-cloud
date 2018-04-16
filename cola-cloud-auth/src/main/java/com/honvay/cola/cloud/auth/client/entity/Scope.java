package com.honvay.cola.cloud.auth.client.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
