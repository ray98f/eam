package com.wzmtr.eam.dto.req.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 检修模板物料导入类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/30
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulMaterialReqDTO {
    @ExcelProperty(value = "*模板名称")
    private String templateName;
    @ExcelProperty(value = "线路")
    private String lineName;
    @ExcelProperty(value = "*专业")
    private String subjectName;
    @ExcelProperty(value = "系统")
    private String systemName;
    @ExcelProperty(value = "设备类别")
    private String equipTypeName;
    @ExcelProperty(value = "*物资编码")
    private String materialCode;
    @ExcelProperty(value = "*物资名称")
    private String materialName;
    @ExcelProperty(value = "规格型号")
    private String materialSpec;
    @ExcelProperty(value = "计量单位")
    private String unitName;
    @ExcelProperty(value = "数量")
    private BigDecimal quantity;
    @ExcelProperty(value = "备注")
    private String remark;
}
