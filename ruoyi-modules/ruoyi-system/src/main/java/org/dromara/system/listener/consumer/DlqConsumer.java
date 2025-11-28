package org.dromara.system.listener.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.dromara.system.domain.dto.DlqMessage;
import org.springframework.stereotype.Service;

@RocketMQMessageListener(topic = "DLQ_CLUE_SERVICE", consumerGroup = "dlq-group")
@Slf4j
@Service
public class DlqConsumer implements RocketMQListener<DlqMessage> {
    @Override
    public void onMessage(DlqMessage msg) {

        // 1. 记录日志
        log.error("死信详情: 原Topic={}, 错误={}", msg.getOriginTopic(), msg.getErrorMsg());

        // 2. 发送告警（钉钉/企业微信/邮件）

        // 3. 存入数据库，供人工审核
    }
}
