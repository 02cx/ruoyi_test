package org.dromara.system.listener.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.dromara.system.domain.bo.RuleBo;
import org.dromara.system.domain.dto.MsgDTO;
import org.dromara.system.service.IRuleService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@RocketMQMessageListener(
    consumerGroup = "clue_consumer", topic = "CLUE_TOPIC")
@RequiredArgsConstructor
@Slf4j
public class ClueConsumer implements RocketMQListener<MessageExt> {

    private final IRuleService ruleService;

    private final ObjectMapper objectMapper;

    private final RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(MessageExt msg) {
        try {
            log.info("【MQ消费】收到线索数据: {}", msg);
            MsgDTO msgDTO = objectMapper.readValue(new String(msg.getBody(), StandardCharsets.UTF_8), MsgDTO.class);
            RuleBo ruleBo = new RuleBo();
            ruleBo.setId(msgDTO.getRuleId());
            ruleBo.setPlatform(msgDTO.getPlatform());
            ruleBo.setExtra(msgDTO.getExtraMsg());

            Boolean result = ruleService.updateByBo(ruleBo);
            if (!result) {
                throw new RuntimeException("更新线索数据失败：" + ruleBo);
            }

        } catch (Exception e) {
            int retryCount = msg.getReconsumeTimes();
            // 最大重试次数 5次
            if (retryCount >= 2) {
                Map<String, Object> dlqMsg = new HashMap<>();
                dlqMsg.put("originTopic", msg.getTopic());
                dlqMsg.put("msgId", msg.getMsgId());
                dlqMsg.put("reconsumeTimes", msg.getReconsumeTimes());
                dlqMsg.put("body", new String(msg.getBody(), StandardCharsets.UTF_8));
                dlqMsg.put("errorMsg", e.getMessage());

                rocketMQTemplate.convertAndSend("CLUE_CLUE_SERVICE", dlqMsg);
                log.warn("【MQ消费】超过最大重试次数，消息已转入死信队列。msgId={}, retryCount={}",msg.getMsgId(), retryCount);
                return;
            }
            // 未达最大重试次数 → 抛异常，触发 RocketMQ 自动重试
            log.warn("【MQ消费】处理失败，即将第 {} 次重试。msgId={}", retryCount + 1, msg.getMsgId(), e);
            throw new RuntimeException("消费失败，等待重试", e);
        }
    }
}
