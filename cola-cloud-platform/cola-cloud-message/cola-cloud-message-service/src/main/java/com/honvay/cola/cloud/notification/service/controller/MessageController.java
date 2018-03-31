package com.honvay.cola.cloud.notification.service.controller;

import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.framework.security.userdetail.User;
import com.honvay.cola.cloud.framework.security.utils.SecurityUtils;
import com.honvay.cola.cloud.message.model.MessageDTO;
import com.honvay.cola.cloud.notification.service.entity.Message;
import com.honvay.cola.cloud.notification.service.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 站内信控制器
 *
 * @author Cola Generated
 * @Date 09:47:03
 */
@RestController
@RequestMapping("/")
@Api(value = "/",tags = "消息中心")
public class MessageController extends BaseController {


    @Autowired
    private MessageService messageService;

    /**
     * 获取站内信列表
     */
    @GetMapping(value = "/list")
    @ApiOperation("获取消息列表")
    public Result<List<Message>> list(Message message) {
        User user= SecurityUtils.getPrincipal();
        //User user=new User();
        return this.success(messageService.selectList(message,user.getId()));
    }

    /**
     * 新增站内信
     */
    @PostMapping(value = "/add")
    @ApiOperation("添加消息")
    public Result<String> add(MessageDTO messageDTO) {
        messageService.add(messageDTO);
        return this.success();
    }

    /**
     * 删除站内信
     */
    @PostMapping(value = "/delete/{id}")
    @ApiOperation("删除消息")
    public Result<String> delete(@PathVariable("id") Long id) {
        messageService.deleteById(id);
        return this.success();
    }

    /**
     * 读站内信-修改站内信的读状态
     */
    @PostMapping(value = "/update")
    @ApiOperation("修改消息状态")
    public Result<String> read(@PathVariable("id") Long id) {
        messageService.read(id);
        return this.success();
    }

    /**
     * 获取站内信
     */
    @GetMapping(value = "/get/{id}")
    @ApiOperation("获取消息内容")
    public Result<Message> get(@PathVariable("id") Long id) {
        return this.success(messageService.selectById(id));
    }
    
}
