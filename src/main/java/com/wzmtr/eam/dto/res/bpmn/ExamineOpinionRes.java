package com.wzmtr.eam.dto.res.bpmn;

import lombok.Data;

/**
 * @Author lize
 * @Date 2023/8/23
 */
@Data
public class ExamineOpinionRes {
    private String nodeName;
    private String createTime;
    private String completeTime;
    private String opinion;
    private String auditor;
    private String status;
}
