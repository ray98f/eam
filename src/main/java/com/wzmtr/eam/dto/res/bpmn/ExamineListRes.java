package com.wzmtr.eam.dto.res.bpmn;

import lombok.Data;

/**
 * @Author lize
 * @Date 2023/8/25
 */
@Data
public class ExamineListRes {
    /**
     *    taskId
     */
    private String id;
    /**
     *    标题
     */
    private String taskTitle;
    /**
     *    当前环节
     */
    private String name;
    /**
     *    接收时间
     */
    private String createTime;

    private String processDefinitionId;

    private String processInstanceId;
}
