package com.wzmtr.eam.dto.res.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检修模板-检修项导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulTplDetailResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "模块顺序")
    private String modelSequence;
    @ExcelProperty(value = "检修模块")
    private String modelName;
    @ExcelProperty(value = "检修项顺序")
    private String sequenceId;
    @ExcelProperty(value = "车组号")
    private String trainNumber;
    @ExcelProperty(value = "检修项")
    private String itemName;
    @ExcelProperty(value = "技术要求")
    private String ext1;
    @ExcelProperty(value = "检修项类型")
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
