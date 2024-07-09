package com.wzmtr.eam.enums;

/**
 * token状态枚举类
 * @author  Ray
 * @version 1.0
 * @date 2023/08/26
 */
public enum TokenStatus {
    /**
     * 过期的
     */
    EXPIRED("EXPIRED"),
    /**
     * 无效的
     */
    INVALID("INVALID"),
    /**
     * 有效的
     */
    VALID("VALID");

    private final String status;

    TokenStatus(String status) {
        this.status = status;
    }

    public String value() {
        return this.status;
    }
}