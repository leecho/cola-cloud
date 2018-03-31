package com.honvay.cola.cloud.notification.service.service;

import com.honvay.cola.cloud.framework.base.service.BaseService;
import com.honvay.cola.cloud.message.model.MessageDTO;
import com.honvay.cola.cloud.notification.service.entity.Message;

import java.util.List;

/**
 * <p>
 * 站内信表 服务类
 * </p>
 *
 * @author LIQIU
 * @date 2018-03-13
 */
public interface MessageService extends BaseService<Message> {
     boolean add(MessageDTO messageDTO);

     boolean deleteById(Long id);
     boolean read(Long id);
     List<Message> selectList(Message message, Long userId);
}
