package com.wzmtr.eam.dto.res.mea.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计量器具管理-送检单明细导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelSubmissionDetailResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "送检单号")
    private String sendVerifyNo;
    @ExcelProperty(value = "计量器具编码")
    private String equipCode;
    @ExcelProperty(value = "计量器具名称")
    private String equipName;
    @ExcelProperty(value = "型号规格")
    private String matSpecifi;
    @ExcelProperty(value = "出厂编号")
    private String manufactureNo;
    @ExcelProperty(value = "检定校准单位")
    private String installationUnit;
}
