package org.dromara.system.listener.producer;

import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.dromara.system.domain.dto.MsgDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClueProducer {

    private final RocketMQTemplate rocketMQTemplate;


    public SendResult sendAsyncClueMessage(MsgDTO msgDTO){
        SendResult sendResult = rocketMQTemplate.syncSend("CLUE_TOPIC", msgDTO);
        return sendResult;
    }

}
