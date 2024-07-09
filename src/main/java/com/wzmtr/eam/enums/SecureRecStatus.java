package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 安全管理状态枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/26
 */
@Getter
public enum SecureRecStatus {
    /**
     * 编辑
     */
    EDIT("10", "编辑"),
    /**
     * 待中铁通安技部部长审核
     */
    Z55("Z55", "待中铁通安技部部长审核"),
    /**
     * 运营安技部确认
     */
    Z60("Z60", "运营安技部确认"),
    /**
     * 待整改部门部长审核
     */
    A25("A25", "待整改部门部长审核"),
    /**
     * 待复查
     */
    A30("A30", "待复查"),
    /**
     * 已关闭
     */
    A99("A99", "已关闭"),
    /**
     * 待追查
     */
    A50("A50","待追查"),
    /**
     * 待整改下达(中铁通安技部)
     */
    Z10("Z10","待整改下达(中铁通安技部)"),
    /**
     * 待再整改回复
     */
    A20("A20" ,"待再整改回复"),
    /**
     * 待整改部门部长审核
     */
    A45("A45","待整改部门部长审核"),
    /**
     * 待整改回复
     */
    A40( "A40","待整改回复");

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
