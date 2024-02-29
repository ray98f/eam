package com.wzmtr.eam.dto.req.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检修模板检修项导入类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/30
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulTplDetailReqDTO {
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
    @ExcelProperty(value = "模块顺序")
    private String modelSequence;
    @ExcelProperty(value = "检修模块")
    private String modelName;
    @ExcelProperty(value = "检修项顺序")
    private String sequenceId;
    @ExcelProperty(value = "车组号")
    private String trainNumber;
    @ExcelProperty(value = "*检修项")
    private String itemName;
    @ExcelProperty(value = "技术要求")
    private String ext1;
    @ExcelProperty(value = "*检修项类型")
    private String itemType;
    @ExcelProperty(value = "可选值")
    private String inspectItemValue;
    @ExcelProperty(value = "默认值")
    private String defaultValue;
    @ExcelProperty(value = "上限")
    private String maxValue;
    @ExcelProperty(value = "下限")
    private String minValue;
    @ExcelProperty(value = "单位")
    private String itemUnit;
    @ExcelProperty(value = "备注")
    private String remark;
}
