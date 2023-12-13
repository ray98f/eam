package com.wzmtr.eam.dto.res.mea.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计量器具管理-检定记录明细导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelSubmissionRecordDetailResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "设备编码")
    private String equipCode;
    @ExcelProperty(value = "设备名称")
    private String equipName;
    @ExcelProperty(value = "型号规格")
    private String matSpecifi;
    @ExcelProperty(value = "出厂编号")
    private String manufacture;
    @ExcelProperty(value = "公司编号")
    private String companyCode;
    @ExcelProperty(value = "检定校准单位")
    private String verifyDept;
    @ExcelProperty(value = "证书编号")
    private String verificationNo;
    @ExcelProperty(value = "送检条码号")
    private String measureBarcode;
    @ExcelProperty(value = "上次检定日期")
    private String lastVerifyDate;
    @ExcelProperty(value = "下次检定日期")
    private String nextVerifyDate;
    @ExcelProperty(value = "使用单位名称")
    private String useDeptCname;
    @ExcelProperty(value = "附件")
    private String verifyNote;
}
