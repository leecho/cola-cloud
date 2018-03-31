package com.honvay.cola.cloud.upm.controller;

import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.upm.entity.SysResource;
import com.honvay.cola.cloud.upm.entity.SysRole;
import com.honvay.cola.cloud.upm.model.SysAuthorityBatchDTO;
import com.honvay.cola.cloud.upm.service.SysAuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * 系统权限类，映射资源和角色的关系
 * @author LIQIU
 * @date 2017.12.10
 */
@EnableAudit
@RestController
@RequestMapping("/authority")
@Api(value="/authority",tags = "系统权限管理")
public class SysAuthorityController extends BaseController {
	
	@Autowired
	private SysAuthorityService sysAuthorityService;

    /**
     * 通过资源ID获取角色列表
     * @param resourceId
     * @return
     */
	@GetMapping("/listRole")
    @ApiOperation(("通过资源ID获取角色列表"))
	public Result<List<SysRole>> listRoleByResource(Long resourceId) {
		return this.success(sysAuthorityService.getRoleListByResourceId(resourceId));
	}

	@GetMapping("/listResourceCodes")
    @ApiOperation("通过授权对象和授权类型获取资源编号")
    public Collection<String> listResourceCodes(Long target, String type){
        return sysAuthorityService.getResourceCodesByAuthorizeTargetAndType(target,type);
    }

	/**
	 * 通过角色ID获取资源列表
	 * @param roleId
	 * @return
	 */
	@GetMapping("/listResource")
	@ApiOperation(("通过角色ID获取资源列表"))
	public Result<List<SysResource>> listResourceByRole(Long roleId) {
		return this.success(sysAuthorityService.getResourcesByRoleId(roleId));
	}

    /**
     * 添加权限，多个资源ID使用","链接
     * @param sysAuthorityBatchDTO
     * @return
     */
    @PostMapping("/batch")
    @ApiOperation("批量添加权限")
    public Result<String> batch(@Validated SysAuthorityBatchDTO sysAuthorityBatchDTO) {
        this.sysAuthorityService.batch(sysAuthorityBatchDTO);
        return this.success();
    }

    /**
     * 删除权限，通过资源ID和角色ID删除
     * @param resourceId
     * @param roleId
     * @return
     */
	@PostMapping("/delete")
    @ApiOperation("删除权限")
	public Result<String> delete(@RequestParam("resourceId") Long resourceId , @RequestParam("roleId")  Long roleId) {
		this.sysAuthorityService.delete(resourceId,roleId);
		return this.success();
	}
}
