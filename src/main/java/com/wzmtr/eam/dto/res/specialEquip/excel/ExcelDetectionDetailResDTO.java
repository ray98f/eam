package com.wzmtr.eam.dto.res.specialEquip.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 特种设备管理-检测记录明细导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelDetectionDetailResDTO {

    @ExcelProperty(value = "设备编码")
    private String equipCode;
    @ExcelProperty(value = "名称")
    private String equipName;
    @ExcelProperty(value = "上次检测日期")
    private String verifyDate;
    @ExcelProperty(value = "上次检测有效期")
    private String verifyValidityDate;
    @ExcelProperty(value = "位置一名称")
    private String position1Name;
    @ExcelProperty(value = "位置二名称")
    private String position2Name;
    @ExcelProperty(value = "型号规格")
    private String matSpecifi;
}
