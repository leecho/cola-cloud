package com.honvay.cola.service.attachment.service;

import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.service.attachment.entity.Attachment;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @author SKL
 * @since 2017-12-14
 */
public interface AttachmentService extends BaseService<Attachment> {


    /**
     * 保存并且清除原来的附件
     * @param input
     * @param fileName
     * @param contentType
     * @param bizId
     * @param bizCode
     * @param size
     */
    String saveAndClear(InputStream input, String fileName, String contentType, String bizId, String bizCode, Long size);

    /**
     * 上传附件
     * @param input 输入流
     * @param fileName 文件名称
     * @param contentType contentType
     * @param bizId 业务表ID
     * @param bizCode 业务代码
     * @param size 文件大小
     */
    String save(InputStream input, String fileName, String contentType, String bizId, String bizCode, Long size);


    /**
     * 通过文件KEY获取附件输入流
     * @param key
     * @return
     */
    public InputStream getInputStream(String key);

    /**
     * 根据业务单据ID和业务代码获取单个文件流
     * @param bizId
     * @param bizCode
     * @return
     */
    InputStream getInputStream(String bizId, String bizCode);

    /**
     * 删除附件
     * @param id
     */
    void delete(Long id);

    /**
     * 根据KEY获取附件对象
     * @param key
     * @return
     */
    Attachment getByKey(String key);

    /**
     * 根据业务单据ID和业务代码获取附件列表
     * @param bizId
     * @param bizCode
     * @return
     */
    List<Attachment> listByBizIdAndBizCode(String bizId, String bizCode);
    /**
     * 根据业务单据ID和业务代码获取单个附件
     * @param bizId
     * @param bizCode
     * @return
     */
    Attachment getByBizIdAndBizCode(String bizId, String bizCode);
}
