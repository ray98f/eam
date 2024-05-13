package com.wzmtr.eam.dto.req.basic;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class WoRuleDetailExportReqDTO {

    /**
     * 规则编号
     */
    private String ruleCode;

    /**
     * ids
     */
    private List<String> ids;
}
