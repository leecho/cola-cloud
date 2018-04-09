package com.honvay.cola.cloud.dict.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.dict.entity.Dict;
import com.honvay.cola.cloud.dict.service.DictService;
import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典目录控制器
 *
 * @author Cola Generated
 * @Date Fri Dec 22 15:05:35 CST 2017
 */
@EnableAudit
@RestController
@RequestMapping("/dict")
@Api(value = "/dict",tags = "数据字典目录")
public class DictController extends BaseController {


    @Autowired
    private DictService dictService;

    /**
     * 获取数据字典目录列表
     */
    @GetMapping(value = "/list")
    @ApiOperation("获取数据字典目录列表")
    public Result<Page<List<Dict>>> list(String code, String name) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.like("code",code);
        wrapper.like("name",name);
        return this.success(dictService.selectPage(this.getPagination(),wrapper));
    }

    /**
     * 新增数据字典目录
     */
    @PostMapping(value = "/add")
    @ApiOperation("新增数据字典目录")
    public Result<String> add(@Validated  Dict dict) {
        dictService.insert(dict);
        return this.success();
    }

    /**
     * 删除数据字典目录
     */
    @PostMapping(value = "/delete/{id}")
    @ApiOperation("删除数据字典目录")
    public Result<String> delete(@PathVariable("id") Long id) {
        dictService.deleteById(id);
        return this.success();
    }

    /**
     * 批量删除数据字典目录
     */
    @PostMapping(value = "/batchDelete")
    @ApiOperation("删除数据字典目录")
    public Result<String> delete(Long[] ids) {
        dictService.batchDelete(ids);
        return this.success();
    }

    /**
     * 修改数据字典目录
     */
    @PostMapping(value = "/update")
    @ApiOperation("修改数据字典目录")
    public Result<String> update(@Validated Dict dict) {
        dictService.updateById(dict);
        return this.success();
    }

    /**
     * 获取数据字典目录
     */
    @GetMapping(value = "/get/{id}")
    @ApiOperation("获取数据字典目录")
    public Result<String> get(@PathVariable("id") Long id) {
        return this.success(dictService.selectById(id));
    }
}
