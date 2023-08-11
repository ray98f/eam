package com.wzmtr.eam.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class OverhaulWorkRecordReqDTO {
    
    @ApiModelProperty(value = "记录ID")
    private String recId;
    
    @ApiModelProperty(value = "检修工单")
    private String orderCode;
    
    @ApiModelProperty(value = "计划编号")
    private String planCode;
    
    @ApiModelProperty(value = "作业工班编码")
    private String workerGroupCode;
    
    @ApiModelProperty(value = "作业人员姓名")
    private String workerName;
    
    @ApiModelProperty(value = "作业人员工号")
    private String workerCode;
    
    @ApiModelProperty(value = "记录状态")
    private String recordStatus;
    
    @ApiModelProperty(value = "上传时间")
    private String uploadTime;
    
    @ApiModelProperty(value = "下载时间")
    private String downloadTime;
    
    @ApiModelProperty(value = "公司代码")
    private String companyCode;
    
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "扩展字段1")
    private String ext1;

    @ApiModelProperty(value = "扩展字段2")
    private String ext2;

    @ApiModelProperty(value = "扩展字段3")
    private String ext3;

    @ApiModelProperty(value = "扩展字段4")
    private String ext4;

    @ApiModelProperty(value = "扩展字段5")
    private String ext5;
}
