package com.honvay.cola.cloud.organization.client;

import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.organization.model.SysOrganizationDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author LIQIU
 * @date 2018-4-20
 **/
@FeignClient(serviceId = "organization-service")
public interface SysOrganizationClient {

    @PostMapping("/organization/organization/add")
    Result<SysOrganizationDTO> add(SysOrganizationDTO sysOrganizationDTO);

}
