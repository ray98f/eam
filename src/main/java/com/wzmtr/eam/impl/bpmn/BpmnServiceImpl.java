package com.wzmtr.eam.impl.bpmn;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.res.bpmn.ExamineOpinionRes;
import com.wzmtr.eam.dto.req.bpmn.LoginDTO;
import com.wzmtr.eam.dto.req.bpmn.StartInstanceVO;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.dto.result.ResultEntity;
import com.wzmtr.eam.utils.bpmn.FastFlowPathUrl;
import com.wzmtr.eam.utils.bpmn.HttpUtil;
import com.wzmtr.eam.utils.bpmn.JointUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author lize
 * @Date 2023/4/6
 */
@Service
@Slf4j
public class BpmnServiceImpl implements BpmnService {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public String login(String account, String password) {
        LoginDTO dto = new LoginDTO();
        dto.setUsername(account);
        dto.setPassword(password);
        String data = JSONObject.toJSONString(dto);
        ResultEntity resultEntity = JSONObject.parseObject(HttpUtil.doPost(FastFlowPathUrl.LOGIN, data), ResultEntity.class);
        return String.valueOf(resultEntity.getData());
    }

    @Override
    public String startInstance(StartInstanceVO startInstanceVO) {
        String data = JSONObject.toJSONString(startInstanceVO);
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.doPost(FastFlowPathUrl.INSTANCE_START, data, httpServletRequest.getHeader("Authorization")));
        if ("0".equals(jsonObject.getString("code"))) {
            return jsonObject.getString("procId");
        } else {
            return null;
        }
    }

    @Override
    public void agreeInstance(BpmnExamineDTO bpmnExamineDTO) {
        String authorization = httpServletRequest.getHeader("Authorization");
        StringBuilder data = JointUtils.jointEntity(bpmnExamineDTO);
        HttpUtil.doPost(FastFlowPathUrl.INSTANCE_AGREE + data, null, authorization);
    }

    @Override
    public void rejectInstance(BpmnExamineDTO bpmnExamineDTO) {
        HttpUtil.doGet(FastFlowPathUrl.INSTANCE_REJECT + bpmnExamineDTO.getTaskId() + "?option=" + bpmnExamineDTO.getOpinion());
    }

    @Override
    public List<ExamineOpinionRes> examineOpinion(String instId) {
        List<ExamineOpinionRes> list = new ArrayList<>();
        String authorization = httpServletRequest.getHeader("Authorization");
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.doGet(FastFlowPathUrl.EXAMINE_OPINION + "?instId=" + instId, authorization));
        JSONArray data = jsonObject.getJSONArray("data");
        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject1 = data.getJSONObject(i);
            ExamineOpinionRes res = new ExamineOpinionRes();
            res.setNodeName(jsonObject1.getString("nodeName"));
            res.setCreateTime(jsonObject1.getString("createTime"));
            res.setCompleteTime(jsonObject1.getString("completeTime"));
            res.setAuditor(jsonObject1.getString("auditor"));
            res.setOpinion(jsonObject1.getJSONObject("a1FlowTaskTrajectoryEntity") == null ? "" : jsonObject1.getJSONObject("a1FlowTaskTrajectoryEntity").getString("opinion"));
            list.add(res);
        }
        return list;
    }

    @Override
    public String nextTaskKey(String procId) {
        String authorization = httpServletRequest.getHeader("Authorization");
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.doGet(FastFlowPathUrl.NEXT_TASK_KEY + procId, authorization));
        JSONArray running = jsonObject.getJSONArray("runing");

        return running.size() == 0 ? null : running.getString(0);
    }

    @Override
    public String queryTaskIdByProcId(String procId) {
        String authorization = httpServletRequest.getHeader("Authorization");
        ResultEntity result = JSONObject.parseObject(HttpUtil.doGet(FastFlowPathUrl.QUERY_TASKID_BY_PROCID + "?procId=" + procId, authorization), ResultEntity.class);
        return String.valueOf(result.getData());
    }
}
