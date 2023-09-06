package com.wzmtr.eam.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class EquipmentQrResDTO {

    @ApiModelProperty(value = "记录编号")
    private String recId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "设备编号")
    private String equipCode;

    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "开始使用时间")
    private String startUseDate;

    @ApiModelProperty(value = "二维码")
    private String qr;
}
