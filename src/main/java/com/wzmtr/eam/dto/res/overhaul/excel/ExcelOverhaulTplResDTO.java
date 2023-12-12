package com.wzmtr.eam.dto.res.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检修模板导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulTplResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "模板编号")
    private String templateId;
    @ExcelProperty(value = "模板名称")
    private String templateName;
    @ExcelProperty(value = "线路")
    private String lineName;
    @ExcelProperty(value = "专业")
    private String subjectName;
    @ExcelProperty(value = "系统")
    private String systemName;
    @ExcelProperty(value = "设备类别")
    private String equipTypeName;
    @ExcelProperty(value = "审批状态")
    private String trialStatus;
}
