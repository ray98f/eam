package com.wzmtr.eam.dto.req.fault;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 故障管理-故障跟踪工单导出请求类
 * @author  Ray
 * @version 1.0
 * @date 2024/05/09
 */
@Data
@ApiModel
public class FaultFollowExportReqDTO {
    /**
     * 跟踪编号
     */
    @ApiModelProperty(value = "跟踪编号")
    private String followNo;
    /**
     * 故障单号
     */
    @ApiModelProperty(value = "故障单号")
    private String faultWorkNo;
    /**
     * 跟踪状态
     */
    @ApiModelProperty(value = "跟踪状态")
    private String followStatus;
    /**
     * ids
     */
    @ApiModelProperty(value = "ids")
    private List<String> ids;
}
