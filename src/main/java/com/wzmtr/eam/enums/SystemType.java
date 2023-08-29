package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/8/8 19:50
 */
@Getter
public enum SystemType {
    TICKET("1", "售票机可靠度"),
    GATE_BRAKE("2", "售票机可靠度"),
    ESCALATOR("3", "自动扶梯"),
    VERTICAL_ESCALATOR("4", "垂直扶梯"),
    TRAIN_PASSENGER("5", "列车乘客信息"),
    STATION_PASSENGER("6", "车站乘客信息系统"),
    FIRE_FIGHTING("7", "车站乘客信息系统");

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
