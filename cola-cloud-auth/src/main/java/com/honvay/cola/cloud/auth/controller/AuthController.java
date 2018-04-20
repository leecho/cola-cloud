package com.honvay.cola.cloud.auth.controller;

import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.vcc.client.VerificationCodeClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author LIQIU
 * @date 2018-3-8
 **/
@RestController
@Api(tags="认证接口")
public class AuthController {

    @Autowired
    private VerificationCodeClient verificationCodeClient;

    @GetMapping("/current")
    @ApiOperation("获取当前用户信息")
    public Principal getUser(Principal principal) {
        return principal;
    }

    @PostMapping("/sms/token")
    @ApiOperation("获取短信登录Token")
    public Result<String> getToken(String phoneNumber){
        return verificationCodeClient.getToken(6,null,"0",phoneNumber,true);
    }
}
