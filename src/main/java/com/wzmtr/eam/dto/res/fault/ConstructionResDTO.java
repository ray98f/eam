package com.wzmtr.eam.dto.res.fault;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 施工计划
 * Author: Li.Wang
 * Date: 2023/8/30 11:31
 */
@Data
public class ConstructionResDTO {

    @ApiModelProperty(value = "施工计划代码")
    private String workpn;

    @ApiModelProperty(value = "提报批准时间")
    private String plantime;

    @ApiModelProperty(value = "销点批准时间")
    private String delaytime;

    @ApiModelProperty(value = "通告时间")
    private String circulartime;

    @ApiModelProperty(value = "提报人")
    private String planworker;

    @ApiModelProperty(value = "施工负责人")
    private String constleader;

    @ApiModelProperty(value = "备注/完成情况")
    private String remark;

    @ApiModelProperty(value = "施工人数")
    private String workersum;

    @ApiModelProperty(value = "携带工器具数量")
    private String devicesum;

    @ApiModelProperty(value = "请点主站")
    private String masterappstaiton;

    @ApiModelProperty(value = "销点主站")
    private String masterdelstaiton;

    @ApiModelProperty(value = "副站")
    private String masterstaiton;

    @ApiModelProperty(value = "是否完成")
    private String finishstatus;

    @ApiModelProperty(value = "未完成原因")
    private String reason;
}
