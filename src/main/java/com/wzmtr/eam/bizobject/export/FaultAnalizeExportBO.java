package com.wzmtr.eam.bizobject.export;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

/**
 * 故障分析导出
 * Author: Li.Wang
 * Date: 2023/12/13 8:55
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaultAnalizeExportBO {
    @ExcelProperty(value = "故障分析编号")
    private String faultAnalysisNo;
    @ExcelProperty(value = "故障编号")
    private String faultNo;
    @ExcelProperty(value = "故障工单编号")
    private String faultWorkNo;
    @ExcelProperty(value = "专业")
    private String majorName;
    @ExcelProperty(value = "故障发现时间")
    private String discoveryTime;
    @ExcelProperty(value = "线路")
    private String lineCode;
    @ExcelProperty(value = "频次")
    private String frequency;
    @ExcelProperty(value = "位置")
    private String positionName;
    @ExcelProperty(value = "牵头部门")
    private String respDeptName;
    @ExcelProperty(value = "故障等级")
    private String faultLevel;
    @ExcelProperty(value = "故障影响")
    private String affectCodes;
    @ExcelProperty(value = "故障现象")
    private String faultDisplayDetail;
    @ExcelProperty(value = "故障原因")
    private String faultReasonDetail;
    @ExcelProperty(value = "故障调查及处置情况")
    private String faultProcessDetail;
    @ExcelProperty(value = "系统")
    private String systemCode;
    @ExcelProperty(value = "本次故障暴露的问题")
    private String problemDescr;
    @ExcelProperty(value = "整改措施")
    private String improveDetail;
}
