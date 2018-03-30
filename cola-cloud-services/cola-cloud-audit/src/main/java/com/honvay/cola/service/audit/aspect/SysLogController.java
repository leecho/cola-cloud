package com.honvay.cola.service.audit.aspect;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.service.audit.entity.SysLog;
import com.honvay.cola.service.audit.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * SysLogController
 *
 * @author YRain
 */
@RestController
@RequestMapping("/sys/log")
@Api(value = "/sys/log", tags = "系统日志")
public class SysLogController extends BaseController {
    
    @Autowired
    private SysLogService sysLogService;

    /**
     * 获取系统日志
     */
    @GetMapping(value = "/get/{id}")
    @ApiOperation("获取系统日志")
    public Result<String> get(@PathVariable("id") Long id) {
        SysLog sysLog = sysLogService.selectById(id);
        return this.success(sysLog);
    }

    /**
     * 查找系统日志
     */
    @GetMapping(value = "/list")
    @ApiOperation("查找系统日志")
    public Result<Page<List<SysLog>>> list(Integer type, String name, Date startDate, Date endDate) {
        EntityWrapper wrapper = new EntityWrapper<>();
        wrapper.like("name", name);
        if (type != null) {
            wrapper.eq("type", type);
        }
        if (startDate != null) {
            wrapper.andNew("create_time >= {0}", startDate);
        }
        //
        if (endDate != null) {
            wrapper.andNew("create_time <= {0}", endDate);
        }
        wrapper.orderBy("id");
        return this.success(sysLogService.selectPage(this.getPagination(), wrapper));
    }

    /**
     * 新增系统日志
     */
    @PostMapping(value = "/create")
    @ApiOperation("新增系统日志")
    public Result<String> create(@Validated SysLog sysLog) {
        this.sysLogService.insert(sysLog);
        return this.success();
    }

    /**
     * 修改
     */
    @PostMapping(value = "/update")
    @ApiOperation("修改系统日志")
    public Result<String> update(@Validated SysLog sysLog) {
        this.sysLogService.updateById(sysLog);
        return this.success();
    }

    /**
     * 删除
     */
    @PostMapping(value = "/delete/{id}")
    @ApiOperation("删除系统日志")
    public Result<String> delete(@PathVariable("id") Long id) {
        this.sysLogService.deleteById(id);
        return this.success();
    }

}
