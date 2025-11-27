package org.dromara.system.listener.consumer;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.dromara.system.domain.bo.RuleBo;
import org.dromara.system.domain.dto.MsgDTO;
import org.dromara.system.service.IRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
    consumerGroup = "clue_consumer", topic = "CLUE_TOPIC")
@Slf4j
public class ClueConsumer implements RocketMQListener<MsgDTO> {

    @Autowired
    private IRuleService ruleService;

    @Override
    public void onMessage(MsgDTO msgDTO) {
        try {
            log.info("【MQ消费】收到线索数据: {}", msgDTO);
            RuleBo ruleBo = new RuleBo();
            ruleBo.setId(msgDTO.getRuleId());
            ruleBo.setPlatform(msgDTO.getPlatform());
            ruleBo.setExtra(msgDTO.getExtraMsg());

            Boolean result= ruleService.updateByBo(ruleBo);
            if(!result){
                throw new RuntimeException("更新线索数据失败：" + ruleBo);
            }

        } catch (Exception e) {
            log.error("【MQ消费失败】数据: {}, 错误: {}", msgDTO, e.getMessage(), e);
        }
    }
}
