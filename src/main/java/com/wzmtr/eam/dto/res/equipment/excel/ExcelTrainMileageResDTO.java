package com.wzmtr.eam.dto.res.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆行走里程历史列表导出类
 * @author  Ray
 * @version 1.0
 * @date 2023/12/07
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelTrainMileageResDTO {
    @ExcelProperty(value = "记录编号")
    private String recId;
    @ExcelProperty(value = "设备编码")
    private String equipCode;
    @ExcelProperty(value = "车号")
    private String equipName;
    @ExcelProperty(value = "里程(公里)")
    private String totalMiles;
    @ExcelProperty(value = "增加里程(公里)")
    private String milesIncrement;
    @ExcelProperty(value = "填报时间")
    private String fillinTime;
    @ExcelProperty(value = "填报人")
    private String fillinUserId;
    @ExcelProperty(value = "牵引总能耗(kW·h)")
    private String totalTractionEnergy;
    @ExcelProperty(value = "牵引能耗增量")
    private String tractionIncrement;
    @ExcelProperty(value = "辅助总能耗(kW·h)")
    private String totalAuxiliaryEnergy;
    @ExcelProperty(value = "辅助能耗增量")
    private String auxiliaryIncrement;
    @ExcelProperty(value = "再生总电量(kW·h)")
    private String totalRegenratedElectricity;
    @ExcelProperty(value = "再生电量增量")
    private String regenratedIncrement;
    @ExcelProperty(value = "备注")
    private String remark;
}
