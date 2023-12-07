package com.wzmtr.eam.dto.res.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆行走里程台账导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelTrainMileResDTO {

    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "设备编码")
    private String equipCode;
    @ExcelProperty(value = "车号")
    private String equipName;
    @ExcelProperty(value = "里程(公里)")
    private String totalMiles;
    @ExcelProperty(value = "牵引总能耗(kW·h)")
    private String totalTractionEnergy;
    @ExcelProperty(value = "辅助总能耗(kW·h)")
    private String totalAuxiliaryEnergy;
    @ExcelProperty(value = "再生总电量(kW·h)")
    private String totalRegenratedElectricity;
    @ExcelProperty(value = "维护时间")
    private String fillinTime;
}
