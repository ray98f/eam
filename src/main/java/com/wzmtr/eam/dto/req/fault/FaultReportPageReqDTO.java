package com.wzmtr.eam.dto.req.fault;

import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 17:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FaultReportPageReqDTO extends PageReqDTO {
    @ApiModelProperty(value = "故障编号")
    private String faultNo;
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    @ApiModelProperty(value = "对象名称")
    private String objectName;
    @ApiModelProperty(value = "车底号/车厢号")
    private String trainTrunk;
    @ApiModelProperty(value = "位置1")
    private String positionCode;
    @ApiModelProperty(value = "位置名字")
    private String positionName;
    @ApiModelProperty(value = "对象编码")
    private String objectCodeTextField;
    @ApiModelProperty(value = "线别姓名")
    private String lineName;
    @ApiModelProperty(value = "位置2")
    private String position2Name;
    @ApiModelProperty(value = "位置2编码")
    private String position2Code;
    @ApiModelProperty(value = "专业")
    private String majorName;
    @ApiModelProperty(value = "系统")
    private String systemName;
    @ApiModelProperty(value = "设备分类")
    private String equipTypeName;
    @ApiModelProperty(value = "线别code")
    private String lineCode;
    @ApiModelProperty(value = "专业code")
    private String majorCode;
    @ApiModelProperty(value = "系统code")
    private String systemCode;
    @ApiModelProperty(value = "设备分类代码")
    private String equipTypeCode;
    @ApiModelProperty(value = "故障模块")
    private String faultModule;
    @ApiModelProperty(value = "故障模块Id")
    private String faultModuleId;
    @ApiModelProperty(value = "故障分类（10-运营故障；20-自检故障；30-新线调试；40-正线故障；50-出库故障）")
    private String faultType;
    @ApiModelProperty(value = "来源编号")
    private String sourceCode;
    @ApiModelProperty(value = "来源编号")
    private String discoveryTime;
    @ApiModelProperty(value = "发现人")
    private String discovererName;
    @ApiModelProperty(value = "发现人工号")
    private String discovererId;
    @ApiModelProperty(value = "发现人手机号")
    private String discovererPhone;
    @ApiModelProperty(value = "知会OCC调度")
    private String maintenance;
    @ApiModelProperty(value = "故障现象")
    private String faultDisplayDetail;
    @ApiModelProperty(value = "提报时间")
    private String fillinTime;
    @ApiModelProperty(value = "提报时间")
    private String fillinTimeStart;
    @ApiModelProperty(value = "提报时间")
    private String fillinTimeEnd;
    @ApiModelProperty(value = "提报人工号")
    private String fillinUserId;
    @ApiModelProperty(value = "提报人")
    private String fillinUserName;
    @ApiModelProperty(value = "提报部门Code")
    private String fillinDeptCode;
    @ApiModelProperty(value = "提报部门")
    private String fillinDeptName;
    @ApiModelProperty(value = "工单状态")
    private String orderStatus;
    @ApiModelProperty(value = "牵头部门")
    private String respDeptCode;
    @ApiModelProperty(value = "配合部门")
    private String assistDeptCode;
    @ApiModelProperty(value = "维修部门")
    private String repairDeptCode;
    @ApiModelProperty(value = "附件编号")
    private String docId;
    @ApiModelProperty(value = "故障详情")
    private String faultDetail;
    /**
     * 外部调用标识
     */
    private String tenant;
    private String faultWorkNo;
    private Set<String> positionCodes;

    @ApiModelProperty(value = "故障影响")
    private String faultAffect;
}
