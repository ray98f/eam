package com.wzmtr.eam.dto.res.mea.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计量器具管理-检定记录导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelSubmissionRecordResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "检定记录号")
    private String checkNo;
    @ExcelProperty(value = "检测校准单位")
    private String verifyDept;
    @ExcelProperty(value = "检定记录状态")
    private String recStatus;
    @ExcelProperty(value = "附件")
    private String ext1;
    @ExcelProperty(value = "备注")
    private String verifyNote;
}
