package com.wzmtr.eam.dto.res.secure;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/8/1 9:48
 */
@Data
@ApiModel
public class SecureCheckRecordListResDTO {
    @ApiModelProperty(value = "记录编号")
    private String recId;
    @ApiModelProperty(value = "安全隐患单号")
    private String secRiskId;
    @ApiModelProperty(value = "发现日期")
    private String inspectDate;
    @ApiModelProperty(value = "检查问题")
    private String secRiskDetail;
    @ApiModelProperty(value = "检查部门")
    private String inspectDeptCode;
    @ApiModelProperty(value = "检查人")
    private String inspectorCode;
    @ApiModelProperty(value = "地点")
    private String positionDesc;
    @ApiModelProperty(value = "位置一")
    private String position1Code;
    @ApiModelProperty(value = "位置二")
    private String position2Code;
    @ApiModelProperty(value = "位置三")
    private String position3;
    @ApiModelProperty(value = "位置补充说明")
    private String positionRemark;
    @ApiModelProperty(value = "隐患照片")
    private String secRiskPic;
    @ApiModelProperty(value = "整改措施")
    private String restoreDetail;
    @ApiModelProperty(value = "计划完成日期")
    private String planDate;
    @ApiModelProperty(value = "整改部门")
    private String restoreDeptCode;
    @ApiModelProperty(value = "整改照片")
    private String restorePic;
    @ApiModelProperty(value = "整改情况")
    private String restoreDesc;
    @ApiModelProperty(value = "复查人")
    private String examinerCode;
    @ApiModelProperty(value = "记录状态")
    private String recStatus;
}
