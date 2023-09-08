package com.wzmtr.eam.dto.req.bpmn;

import lombok.Data;

/**
 * @Author lize
 * @Date 2023/8/31
 */
@Data
public class BpmnExaminePersonIdReq {
    private String nodeId;
    private String flowId;
}
