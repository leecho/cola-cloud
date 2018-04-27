package com.honvay.cola.cloud.upm.controller;

import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.upm.cache.SysResourceCacheService;
import com.honvay.cola.cloud.upm.entity.SysResource;
import com.honvay.cola.cloud.upm.model.SysResourceDTO;
import com.honvay.cola.cloud.upm.model.SysResourceTreeNode;
import com.honvay.cola.cloud.upm.model.SysResourceVO;
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
    private SysResourceCacheService sysResourceCacheService;

    @GetMapping("/menu")
    @ApiOperation("获取当前用户已授权的资源树")
    public Result<List<SysResourceTreeNode>> tree() {
        List<SysResourceTreeNode> treeNodes = this.sysResourceService.getResourceTree();
        return this.success(treeNodes);
    }

    @GetMapping("/all")
    @ApiOperation("获取当前用户已授权的资源列表")
    public Result<List<SysResourceVO>> menu() {
        List<SysResourceVO> resources = this.sysResourceService.getResourceList();
        return this.success(resources);
    }

    @ApiOperation(value = "获取数据库中的子资源")
    @GetMapping("/tree")
    public Result<List<SysResourceTreeNode>> all(Long id) {
        return this.success(this.sysResourceService.getChildren(id));
    }

    @ApiOperation(value = "删除资源")
    @PostMapping("/delete")
    public Result<String> del(@RequestParam Long id) {
        sysResourceService.delete(id);
        return this.success();
    }

    @ApiOperation(value = "新增/修改资源")
    @PostMapping("/save")
    public Result<String> save(SysResourceDTO sysResourceDTO) {
        if (sysResourceDTO.getId() != null) {
            this.sysResourceService.update(sysResourceDTO);
        } else {
            this.sysResourceService.insert(sysResourceDTO);
        }
        return this.success(sysResourceDTO.getId());
    }

    @ApiOperation(value = "获取资源")
    @GetMapping(value = "/get")
    public Result<String> get(@RequestParam Long id) {
        return this.success(this.sysResourceService.selectById(id));
    }

    @ApiOperation(value = "刷新缓存")
    @GetMapping(value = "/refreshCache")
    public Result<String> refreshCache() {
        sysResourceCacheService.doCacheAll();
        return this.success();
    }

    @PostMapping("/move")
    @ApiOperation("移动资源")
    public Result<String> move(SysResource resource) {
        SysResource entity = this.sysResourceService.selectById(resource.getId());
        entity.setParent(resource.getParent());
        this.sysResourceService.updateAllColumnById(entity);
        return this.success();
    }
}
