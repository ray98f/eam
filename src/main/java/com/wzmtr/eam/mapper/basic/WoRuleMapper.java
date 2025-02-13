package com.wzmtr.eam.mapper.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.WoRuleDetailExportReqDTO;
import com.wzmtr.eam.dto.req.basic.WoRuleReqDTO;
import com.wzmtr.eam.dto.res.basic.WoRuleResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface WoRuleMapper {

    /**
     * 获取工单触发规则列表-分页
     *
     * @param page
     * @param ruleCode
     * @param ruleName
     * @param ruleUseage
     * @return
     */
    Page<WoRuleResDTO> pageWoRule(Page<WoRuleResDTO> page, String ruleCode, String ruleName, String ruleUseage);

    /**
     * 获取工单触发规则详情
     *
     * @param id
     * @return
     */
    WoRuleResDTO getWoRule(String id);

    /**
     * 获取工单触发规则明细列表-分页
     *
     * @param page
     * @param ruleCode
     * @return
     */
    Page<WoRuleResDTO.WoRuleDetail> pageWoRuleDetail(Page<WoRuleResDTO.WoRuleDetail> page, String ruleCode);

    /**
     * 获取工单触发规则明细详情
     *
     * @param id
     * @return
     */
    WoRuleResDTO.WoRuleDetail getWoRuleDetail(String id);

    /**
     * 根据用途获取最新规则编号
     *
     * @param ruleUseage
     * @return
     */
    String getMaxCodeByUseage(String ruleUseage);

    /**
     * 新增工单触发规则
     *
     * @param woRuleReqDTO
     */
    void addWoRule(WoRuleReqDTO woRuleReqDTO);

    /**
     * 新增工单触发规则明细
     *
     * @param woRuleDetail
     */
    void addWoRuleDetail(WoRuleReqDTO.WoRuleDetail woRuleDetail);

    /**
     * 修改工单触发规则
     *
     * @param woRuleReqDTO
     */
    void modifyWoRule(WoRuleReqDTO woRuleReqDTO);

    /**
     * 修改工单触发规则明细编号
     *
     * @param oldCode
     * @param newCode
     */
    void modifyWoRuleDetailCode(String oldCode, String newCode);

    /**
     * 修改工单触发规则明细
     *
     * @param woRuleDetail
     */
    void modifyWoRuleDetail(WoRuleReqDTO.WoRuleDetail woRuleDetail);

    /**
     * 删除工单触发规则
     *
     * @param ids
     * @param userId
     * @param time
     */
    void deleteWoRule(List<String> ids, String userId, String time);

    /**
     * 删除工单触发规则明细
     *
     * @param ids
     * @param userId
     * @param time
     */
    void deleteWoRuleDetail(List<String> ids, String userId, String time);

    /**
     * 根据规则编号删除工单触发规则明细
     *
     * @param ids
     * @param userId
     * @param time
     */
    void deleteWoRuleDetailByCode(List<String> ids, String userId, String time);

    /**
     * 获取工单触发规则列表
     *
     * @param ruleCode
     * @param ruleName
     * @param ruleUseage
     * @return
     */
    List<WoRuleResDTO> listWoRule(String ruleCode, String ruleName, String ruleUseage);

    /**
     * 获取工单触发规则明细列表
     *
     * @param ruleCode
     * @param startDate
     * @param endDate
     * @return
     */
    List<WoRuleResDTO.WoRuleDetail> listWoRuleDetail(String ruleCode, String startDate, String endDate);

    /**
     * 导出工单触发规则列表
     * @param ids ids
     * @return 工单触发规则列表
     */
    List<WoRuleResDTO> exportWoRule(List<String> ids);

    /**
     * 导出工单触发规则明细列表
     * @param reqDTO 请求参数
     * @return 工单触发规则明细列表
     */
    List<WoRuleResDTO.WoRuleDetail> exportWoRuleDetail(WoRuleDetailExportReqDTO reqDTO);

    /**
     * 获取检修规则列表
     * @param planCode 检修计划
     * @param nowDate 今天日期
     * @return 规则列表
     */
    List<WoRuleResDTO.WoRuleDetail> queryRuleList(String planCode, String nowDate);


}
