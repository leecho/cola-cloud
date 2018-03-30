import com.honvay.cola.cloud.framework.core.protocol.Result;
import com.honvay.cola.cloud.message.model.MessageDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 消息中心客户端，在应用中调用发送消息
 * 可配置<b>cola.message.serviceId</b>指定message的serviceId,用于将消息服务整合到其他服务中
 * @author LIQIU
 * @date 2018-3-26
 **/
@FeignClient(serviceId = "${cola.message.serviceId:message-service}")
public interface MessageClient {
    @RequestMapping("/add")
    Result<String> add(MessageDTO messageDTO);
}
