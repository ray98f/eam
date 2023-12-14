package com.wzmtr.eam.bizobject.export;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/12/14 8:38
 */
@Data
public class SecureHazardExportBO {

    @ExcelProperty(value = "安全隐患排查单号")
    private String riskId;

    @ExcelProperty(value = "发现日期")
    private String inspectDate;

    @ExcelProperty(value = "安全隐患等级")
    private String riskRank;

    @ExcelProperty(value = "安全隐患内容")
    private String riskDetail;

    @ExcelProperty(value = "检查部门")
    private String inspectDeptCode;

    @ExcelProperty(value = "检查人")
    private String inspectorCode;

    @ExcelProperty(value = "地点")
    private String positionDesc;

    @ExcelProperty(value = "计划完成日期")
    private String planDate;

    @ExcelProperty(value = "整改部门")
    private String restoreDeptCode;

    @ExcelProperty(value = "整改情况")
    private String restoreDesc;

    @ExcelProperty(value = "记录状态")
    private String recStatus;

    @ExcelProperty(value = "备注")
    private String planNote;
}
