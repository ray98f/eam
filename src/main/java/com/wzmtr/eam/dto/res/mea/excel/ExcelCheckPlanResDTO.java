package com.wzmtr.eam.dto.res.mea.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计量器具管理-定检计划导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelCheckPlanResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "定检计划号")
    private String instrmPlanNo;
    @ExcelProperty(value = "年月")
    private String planPeriodMark;
    @ExcelProperty(value = "编制部门")
    private String editDeptCode;
    @ExcelProperty(value = "计划人")
    private String planCreaterName;
    @ExcelProperty(value = "计划状态")
    private String planStatus;
    @ExcelProperty(value = "备注")
    private String planNote;
}
