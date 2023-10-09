package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/9/15 16:32
 */
@Getter
public enum FaultType {
    NORMAL_LINE_FAULT("10","正线运营故障"),
    STORAGE_FAULT("20","出入库非运营故障"),
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
