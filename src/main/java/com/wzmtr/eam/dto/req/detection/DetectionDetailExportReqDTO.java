package com.wzmtr.eam.dto.req.detection;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class DetectionDetailExportReqDTO {
    /**
     * 检测记录表REC_ID
     */
    @ApiModelProperty(value = "检测记录表REC_ID")
    private String testRecId;
    /**
     * 设备代码
     */
    @ApiModelProperty(value = "设备代码")
    private String equipCode;

    /**
     * ids
     */
    private List<String> ids;
}
