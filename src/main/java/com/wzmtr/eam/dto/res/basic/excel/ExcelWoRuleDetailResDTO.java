package com.wzmtr.eam.dto.res.basic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工单触发规则明细导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelWoRuleDetailResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "规则编号")
    private String ruleCode;
    @ExcelProperty(value = "规则明细名称")
    private String ruleDetailName;
    @ExcelProperty(value = "起始日期")
    private String startDate;
    @ExcelProperty(value = "结束日期")
    private String endDate;
    @ExcelProperty(value = "周期(小时)")
    private String period;
    @ExcelProperty(value = "里程周期")
    private String cycle;
    @ExcelProperty(value = "提前天数")
    private String beforeTime;
    @ExcelProperty(value = "规则排序")
    private String ruleSort;
    @ExcelProperty(value = "备注")
    private String remark;
    @ExcelProperty(value = "创建者")
    private String recCreator;
    @ExcelProperty(value = "创建时间")
    private String recCreateTime;
}
