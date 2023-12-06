package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 故障分析流程
 * Author: Li.Wang
 * Date: 2023/12/5 15:43
 */
@Getter
public enum FaultTrackFlow {

    FAULT_TRACK_REVIEW_NODE("UserTask_1ftz952", "部长审核节点"),
    FAULT_TRACK_END_NODE("EndEvent_05wcpma", "结束");
    FaultTrackFlow(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static FaultTrackFlow getByCode(String code) {
        for (FaultTrackFlow value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    private final String code;
    private final String desc;

}
