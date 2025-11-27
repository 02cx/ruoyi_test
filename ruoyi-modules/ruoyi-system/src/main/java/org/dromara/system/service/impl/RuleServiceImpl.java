package org.dromara.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.dromara.common.core.constant.SystemConstants;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.Rule;
import org.dromara.system.domain.bo.RuleBo;
import org.dromara.system.domain.dto.MsgDTO;
import org.dromara.system.domain.vo.RuleResultVO;
import org.dromara.system.domain.vo.RuleVo;
import org.dromara.system.mapper.RuleMapper;
import org.dromara.system.service.IRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 规则单表Service业务层处理
 *
 * @author wyd
 * @date 2025-11-26
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RuleServiceImpl implements IRuleService {

    private final RuleMapper baseMapper;

    private final RocketMQTemplate rocketMQTemplate;

    private final ObjectMapper objectMapper;



    /**
     * 查询规则单表
     *
     * @param id 主键
     * @return 规则单表
     */
    @Override
    public RuleResultVO queryById(Long id) {
        RuleVo ruleVo = baseMapper.selectVoById(id);
        log.info("ruleVo={}", ruleVo);
        RuleResultVO resultVO = new RuleResultVO();
        BeanUtils.copyProperties(ruleVo, resultVO);
        try {
            Map<String,Object> map = objectMapper.readValue(ruleVo.getExtra(), Map.class);
            resultVO.setExtra(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return resultVO;
    }

    /**
     * 分页查询规则单表列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 规则单表分页列表
     */
    @Override
    public TableDataInfo<RuleVo> queryPageList(RuleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Rule> lqw = buildQueryWrapper(bo);
        Page<RuleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        log.info("ruleVoList={}", result);

        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的规则单表列表
     *
     * @param bo 查询条件
     * @return 规则单表列表
     */
    @Override
    public List<RuleVo> queryList(RuleBo bo) {
        LambdaQueryWrapper<Rule> lqw = buildQueryWrapper(bo);
        List<RuleVo> ruleVoList = baseMapper.selectVoList(lqw);

        return ruleVoList;
    }

    private LambdaQueryWrapper<Rule> buildQueryWrapper(RuleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Rule> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(Rule::getId);
        lqw.like(StringUtils.isNotBlank(bo.getRuleName()), Rule::getRuleName, bo.getRuleName());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), Rule::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getExtra()), Rule::getExtra, bo.getExtra());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), Rule::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增规则单表
     *
     * @param bo 规则单表
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(RuleBo bo) {
        Rule rule = MapstructUtils.convert(bo, Rule.class);
        validEntityBeforeSave(rule);

        boolean flag = baseMapper.insert(rule) > 0;
        if (flag) {
            bo.setId(rule.getId());
        }
        return flag;
    }

    /**
     * 修改规则单表
     *
     * @param bo 规则单表
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(RuleBo bo) {
        Rule update = MapstructUtils.convert(bo, Rule.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Rule entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除规则单表信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 生成回调地址
     *
     * @return
     */
    @Override
    public String buildCallbackUrl(Long ruleId) {

        Rule rule = baseMapper.selectById(ruleId);

        // 回调地址拼接
        String callbackUrl = SystemConstants.RULE_CALLBACK_URL + SystemConstants.RULE_CALLBACK_URL_PLATFORM + rule.getPlatform() + "&" + SystemConstants.RULE_CALLBACK_URL_RULEID + ruleId;

        return callbackUrl;
    }

    /**
     * 供三方接口回调使用
     *
     * @param platform
     * @param ruleId
     * @param msg
     */
    @Override
    public Boolean callback(String platform, String ruleId, String msg) {
        // 构造消息体
        MsgDTO msgDTO = new MsgDTO(Long.parseLong(ruleId), Integer.parseInt(platform), msg);

        // 发送消息
        SendResult sendResult = rocketMQTemplate.syncSend("CLUE_TOPIC", msgDTO);
        log.info("【MQ发送】发送结果: {}", sendResult);
        return "SEND_OK".equals(sendResult.getSendStatus().toString());
    }
}
