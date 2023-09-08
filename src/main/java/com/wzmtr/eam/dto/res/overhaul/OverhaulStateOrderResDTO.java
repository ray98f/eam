package com.wzmtr.eam.dto.res.overhaul;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulStateOrderResDTO {

    @ApiModelProperty(value = "线路编码")
    private String lineNo;

    @ApiModelProperty(value = "位置1")
    private String position1Name;

    @ApiModelProperty(value = "位置1代码")
    private String position1Code;

    @ApiModelProperty(value = "设备专业编码")
    private String subjectCode;

    @ApiModelProperty(value = "设备专业名称")
    private String subjectName;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "发现人")
    private String discovererId;

    @ApiModelProperty(value = "发现人名")
    private String discovererName;

    @ApiModelProperty(value = "发现人联系方式")
    private String discovererPhone;

    @ApiModelProperty(value = "发现时间")
    private String discoveryTime;

    @ApiModelProperty(value = "提报人")
    private String fillinUserId;

    @ApiModelProperty(value = "提报人名")
    private String fillinUserName;

    @ApiModelProperty(value = "提报时间")
    private String fillinTime;

    @ApiModelProperty(value = "提报部门名称")
    private String fillinDeptName;

    @ApiModelProperty(value = "提报部门编号")
    private String fillinDeptCode;
}
