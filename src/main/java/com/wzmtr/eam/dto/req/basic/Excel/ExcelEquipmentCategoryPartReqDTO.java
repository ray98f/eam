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
     * 专业名称
     */
    @ExcelProperty(value = "专业名称")
    private String majorName;
    /**
     * 系统名称
     */
    @ExcelProperty(value = "系统名称")
    private String systemName;
    /**
     * 设备类型名称
     */
    @ExcelProperty(value = "设备类型名称")
    private String equipTypeName;
    /**
     * 部件名称
     */
    @ExcelProperty(value = "部件名称")
    private String partName;
    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Long quantity;
}
