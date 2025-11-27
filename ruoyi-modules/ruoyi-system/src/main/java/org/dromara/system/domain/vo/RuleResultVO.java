package org.dromara.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleResultVO {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 额外信息（线索）
     */
    private Map<String, Object> extra;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 平台类型
     */
    private Long platform;
}
