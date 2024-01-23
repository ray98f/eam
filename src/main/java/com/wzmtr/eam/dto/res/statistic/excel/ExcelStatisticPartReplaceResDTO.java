package com.wzmtr.eam.dto.res.statistic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计-部件更换导出类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/23
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelStatisticPartReplaceResDTO {
    @ExcelProperty(value = "故障工单编号")
    private String faultWorkNo;
    @ExcelProperty(value = "作业单位")
    private String orgType;
    @ExcelProperty(value = "作业人员")
    private String operator;
    @ExcelProperty(value = "更换配件代码")
    private String replacementNo;
    @ExcelProperty(value = "更换原因")
    private String replaceReason;
    @ExcelProperty(value = "旧配件编号")
    private String oldRepNo;
    @ExcelProperty(value = "新配件编号")
    private String newRepNo;
    @ExcelProperty(value = "更换所用时间")
    private String operateCostTime;
    @ExcelProperty(value = "处理日期")
    private String replaceDate;
    @ExcelProperty(value = "备注")
    private String remark;
}
