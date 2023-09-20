package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/8/21 11:30
 */
@Getter
public enum OrderStatus {
    TI_BAO("10", "提报"),
    XIA_FA("20", "下发"),
    PAI_GONG("30", "派工"),
    WAN_GONG("50", "完工"),
    YAN_SHOU("55", "验收"),
    WAN_GONG_QUE_REN("60", "完工确认"),
    GUAN_BI("70", "关闭"),
    ZUO_FEI("99", "作废");

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
