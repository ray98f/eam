package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * @Author: Li.Wang
 * Date: 2023/12/28 11:21
 */
@Getter
public enum VideoApplyStatus {
    EDIT("10", "编辑"),
    TRANSMIT("20", "下达"),
    SEND_WORK("30", "派工"),
    COMPLETE("40","完工"),
    CLOSE("50","关闭");
    private final String code;
    private final String desc;

    VideoApplyStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static VideoApplyStatus getByCode(String code) {
        for (VideoApplyStatus value : values()) {
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
