package com.wzmtr.eam.service.bpmn;

import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.res.bpmn.ExamineOpinionRes;
import com.wzmtr.eam.dto.req.bpmn.StartInstanceVO;
import java.util.List;

/**
 * @Author lize
 * @Date 2023/4/6
 */
public interface BpmnService {

    String login(String account, String password);

    String startInstance(StartInstanceVO startInstanceVO);

    void agreeInstance(BpmnExamineDTO bpmnExamineDTO);

    void rejectInstance(BpmnExamineDTO bpmnExamineDTO);

    List<ExamineOpinionRes> examineOpinion(String instId);

    String nextTaskKey(String procId);

    String queryTaskIdByProcId(String procId);
}
