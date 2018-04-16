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
import java.util.Date;

/**
 * <p>
 * OAuth2客户端
 * </p>
 *
 * @author LIQIU
 * @since 2018-04-13
 */
@Data
@Accessors(chain = true)
@TableName("cola_oauth_client")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("created_by")
    private Long createdBy;

    @TableField("created_date")
    private Date createdDate;

    @TableField("last_modified_by")
    private Long lastModifiedBy;

    @TableField("last_modified_date")
    private Date lastModifiedDate;

    @TableField("access_token_validity_seconds")
    private Integer accessTokenValiditySeconds;

    @TableField("client_id")
    private String clientId;

    @TableField("client_secret")
    private String clientSecret;

    @TableField("refresh_token_validity_seconds")
    private Integer refreshTokenValiditySeconds;

    /**
     * 授权类型
     */
    @TableField("grant_type")
    private String grantType;

    /**
     * 资源ID
     */
    @TableField("resource_ids")
    private String resourceIds;

    /**
     * 跳转URL
     */
    @TableField("redirect_uri")
    private String redirectUri;

    /**
     * 是否启用
     */
    private Boolean enable;



}
