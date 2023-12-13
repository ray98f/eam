package com.wzmtr.eam.bizobject;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

/**
 * Author: Li.Wang
 * Date: 2023/12/13 9:36
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaultExportBO {
    @ExcelProperty(value = "故障编号")
    private String faultNo;
    @ExcelProperty(value = "故障现象")
    private String faultDisplayDetail;
    @ExcelProperty(value = "故障详情")
    private String faultDetail;
    @ExcelProperty(value = "对象名称")
    private String objectName;
    @ExcelProperty(value = "部件名称")
    private String partName;
    @ExcelProperty(value = "故障工单编号")
    private String faultWorkNo;
    @ExcelProperty(value = "对象编码")
    private String objectCode;
    @ExcelProperty(value = "故障状态")
    private String orderStatus;
    @ExcelProperty(value = "提报人员")
    private String fillinUserName;
    @ExcelProperty(value = "联系电话")
    private String discovererPhone;
    @ExcelProperty(value = "提报时间")
    private String fillinTime;
    @ExcelProperty(value = "发现人")
    private String discovererName;
    @ExcelProperty(value = "发现时间")
    private String discoveryTime;
    @ExcelProperty(value = "故障分类")
    private String faultType;
    @ExcelProperty(value = "故障紧急程度")
    private String faultLevel;
    @ExcelProperty(value = "故障影响")
    private String faultAffect;
    @ExcelProperty(value = "线路")
    private String lineName;
    @ExcelProperty(value = "车底号/车厢号")
    private String trainTrunk;
    @ExcelProperty(value = "位置一")
    private String positionName;
    @ExcelProperty(value = "位置二")
    private String position2Name;
    @ExcelProperty(value = "专业")
    private String majorName;
    @ExcelProperty(value = "系统")
    private String systemName;
    @ExcelProperty(value = "设备分类")
    private String equipTypeName;
    @ExcelProperty(value = "模块")
    private String faultModule;
    @ExcelProperty(value = "故障处理情况")
    private String faultActionDetail;
    @ExcelProperty(value = "故障处理人数")
    private String dealerNum;
    @ExcelProperty(value = "故障处理人员")
    private String dealerUnit;
    @ExcelProperty(value = "维修部门")
    private String repairDept;
    @ExcelProperty(value = "维修部门")
    private String fillinDept;
    @ExcelProperty(value = "更换配件名称")
    private String replacementName;
    @ExcelProperty(value = "旧配件编号")
    private String oldRepNo;
    @ExcelProperty(value = "新配件编号")
    private String newRepNo;
    @ExcelProperty(value = "更换所用时间")
    private String operateCostTime;
}
