package com.wzmtr.eam.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.basic.WoRuleDetailExportReqDTO;
import com.wzmtr.eam.dto.req.basic.WoRuleReqDTO;
import com.wzmtr.eam.dto.res.basic.WoRuleResDTO;
import com.wzmtr.eam.dto.res.basic.excel.ExcelWoRuleDetailResDTO;
import com.wzmtr.eam.dto.res.basic.excel.ExcelWoRuleResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.WoRuleMapper;
import com.wzmtr.eam.service.basic.WoRuleService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return woRuleMapper.pageWoRule(pageReqDTO.of(), ruleCode, ruleName, ruleUseage);
    }

    @Override
    public WoRuleResDTO getWoRule(String id) {
        return woRuleMapper.getWoRule(id);
    }

    @Override
    public Page<WoRuleResDTO.WoRuleDetail> listWoRuleDetail(String ruleCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return woRuleMapper.pageWoRuleDetail(pageReqDTO.of(), ruleCode);
    }

    @Override
    public WoRuleResDTO.WoRuleDetail getWoRuleDetail(String id) {
        return woRuleMapper.getWoRuleDetail(id);
    }

    @Override
    public void addWoRule(WoRuleReqDTO woRuleReqDTO) {
        String code = woRuleMapper.getMaxCodeByUseage(woRuleReqDTO.getRuleUseage());
        String prefix = "";
        switch (woRuleReqDTO.getRuleUseage()){
            case "10":
                prefix = "G";
                break;
            case "20":
                prefix = "X";
                break;
            default:
                prefix = "J";
                break;
        }
        woRuleReqDTO.setRuleCode(CodeUtils.getNextCode(code == null? prefix+"000":code, 1));
        woRuleReqDTO.setRecId(TokenUtils.getUuId());
        woRuleReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        woRuleReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        woRuleMapper.addWoRule(woRuleReqDTO);
    }

    @Override
    public void addWoRuleDetail(WoRuleReqDTO.WoRuleDetail woRuleDetail) {
        woRuleDetail.setRecId(TokenUtils.getUuId());
        woRuleDetail.setRecCreator(TokenUtils.getCurrentPersonId());
        woRuleDetail.setRecCreateTime(DateUtils.getCurrentTime());
        woRuleDetail.setPeriod(Optional.ofNullable(woRuleDetail.getPeriod()).orElse(CommonConstants.ZERO_LONG));
        woRuleMapper.addWoRuleDetail(woRuleDetail);
    }

    @Override
    public void modifyWoRule(WoRuleReqDTO woRuleReqDTO) {
        if (!woRuleReqDTO.getRuleCode().substring(0, 1).equals(RULE_USE_CODE_MAP.get(woRuleReqDTO.getRuleUseage()))) {
            String newCode = CodeUtils.getNextCode(woRuleMapper.getMaxCodeByUseage(woRuleReqDTO.getRuleUseage()), 1);
            woRuleMapper.modifyWoRuleDetailCode(woRuleReqDTO.getRuleCode(), newCode);
            woRuleReqDTO.setRuleCode(newCode);
        }
        woRuleReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        woRuleReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        woRuleMapper.modifyWoRule(woRuleReqDTO);
    }

    @Override
    public void modifyWoRuleDetail(WoRuleReqDTO.WoRuleDetail woRuleDetail) {
        woRuleDetail.setRecRevisor(TokenUtils.getCurrentPersonId());
        woRuleDetail.setRecReviseTime(DateUtils.getCurrentTime());
        woRuleMapper.modifyWoRuleDetail(woRuleDetail);
    }

    @Override
    public void deleteWoRule(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            woRuleMapper.deleteWoRule(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            woRuleMapper.deleteWoRuleDetailByCode(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void deleteWoRuleDetail(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            woRuleMapper.deleteWoRuleDetail(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportWoRule(List<String> ids, HttpServletResponse response) throws IOException {
        List<WoRuleResDTO> woRules = woRuleMapper.exportWoRule(ids);
        if (StringUtils.isNotEmpty(woRules)) {
            List<ExcelWoRuleResDTO> list = new ArrayList<>();
            for (WoRuleResDTO resDTO : woRules) {
                ExcelWoRuleResDTO res = ExcelWoRuleResDTO.builder()
                        .recId(resDTO.getRecId())
                        .ruleCode(resDTO.getRuleCode())
                        .ruleName(resDTO.getRuleName())
                        .ruleUseage(RULE_USE_MAP.get(resDTO.getRuleUseage()))
                        .recStatus(CommonConstants.TEN_STRING.equals(resDTO.getRecStatus()) ? "启用" : "禁用")
                        .remark(resDTO.getRemark())
                        .recCreator(resDTO.getRecCreator())
                        .recCreateTime(resDTO.getRecCreateTime())
                        .build();
                list.add(res);
            }
            EasyExcelUtils.export(response, "工单触发规则信息", list);
        }
    }

    @Override
    public void exportWoRuleDetail(WoRuleDetailExportReqDTO reqDTO, HttpServletResponse response) throws IOException {
        List<WoRuleResDTO.WoRuleDetail> woRuleDetails = woRuleMapper.exportWoRuleDetail(reqDTO);
        if (StringUtils.isNotEmpty(woRuleDetails)) {
            List<ExcelWoRuleDetailResDTO> list = new ArrayList<>();
            for (WoRuleResDTO.WoRuleDetail resDTO : woRuleDetails) {
                ExcelWoRuleDetailResDTO res = ExcelWoRuleDetailResDTO.builder()
                        .recId(resDTO.getRecId())
                        .ruleCode(resDTO.getRuleCode())
                        .ruleDetailName(resDTO.getRuleDetalName())
                        .startDate(resDTO.getStartDate())
                        .endDate(resDTO.getEndDate())
                        .period(String.valueOf(resDTO.getPeriod()))
                        .cycle(resDTO.getExt1() == null ? "" : String.valueOf(resDTO.getExt1()))
                        .beforeTime(String.valueOf(resDTO.getBeforeTime()))
                        .ruleSort(String.valueOf(resDTO.getRuleSort()))
                        .remark(resDTO.getRemark())
                        .recCreator(resDTO.getRecCreator())
                        .recCreateTime(resDTO.getRecCreateTime())
                        .build();
                list.add(res);
            }
            EasyExcelUtils.export(response, "工单触发规则明细信息", list);
        }
    }

}
