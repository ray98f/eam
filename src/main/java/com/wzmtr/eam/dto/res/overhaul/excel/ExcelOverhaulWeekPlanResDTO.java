package com.wzmtr.eam.dto.res.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检修周计划（中铁通）导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulWeekPlanResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "周计划编号")
    private String weekPlanCode;
    @ExcelProperty(value = "周计划名称")
    private String planName;
    @ExcelProperty(value = "周末")
    private String firstBeginTime;
    @ExcelProperty(value = "线路")
    private String lineNo;
    @ExcelProperty(value = "专业")
    private String subjectName;
    @ExcelProperty(value = "作业工班")
    private String workerGroupCode;
    @ExcelProperty(value = "工班长")
    private String workerName;
    @ExcelProperty(value = "审批状态")
    private String trialStatus;
}
