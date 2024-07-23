package com.wzmtr.eam.dto.req.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 人员车站关联类
 * @author  Ray
 * @version 1.0
 * @date 2024/07/23
 */
@Data
public class UserStationReqDTO {
    @ApiModelProperty(value = "id")
    private String recId;
    @ApiModelProperty(value = "工号")
    private String userNo;
    @ApiModelProperty(value = "姓名")
    private String userName;
    @ApiModelProperty(value = "车站编号")
    private String stationCode;
    @ApiModelProperty(value = "车站名称")
    private String stationName;
}
