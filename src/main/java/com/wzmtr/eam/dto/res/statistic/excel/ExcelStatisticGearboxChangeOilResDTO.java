package com.wzmtr.eam.dto.res.statistic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计-齿轮箱换油台账导出类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/23
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelStatisticGearboxChangeOilResDTO {

    @ExcelProperty(value = "处理时间")
    private String completeDate;
    @ExcelProperty(value = "作业单位")
    private String orgType;
    @ExcelProperty(value = "作业人员")
    private String operator;
    @ExcelProperty(value = "确认人员")
    private String confirmor;
    @ExcelProperty(value = "备注")
    private String remark;
}
