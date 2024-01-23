package com.wzmtr.eam.bizobject.export;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

/**
 * Author: Li.Wang
 * Date: 2023/12/13 13:52
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FaultTrackExportBO {
    @ExcelProperty(value = "跟踪单号")
    private String faultTrackNo;
    @ExcelProperty(value = "故障编号")
    private String faultNo;
    @ExcelProperty(value = "对象名称")
    private String objectName;
    @ExcelProperty(value = "线路编码")
    private String lineCode;
    @ExcelProperty(value = "故障现象")
    private String faultDetail;
    @ExcelProperty(value = "故障原因")
    private String faultReasonDetail;
    @ExcelProperty(value = "故障处理")
    private String faultActionDetail;
    @ExcelProperty(value = "转跟踪人员")
    private String trackUserName;
    @ExcelProperty(value = "转跟踪时间")
    private String trackTime;
    @ExcelProperty(value = "跟踪期限")
    private String trackDDL;
    @ExcelProperty(value = "跟踪周期")
    private String trackCyc;
    @ExcelProperty(value = "跟踪结果")
    private String trackResult;
    @ExcelProperty(value = "跟踪状态")
    private String trackStatus;
}
