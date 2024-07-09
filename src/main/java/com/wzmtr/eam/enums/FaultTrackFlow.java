package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 故障分析流程枚举类
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/12/26
 */
@Getter
public enum FaultTrackFlow {

    /**
     * 技术主管审核
     */
    TECHNICAL_LEAD("UserTask_0oxqw8s", "技术主管审核"),
    /**
     * 部长审核节点
     */
    REVIEW_NODE("UserTask_1ftz952", "部长审核节点"),
    /**
     * 结束
     */
    END_NODE("EndEvent_05wcpma", "结束");
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
