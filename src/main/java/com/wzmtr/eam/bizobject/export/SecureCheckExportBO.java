package com.wzmtr.eam.bizobject.export;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

/**
 * Author: Li.Wang
 * Date: 2023/12/13 15:33
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SecureCheckExportBO {
    @ExcelProperty(value = "检查问题单号")
    private String secRiskId;

    @ExcelProperty(value = "发现日期")
    private String inspectDate;

    @ExcelProperty(value = "检查问题")
    private String secRiskDetail;

    @ExcelProperty(value = "检查部门")
    private String inspectDept;

    @ExcelProperty(value = "检查人")
    private String inspectorCode;

    @ExcelProperty(value = "地点")
    private String positionDesc;

    @ExcelProperty(value = "整改措施")
    private String restoreDetail;

    @ExcelProperty(value = "计划完成日期")
    private String planDate;

    @ExcelProperty(value = "整改部门")
    private String restoreDept;

    @ExcelProperty(value = "整改情况")
    private String isRestoredName;

    @ExcelProperty(value = "记录状态")
    private String recStatus;
}
