package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * Author: Li.Wang
 * Date: 2023/8/8 19:50
 */
@Getter
public enum SystemClassify {
    DOOR_SYSTEM("01", "车门系统"),
    BRAKE_SYSTEM("02", "制动系统"),
    ESCALATOR("03", "空调系统"),
    AIR_CONDITIONING_SYSTEM("04", "转向架"),
    PIDS("05", "PIDS"),
    NETWORK("06", "网络系统"),
    BODY("07", "车体结构及车身内部"),
    COUPLER("08", "通道与车钩系统"),
    TRACTION("09", "牵引设备系统"),
    SUPPLY("10", "辅助供电设备系统");

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
