package com.honvay.cola.cloud.auth.client.controller;


import com.honvay.cola.cloud.auth.client.entity.Scope;
import com.honvay.cola.cloud.auth.client.service.ScopeService;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * OAuth2客户端授权范围 控制器
 * </p>
 *
 * @author LIQIU
 * @since 2018-04-13
 */
@RestController
@RequestMapping("/client/scope")
@Api(tags = "OAuth2客户端授权范围 接口")
public class ScopeController extends BaseController {

        @Autowired
        private ScopeService scopeService;

        /**
         * 获取OAuth2客户端授权范围列表
         */
        @GetMapping(value = "/list")
        @ApiOperation("获取OAuth2客户端授权范围列表")
        public Result<List<Scope>> list() {
            return this.success(scopeService.selectList());
        }

        /**
         * 新增OAuth2客户端授权范围
         */
        @PostMapping(value = "/add")
        @ApiOperation("新增OAuth2客户端授权范围")
        public Result<String> add(Scope scope) {
            scopeService.insert(scope);
            return this.success();
        }

        /**
         * 删除OAuth2客户端授权范围
         */
        @PostMapping(value = "/delete/{id}")
        @ApiOperation("删除OAuth2客户端授权范围")
        public Result<String> delete(@PathVariable("id") Long id) {
            scopeService.deleteById(id);
            return this.success();
        }

        /**
         * 修改OAuth2客户端授权范围
         */
        @PostMapping(value = "/update")
        @ApiOperation("修改OAuth2客户端授权范围")
        public Result<String> update(Scope scope) {
            scopeService.updateById(scope);
            return this.success();
        }

        /**
         * 获取OAuth2客户端授权范围
         */
        @GetMapping(value = "/get/{id}")
        @ApiOperation("获取OAuth2客户端授权范围")
        public Result<Scope> get(@PathVariable("id") Long id) {
            return this.success(scopeService.selectById(id));
        }
}

