package com.wzmtr.eam.dto.req.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class EquipmentSiftReqDTO {

    @ApiModelProperty(value = "BOM类型")
    private String bomType;

    @ApiModelProperty(value = "来源记录编号")
    private String sourceRecId;

    @ApiModelProperty(value = "审批状态")
    private String approvalStatus;

    @ApiModelProperty(value = "设备编号")
    private String equipCode;

    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "应用线别代码")
    private String useLineNo;

    @ApiModelProperty(value = "应用线段代码")
    private String useSegNo;

    @ApiModelProperty(value = "位置一")
    private String position1Code;

    @ApiModelProperty(value = "是否为特殊设备 10 20")
    private String specialEquipFlag;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "设备状态")
    private String equipStatus;

    @ApiModelProperty(value = "生产厂家")
    private String manufacture;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "停止时间")
    private String endTime;

    @ApiModelProperty(value = "专业编号")
    private String majorCode;

    @ApiModelProperty(value = "系统编号")
    private String systemCode;

    @ApiModelProperty(value = "设备类型编号")
    private String equipTypeCode;

    @ApiModelProperty(value = "专业编号")
    private String subjectCode;

    @ApiModelProperty(value = "线路编号")
    private String lineNo;
}
