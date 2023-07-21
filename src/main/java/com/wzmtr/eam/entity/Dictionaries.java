package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Dictionaries {
    @ApiModelProperty(value = "字典类型编号")
    private String codesetCode;

    @ApiModelProperty(value = "字典值编号")
    private String itemCode;

    @ApiModelProperty(value = "字典值名称")
    private String itemCname;

    @ApiModelProperty(value = "字典值英文名")
    private String itemEname;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序")
    private String sortId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "创建人")
    private String recCreator;

    @ApiModelProperty(value = "创建时间")
    private String recRevisor;

    @ApiModelProperty(value = "修改人")
    private String recCreateTime;

    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;

}
