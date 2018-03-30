package com.honvay.cola.cloud.dict.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.dict.entity.DictItem;
import com.honvay.cola.cloud.dict.service.DictItemService;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 数据字典值控制器
 *
 * @author Cola Generated
 * @Date Fri Dec 22 15:05:45 CST 2017
 */
@EnableAudit
@RestController
@RequestMapping("/dict/item")
@Api(value = "/dict/item",tags = "数据字典项")
public class DictItemController extends BaseController {


    @Autowired
    private DictItemService dictValueService;


    @GetMapping("/listByCode/{code}")
    @ApiOperation("根据数据字典代码获取值列表")
    public Result<List<DictItem>> listByDictCode(@PathVariable  String code){
        return this.success(this.dictValueService.listByCode(code));
    }

    /**
     * 获取数据字典值列表
     */
    @GetMapping(value = "/list/{code}")
    @ApiOperation("获取数据字典值列表")
    public Result<Page<List<DictItem>>> list(@PathVariable String code, String value, String name) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.like("value",value);
        wrapper.like("name",name);
        wrapper.eq("code",code);
        Page page = this.getPagination();
        if(page.getOrderByField() == null){
            page.setOrderByField("orderNo");
        }
        return this.success(dictValueService.selectPage(page,wrapper));
    }

    /**
     * 新增数据字典值
     */
    @PostMapping(value = "/add")
    @ApiOperation("新增数据字典值")
    public Result<String> add(@Validated DictItem dictItem) {
        dictValueService.insert(dictItem);
        return this.success();
    }

    /**
     * 删除数据字典值
     */
    @PostMapping(value = "/delete/{id}")
    @ApiOperation("删除数据字典值")
    public Result<String> delete(@PathVariable("id") Integer id) {
        dictValueService.deleteById(id);
        return this.success();
    }

    /**
     * 修改数据字典值
     */
    @PostMapping(value = "/update")
    @ApiOperation("修改数据字典值")
    public Result<String> update(@Validated  DictItem dictItem) {
        dictValueService.updateById(dictItem);
        return this.success();
    }

    /**
     * 获取数据字典值
     */
    @GetMapping(value = "/get/{id}")
    @ApiOperation("获取数据字典值")
    public Result<DictItem> get(@PathVariable("id") Integer id) {
        return this.success(dictValueService.selectById(id));
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除数据字典值")
    public Result<String> batchDelete(Long[] ids){
        Assert.isTrue(ids.length > 0,"参数不能为空");
        dictValueService.deleteBatchIds(Arrays.asList(ids));
        return  this.success();
    }
}
