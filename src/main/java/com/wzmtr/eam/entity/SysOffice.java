package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class SysOffice extends BaseEntity {

    @ApiModelProperty(value = "上级ID")
    private String parentId;

    @ApiModelProperty(value = "上级ID路径")
    private String parentIds;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "名称路径")
    private String names;

    @ApiModelProperty(value = "类型(0公司1部门2虚拟组织 3外部单位)")
    private String type;

    @ApiModelProperty(value = "等级(1集团公司2分公司3一级部门4二级部门5科室6站 7外部)")
    private String grade;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "状态")
    private String useable;

    @ApiModelProperty(value = "区域ID")
    private String areaId;
}
