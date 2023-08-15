package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author: Li.Wang
 * Date: 2023/8/9 15:22
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class TrackQueryReqDTO extends PageReqDTO {

    @ApiModelProperty(value = "故障单号")
    private String faultTrackNo;
    @ApiModelProperty(value = "故障跟踪工单号")
    private String faultTrackWorkNo;
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    @ApiModelProperty(value = "记录状态")
    private String recStatus;
    @ApiModelProperty(value = "线路")
    private String lineCode;
    @ApiModelProperty(value = "位置")
    private String positionCode;
    @ApiModelProperty(value = "专业")
    private String majorCode;
    @ApiModelProperty(value = "系统")
    private String systemCode;
    @ApiModelProperty(value = "设备类别")
    private String equipTypeCode;
}
