package com.wzmtr.eam.dto.res.home;

import lombok.Data;

/**
 * Author: Li.Wang
 * Date: 2023/9/12 17:30
 */
@Data
public class IndexResDTO {
    private String taskId;
    private String taskName;
    private String processInstanceId;
    private String assigneeId;
    private String assigneeFullname;
    private String recCreator;
    private String recCreatorName;
    private String startTime;
    private String form;
    private String duration;
    private String opinion;
    private String businessKey;
    private String processDefName;
}
