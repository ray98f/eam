package com.wzmtr.eam.dto.res.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检修对象导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulObjectResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "对象编号")
    private String objectCode;
    @ExcelProperty(value = "对象名称")
    private String objectName;
    @ExcelProperty(value = "检修情况")
    private String repairStatus;
    @ExcelProperty(value = "检修情况说明")
    private String repairDetail;
    @ExcelProperty(value = "开始时间")
    private String startTime;
    @ExcelProperty(value = "完成时间")
    private String compliteTime;
    @ExcelProperty(value = "异常数量")
    private String abnormalNumber;
    @ExcelProperty(value = "作业人员")
    private String taskPersonName;
    @ExcelProperty(value = "备注")
    private String taskRemark;
}
