package com.wzmtr.eam.dto.res.statistic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.wzmtr.eam.entity.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Li.Wang
 * Date: 2023/8/18 10:27
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelTrackQueryResDTO {

    @ExcelProperty(value = "故障描述")
    private String faultDisplayDetail;
    @ExcelProperty(value = "跟踪原因")
    private String trackReason;
    @ExcelProperty(value = "跟踪开始时间")
    private String trackStartDate;
    @ExcelProperty(value = "跟踪截止时间")
    private String trackEndDate;
    @ExcelProperty(value = "跟踪周期")
    private String trackCycle;
    @ExcelProperty(value = "跟踪结果")
    private String trackResult;
    @ExcelProperty(value = "转跟踪人员")
    private String trackUserName;

}
