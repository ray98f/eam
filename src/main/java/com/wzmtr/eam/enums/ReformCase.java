package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * @Author: Li.Wang
 * Date: 2024/1/8 10:20
 */
@Getter
public enum ReformCase {
    COMPLETED("10","已完成整改"),
    UNCOMPLETED("20","未完成整改"),
    IN_REF0RM("30","整改中");
    ReformCase(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static ReformCase getByCode(String code) {
        for (ReformCase value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    private final String code;
    private final String desc;
}
