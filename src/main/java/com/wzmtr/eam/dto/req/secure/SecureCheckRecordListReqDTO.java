package com.wzmtr.eam.dto.req.secure;

import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author: Li.Wang
 * Date: 2023/7/31 18:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class SecureCheckRecordListReqDTO extends PageReqDTO {
    @ApiModelProperty(value = "检查问题单号")
    private String secRiskId;
    @ApiModelProperty(value = "发现日期")
    private String inspectDate;
    @ApiModelProperty(value = "整改情况")
    private String restoreDesc;
    @ApiModelProperty(value = "流程状态")
    private String workFlowInstStatus;
    @ApiModelProperty(value = "安全隐患等级")
    private String riskRank;
}
