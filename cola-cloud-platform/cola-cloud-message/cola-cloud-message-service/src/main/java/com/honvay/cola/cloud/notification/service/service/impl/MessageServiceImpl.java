package com.honvay.cola.cloud.notification.service.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.honvay.cola.cloud.framework.base.service.impl.BaseServiceImpl;
import com.honvay.cola.cloud.framework.util.Assert;
import com.honvay.cola.cloud.framework.util.BeanUtils;
import com.honvay.cola.cloud.message.model.MessageDTO;
import com.honvay.cola.cloud.notification.client.NotificationClient;
import com.honvay.cola.cloud.notification.model.EmailNotification;
import com.honvay.cola.cloud.notification.model.SmsNotification;
import com.honvay.cola.cloud.notification.service.entity.Message;
import com.honvay.cola.cloud.notification.service.service.MessageService;
import com.honvay.cola.cloud.uc.client.SysUserClient;
import com.honvay.cola.cloud.uc.model.SysUserDTO;
import com.honvay.cola.cloud.uc.model.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 站内信表 服务实现类
 * </p>
 *
 * @author LIQIU
 * @date 2018-03-13
 */
@Service
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService {

    private static String NO_SEND_STATUS = "-1";
    private static String WAIT_SEND_STATUS = "0";
    private static String FINISH_SEND_STATUS = "1";
    private static String STATUS_OK = "OK";

    @Autowired
    private SysUserClient sysUserClient;

    @Autowired
    private NotificationClient notificationClient;

    @Override
    public List<Message> selectList(Message message, Long userId) {
        EntityWrapper<Message> wrapper = this.newEntityWrapper();

        if (userId != null) {
            wrapper.eq("sys_user_id", userId);
        }
        if (message != null) {
            if (message.getDeleteFlag() != null) {
                wrapper.eq("delete_flag", "N");
            } else {
                wrapper.eq("delete_flag", message.getDeleteFlag());
            }
            if (message.getReadFlag() != null) {
                wrapper.eq("read_flag", "N");
            } else {
                wrapper.eq("read_flag", message.getReadFlag());
            }
        }
        return super.selectList(wrapper);
    }

    @Override
    public boolean add(MessageDTO messageDTO){
        SysUserVO sysUserVO = null;
        Message message = new Message();
        BeanUtils.copy(messageDTO,message);
        message.setSmsStatus(NO_SEND_STATUS);
        message.setEmailStatus(NO_SEND_STATUS);
        //发送邮件,发送短信
        if (messageDTO.getSendSms() || messageDTO.getSendEmail()) {
            sysUserVO = sysUserClient.findUserById(messageDTO.getSysUserId());
            if(messageDTO.getSendSms()){
                message.setSmsStatus(WAIT_SEND_STATUS);
                Assert.hasText(message.getTemplateCode(), "站内信的参数是发送短信，短信模板不能为空");
                if (sendSms(messageDTO, sysUserVO)) {
                    message.setSmsStatus(FINISH_SEND_STATUS);
                }
            }

            if(messageDTO.getSendEmail()){
                message.setEmailStatus(WAIT_SEND_STATUS);
                if (sendMail(messageDTO, sysUserVO)) {
                    message.setEmailStatus(FINISH_SEND_STATUS);
                }
            }
        }
        return super.insert(message);
    }

    /**
     * 站内信删除 逻辑删除
     *
     * @param id 站内信ID
     * @return
     */
    @Override
    public boolean deleteById(Long id) {
        Assert.notNull(id, "站内信id不能为空");
        Message message = new Message();
        message.setId(id);
        message.setDeleteFlag("Y");
        return super.updateById(message);
    }

    @Override
    public boolean read(Long id) {
        Assert.notNull(id, "站内信id不能为空");
        Message message = new Message();
        message.setId(id);
        message.setReadFlag("Y");
        return super.updateById(message);
    }

    public boolean sendMail(MessageDTO messageDTO, SysUserVO sysUserVO) {
        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setContent(messageDTO.getContent());
        emailNotification.setReceiver(sysUserVO.getEmail());
        emailNotification.setTitle(messageDTO.getTitle());
        notificationClient.send(emailNotification);
        //TODO 邮件发送异常未捕获
        return true;
    }

    public boolean sendSms(MessageDTO messageDTO, SysUserVO sysUserVO) {
        SmsNotification smsNotification = new SmsNotification();
        smsNotification.setPhoneNumber(sysUserVO.getPhoneNumber());
        smsNotification.setSignName(messageDTO.getSignName());
        smsNotification.setParams(messageDTO.getSmsParams());
        smsNotification.setTemplateCode(messageDTO.getTemplateCode());
        notificationClient.send(smsNotification);
        return true;
    }
}
