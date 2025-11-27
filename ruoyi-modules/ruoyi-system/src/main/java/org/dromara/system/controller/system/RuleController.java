package org.dromara.system.controller.system;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.common.mybatis.annotation.DataColumn;
import org.dromara.common.mybatis.annotation.DataPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.system.domain.vo.RuleVo;
import org.dromara.system.domain.bo.RuleBo;
import org.dromara.system.service.IRuleService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 规则单表
 * 前端访问路由地址为:/system/rule
 *
 * @author wyd
 * @date 2025-11-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/rule")
public class RuleController extends BaseController {

    private final IRuleService ruleService;


    /**
     * 获取回调地址
     */
    @GetMapping("/callback_url")
    public R<String> buildCallbackUrl(Long ruleId) {
        return R.ok("success",ruleService.buildCallbackUrl(ruleId));
    }


    /**
     * 查询规则单表列表
     */
   // @SaCheckPermission("system:rule:list")
    @GetMapping("/list")
    @DataPermission(@DataColumn(value = "create_dept"))
    public TableDataInfo<RuleVo> list(RuleBo bo, PageQuery pageQuery) {
        return ruleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出规则单表列表
     */
    //@SaCheckPermission("system:rule:export")
    @Log(title = "规则单表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RuleBo bo, HttpServletResponse response) {
        List<RuleVo> list = ruleService.queryList(bo);
        ExcelUtil.exportExcel(list, "规则单表", RuleVo.class, response);
    }

    /**
     * 获取规则单表详细信息
     *
     * @param id 主键
     */
    //@SaCheckPermission("system:rule:query")
    @GetMapping("/{id}")
    public R<RuleVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable("id") Long id) {
        return R.ok(ruleService.queryById(id));
    }

    /**
     * 新增规则单表
     */
    //@SaCheckPermission("system:rule:add")
    @Log(title = "规则单表", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RuleBo bo) {
        return toAjax(ruleService.insertByBo(bo));
    }

    /**
     * 修改规则单表
     */
    //@SaCheckPermission("system:rule:edit")
    @Log(title = "规则单表", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RuleBo bo) {
        return toAjax(ruleService.updateByBo(bo));
    }

    /**
     * 删除规则单表
     *
     * @param ids 主键串
     */
    //@SaCheckPermission("system:rule:remove")
    @Log(title = "规则单表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable("ids") Long[] ids) {
        return toAjax(ruleService.deleteWithValidByIds(List.of(ids), true));
    }
}
