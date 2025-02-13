package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 统计类型枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/26
 */
@Getter
public enum RateIndex {
    /**
     * 车辆系统故障率
     */
    VEHICLE_RATE("1", "车辆系统故障率"),
    /**
     * 信号系统故障率
     */
    SIGNAL_RATE("2", "信号系统故障率"),
    /**
     * 供电系统故障率
     */
    POWER_RATE("3", "供电系统故障率"),
    /**
     * 站台门故障率
     */
    PSD_RATE("4", "站台门故障率"),
    /**
     * 退出正线运营故障率
     */
    EXITING_RATE("5", "退出正线运营故障率");
    private final String code;
    private final String desc;

    RateIndex(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static RateIndex getByCode(String code) {
        for (RateIndex value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public String value() {
        return desc;
    }

    public String getId() {
        return code;
    }
}
