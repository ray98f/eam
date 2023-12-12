package com.wzmtr.eam.dto.res.bpmn;

import lombok.Data;

/**
 * @Author lize
 * @Date 2023/8/25
 */
@Data
public class RunningListRes {
    /**
     *    taskId
     */
    private String taskId;
    /**
     *    procId
     */
    private String procId;
    /**
     *    标题
     */
    private String procDefName;
    /**
     *    当前环节
     */
    private String taskName;
    /**
     *    发起时间
     */
    private String createTime;

}
