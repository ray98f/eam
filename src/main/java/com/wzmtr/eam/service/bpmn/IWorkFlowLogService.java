package com.wzmtr.eam.service.bpmn;

import com.wzmtr.eam.bizobject.WorkFlowLogBO;

/**
 * Author: Li.Wang
 * Date: 2023/11/23 10:36
 */
public interface IWorkFlowLogService {
    void add(WorkFlowLogBO logBO);

    /**
     * 判断是否为审核人
     * @param workFlowInstId 流程id
     */
    void ifReviewer(String workFlowInstId);
}
