package com.honvay.cola.cloud.auth.client.controller;


import com.honvay.cola.cloud.auth.client.entity.Client;
import com.honvay.cola.cloud.auth.client.service.ClientService;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * OAuth2客户端 控制器
 * </p>
 *
 * @author LIQIU
 * @since 2018-04-13
 */
@RestController
@RequestMapping("/client/client")
@Api(tags = "OAuth2客户端 接口")
public class ClientController extends BaseController {

        @Autowired
        private ClientService clientService;

        /**
         * 获取OAuth2客户端列表
         */
        @GetMapping(value = "/list")
        @ApiOperation("获取OAuth2客户端列表")
        public Result<List<Client>> list() {
            return this.success(clientService.selectList());
        }

        /**
         * 新增OAuth2客户端
         */
        @PostMapping(value = "/add")
        @ApiOperation("新增OAuth2客户端")
        public Result<String> add(Client client) {
            clientService.insert(client);
            return this.success();
        }

        /**
         * 删除OAuth2客户端
         */
        @PostMapping(value = "/delete/{id}")
        @ApiOperation("删除OAuth2客户端")
        public Result<String> delete(@PathVariable("id") Long id) {
            clientService.deleteById(id);
            return this.success();
        }

        /**
         * 修改OAuth2客户端
         */
        @PostMapping(value = "/update")
        @ApiOperation("修改OAuth2客户端")
        public Result<String> update(Client client) {
            clientService.updateById(client);
            return this.success();
        }

        /**
         * 获取OAuth2客户端
         */
        @GetMapping(value = "/get/{id}")
        @ApiOperation("获取OAuth2客户端")
        public Result<Client> get(@PathVariable("id") Long id) {
            return this.success(clientService.selectById(id));
        }
}

