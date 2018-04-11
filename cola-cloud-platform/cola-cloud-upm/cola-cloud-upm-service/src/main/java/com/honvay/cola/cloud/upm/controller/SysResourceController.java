package com.honvay.cola.cloud.upm.controller;

import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.upm.cache.SysResourceCacheService;
import com.honvay.cola.cloud.upm.entity.SysResource;
import com.honvay.cola.cloud.upm.model.SysMenuVO;
import com.honvay.cola.cloud.upm.service.SysResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统资源控制类
 * @author LIQIU
 * @date 2017-12-11
 */
@EnableAudit
@RestController
@RequestMapping("/resource")
@Api(value="/resource",tags = "系统资源管理")
public class SysResourceController extends BaseController {

    @Autowired
    private SysResourceService sysResourceService;

    @Autowired
    private SysResourceCacheService upmCacheService;

    @GetMapping("/menu")
    @ApiOperation("获取当前用户已授权的菜单")
    public Result<List<SysMenuVO>> menu(){
        List<SysMenuVO> menus = this.sysResourceService.getAuthorizedResource();
        return this.success(menus);
    }

    @ApiOperation(value = "获取子资源")
    @GetMapping("/list")
    public Object list(Long id) {
        return this.sysResourceService.getResourceListByPid(id);
    }

    @ApiOperation(value = "删除资源")
    @PostMapping("/delete")
    public Object del(@RequestParam Long id) {
        sysResourceService.delete(id);
        return this.success();
    }

    @ApiOperation(value = "新增/修改资源")
    @PostMapping("/save")
    public Object save(SysResource t) {

        if (t.getId() != null) {
            this.sysResourceService.updateById(t);
        } else {
            this.sysResourceService.insert(t);
        }
        return this.success(t.getId());
    }

    @ApiOperation(value = "获取资源")
    @GetMapping(value = "/get")
    public Object show(@RequestParam Long id) {
        SysResource entity = this.sysResourceService.selectById(id);
        Result result = this.success();
        result.setData(entity);
        return result;
    }

    @ApiOperation(value="刷新缓存")
    @GetMapping(value="/refreshCache")
    public Result<String> refreshCache(){
        upmCacheService.doCacheAll();
        return this.success();
    }
    
    @PostMapping("/move")
    @ApiOperation("移动资源")
	public Object move(SysResource resource) {
		SysResource entity = this.sysResourceService.selectById(resource.getId());
		entity.setPid(resource.getPid());
		this.sysResourceService.updateAllColumnById(entity);
		return this.success();
	}
}
