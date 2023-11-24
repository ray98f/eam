package com.wzmtr.eam.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/11/23 15:01
 */
@TableName("T_WORK_FLOW_LOG")
@Data
public class WorkFlowLogDO {
    private String id;
    private String workFlowInstId;
    private String userIds;
    private String status;
}
