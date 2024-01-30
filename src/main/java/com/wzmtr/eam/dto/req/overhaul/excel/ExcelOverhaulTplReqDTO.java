package com.wzmtr.eam.dto.req.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检修模板导入类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/30
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulTplReqDTO {
    @ExcelProperty(value = "*模板名称")
    private String templateName;
    @ExcelProperty(value = "线路")
    private String lineName;
    @ExcelProperty(value = "*专业")
    private String subjectName;
    @ExcelProperty(value = "系统")
    private String systemName;
    @ExcelProperty(value = "设备类别")
    private String equipTypeName;
}
