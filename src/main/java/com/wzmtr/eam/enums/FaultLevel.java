package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 故障等级枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/12/26
 */
@Getter
public enum FaultLevel {
    /**
     * 紧急
     */
    URGENCY("01","紧急"),
    /**
     * 重要
     */
    SIGNIFICANCE("02","重要"),
    /**
     * 一般
     */
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
