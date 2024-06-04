package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 流程状态枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/12/26
 */
@Getter
public enum BpmnStatus {
    /**
     * 提交审核
     */
    SUBMIT(0, "提交审核"),
    /**
     * 通过
     */
    PASS(1, "通过"),
    /**
     * 驳回
     */
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
