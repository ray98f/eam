package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/8/21 11:30
 */
@Getter
public enum OrderStatus {
    tibao("10", "提报"),
    xiafa("20", "下发"),
    paigong("30", "派工"),
    wangong("50", "完工"),
    yanshou("55", "验收"),
    wangongqueren("60", "完工确认"),
    guanbi("70", "关闭"),
    zuofei("99", "作废");

    private final String code;
    private final String desc;

    public static OrderStatus getByCode(String code) {
        for (OrderStatus value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    OrderStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
