package com.honvay.cola.cloud.devtools.generator.controller;

import com.honvay.cola.cloud.devtools.generator.properties.DevtoolsProperties;
import com.honvay.cola.cloud.devtools.generator.service.GenerateService;
import com.honvay.cola.cloud.devtools.generator.service.TableService;
import com.honvay.cola.cloud.devtools.generator.task.GenerateTask;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 代码生成控制器
 *
 * @author LIQIU
 * @Date 2017年11月30日16:39:19
 */
@RestController
@Api(value="/generator",tags = "代码生成器接口")
@RequestMapping("/generator")
public class GeneratorController extends BaseController {

    @Autowired
    private TableService tableService;

    @Autowired
    private GenerateService generateService;

    @Autowired
    private DevtoolsProperties devtoolsProperties;

    /**
     * 获取环境信息
     */
    @GetMapping("/env")
    @ApiOperation("获取代码生成器配置")
    public Object blackboard() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("tables",tableService.getAllTables());
        hashMap.put("params",devtoolsProperties);
        Result result = this.success();
        result.setData(hashMap);
        return result;
    }

    /**
     * 生成代码
     */
    @ApiOperation("生成代码")
    @PostMapping("/generate")
    public Object generate(GenerateTask generateTask){
        generateService.generate(generateTask);
        return this.success();
    }
}
