package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 故障类型枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/12/26
 */
@Getter
public enum FaultType {
    /**
     * 正线运营故障
     */
    NORMAL_LINE_FAULT("10","正线运营故障"),
    /**
     * 出入库非运营故障
     */
    STORAGE_FAULT("20","出入库非运营故障"),
    /**
     * 预防性维修故障
     */
    PREVENTIVE_MAINTENANCE_FAULT("30","预防性维修故障");
    FaultType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static FaultType getByCode(String code) {
        for (FaultType value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    private final String code;
    private final String desc;

}
