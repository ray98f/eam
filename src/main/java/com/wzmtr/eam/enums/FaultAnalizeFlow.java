package com.wzmtr.eam.enums;

import lombok.Getter;

/**
 * 故障分析流程
 * Author: Li.Wang
 * Date: 2023/12/5 15:43
 */
@Getter
public enum FaultAnalizeFlow {

    FAULT_ANALIZE_REVIEW_NODE("UserTask_0zkqpyn", "部长审核节点"),
    FAULT_ANALIZE_TECHNICAL_LEAD("UserTask_08krsy8", "技术主管审核节点"),
    FAULT_ANALIZE_END_NODE("EndEvent_05wcpma", "结束");
    FaultAnalizeFlow(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static FaultAnalizeFlow getByCode(String code) {
        for (FaultAnalizeFlow value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    private final String code;
    private final String desc;

}
