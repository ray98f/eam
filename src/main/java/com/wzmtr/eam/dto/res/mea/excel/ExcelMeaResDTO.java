package com.wzmtr.eam.dto.res.mea.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计量器具管理-计量器具台账导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelMeaResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "计量器具编码")
    private String equipCode;
    @ExcelProperty(value = "计量器具名称")
    private String equipName;
    @ExcelProperty(value = "型号规格")
    private String matSpecifi;
    @ExcelProperty(value = "证书编号")
    private String certificateNo;
    @ExcelProperty(value = "移交日期")
    private String transferDate;
    @ExcelProperty(value = "制造单位")
    private String manufacture;
    @ExcelProperty(value = "条件代码")
    private String source;
    @ExcelProperty(value = "检定/校准周期（月）")
    private String verifyPeriod;
    @ExcelProperty(value = "使用公司")
    private String useDeptCode;
    @ExcelProperty(value = "使用单位")
    private String useDeptCname;
    @ExcelProperty(value = "开始使用日期")
    private String useBeginDate;
    @ExcelProperty(value = "上次检定/校准日期")
    private String lastVerifyDate;
    @ExcelProperty(value = "证书有效日期")
    private String expirationDate;
    @ExcelProperty(value = "出厂编号")
    private String manufactureNo;
    @ExcelProperty(value = "使用保管人手机号")
    private String phoneNo;
    @ExcelProperty(value = "使用保管人姓名")
    private String useName;
    @ExcelProperty(value = "线别")
    private String lineNo;
}
