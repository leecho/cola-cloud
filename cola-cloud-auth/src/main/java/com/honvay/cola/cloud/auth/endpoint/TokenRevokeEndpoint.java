package com.honvay.cola.cloud.auth.endpoint;

import com.honvay.cola.cloud.framework.core.protocol.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 登出接口
 * @author LIQIU
 * @date 2018-3-26
 **/
@RestController
@Api(tags = "登出接口")
public class TokenRevokeEndpoint {

    @Autowired
    @Qualifier("consumerTokenServices")
    private ConsumerTokenServices tokenServices;

    @PostMapping("/oauth/logout")
    @ApiOperation("退出登录")
    public Result<String> deleteAccessToken(Principal principal){
        OAuth2Authentication auth2Authentication = (OAuth2Authentication)principal;
        OAuth2AuthenticationDetails details =  (OAuth2AuthenticationDetails)auth2Authentication.getDetails();
        tokenServices.revokeToken(details.getTokenValue());
        return Result.buildSuccess("");
    }

}
