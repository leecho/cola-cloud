package com.honvay.cola.service.attachment.controller;

import com.honvay.cola.cloud.framework.base.audit.EnableAudit;
import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.service.attachment.entity.Attachment;
import com.honvay.cola.service.attachment.service.AttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 文件上传控制器
 *
 * @author LIQIU
 * @Date Thu Dec 14 15:57:29 CST 2017
 */
@EnableAudit
@RestController
@RequestMapping("/attachment")
@Api(value="/attachment",tags="附件服务")
public class AttachmentController extends BaseController {

    @Autowired
    private AttachmentService attachmentService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @ApiOperation("上传附件")
    public Result<String> upload(@RequestPart("file") MultipartFile file,String bizId,String bizCode) {
        Object key;
        try {
            key = this.attachmentService.save(file.getInputStream(),file.getOriginalFilename(),file.getContentType(),bizId,bizCode,file.getSize());
        } catch (IOException e) {
            throw new RuntimeException("存储文件失败" + e);
        }
        return this.success(key);
    }

    /**
     * 删除附件
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    @ApiOperation("删除附件")
    public Result<String> delete(@PathVariable Long id){
        this.attachmentService.delete(id);
        return this.success();
    }

    /**
     * 根据业务单据ID和业务代码获取附件列表
     * @param bizId
     * @param bizCode
     * @return
     */
    @PostMapping("/list")
    @ApiOperation("获取附件列表")
    public Result<String> delete(String bizId,String bizCode){
       return this.success(this.attachmentService.listByBizIdAndBizCode(bizId,bizCode));
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{id}")
    @ApiOperation("下载附件")
    public void download(@PathVariable Long id, HttpServletResponse response) {
        Attachment attachment = this.attachmentService.selectById(id);
        response.addHeader("Content-Type",attachment.getContentType());
        String encodedfileName = null;
        try {
            encodedfileName = new String(attachment.getFileName().getBytes(), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
        response.setHeader("Content-Length",String.valueOf(attachment.getSize()));
        try {
            IOUtils.copy(this.attachmentService.getInputStream(attachment.getAttachmentKey()),response.getOutputStream());
        } catch (IOException e) {
           throw new RuntimeException("下载文件失败" + e);
        }
    }

    /**
     * 下载文件
     */
    @GetMapping("/image/{key}")
    @ApiOperation("下载附件")
    public void image(@PathVariable String key, HttpServletResponse response) {
        Attachment attachment = this.attachmentService.getByKey(key);
        String contentType = attachment.getContentType();
        Assert.isTrue(contentType.startsWith("image"),"该附件不是图片文件");
        try {
            response.setContentType(attachment.getContentType());
            IOUtils.copy(this.attachmentService.getInputStream(key),response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("下载图片失败" + e);
        }
    }

}
