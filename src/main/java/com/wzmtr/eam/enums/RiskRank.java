package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 异常程度枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/26
 */
@Getter
public enum RiskRank {
    /**
     * 一般
     */
    NORMAL("10", "一般"),
    /**
     * 较大
     */
    LARGER("20", "较大"),
    /**
     * 重大
     */
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
