package com.wzmtr.eam.service.impl.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.WoRuleReqDTO;
import com.wzmtr.eam.dto.res.WoRuleResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.OrganizationMapper;
import com.wzmtr.eam.mapper.basic.WoRuleMapper;
import com.wzmtr.eam.service.basic.WoRuleService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.StringUtils;
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
        int no = 1;
        String code = woRuleMapper.getMaxCodeByUseage(woRuleReqDTO.getRuleUseage());
        if (!StringUtils.isNull(code)) {
            no = Integer.parseInt(code.substring(code.length() - 3));
            no++;
        }
        woRuleReqDTO.setRuleCode(RULE_USE_CODE_MAP.get(woRuleReqDTO.getRuleUseage()) + String.format("%03d", no));
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
            int no = 1;
            String code = woRuleMapper.getMaxCodeByUseage(woRuleReqDTO.getRuleUseage());
            if (!StringUtils.isNull(code)) {
                no = Integer.parseInt(code.substring(code.length() - 3));
                no++;
            }
            String newCode = RULE_USE_CODE_MAP.get(woRuleReqDTO.getRuleUseage()) + String.format("%03d", no);
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
        }
    }

    @Override
    public void deleteWoRuleDetail(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            woRuleMapper.deleteWoRuleDetail(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        }
    }

}
