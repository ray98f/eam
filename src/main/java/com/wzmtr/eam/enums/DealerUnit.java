package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/9/15 16:32
 */
@Getter
public enum DealerUnit {
    ZCWB("10","中车维保"),
    SIFANG("20","四方售后");
    DealerUnit(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static DealerUnit getByCode(String code) {
        for (DealerUnit value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    private final String code;
    private final String desc;

}
