package org.dromara.system.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.bo.RuleBo;
import org.dromara.system.domain.vo.RuleResultVO;
import org.dromara.system.domain.vo.RuleVo;

import java.util.Collection;
import java.util.List;

/**
 * 规则单表Service接口
 *
 * @author wyd
 * @date 2025-11-26
 */
public interface IRuleService {

    /**
     * 查询规则单表
     *
     * @param id 主键
     * @return 规则单表
     */
    RuleResultVO queryById(Long id);

    /**
     * 分页查询规则单表列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 规则单表分页列表
     */
    TableDataInfo<RuleVo> queryPageList(RuleBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的规则单表列表
     *
     * @param bo 查询条件
     * @return 规则单表列表
     */
    List<RuleVo> queryList(RuleBo bo);

    /**
     * 新增规则单表
     *
     * @param bo 规则单表
     * @return 是否新增成功
     */
    Boolean insertByBo(RuleBo bo);

    /**
     * 修改规则单表
     *
     * @param bo 规则单表
     * @return 是否修改成功
     */
    Boolean updateByBo(RuleBo bo);

    /**
     * 校验并批量删除规则单表信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     *  生成回调地址
     * @return
     */
    String buildCallbackUrl(Long ruleId);


    /**
     *  供三方接口回调使用
     * @param platform
     * @param ruleId
     * @param msg
     */
    Boolean callback(String platform, String ruleId, String msg);
}
