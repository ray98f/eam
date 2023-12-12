package com.wzmtr.eam.dto.res.overhaul.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 检修模板-物料导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelOverhaulMaterialResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "模板编号")
    private String templateId;
    @ExcelProperty(value = "模板名称")
    private String templateName;
    @ExcelProperty(value = "物资编码")
    private String materialCode;
    @ExcelProperty(value = "物资名称")
    private String materialName;
    @ExcelProperty(value = "规格型号")
    private String materialSpec;
    @ExcelProperty(value = "计量单位")
    private String unitName;
    @ExcelProperty(value = "数量")
    private String quantity;
    @ExcelProperty(value = "备注")
    private String remark;
}
