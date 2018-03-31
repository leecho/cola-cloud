package com.honvay.cola.cloud.vcc.controller;

import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.framework.util.StringUtils;
import com.honvay.cola.cloud.vcc.service.VerificationCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LIQIU
 * @date 2018-3-9
 **/
@RestController
@RequestMapping("/vcc")
@Api(tags = "验证码中心接口")
public class VerificationCodeController extends BaseController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping("/token")
    @ApiOperation("获取新的验证码")
    public Result<String> getToken(Integer size, Long expire, String type, String subject,Boolean sendSms){
        Object token = this.verificationCodeService.getToken(size,expire,type,subject);
        //如果有手机号则发送短信
        if(sendSms != null && sendSms && StringUtils.isNumeric(subject)){
            this.verificationCodeService.sendSms((String)token,subject);
        }
        return this.success(token);
    }

    @GetMapping("/validate")
    @ApiOperation("校验验证码")
    public Result<Boolean> validate(String token,String code,String subject){
        return this.success(this.verificationCodeService.validate(token,code,subject));
    }

    @GetMapping("/image")
    @ApiOperation("生成验证码图片")
    public void validate(String token, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        this.verificationCodeService.renderImage(token,response.getOutputStream());
    }

    @GetMapping("/sendSms")
    @ApiOperation("发送验证码短信")
    public Result<String> sendSms(String token,String phoneNumber,String templateCode,String signName, String codeParamName){

        if(StringUtils.isEmpty(templateCode) && StringUtils.isEmpty(signName) && StringUtils.isEmpty(codeParamName)){
            this.verificationCodeService.sendSms(token,phoneNumber);
        }else{
            this.verificationCodeService.sendSms(token,phoneNumber,templateCode,signName,codeParamName,null);
        }
        return this.success();
    }
}
