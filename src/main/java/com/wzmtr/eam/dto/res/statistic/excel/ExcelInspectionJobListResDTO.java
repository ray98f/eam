package com.wzmtr.eam.dto.res.statistic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
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
public class ExcelInspectionJobListResDTO {
    @ExcelProperty(value = "当天总公里数")
    private String dmer3km;
    @ExcelProperty(value = "日期")
    private String dmer3date;
}