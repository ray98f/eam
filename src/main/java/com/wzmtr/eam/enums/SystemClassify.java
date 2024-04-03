package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/8/8 19:50
 */
@Getter
public enum SystemClassify {
    DOOR_SYSTEM("01", "门窗系统"),
    BRAKE_SYSTEM("02", "制动系统"),
    ESCALATOR("03", "空调及通风系统"),
    AIR_CONDITIONING_SYSTEM("04", "转向架"),
    PIDS("05", "旅客信息系统"),
    NETWORK("06", "网络系统"),
    BODY("07", "车体结构及内装"),
    COUPLER("08", "贯通道和车钩"),
    TRACTION("09", "牵引及高压系统"),
    SUPPLY("10", "辅助供电设备系统"),
    SUPPLY_1("11", "11"),
    SUPPLY_2("12", "12"),
    SUPPLY_3("13", "13"),
    SUPPLY_4("14", "14"),
    SUPPLY_5("15", "15");

    private final String code;
    private final String desc;

    SystemClassify(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static SystemClassify getByCode(String code) {
        for (SystemClassify value : values()) {
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
