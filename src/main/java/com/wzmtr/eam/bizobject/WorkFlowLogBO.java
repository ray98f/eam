package com.wzmtr.eam.bizobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/9/22 10:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkFlowLogBO {
    private String id;
    private String workFlowInstId;
    private List<String> userIds;
    private String status;
    //转换成逗号分割的userId
    private String userId;
    private String remark;
    private String creator;
    private String createTime;
}
