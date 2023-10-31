package com.wzmtr.eam.dto.res.bpmn;

import lombok.Data;

/**
 * @Author lize
 * @Date 2023/9/6
 */
@Data
public class BpmnExaminePersonRes {

    private String userId;

    private String officeId;

    private String isOwnerOrg;
}
