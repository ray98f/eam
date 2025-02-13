package com.wzmtr.eam.service.bpmn;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.req.bpmn.ExamineListReq;
import com.wzmtr.eam.dto.req.bpmn.StartInstanceVO;
import com.wzmtr.eam.dto.res.bpmn.*;
import com.wzmtr.eam.dto.res.common.FlowRoleResDTO;
import com.wzmtr.eam.dto.result.ResultEntity;

import java.util.List;

/**
 * @Author lize
 * @Date 2023/4/6
 */
public interface BpmnService {

    String login(String account, String password);

    String startInstance(StartInstanceVO startInstanceVO);

    ResultEntity<?> agreeInstance(BpmnExamineDTO bpmnExamineDTO);

    void rejectInstance(BpmnExamineDTO bpmnExamineDTO);

    List<ExamineOpinionRes> examineOpinion(String instId);

    String taskProgress(String instId);

    String nextTaskKey(String procId);

    String queryTaskIdByProcId(String procId);

    List<FlowRes> queryFlowList(String name, String modelKey) throws Exception;

    String queryFirstTaskKeyByModelId(String modelId) throws Exception;

    String queryTaskNameByModelIdAndTaskKey(String modelId, String taskKey) throws Exception;

    List<ExamineListRes> examineList(ExamineListReq req);

    Page<ExaminedListRes> examinedList(ExamineListReq req);

    Page<RunningListRes> runningList(ExamineListReq req);

    Page<HisListRes> hisList(ExamineListReq req);

    String getSelfId(String procId);

    void agree(String taskId, String opinion, String fromId, String formData, String nodeId);

    void reject(String id, String opinion);

    String commit(String id, String flow, String otherParam, String roleId, List<String> userIds, String modelId) throws Exception;

    String getNextNodeId(String flowId, String nodeId);

    FlowRoleResDTO getNextNode(String flowId, String nodeId, String line);

    /**
     * 根据流程实例id获取最新流程图
     * @param processId 流程实例id
     * @return 流程图信息
     */
    FlowChartRes getFlowChartByProcessId(String processId);
}
