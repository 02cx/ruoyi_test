package org.dromara.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DlqMessage {

    private String originTopic;
    private String msgId;
    private Integer reconsumeTimes;
    private String body;
    private String errorMsg;
}
