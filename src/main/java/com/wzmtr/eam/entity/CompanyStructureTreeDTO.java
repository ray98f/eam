package com.wzmtr.eam.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class CompanyStructureTreeDTO {

    @ApiModelProperty(value = "组织编码")
    private String id;

    @ApiModelProperty(value = "组织名称")
    private String name;

    @ApiModelProperty(value = "组织路径")
    private String names;

    @ApiModelProperty(value = "上级机构编码")
    private String parentId;

    @ApiModelProperty(value = "上级机构编码列")
    private String parentIds;

    @ApiModelProperty(value = "外部公司编码")
    private String areaId;

    @ApiModelProperty(value = "子集")
    private List<CompanyStructureTreeDTO> children;
}
