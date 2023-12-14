package com.wzmtr.eam.bizobject.export;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/12/14 8:38
 */
@Data
public class CarVideoExportBO {

    @ExcelProperty(value = "调阅记录号")
    private String applyNo;

    @ExcelProperty(value = "车组号")
    private String trainNo;

    @ExcelProperty(value = "调阅性质")
    private String applyType;

    @ExcelProperty(value = "申请部门")
    private String applyDeptName;

    @ExcelProperty(value = "视频起始时间")
    private String videoStartTime;

    @ExcelProperty(value = "视频截止时间")
    private String videoEndTime;

    @ExcelProperty(value = "申请调阅原因")
    private String applyReason;

    @ExcelProperty(value = "状态")
    private String recStatus;
}
