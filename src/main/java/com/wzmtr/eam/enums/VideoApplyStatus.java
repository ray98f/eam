package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 视频调阅状态枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/26
 */
@Getter
public enum VideoApplyStatus {
    /**
     * 编辑
     */
    EDIT("10", "编辑"),
    /**
     * 下达
     */
    TRANSMIT("20", "下达"),
    /**
     * 派工
     */
    SEND_WORK("30", "派工"),
    /**
     * 完工
     */
    COMPLETE("40","完工"),
    /**
     * 关闭
     */
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
