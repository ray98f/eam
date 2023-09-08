package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/8/8 19:50
 */
@Getter
public enum SubmitType {
    COMMIT("1", "送审"),
    PASS("2", "审批通过");
    private final String code;
    private final String desc;

    SubmitType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static SubmitType getByCode(String code) {
        for (SubmitType value : values()) {
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
