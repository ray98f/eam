package com.wzmtr.eam.dto.req.bpmn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author lize
 * @Date 2023/8/31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BpmnExamineFlowRoleReq {

    private String nodeId;

    private String flowId;

    private String step;
    private String line;
    private String parentId;
}
