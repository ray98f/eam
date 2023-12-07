package com.wzmtr.eam.dto.res.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检修异常导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulStateResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "检修项目")
    private String itemName;
    @ExcelProperty(value = "检修结果")
    private String faultStatus;
    @ExcelProperty(value = "问题描述")
    private String problemDescription;
    @ExcelProperty(value = "处理意见")
    private String handlingSuggestion;
    @ExcelProperty(value = "跟踪结果")
    private String followStatus;
    @ExcelProperty(value = "故障单号")
    private String faultCode;
}
