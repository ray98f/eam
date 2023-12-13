package com.wzmtr.eam.dto.req.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 普查与技改台账导入类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@Data
public class ExcelGeneralSurveyReqDTO {

    @ExcelProperty(value = "列车号")
    private String trainNo;
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
