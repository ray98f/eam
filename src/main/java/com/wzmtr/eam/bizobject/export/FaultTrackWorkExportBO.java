package com.wzmtr.eam.bizobject.export;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

/**
 * Author: Li.Wang
 * Date: 2023/12/13 13:52
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaultTrackWorkExportBO {
    @ExcelProperty(value = "跟踪单工单号")
    private String faultTrackWorkNo;

    @ExcelProperty(value = "跟踪单号")
    private String faultTrackNo;

    @ExcelProperty(value = "对象编码")
    private String objectCode;

    @ExcelProperty(value = "对象名称")
    private String objectName;

    @ExcelProperty(value = "跟踪状态")
    private String trackStatus;

    @ExcelProperty(value = "派工人")
    private String dispatchUserName;

    @ExcelProperty(value = "跟踪报告人")
    private String trackReportName;

    @ExcelProperty(value = "派工时间")
    private String dispatchTime;

    @ExcelProperty(value = "跟踪结果")
    private String trackResult;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "跟踪报告时间")
    private String trackReportTime;

    @ExcelProperty(value = "跟踪关闭人")
    private String trackCloserName;

    @ExcelProperty(value = "跟踪关闭时间")
    private String trackCloseTime;
}
