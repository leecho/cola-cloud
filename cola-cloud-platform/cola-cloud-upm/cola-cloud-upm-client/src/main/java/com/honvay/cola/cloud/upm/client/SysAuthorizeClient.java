package com.honvay.cola.cloud.upm.client;

import com.honvay.cola.cloud.upm.model.Authorize;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author LIQIU
 * @date 2018-3-8
 **/
@FeignClient(serviceId = "upm-service")
public interface SysAuthorizeClient {

    /**
     * 获取授权用户
     * @param userId
     * @return
     */
    @GetMapping(value = "/upm/authorize/get")
    Authorize getAuthorize(@RequestParam("userId") Long userId);
}
