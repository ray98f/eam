package com.wzmtr.eam.dto.res.statistic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Li.Wang
 * Date: 2023/8/18 10:27
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelFmFaultDetailResDTO {
    @ExcelProperty(value = "故障编号")
    private String faultNo;
    @ExcelProperty(value = "发现时间")
    private String discoveryTime;
    @ExcelProperty(value = "故障影响")
    private String faultAffect;
    @ExcelProperty(value = "故障系统")
    private String systemName;
    @ExcelProperty(value = "故障描述")
    private String faultDisplayDetail;
    @ExcelProperty(value = "处理人员")
    private String dealerUnit;
    @ExcelProperty(value = "处理时间")
    private String repairTime;
}
