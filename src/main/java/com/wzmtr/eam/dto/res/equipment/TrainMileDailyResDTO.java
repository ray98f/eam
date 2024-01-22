package com.wzmtr.eam.dto.res.equipment;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 每日列车里程及能耗结果类
 * @author  Ray
 * @version 1.0
 * @date 2024/01/22
 */
@Data
public class TrainMileDailyResDTO {

    /**
     * 记录编号
     */
    private String recId;
    /**
     * 车辆设备编码
     */
    private String equipCode;
    /**
     * 车号
     */
    private String equipName;
    /**
     * 日期
     */
    private String day;
    /**
     * 当天运营里程
     */
    private BigDecimal dailyWorkMile;
    /**
     * 累计运营里程
     */
    private BigDecimal totalWorkMile;
    /**
     * 当天总里程（含非运营时段）
     */
    private BigDecimal dailyMile;
    /**
     * 累计总里程（含非运营时段）
     */
    private BigDecimal totalMile;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建人
     */
    private String recCreator;
    /**
     * 创建时间
     */
    private String recCreateTime;
    /**
     * 创建人
     */
    private String recRevisor;
    /**
     * 创建时间
     */
    private String recReviseTime;
    /**
     * 删除者
     */
    private String recDeletor;
    /**
     * 删除时间
     */
    private String recDeleteTime;
    /**
     * 删除时间
     */
    private String deleteFlag;
    /**
     * 当天牵引能耗
     */
    private BigDecimal tractionIncrement;
    /**
     * 累计牵引能耗
     */
    private BigDecimal totalTractionEnergy;
    /**
     * 当天辅助能耗
     */
    private BigDecimal auxiliaryIncrement;
    /**
     * 累计辅助能耗
     */
    private BigDecimal totalAuxiliaryEnergy;
    /**
     * 当天再生电量
     */
    private BigDecimal regenratedIncrement;
    /**
     * 累计再生电量
     */
    private BigDecimal totalRegenratedElectricity;
}
