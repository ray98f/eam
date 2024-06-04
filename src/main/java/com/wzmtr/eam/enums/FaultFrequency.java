package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 故障频次枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/12/26
 */
@Getter
public enum FaultFrequency {
    /**
     * 偶尔
     */
    OCCASIONALLY("10","偶尔"),
    /**
     * 一般
     */
    NORMAL("20","一般"),
    /**
     * 频发
     */
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
