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
    private String workFlowInstId;
    private List<String> userIds;
    private String status;
    private String remark;

//    --------不必填--------
    /**
     * 转换成逗号分割的userId
     */
    private String userId;
    private String id;
    private String creator;
    private String createTime;
}
