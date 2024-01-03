package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 流程状态
 * Author: Li.Wang
 * Date: 2023/11/30 16:49
 */
@Getter
public enum BpmnStatus {
    SUBMIT(0, "提交审核"),
    PASS(1, "通过"),
    REJECT(2, "驳回");

    BpmnStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BpmnStatus getByCode(Integer code) {
        for (BpmnStatus value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    private final Integer code;
    private final String desc;

}
