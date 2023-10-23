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
public class TrackExportReqDTO {

    @ApiModelProperty(value = "专业")
    private String majorCode;
    @ApiModelProperty(value = "系统")
    private String systemCode;
    @ApiModelProperty(value = "设备类别编号")
    private String equipTypeCode;
    @ApiModelProperty(value = "故障跟踪编号")
    private String faultTrackNo;
    @ApiModelProperty(value = "故障跟踪工单号")
    private String faultTrackWorkNo;

    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    @ApiModelProperty(value = "记录状态")
    private String recStatus;

}
