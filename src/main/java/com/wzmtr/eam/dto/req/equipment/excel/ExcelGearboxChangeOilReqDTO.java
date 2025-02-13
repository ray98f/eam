package com.wzmtr.eam.dto.req.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 齿轮箱换油台账导入类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelGearboxChangeOilReqDTO {

    @ExcelProperty(value = "列车号")
    private String trainNo;
    @ExcelProperty(value = "列车公里数")
    private String totalMiles;
    @ExcelProperty(value = "完成日期")
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
