package com.wzmtr.eam.dto.req.secure;

import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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
    @ApiModelProperty(value = "发现日期开始")
    private String inspectDateStart;
    @ApiModelProperty(value = "发现日期结束")
    private String inspectDateEnd;
    @ApiModelProperty(value = "整改情况")
    private String isRestoredCode;
    @ApiModelProperty(value = "流程状态")
    private String recStatus;
    @ApiModelProperty(value = "安全隐患等级")
    private String riskRank;

    @ApiModelProperty(value = "ids")
    private List<String> ids;
}
