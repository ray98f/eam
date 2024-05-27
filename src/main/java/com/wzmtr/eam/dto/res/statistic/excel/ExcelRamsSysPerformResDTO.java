package com.wzmtr.eam.dto.res.statistic.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 各系统可靠性情况统计导出类
 * @author  Ray
 * @version 1.0
 * @date 2024/05/23
 */
@ApiModel
@Data
public class ExcelRamsSysPerformResDTO {
    @ExcelProperty(value = "主要子系统")
    private String moduleName;
    @ExcelProperty(value = "平均无故障时间(小时)")
    private String zb;
}
