package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 系统统计指标枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/26
 */
@Getter
public enum SystemType {
    /**
     * 售票机可靠度
     */
    TICKET("1", "售票机可靠度"),
    /**
     * 进出站闸机可靠度
     */
    GATE_BRAKE("2", "进出站闸机可靠度"),
    /**
     * 自动扶梯可靠度
     */
    ESCALATOR("3", "自动扶梯可靠度"),
    /**
     * 垂直扶梯可靠度
     */
    VERTICAL_ESCALATOR("4", "垂直扶梯可靠度"),
    /**
     * 列车乘客信息系统可靠度
     */
    TRAIN_PASSENGER("5", "列车乘客信息系统可靠度"),
    /**
     * 车站乘客信息系统可靠度
     */
    STATION_PASSENGER("6", "车站乘客信息系统可靠度"),
    /**
     * 消防设备可靠度
     */
    FIRE_FIGHTING("7", "消防设备可靠度");

    private final String code;
    private final String desc;

    SystemType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static SystemType getByCode(String code) {
        for (SystemType value : values()) {
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
