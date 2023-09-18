package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/9/15 16:32
 */
@Getter
public enum FaultLevel {
    URGENCY("01","紧急"),
    SIGNIFICANCE("02","重要"),
    NORMAL("03","一般");
    FaultLevel(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static FaultLevel getByCode(String code) {
        for (FaultLevel value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    private final String code;
    private final String desc;

}
