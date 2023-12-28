package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * @Author: Li.Wang
 * Date: 2023/12/28 16:38
 */
@Getter
public enum FaultFrequency {
    OCCASIONALLY("10","偶尔"),
    NORMAL("20","一般"),
    FREQUENTLY("30","频发");
    FaultFrequency(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static FaultFrequency getByCode(String code) {
        for (FaultFrequency value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    private final String code;
    private final String desc;

}
