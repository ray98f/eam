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
@NoArgsConstructor
@AllArgsConstructor
public class BpmnExaminePersonIdReq {

    private String nodeId;

    private String flowId;
}
