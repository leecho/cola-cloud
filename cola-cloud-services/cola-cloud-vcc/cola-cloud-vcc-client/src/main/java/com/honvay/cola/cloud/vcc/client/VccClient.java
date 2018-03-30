package com.honvay.cola.cloud.vcc.client;

import com.honvay.cola.cloud.framework.core.protocol.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author fyk
 */
@FeignClient(name = VccClient.SERVICE_ID)
public interface VccClient {
    /**
     * eureka service name
     */
    String SERVICE_ID = "base-service";
    /**
     * common api prefix
     */
    String API_PATH = "/api/v1";

    @RequestMapping(value = "/base/vcc/token", method = RequestMethod.GET)
    Result<String> getToken(@RequestParam("size") Integer size,
                            @RequestParam("expire") Long expire,
                            @RequestParam("type") String type,
                            @RequestParam("phoneNumber") String phoneNumber);

    @RequestMapping(value = "/base/vcc/validate", method = RequestMethod.GET)
    Result<Boolean> validate(@RequestParam("token") String token, @RequestParam("code") String code, @RequestParam("phoneNumber") String phoneNumber);

}
