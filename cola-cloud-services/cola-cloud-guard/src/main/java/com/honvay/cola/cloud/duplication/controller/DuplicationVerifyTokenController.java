package com.honvay.cola.cloud.duplication.controller;

import com.honvay.cola.cloud.duplication.repository.DuplicationVerifyTokenRepository;
import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 防重复提交的控制器
 *
 * @author LIQIU
 * @date 2017年12月06日16:17:29
 */
@EnableAudit
@RestController
@RequestMapping("/duplication/verify")
@Api(value = "/duplication/verify", tags = "防重复提交TOKEN发放器")
public class DuplicationVerifyTokenController {


    @Autowired
    DuplicationVerifyTokenRepository repeatKeyRepository;

    /**
     * 获取防重复提交的唯一标识码
     *
     * @author LIQIU
     */
    @GetMapping(path = "/token")
    @ResponseBody
    @ApiOperation("获取防重复提交校验TOKEN")
    public String token() {
        return repeatKeyRepository.push();
    }
}
