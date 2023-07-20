package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class SysExternalOffice extends BaseEntity {

    @ApiModelProperty(value = "上级ID")
    private String parentId;

    @ApiModelProperty(value = "上级ID路径")
    private String parentIds;

    @ApiModelProperty(value = "账号过期标记")
    private String name;

    @ApiModelProperty(value = "类型(0，明细节点；1，汇总节点)")
    private String nodeType;

    @ApiModelProperty(value = "等级(1集团公司2分公司3一级部门4二级部门5科室6站 7外部)")
    private String orgLevel;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "状态 -1，未使用；0，已启用；1，已使用；2，禁用")
    private Integer status;

    @ApiModelProperty(value = "代码")
    private String code;
}
