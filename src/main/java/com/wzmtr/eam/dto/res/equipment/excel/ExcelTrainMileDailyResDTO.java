package com.wzmtr.eam.dto.res.equipment.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 每日列车里程及能耗导出类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/22
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExcelTrainMileDailyResDTO {

    /**
     * 车辆设备编码
     */
    @ExcelProperty(value = "设备编号")
    private String equipCode;
    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private String day;
    /**
     * 车号
     */
    @ExcelProperty(value = "车号")
    private String equipName;
    /**
     * 当天运营里程
     */
    @ExcelProperty(value = "当天运营里程（公里）")
    private BigDecimal dailyWorkMile;
    /**
     * 累计运营里程
     */
    @ExcelProperty(value = "累计运营里程（公里）")
    private BigDecimal totalWorkMile;
    /**
     * 当天总里程（含非运营时段）
     */
    @ExcelProperty(value = "当天总里程（含非运营时段）（公里）")
    private BigDecimal dailyMile;
    /**
     * 累计总里程（含非运营时段）
     */
    @ExcelProperty(value = "累计总里程（含非运营时段）（公里）")
    private BigDecimal totalMile;
    /**
     * 当天牵引能耗
     */
    @ExcelProperty(value = "当天牵引能耗kW.h")
    private BigDecimal tractionIncrement;
    /**
     * 累计牵引能耗
     */
    @ExcelProperty(value = "累计牵引能耗kW.h")
    private BigDecimal totalTractionEnergy;
    /**
     * 当天辅助能耗
     */
    @ExcelProperty(value = "当天辅助能耗kW.h")
    private BigDecimal auxiliaryIncrement;
    /**
     * 累计辅助能耗
     */
    @ExcelProperty(value = "累计辅助能耗kW.h")
    private BigDecimal totalAuxiliaryEnergy;
    /**
     * 当天再生电量
     */
    @ExcelProperty(value = "当天再生电量kW.h")
    private BigDecimal regenratedIncrement;
    /**
     * 累计再生电量
     */
    @ExcelProperty(value = "累计再生电量kW.h")
    private BigDecimal totalRegenratedElectricity;
}
