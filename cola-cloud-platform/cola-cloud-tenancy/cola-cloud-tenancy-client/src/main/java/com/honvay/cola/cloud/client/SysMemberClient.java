package com.honvay.cola.cloud.client;

import com.honvay.cola.cloud.framework.core.protocol.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author LIQIU
 * @date 2018-4-20
 **/
@FeignClient(serviceId = "tenancy-service")
public interface SysMemberClient {

    @PostMapping("/tenancy/member/add")
    Result<String> add(SysMemberDTO sysMemberDTO);

    @PostMapping("/tenancy/member/delete")
    Result<String> delete(SysMemberDTO sysMemberDTO);

}
