package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/9/15 16:44
 */
@Getter
public enum LineCode {
    S1("01","S1线"),
    S2("02", "S2线");
    private final String code;
    private final String desc;

    public static LineCode getByCode(String code) {
        for (LineCode value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    LineCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
