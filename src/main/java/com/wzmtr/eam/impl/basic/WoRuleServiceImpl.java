package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.basic.WoRuleReqDTO;
import com.wzmtr.eam.dto.res.basic.WoRuleResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.WoRuleMapper;
import com.wzmtr.eam.service.basic.WoRuleService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class WoRuleServiceImpl implements WoRuleService {

    private static final Map<String, String> RULE_USE_MAP = new HashMap<>();
    private static final Map<String, String> RULE_USE_CODE_MAP = new HashMap<>();

    static {
        RULE_USE_MAP.put("10", "通用");
        RULE_USE_MAP.put("20", "点巡检");
        RULE_USE_MAP.put("30", "检修");
        RULE_USE_CODE_MAP.put("10", "G");
        RULE_USE_CODE_MAP.put("20", "X");
        RULE_USE_CODE_MAP.put("30", "J");
    }

    @Autowired
    private WoRuleMapper woRuleMapper;

    @Override
    public Page<WoRuleResDTO> listWoRule(String ruleCode, String ruleName, String ruleUseage, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return woRuleMapper.pageWoRule(pageReqDTO.of(), ruleCode, ruleName, ruleUseage);
    }

    @Override
    public WoRuleResDTO getWoRule(String id) {
        return woRuleMapper.getWoRule(id);
    }

    @Override
    public Page<WoRuleResDTO.WoRuleDetail> listWoRuleDetail(String ruleCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return woRuleMapper.pageWoRuleDetail(pageReqDTO.of(), ruleCode);
    }

    @Override
    public WoRuleResDTO.WoRuleDetail getWoRuleDetail(String id) {
        return woRuleMapper.getWoRuleDetail(id);
    }

    @Override
    public void addWoRule(WoRuleReqDTO woRuleReqDTO) {
        woRuleReqDTO.setRuleCode(CodeUtils.getNextCode(woRuleMapper.getMaxCodeByUseage(woRuleReqDTO.getRuleUseage()), 1));
        woRuleReqDTO.setRecId(TokenUtil.getUuId());
        woRuleReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        woRuleReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        woRuleMapper.addWoRule(woRuleReqDTO);
    }

    @Override
    public void addWoRuleDetail(WoRuleReqDTO.WoRuleDetail woRuleDetail) {
        woRuleDetail.setRecId(TokenUtil.getUuId());
        woRuleDetail.setRecCreator(TokenUtil.getCurrentPersonId());
        woRuleDetail.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        woRuleMapper.addWoRuleDetail(woRuleDetail);
    }

    @Override
    public void modifyWoRule(WoRuleReqDTO woRuleReqDTO) {
        if (!woRuleReqDTO.getRuleCode().substring(0, 1).equals(RULE_USE_CODE_MAP.get(woRuleReqDTO.getRuleUseage()))) {
            String newCode = CodeUtils.getNextCode(woRuleMapper.getMaxCodeByUseage(woRuleReqDTO.getRuleUseage()), 1);
            woRuleMapper.modifyWoRuleDetailCode(woRuleReqDTO.getRuleCode(), newCode);
            woRuleReqDTO.setRuleCode(newCode);
        }
        woRuleReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        woRuleReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        woRuleMapper.modifyWoRule(woRuleReqDTO);
    }

    @Override
    public void modifyWoRuleDetail(WoRuleReqDTO.WoRuleDetail woRuleDetail) {
        woRuleDetail.setRecRevisor(TokenUtil.getCurrentPersonId());
        woRuleDetail.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        woRuleMapper.modifyWoRuleDetail(woRuleDetail);
    }

    @Override
    public void deleteWoRule(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            woRuleMapper.deleteWoRule(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            woRuleMapper.deleteWoRuleDetailByCode(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void deleteWoRuleDetail(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            woRuleMapper.deleteWoRuleDetail(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportWoRule(String ruleCode, String ruleName, String ruleUseage, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "规则编号", "规则名称", "用途", "记录状态", "备注", "创建者", "创建时间");
        List<WoRuleResDTO> woRules = woRuleMapper.listWoRule(ruleCode, ruleName, ruleUseage);
        List<Map<String, String>> list = new ArrayList<>();
        if (woRules != null && !woRules.isEmpty()) {
            for (WoRuleResDTO woRule : woRules) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", woRule.getRecId());
                map.put("规则编号", woRule.getRuleCode());
                map.put("规则名称", woRule.getRuleName());
                map.put("用途", RULE_USE_MAP.get(woRule.getRuleUseage()));
                map.put("记录状态", "10".equals(woRule.getRecStatus()) ? "无效" : "有效");
                map.put("备注", woRule.getRemark());
                map.put("创建者", woRule.getRecCreator());
                map.put("创建时间", woRule.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("工单触发规则信息", listName, list, null, response);
    }

    @Override
    public void exportWoRuleDetail(String ruleCode, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "规则编号", "规则明细名称", "起始日期", "结束日期", "周期(小时)", "里程周期", "提前天数", "规则排序", "备注", "创建者", "创建时间");
        List<WoRuleResDTO.WoRuleDetail> woRuleDetails = woRuleMapper.listWoRuleDetail(ruleCode, null, null);
        List<Map<String, String>> list = new ArrayList<>();
        if (woRuleDetails != null && !woRuleDetails.isEmpty()) {
            for (WoRuleResDTO.WoRuleDetail woRuleDetail : woRuleDetails) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", woRuleDetail.getRecId());
                map.put("规则编号", woRuleDetail.getRuleCode());
                map.put("规则明细名称", woRuleDetail.getRuleDetalName());
                map.put("起始日期", woRuleDetail.getStartDate());
                map.put("结束日期", woRuleDetail.getEndDate());
                map.put("周期(小时)", String.valueOf(woRuleDetail.getPeriod()));
                map.put("里程周期", woRuleDetail.getExt1() == null ? "" : String.valueOf(woRuleDetail.getExt1()));
                map.put("提前天数", String.valueOf(woRuleDetail.getBeforeTime()));
                map.put("规则排序", String.valueOf(woRuleDetail.getRuleSort()));
                map.put("备注", woRuleDetail.getRemark());
                map.put("创建者", woRuleDetail.getRecCreator());
                map.put("创建时间", woRuleDetail.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("工单触发规则明细信息", listName, list, null, response);
    }

}
