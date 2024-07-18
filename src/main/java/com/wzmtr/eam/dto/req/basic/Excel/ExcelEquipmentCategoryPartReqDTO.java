package com.wzmtr.eam.dto.req.basic.Excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备分类部件导入类
 * @author  Ray
 * @version 1.0
 * @date 2024/07/04
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelEquipmentCategoryPartReqDTO {
    /**
     * 专业
     */
    @ExcelProperty(value = "专业")
    private String majorName;
    /**
     * 系统
     */
    @ExcelProperty(value = "系统")
    private String systemName;
    /**
     * 设备大类
     */
    @ExcelProperty(value = "设备大类")
    private String equipTypeName;
    /**
     * 设备小类
     */
    @ExcelProperty(value = "设备小类")
    private String equipSubclassName;
    /**
     * 模块名称
     */
    @ExcelProperty(value = "模块名称")
    private String moduleName;
    /**
     * 部件名称
     */
    @ExcelProperty(value = "部件名称")
    private String partName;
    /**
     * 部件数量
     */
    @ExcelProperty(value = "部件数量")
    private Long quantity;
}
