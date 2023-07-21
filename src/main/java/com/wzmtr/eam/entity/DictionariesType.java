package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictionariesType {

    @ApiModelProperty(value = "字典类型编码")
    private String codesetCode;

    @ApiModelProperty(value = "字典类型名称")
    private String codesetName;

    @ApiModelProperty(value = "类型")
    private String codesetType;

    @ApiModelProperty(value = "等级")
    private String codesetHierarchy;

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
