package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 工单状态枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/26
 */
@Getter
public enum OrderStatus {
    /**
     * 提报
     */
    TI_BAO("10", "提报"),
    /**
     * 下发
     */
    XIA_FA("20", "下发"),
    /**
     * 派工
     */
    PAI_GONG("30", "派工"),
    /**
     * 完工
     */
    WAN_GONG("50", "完工"),
    /**
     * 验收
     */
    YAN_SHOU("55", "验收"),
    /**
     * 完工确认
     */
    WAN_GONG_QUE_REN("60", "完工确认"),
    /**
     * 关闭
     */
    GUAN_BI("70", "关闭"),
    /**
     * 作废
     */
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
