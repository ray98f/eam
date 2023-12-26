package com.wzmtr.eam.dto.res.specialEquip.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 特种设备检测历史记录导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelSpecialEquipHistoryResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "设备编码")
    private String equipCode;
    @ExcelProperty(value = "设备名称")
    private String equipName;
    @ExcelProperty(value = "编制部门")
    private String useDeptCname;
    @ExcelProperty(value = "检测结果")
    private String verifyResult;
    @ExcelProperty(value = "检测结果说明")
    private String verifyConclusion;
    @ExcelProperty(value = "检测日期")
    private String lastVerifyDate;
    @ExcelProperty(value = "检测有效期")
    private String verifyValidityDate;
    @ExcelProperty(value = "检测单状态")
    private String recStatus;
}
