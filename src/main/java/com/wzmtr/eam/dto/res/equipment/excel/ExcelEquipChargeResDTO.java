package com.wzmtr.eam.dto.res.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备充电台账导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelEquipChargeResDTO {
    @ExcelProperty(value = "设备编码")
    private String equipCode;
    @ExcelProperty(value = "设备名称")
    private String equipName;
    @ExcelProperty(value = "充电日期")
    private String chargeDate;
    @ExcelProperty(value = "充电时长")
    private String chargeDuration;
    @ExcelProperty(value = "位置一")
    private String position1Name;
    @ExcelProperty(value = "专业")
    private String majorName;
    @ExcelProperty(value = "系统")
    private String systemName;
    @ExcelProperty(value = "设备类别")
    private String equipTypeName;
    @ExcelProperty(value = "备注")
    private String remark;
}
