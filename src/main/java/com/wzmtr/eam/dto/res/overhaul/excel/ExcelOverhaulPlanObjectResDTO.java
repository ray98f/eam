package com.wzmtr.eam.dto.res.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检修计划（中车）-检修对象导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulPlanObjectResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "对象编号")
    private String objectCode;
    @ExcelProperty(value = "对象名称/车号")
    private String objectName;
    @ExcelProperty(value = "检修项模板")
    private String templateName;
    @ExcelProperty(value = "作业内容")
    private String taskContent;
    @ExcelProperty(value = "作业需求")
    private String taskRequest;
    @ExcelProperty(value = "作业备注")
    private String taskRemark;
}
