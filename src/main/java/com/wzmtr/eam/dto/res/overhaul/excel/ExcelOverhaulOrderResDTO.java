package com.wzmtr.eam.dto.res.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检修工单导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulOrderResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "工单编号")
    private String orderCode;
    @ExcelProperty(value = "计划编号")
    private String planCode;
    @ExcelProperty(value = "计划名称")
    private String planName;
    @ExcelProperty(value = "对象名称")
    private String objectName;
    @ExcelProperty(value = "工单状态")
    private String workStatus;
    @ExcelProperty(value = "检修情况")
    private String workFinishStatus;
    @ExcelProperty(value = "异常数量")
    private String abnormalNumber;
    @ExcelProperty(value = "工器具")
    private String tools;
    @ExcelProperty(value = "计划开始时间")
    private String planStartTime;
    @ExcelProperty(value = "计划完成时间")
    private String planEndTime;
    @ExcelProperty(value = "作业工班")
    private String workGroupName;
    @ExcelProperty(value = "作业人员")
    private String workerName;
    @ExcelProperty(value = "实际开始时间")
    private String realStartTime;
    @ExcelProperty(value = "实际完成时间")
    private String realEndTime;
    @ExcelProperty(value = "线路")
    private String lineNo;
    @ExcelProperty(value = "位置")
    private String position1Name;
    @ExcelProperty(value = "专业")
    private String subjectName;
    @ExcelProperty(value = "系统")
    private String systemName;
    @ExcelProperty(value = "设备类别")
    private String equipTypeName;
    @ExcelProperty(value = "派工人")
    private String sendPersonName;
    @ExcelProperty(value = "确认人")
    private String ackPersonName;
    @ExcelProperty(value = "施工计划号")
    private String constructionPlanNo;
    @ExcelProperty(value = "最后修改人")
    private String recRevisor;
    @ExcelProperty(value = "备注")
    private String remark;
}
