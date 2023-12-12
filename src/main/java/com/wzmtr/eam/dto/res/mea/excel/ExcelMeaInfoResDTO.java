package com.wzmtr.eam.dto.res.mea.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计量器具管理-定检计划明细导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelMeaInfoResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "计划号")
    private String instrmPlanNo;
    @ExcelProperty(value = "计量器具编码")
    private String equipCode;
    @ExcelProperty(value = "名称")
    private String equipName;
    @ExcelProperty(value = "出厂编号")
    private String manufactureNo;
}
