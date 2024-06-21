package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 故障影响枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/12/26
 */
@Getter
public enum FaultAffect {
    /**
     * 无影响
     */
    URGENCY("01","无影响"),
    /**
     * 晚点3-5分钟
     */
    LATE35("02","晚点3-5分钟"),
    /**
     * 晚点5-15分钟
     */
    LATE515("03","晚点5-15分钟"),
    /**
     * 晚点15分钟以上
     */
    LATE15("04","晚点15分钟以上"),
    /**
     * 清客
     */
    HANGER_ON("05","清客"),
    /**
     * 救援
     */
    RESCUE("06","救援"),
    /**
     * 运休
     */
    OPERATIONAL_REST("07","运休"),
    /**
     * 替开
     */
    REPLACE_WITH_OPEN("08","替开"),
    /**
     * 下线
     */
    OFFLINE("09","下线"),
    /**
     * 故障扣修
     */
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
