package com.wzmtr.eam.dto.res.bpmn;

import lombok.Data;

/**
 * @Author lize
 * @Date 2023/8/25
 */
@Data
public class ExaminedListRes {
    /**
     *    taskId
     */
    private String taskId;
    /**
     *    procId
     */
    private String processInstanceId;
    /**
     *    标题
     */
    private String processDefName;
    /**
     *    当前环节
     */
    private String taskName;
    /**
     *    开始时间
     */
    private String startTime;
    /**
     *    结束时间
     */
    private String endTime;

}
