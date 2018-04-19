package com.honvay.cola.service.attachment.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cola.cloud.framework.base.service.impl.BaseServiceImpl;
import com.honvay.cola.cloud.framework.security.utils.SecurityUtils;
import com.honvay.cola.cloud.framework.util.CryptUtils;
import com.honvay.cola.framework.storage.FileStorage;
import com.honvay.cola.service.attachment.entity.Attachment;
import com.honvay.cola.service.attachment.service.AttachmentService;
import com.honvay.cola.service.attachment.validator.FileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author @author SKL
 * @since 2017-12-14
 */
@Service
public class AttachmentServiceImpl extends BaseServiceImpl<Attachment> implements AttachmentService {

    @Autowired
    FileValidator fileValidator;

    @Autowired
    FileStorage fileStorage;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveAndClear(InputStream input, String fileName, String contentType, String bizId, String bizCode, Long size){
        //清除原来的附件
        EntityWrapper<Attachment> wrapper = this.newEntityWrapper();
        wrapper.eq("biz_id",bizId).eq("biz_code",bizCode);
        this.delete(wrapper);

        return this.save(input, fileName, contentType, bizId, bizCode, size);
    }

    @Override
    @Transactional
    public String save(InputStream input, String fileName, String contentType, String bizId, String bizCode, Long size) {
        //校验文件合法性
        fileValidator.validate(size, fileName, contentType);

        String key = null;
        try {
            key = CryptUtils.encryptMD5(bizCode + "-" + bizId + "-" + UUID.randomUUID().toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加密附件KEY失败");
        }

        //保存到数据库记录
        Attachment attachment = new Attachment();
        attachment.setAttachmentKey(key);
        attachment.setSize(size);
        attachment.setFileName(fileName);
        attachment.setCreateTime(new Date());
        attachment.setCreateBy(SecurityUtils.getUsername());
        attachment.setBizCode(bizCode);
        attachment.setBizId(bizId);
        attachment.setContentType(contentType);
        this.insert(attachment);
        //存储上传
        fileStorage.store(input, key);
        return key;

    }

    @Override
    public InputStream getInputStream(String key) {
        return fileStorage.getInputStream(key);
    }

    @Override
    public InputStream getInputStream(String bizId,String bizCode){
        Attachment attachment = this.getByBizIdAndBizCode(bizId,bizCode);
        if(attachment != null){
            return this.getInputStream(attachment.getAttachmentKey());
        }
        return null;
    }

    @Override
    public void delete(Long id){
        Attachment attachment = this.selectById(id);
        Assert.notNull(attachment,"附件不存在");
        this.fileStorage.remove(attachment.getAttachmentKey());
        this.deleteById(id);
    }

    @Override
    public Attachment getByKey(String key){
        return this.findOneByColumn("attachment_key",key);
    }

    @Override
    public List<Attachment> listByBizIdAndBizCode(String bizId, String bizCode) {
        EntityWrapper<Attachment> wrapper = new EntityWrapper<>();
        wrapper.eq("biz_id",bizId).eq("biz_code",bizCode);
        return this.selectList(wrapper);
    }

    @Override
    public Attachment getByBizIdAndBizCode(String bizId, String bizCode) {
        EntityWrapper<Attachment> wrapper = new EntityWrapper<>();
        wrapper.eq("biz_id",bizId).eq("biz_code",bizCode);
        return this.selectOne(wrapper);
    }
}
