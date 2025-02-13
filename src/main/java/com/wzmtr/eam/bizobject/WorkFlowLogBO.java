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
    /**
     * 提交流程时选择的下一步人员
     */
    private List<String> userIds;
    /**
     * 记录当前操作的行为状态
     */
    private String status;
    private String remark;

    /**
     * 转换成逗号分割的userId
     */
    private String userId;
    private String id;
    private String creator;
    private String createTime;
}
