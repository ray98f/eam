package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Li.Wang
 * Date: 2024/1/4 9:46
 */
@Data
public class FaultAnalyzeUploadReqDTO {
    @ApiModelProperty("故障分析编号")
    private String faultAnalysisNo;

    @ApiModelProperty("附件编号")
    private String docId;

}
