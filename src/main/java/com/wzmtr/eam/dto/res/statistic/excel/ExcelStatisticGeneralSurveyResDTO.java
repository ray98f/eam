package com.wzmtr.eam.dto.res.statistic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计-普查与技改台账导出类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/23
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelStatisticGeneralSurveyResDTO {
    @ExcelProperty(value = "类别")
    private String recType;
    @ExcelProperty(value = "技术通知单编号")
    private String recNotifyNo;
    @ExcelProperty(value = "项目内容")
    private String recDetail;
    @ExcelProperty(value = "完成时间")
    private String completeDate;
    @ExcelProperty(value = "作业单位")
    private String orgType;
    @ExcelProperty(value = "备注")
    private String remark;
}
