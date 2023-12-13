package com.wzmtr.eam.dto.res.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检修项导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulItemResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "检修模块")
    private String modelName;
    @ExcelProperty(value = "检修项目")
    private String itemName;
    @ExcelProperty(value = "技术要求")
    private String ext1;
    @ExcelProperty(value = "检修项类型")
    private String itemType;
    @ExcelProperty(value = "车组号")
    private String trainNumber;
    @ExcelProperty(value = "检修结果")
    private String workResult;
    @ExcelProperty(value = "上限")
    private String maxValue;
    @ExcelProperty(value = "下限")
    private String minValue;
    @ExcelProperty(value = "单位")
    private String itemUnit;
    @ExcelProperty(value = "检修人")
    private String workUserName;
    @ExcelProperty(value = "附件")
    private String docId;
}
