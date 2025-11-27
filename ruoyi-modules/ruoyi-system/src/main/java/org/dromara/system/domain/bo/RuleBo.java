package org.dromara.system.domain.bo;

import org.dromara.system.domain.Rule;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 规则单表业务对象 rule
 *
 * @author wyd
 * @date 2025-11-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Rule.class, reverseConvertGenerate = false)
public class RuleBo extends BaseEntity {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空", groups = { EditGroup.class })
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
    private String extra;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 平台类型
     */
    private Integer platform;


}
