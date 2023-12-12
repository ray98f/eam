package com.wzmtr.eam.service.basic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.basic.WoRuleReqDTO;
import com.wzmtr.eam.dto.res.basic.WoRuleResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface WoRuleService {

    Page<WoRuleResDTO> listWoRule(String ruleCode, String ruleName, String ruleUseage, PageReqDTO pageReqDTO);

    WoRuleResDTO getWoRule(String id);

    Page<WoRuleResDTO.WoRuleDetail> listWoRuleDetail(String ruleCode, PageReqDTO pageReqDTO);

    WoRuleResDTO.WoRuleDetail getWoRuleDetail(String id);

    void addWoRule(WoRuleReqDTO woRuleReqDTO);

    void addWoRuleDetail(WoRuleReqDTO.WoRuleDetail woRuleDetail);

    void modifyWoRule(WoRuleReqDTO woRuleReqDTO);

    void modifyWoRuleDetail(WoRuleReqDTO.WoRuleDetail woRuleDetail);

    void deleteWoRule(BaseIdsEntity baseIdsEntity);

    void deleteWoRuleDetail(BaseIdsEntity baseIdsEntity);

    void exportWoRule(String ruleCode, String ruleName, String ruleUseage, HttpServletResponse response) throws IOException;

    void exportWoRuleDetail(String ruleCode, HttpServletResponse response) throws IOException;
}
