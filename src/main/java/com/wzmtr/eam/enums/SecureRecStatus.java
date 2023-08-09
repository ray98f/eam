package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/8/8 19:50
 */
@Getter
public enum SecureRecStatus {
    EDIT("10", "编辑"),
    Z55("Z55", "待中铁通安技部部长审核"),
    Z60("Z60", "运营安技部确认"),
    A25("A25", "待整改部门部长审核"),
    A30("A30", "待复查"),
    A99("A99", "已关闭"),
    A50("A50","待追查"),
    Z10("Z10","待整改下达(中铁通安技部)"),
    A20("A20" ,"待再整改回复"),
    A45("A45","待整改部门部长审核"),
    A40( "A40","待整改回复")
    ;
    private final String code;
    private final String desc;

    SecureRecStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static SecureRecStatus getByCode(String code) {
        for (SecureRecStatus value : values()) {
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
