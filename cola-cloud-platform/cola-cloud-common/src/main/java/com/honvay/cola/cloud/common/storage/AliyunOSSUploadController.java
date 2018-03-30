package com.honvay.cola.cloud.common.storage;

import com.honvay.cola.cloud.framework.base.controller.BaseController;
import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.framework.storage.FileStorage;
import com.honvay.cola.framework.storage.impl.AliyunFileStorage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author LIQIU
 * @date 2018-3-22
 **/
@RestController
@Api(tags = "阿里云OSS上传接口")
public class AliyunOSSUploadController extends BaseController {

    @Autowired(required = false)
    private FileStorage fileStorage;

    @PostMapping("/aliyun/oss/upload")
    @ApiOperation("上传文件")
    public Result<String> upload(@RequestParam("file") MultipartFile file){

        Assert.notNull(fileStorage,"文件上传没有初始化");
        Assert.isTrue(fileStorage.getClass().equals(AliyunFileStorage.class),"没有初始化阿里OSS上传接口");

        try {
            Assert.notNull(file,"文件不能为空");
            String  fileName = file.getOriginalFilename();
            //图片限制 裁剪？
            String key =  UUID.randomUUID().toString().replaceAll("-", "")
                    + fileName.substring(fileName.lastIndexOf("."));
            fileStorage.store(file.getInputStream(),key);
            Result result = this.success();
            result.setData(this.fileStorage.getDownloadUrl(key));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("上传失败");
        }
    }
}
