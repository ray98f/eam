package com.wzmtr.eam.dto.req.mea;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class MeaListReqDTO {

    @ApiModelProperty(value = "设备分类id")
    private String recId;

    @ApiModelProperty(value = "计量器具名称")
    private String equipName;

    @ApiModelProperty(value = "计量器具代码")
    private String equipCode;

    @ApiModelProperty(value = "型号规格")
    private String matSpecifi;

    @ApiModelProperty(value = "使用单位")
    private String useDeptCname;

    @ApiModelProperty(value = "使用责任人")
    private String useName;

    @ApiModelProperty(value = "线路编号 01 S1线 02 S2线")
    private String lineNo;

    @ApiModelProperty(value = "出厂编号")
    private String manufactureNo;

    @ApiModelProperty(value = "送检10:否;20:是")
    private String sendVerifyFlag;

    @ApiModelProperty(value = "开始使用日期开始时间")
    private String useBeginBeginDate;

    @ApiModelProperty(value = "开始使用日期结束时间")
    private String useBeginEndDate;
}
