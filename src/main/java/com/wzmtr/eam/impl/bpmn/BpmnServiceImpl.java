package com.wzmtr.eam.impl.bpmn;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.res.bpmn.ExamineOpinionRes;
import com.wzmtr.eam.dto.req.bpmn.LoginDTO;
import com.wzmtr.eam.dto.req.bpmn.StartInstanceVO;
import com.wzmtr.eam.dto.res.bpmn.FlowRes;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.dto.result.ResultEntity;
import com.wzmtr.eam.utils.bpmn.FastFlowPathUrl;
import com.wzmtr.eam.utils.bpmn.HttpUtil;
import com.wzmtr.eam.utils.bpmn.JointUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @Override
    public List<FlowRes> queryFlowList(String name, String modelKey) throws Exception {
        String authorization = httpServletRequest.getHeader("Authorization");
        ResultEntity resultEntity = JSONObject.parseObject(HttpUtil.doGet(FastFlowPathUrl.FLOW_LIST + "?name=" + URLEncoder.encode(name, "UTF-8") + "&modelkey=" + modelKey + "&page=1&limit=100000&pageSize=100000&type=2", authorization), ResultEntity.class);
        JSONArray jsonArray = JSONArray.parseArray(String.valueOf(resultEntity.getData()));
        List<FlowRes> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            FlowRes res = new FlowRes();
            res.setModelId(jsonObject.getString("id"));
            res.setName(jsonObject.getString("name"));
            res.setVersion(jsonObject.getString("version"));
            res.setDefId(jsonObject.getString("defId"));
            res.setGroupId(jsonObject.getString("typeKey"));
            String[] parts = jsonObject.getString("defId").split(":");
            if (parts.length > 0) {
                String extractedString = parts[0];
                res.setDefKey(extractedString);
            }
            res.setLastUpdated(jsonObject.getString("lastUpdated"));
            list.add(res);
        }
        return list;
    }

    @Override
    public String queryFirstTaskKeyByModelId(String modelId) throws Exception {
        String authorization = httpServletRequest.getHeader("Authorization");
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.doGet(FastFlowPathUrl.QUERY_MODEL_INFO + modelId, authorization));
        if (!"0".equals(jsonObject.getString("code"))) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, jsonObject.getString("msg"));
        }
        String xml = jsonObject.getString("xml");
        // 创建DOM解析器工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream xmlStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Document document = builder.parse(xmlStream);
        // 获取第一个userTask元素
        NodeList userTaskList = document.getElementsByTagName("bpmn:userTask");
        Element firstUserTask = (Element) userTaskList.item(0);
        // 获取id属性值
        return firstUserTask.getAttribute("id");
    }
}
