package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/9/15 16:32
 */
@Getter
public enum FaultAffect {
    URGENCY("01","无影响"),
    LATE35("02","晚点3-5分钟"),
    LATE515("03","晚点5-15分钟"),
    LATE15("04","晚点15分钟以上"),
    HANGER_ON("05","清客"),
    RESCUE("06","救援"),
    OPERATIONAL_REST("07","运休"),
    REPLACE_WITH_OPEN("08","替开"),
    OFFLINE("09","下线"),
    FAULT_REPAIR("10","故障扣修");
    FaultAffect(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static FaultAffect getByCode(String code) {
        for (FaultAffect value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    private final String code;
    private final String desc;

}
