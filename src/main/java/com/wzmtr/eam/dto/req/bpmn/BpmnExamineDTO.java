package com.wzmtr.eam.dto.req.bpmn;

import lombok.Data;

/**
 * @Author lize
 * @Date 2023/5/30
 */
@Data
public class BpmnExamineDTO {
    /**
     *    任务ID
     */
    private String taskId;
    /**
     *    处理意见
     */
    private String opinion;
    private String action="agree";
    private String actionName="agree";
    /**
     *    下一个审批人，可多个 逗号隔开
     */
    private String chooseNodeUser;
    /**
     *    下一环节 如果要修改下个环节的审批人必须带上下一环节
     */
    private String chooseNode;
    /**
     *    标题
     */
    private String taskTitle;
    private String formData;

    /* 下面好像都不重要
    private String chooseNode="EndEvent_0xonpfg";

    private String formData;
    private String nodeIndex;
    //无意义，不传
    private String priority;
    //无意义，不传

    private String monitor;*/

}
