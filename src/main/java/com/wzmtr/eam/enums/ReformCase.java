package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 整改流程枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/26
 */
@Getter
public enum ReformCase {
    /**
     * 已完成整改
     */
    COMPLETED("10","已完成整改"),
    /**
     * 未完成整改
     */
    UNCOMPLETED("20","未完成整改"),
    /**
     * 整改中
     */
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
