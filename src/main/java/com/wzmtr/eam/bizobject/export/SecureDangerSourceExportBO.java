package com.wzmtr.eam.bizobject.export;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Li.Wang
 * Date: 2023/12/13 15:33
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SecureDangerSourceExportBO {
    @ExcelProperty(value = "危险源排查单号")
    private String dangerRiskId;
    @ExcelProperty(value = "危险源")
    private String dangerRisk;
    @ExcelProperty(value = "危险源级别")
    private String dangerRiskRank;
    @ExcelProperty(value = "危险源内容")
    private String dangerRiskDetail;
    @ExcelProperty(value = "发现部门")
    private String recDeptName;
    @ExcelProperty(value = "后果")
    private String consequense;
    @ExcelProperty(value = "地点")
    private String positionDesc;
    @ExcelProperty(value = "防范措施")
    private String controlDetail;
    @ExcelProperty(value = "责任部门")
    private String respDeptName;
    @ExcelProperty(value = "责任人")
    private String respCode;
    @ExcelProperty(value = "危险源照片")
    private String dangerRiskPic;
}
