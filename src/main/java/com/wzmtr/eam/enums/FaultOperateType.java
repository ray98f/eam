package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/9/19 17:15
 */
@Getter
public enum FaultOperateType {
    LOWERHAIR("01","下发"),
    FINISH("02","完工");
    FaultOperateType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static FaultOperateType getByCode(String code) {
        for (FaultOperateType value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    private final String code;
    private final String desc;

}
