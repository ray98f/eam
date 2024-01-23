package com.wzmtr.eam.dto.res.statistic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Li.Wang
 * Date: 2023/8/18 10:27
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelMaterialResDTO {
    @ExcelProperty(value = "检修作业时间")
    private String realTime;
    @ExcelProperty(value = "对象号")
    private String objectName;
    @ExcelProperty(value = "工单名称")
    private String planName;
    @ExcelProperty(value = "物资名称")
    private String matname;
    @ExcelProperty(value = "物资编码")
    private String matcode;
    @ExcelProperty(value = "规格型号")
    private String specifi;
    @ExcelProperty(value = "领用数量")
    private String deliveryNum;
    @ExcelProperty(value = "计量单位")
    private String unit;
}