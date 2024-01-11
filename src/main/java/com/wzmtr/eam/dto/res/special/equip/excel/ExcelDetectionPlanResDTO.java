package com.wzmtr.eam.dto.res.special.equip.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 特种设备管理-检测计划导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelDetectionPlanResDTO {

    @ExcelProperty(value = "检测计划号")
    private String instrmPlanNo;
    @ExcelProperty(value = "特种设备分类")
    private String instrmPlanType;
    @ExcelProperty(value = "年月")
    private String planPeriodMark;
    @ExcelProperty(value = "检测委托人")
    private String sendConsignerName;
    @ExcelProperty(value = "检测委托人电话")
    private String sendConsignerTele;
    @ExcelProperty(value = "管理部门")
    private String manageOrg;
    @ExcelProperty(value = "维管部门")
    private String secOrg;
    @ExcelProperty(value = "编制部门")
    private String editDeptCode;
    @ExcelProperty(value = "检测单位")
    private String verifyDept;
    @ExcelProperty(value = "计划状态")
    private String planStatus;
    @ExcelProperty(value = "备注")
    private String planNote;
}
