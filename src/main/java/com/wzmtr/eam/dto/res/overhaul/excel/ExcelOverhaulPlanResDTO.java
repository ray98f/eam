package com.wzmtr.eam.dto.res.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检修计划（中车）导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulPlanResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "计划编号")
    private String planCode;
    @ExcelProperty(value = "计划名称")
    private String planName;
    @ExcelProperty(value = "对象名称")
    private String ext1;
    @ExcelProperty(value = "线路")
    private String lineNo;
    @ExcelProperty(value = "审批状态")
    private String trialStatus;
    @ExcelProperty(value = "位置一")
    private String position1Name;
    @ExcelProperty(value = "专业")
    private String subjectName;
    @ExcelProperty(value = "系统")
    private String systemName;
    @ExcelProperty(value = "设备类别")
    private String equipTypeName;
    @ExcelProperty(value = "规则")
    private String ruleName;
    @ExcelProperty(value = "作业工班")
    private String workerGroupCode;
    @ExcelProperty(value = "启用状态")
    private String planStatus;
    @ExcelProperty(value = "首次开始日期")
    private String firstBeginTime;
    @ExcelProperty(value = "是否关联")
    private String deleteFlag;
    @ExcelProperty(value = "预警里程")
    private String kilometerScale;
}
