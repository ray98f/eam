package com.wzmtr.eam.dto.res.statistic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计-轮对镟修台账导出类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/23
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelStatisticWheelsetLathingResDTO {
    @ExcelProperty(value = "车厢号")
    private String carriageNo;
    @ExcelProperty(value = "镟修轮对车轴")
    private String axleNo;
    @ExcelProperty(value = "镟修详情")
    private String repairDetail;
    @ExcelProperty(value = "开始日期")
    private String startDate;
    @ExcelProperty(value = "完成日期")
    private String completeDate;
    @ExcelProperty(value = "负责人")
    private String respPeople;
    @ExcelProperty(value = "备注")
    private String remark;
}
