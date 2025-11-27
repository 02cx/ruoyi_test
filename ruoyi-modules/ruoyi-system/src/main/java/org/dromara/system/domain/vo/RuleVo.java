package org.dromara.system.domain.vo;

import org.dromara.system.domain.Rule;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 规则单表视图对象 rule
 *
 * @author wyd
 * @date 2025-11-26
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Rule.class)
public class RuleVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ExcelProperty(value = "主键id")
    private Long id;

    /**
     * 规则名称
     */
    @ExcelProperty(value = "规则名称")
    private String ruleName;

    /**
     * 联系方式
     */
    @ExcelProperty(value = "联系方式")
    private String phone;

    /**
     * 额外信息（线索）
     */
    @ExcelProperty(value = "额外信息", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "线=索")
    private String extra;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 平台类型
     */
    @ExcelProperty(value = "平台类型")
    private Long platform;


}
