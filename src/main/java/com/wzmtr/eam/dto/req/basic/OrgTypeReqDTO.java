package com.wzmtr.eam.dto.req.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class OrgTypeReqDTO {

    @ApiModelProperty(value = "设备分类id")
    private String recId;

    @ApiModelProperty(value = "组织机构id")
    private String orgCode;

    @ApiModelProperty(value = "组织机构名称")
    private String orgName;

    @ApiModelProperty(value = "类别 10 20 30")
    private String orgType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "记录状态 10 启用 20 禁用")
    private String recStatus;

    @ApiModelProperty(value = "创建人")
    private String recCreator;

    @ApiModelProperty(value = "修改人")
    private String recRevisor;

    @ApiModelProperty(value = "创建时间")
    private String recCreateTime;

    @ApiModelProperty(value = "修改时间")
    private String recReviseTime;
}
