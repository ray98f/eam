package com.wzmtr.eam.dto.res.mea.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计量器具管理-送检单导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelSubmissionResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "送检单号")
    private String sendVerifyNo;
    @ExcelProperty(value = "送检委托人")
    private String sendConsignerName;
    @ExcelProperty(value = "送检委托人电话")
    private String sendConsignerTele;
    @ExcelProperty(value = "送检接收人")
    private String sendReceiverName;
    @ExcelProperty(value = "送检接收人电话")
    private String sendReceiverTele;
    @ExcelProperty(value = "送检日期")
    private String sendVerifyDate;
    @ExcelProperty(value = "返送人")
    private String backReturnName;
    @ExcelProperty(value = "返送人电话")
    private String backReturnTele;
    @ExcelProperty(value = "返还日期")
    private String verifyBackDate;
    @ExcelProperty(value = "返还接收人")
    private String backReceiverName;
    @ExcelProperty(value = "返还接收人电话")
    private String backConsignerTele;
    @ExcelProperty(value = "送检单状态")
    private String sendVerifyStatus;
}
