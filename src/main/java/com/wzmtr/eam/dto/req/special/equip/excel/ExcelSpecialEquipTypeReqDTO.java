package com.wzmtr.eam.dto.req.special.equip.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 特种设备分类导入类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/30
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelSpecialEquipTypeReqDTO {
    /**
     * 分类编号
     */
    @ExcelProperty(value = "分类编号")
    private String typeCode;
    /**
     * 分类名称
     */
    @ExcelProperty(value = "分类名称")
    private String typeName;
    /**
     * 检测周期
     */
    @ExcelProperty(value = "检测周期")
    private String detectionPeriod;
}
