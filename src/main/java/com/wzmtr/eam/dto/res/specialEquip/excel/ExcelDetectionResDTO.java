package com.wzmtr.eam.dto.res.specialEquip.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 特种设备管理-检测记录导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelDetectionResDTO {

    @ExcelProperty(value = "检测单号")
    private String checkNo;
    @ExcelProperty(value = "特种设备分类")
    private String assetKindCode;
    @ExcelProperty(value = "管理部门")
    private String manageOrg;
    @ExcelProperty(value = "维管部门")
    private String secOrg;
    @ExcelProperty(value = "编制部门")
    private String editDeptCode;
    @ExcelProperty(value = "检测单状态")
    private String recStatus;
    @ExcelProperty(value = "备注")
    private String verifyNote;
}
