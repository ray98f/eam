package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/8/8 19:50
 */
@Getter
public enum RiskRank {
    NORMAL("10", "一般"),
    LARGER("20", "较大"),
    GREAT("30", "重大");
    private final String code;
    private final String desc;

    RiskRank(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static RiskRank getByCode(String code) {
        for (RiskRank value : values()) {
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
