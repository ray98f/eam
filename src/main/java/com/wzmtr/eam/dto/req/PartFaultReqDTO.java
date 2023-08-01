package com.wzmtr.eam.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author frp
 */
@Data
@ApiModel
public class PartFaultReqDTO {

    @ApiModelProperty(value = "记录编号")
    private String recId;

    @ApiModelProperty(value = "公司代码")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "部件编号")
    private String partCode;

    @ApiModelProperty(value = "部件名称")
    private String partName;

    @ApiModelProperty(value = "故障编号")
    private String faultNo;

    @ApiModelProperty(value = "故障工单号")
    private String faultWorkNo;

    @ApiModelProperty(value = "履历类别（10-安装，20-拆卸）")
    private String logType;

    @ApiModelProperty(value = "处理时间")
    private String operateTime;

    @ApiModelProperty(value = "设备编码")
    private String equipCode;

    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "走行里程")
    private BigDecimal currentMiles = new BigDecimal("0");

    @ApiModelProperty(value = "创建者")
    private String recCreator;

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "修改者")
    private String recRevisor;

    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;

    @ApiModelProperty(value = "删除者")
    private String recDeletor;

    @ApiModelProperty(value = "删除时间")
    private String recDeleteTime;

    @ApiModelProperty(value = "删除标志")
    private String deleteFlag;

    @ApiModelProperty(value = "归档标记")
    private String archiveFlag;

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
