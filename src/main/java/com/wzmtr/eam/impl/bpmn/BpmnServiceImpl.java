package com.wzmtr.eam.impl.bpmn;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.bpmn.BpmnExaminePersonIdReq;
import com.wzmtr.eam.dto.req.bpmn.ExamineListReq;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.req.bpmn.LoginDTO;
import com.wzmtr.eam.dto.req.bpmn.StartInstanceVO;
import com.wzmtr.eam.dto.res.bpmn.*;
import com.wzmtr.eam.dto.res.common.PersonListResDTO;
import com.wzmtr.eam.dto.result.ResultEntity;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.bpmn.FastFlowPathUrl;
import com.wzmtr.eam.utils.bpmn.HttpUtil;
import com.wzmtr.eam.utils.bpmn.JointUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Author lize
 * @Date 2023/4/6
 */
@Service
@Slf4j
public class BpmnServiceImpl implements BpmnService {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private RoleMapper roleMapper;

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
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.doPost(FastFlowPathUrl.INSTANCE_START, data, httpServletRequest.getHeader("Authorization-Flow")));
        if ("0".equals(jsonObject.getString("code"))) {
            return jsonObject.getString("procId");
        } else {
            return null;
        }
    }

    @Override
    public ResultEntity agreeInstance(BpmnExamineDTO bpmnExamineDTO) {
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
        StringBuilder data = JointUtils.jointEntity(bpmnExamineDTO);
        return JSONObject.parseObject(HttpUtil.doPost(FastFlowPathUrl.INSTANCE_AGREE + data, null, authorization), ResultEntity.class);
    }

    @Override
    public void rejectInstance(BpmnExamineDTO bpmnExamineDTO) {
        HttpUtil.doGet(FastFlowPathUrl.INSTANCE_REJECT + bpmnExamineDTO.getTaskId()
                + "?option=" + bpmnExamineDTO.getOpinion()
                + "&userId=" + TokenUtil.getCurrentPersonId()
                + "&userName=" + TokenUtil.getCurrentPerson().getPersonName());
    }

    @Override
    public List<ExamineOpinionRes> examineOpinion(String instId) {
        List<ExamineOpinionRes> list = new ArrayList<>();
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.doGet(FastFlowPathUrl.EXAMINE_OPINION + "?instId=" + instId, authorization));
        if (jsonObject != null) {
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.size(); i++) {
                JSONObject jsonObject1 = data.getJSONObject(i);
                ExamineOpinionRes res = new ExamineOpinionRes();
                res.setNodeName(jsonObject1.getString("nodeName"));
                res.setCreateTime(jsonObject1.getString("createTime"));
                res.setCompleteTime(jsonObject1.getString("completeTime"));
                res.setAuditor(jsonObject1.getString("auditor"));
                res.setOpinion(jsonObject1.getJSONObject("a1FlowTaskTrajectoryEntity") == null ? "" : jsonObject1.getJSONObject("a1FlowTaskTrajectoryEntity").getString("opinion"));
                res.setStatus(res.getAuditor() == null ? "待办" : "已办");
                list.add(res);
            }
        }
        return list;
    }

    @Override
    public String taskProgress(String instId) {
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.doGet(FastFlowPathUrl.EXAMINE_OPINION + "?instId=" + instId, authorization));
        return jsonObject.getString("XML");
    }

    @Override
    public String nextTaskKey(String procId) {
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
        log.info("流程引擎调用入参：[{}]", FastFlowPathUrl.NEXT_TASK_KEY + procId);
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.doGet(FastFlowPathUrl.NEXT_TASK_KEY + procId, authorization));
        log.info("流程引擎调用结果：[{}]", JSONObject.toJSONString(jsonObject));
        JSONArray data = jsonObject.getJSONArray("data");
        return CollectionUtil.isEmpty(data) ? null : data.getJSONObject(0).getString("taskId");
    }

    @Override
    public String queryTaskIdByProcId(String procId) {
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
        ResultEntity result = JSONObject.parseObject(HttpUtil.doGet(FastFlowPathUrl.QUERY_TASKID_BY_PROCID + "?procId=" + procId, authorization), ResultEntity.class);
        return String.valueOf(result.getData());
    }

    @Override
    public List<FlowRes> queryFlowList(String name, String modelKey) throws Exception {
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
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
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
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

    @Override
    public String queryTaskNameByModelIdAndTaskKey(String modelId, String taskKey) throws Exception {
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
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
        for (int i = 0; i < userTaskList.getLength(); i++) {
            Element userTask = (Element) userTaskList.item(i);
            // 获取id属性值
            String id = userTask.getAttribute("id");
            if (taskKey.equals(id)) {
                return userTask.getAttribute("name");
            }
        }
        return null;
    }

    @Override
    public List<ExamineListRes> examineList(ExamineListReq req) {
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
        req.setPageNo(1);
        req.setPageSize(10000);
        String data = JSONObject.toJSONString(req);
        ResultEntity resultEntity = JSONObject.parseObject(HttpUtil.doPost(FastFlowPathUrl.EXAMINE_LIST, data, authorization), ResultEntity.class);
        JSONArray jsonArray = JSONArray.parseArray(String.valueOf(resultEntity.getData()));
        return JSONArray.parseArray(jsonArray.toJSONString(), ExamineListRes.class);
    }

    @Override
    public Page<ExaminedListRes> examinedList(ExamineListReq req) {
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
        StringBuilder data = JointUtils.jointEntity(req, 1, 100000, 100000);
        ResultEntity resultEntity = JSONObject.parseObject(HttpUtil.doPost(FastFlowPathUrl.EXAMINED_LIST + data, null, authorization), ResultEntity.class);
        JSONArray jsonArray = JSONArray.parseArray(String.valueOf(resultEntity.getData()));
        // pagehelper不支持普通list分页 手动分页
        List<ExaminedListRes> originalList = JSONArray.parseArray(jsonArray.toJSONString(), ExaminedListRes.class);
        int total = originalList.size();  // 总记录数
        int startIndex = (req.getPageNo() - 1) * req.getPageSize();    // 起始索引
        int endIndex = Math.min(startIndex + req.getPageSize(), total);   // 结束索引，防止越界
        if (startIndex > endIndex) {
            return null;
        }
        List<ExaminedListRes> paginatedList = originalList.subList(startIndex, endIndex);   // 获取分页结果
        Page<ExaminedListRes> pageInfo = new Page<>();
        pageInfo.setRecords(paginatedList);
        pageInfo.setTotal(total);
        pageInfo.setCurrent(req.getPageNo());
        pageInfo.setSize(req.getPageSize());
        return pageInfo;
    }

    @Override
    public Page<RunningListRes> runningList(ExamineListReq req) {
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
        StringBuilder data = JointUtils.jointEntity(req, 1, 100000, 100000);
        ResultEntity resultEntity = JSONObject.parseObject(HttpUtil.doPost(FastFlowPathUrl.INSTANCE_RUNNING_LIST + data, null, authorization), ResultEntity.class);
        JSONArray jsonArray = JSONArray.parseArray(String.valueOf(resultEntity.getData()));
        // pagehelper不支持普通list分页 手动分页
        List<RunningListRes> originalList = JSONArray.parseArray(jsonArray.toJSONString(), RunningListRes.class);
        int total = originalList.size();  // 总记录数
        int startIndex = (req.getPageNo() - 1) * req.getPageSize();    // 起始索引
        int endIndex = Math.min(startIndex + req.getPageSize(), total);   // 结束索引，防止越界
        if (startIndex > endIndex) {
            return null;
        }
        List<RunningListRes> paginatedList = originalList.subList(startIndex, endIndex);   // 获取分页结果
        Page<RunningListRes> pageInfo = new Page<>();
        pageInfo.setRecords(paginatedList);
        pageInfo.setTotal(total);
        pageInfo.setCurrent(req.getPageNo());
        pageInfo.setSize(req.getPageSize());
        for (int i = 0; i < req.getPageSize(); i++) {
            if (req.getPageSize() * (req.getPageNo() - 1) + i >= total) {
                return pageInfo;
            }
            pageInfo.getRecords().get(i).setTaskId(jsonArray.getJSONObject((req.getPageNo() - 1) * req.getPageSize()).getJSONArray("newTask").getJSONObject(0).getString("taskId"));
            pageInfo.getRecords().get(i).setTaskName(jsonArray.getJSONObject(i).getJSONArray("newTask").getJSONObject(0).getString("taskName"));
        }
        return pageInfo;
    }

    @Override
    public Page<HisListRes> hisList(ExamineListReq req) {
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
        StringBuilder data = JointUtils.jointEntity(req, 1, 100000, 100000);
        ResultEntity resultEntity = JSONObject.parseObject(HttpUtil.doPost(FastFlowPathUrl.INSTANCE_ENDING_LIST + data, null, authorization), ResultEntity.class);
        JSONArray jsonArray = JSONArray.parseArray(String.valueOf(resultEntity.getData()));

        // pagehelper不支持普通list分页 手动分页
        List<HisListRes> originalList = JSONArray.parseArray(jsonArray.toJSONString(), HisListRes.class);
        int total = originalList.size();  // 总记录数
        int startIndex = (req.getPageNo() - 1) * req.getPageSize();    // 起始索引
        int endIndex = Math.min(startIndex + req.getPageSize(), total);   // 结束索引，防止越界
        if (startIndex > endIndex) {
            return null;
        }
        List<HisListRes> paginatedList = originalList.subList(startIndex, endIndex);   // 获取分页结果
        Page<HisListRes> pageInfo = new Page<>();
        pageInfo.setRecords(paginatedList);
        pageInfo.setTotal(total);
        pageInfo.setCurrent(req.getPageNo());
        pageInfo.setSize(req.getPageSize());

        return pageInfo;
    }

    @Override
    public String getSelfId(String procId) {
        String authorization = httpServletRequest.getHeader("Authorization-Flow");
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.doGet(FastFlowPathUrl.RENDER_HIS_FORM + procId, authorization));
        JSONObject editData = jsonObject.getJSONObject("editData");
        return editData.getString("id");
    }

    @Override
    public void agree(String taskId, String opinion, String fromId, String formData) {
        BpmnExamineDTO bpmnExamineDTO = new BpmnExamineDTO();
        if (StringUtils.isNotEmpty(formData)) {
            bpmnExamineDTO.setFormData(formData);
        }
        // 根据procId获取最新的taskId
        bpmnExamineDTO.setTaskId(taskId);
        // 获取流程引擎下一个流程节点key
        String nodeId = nextTaskKey(taskId);
        String flowId = getDefKeyByTaskId(taskId);
        // 获取审核人是谁填入
        if (StringUtils.isEmpty(fromId)) {
            // 如果没有审核人则走默认的下一步流程与审核人
            BpmnExaminePersonIdReq req = new BpmnExaminePersonIdReq();
            req.setFlowId(flowId);
            req.setNodeId(nodeId);
            List<BpmnExaminePersonRes> bpmnExaminePersonId = roleMapper.getBpmnExaminePerson(req);
            if (null != bpmnExaminePersonId && bpmnExaminePersonId.size() > 0) {
                StringBuilder chooseNodeUser = new StringBuilder();
                String currentUserDepartCode = TokenUtil.getCurrentPerson().getOfficeAreaId();
                for (BpmnExaminePersonRes res : bpmnExaminePersonId) {
                    if (res.getUserId() == null || "".equals(res.getUserId())) {
                        continue;
                    }
                    if (null != res.getIsOwnerOrg() && "1".equals(res.getIsOwnerOrg())) {
                        // 找出为自己部门的
                        if (currentUserDepartCode.equals(res.getOfficeId())) {
                            String s = "eam" + res.getUserId();
                            chooseNodeUser.append(s).append(",");
                        }
                    } else {
                        String s = "eam" + res.getUserId();
                        chooseNodeUser.append(s).append(",");
                    }
                    // 获取审核人是谁填入,这个是逗号隔开
                    bpmnExamineDTO.setChooseNodeUser(chooseNodeUser.substring(0, chooseNodeUser.length() - 1));
                    bpmnExamineDTO.setChooseNode(nodeId);
                }
            }
        } else {
            StringBuilder chooseNodeUser = new StringBuilder();
            String[] list = fromId.split(",");
            for (String s : list) {
                chooseNodeUser.append("eam").append(s).append(",");
            }
            bpmnExamineDTO.setChooseNodeUser(chooseNodeUser.substring(0, chooseNodeUser.length() - 1));
            bpmnExamineDTO.setChooseNode(nodeId);
        }

        // 这个是显示在流程引擎的标题
        bpmnExamineDTO.setTaskTitle(BpmnFlowEnum.getLabelByValue(flowId));
        // 审核意见
        bpmnExamineDTO.setOpinion(opinion);
        ResultEntity result = agreeInstance(bpmnExamineDTO);
        if (result.getCode() != 0) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, result.getMsg());
        }
    }

    @Override
    public void reject(String id, String opinion) {
        BpmnExamineDTO bpmnExamineDTO = new BpmnExamineDTO();
        // 根据procId获取最新的taskId
        bpmnExamineDTO.setTaskId(id);
        // 审核意见
        bpmnExamineDTO.setOpinion(opinion);
        rejectInstance(bpmnExamineDTO);
    }

    public String getDefKeyByTaskId(String taskId) {
        String processDefinitionId = JSONObject.parseObject(HttpUtil.doGet(FastFlowPathUrl.GET_DEF_KEY + taskId, null)).getString("processDefinitionId");
        return processDefinitionId.substring(0, processDefinitionId.indexOf(":"));
    }

    @Override
    public String commit(String id, String flow, String otherParam, String roleCode, List<String> userIds) throws Exception {
        List<FlowRes> list = queryFlowList(BpmnFlowEnum.getLabelByValue(flow), flow);
        if (null == list || list.size() == 0) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "没有找到流程");
        }
        StartInstanceVO startInstanceVO = new StartInstanceVO();
        BeanUtils.copyProperties(list.get(0), startInstanceVO);
        startInstanceVO.setTypeTitle("eam流程");
        if (otherParam == null) {
            startInstanceVO.setFormData("{\"id\":\"" + id + "\"}");
        } else {
            startInstanceVO.setFormData(otherParam);
        }
        // 获取流程引擎该表单第一个taskKey
        String nodeId = queryFirstTaskKeyByModelId(startInstanceVO.getModelId());
        List<String> bpmnExaminePersonId = new ArrayList<>();
        if (userIds != null && !userIds.isEmpty()) {
            bpmnExaminePersonId = userIds;
        } else if (roleCode != null) {
            bpmnExaminePersonId = roleMapper.listRoleUsers(null, roleCode).stream().map(PersonListResDTO::getId).collect(Collectors.toList());
        } else {
            // 获取审核人是谁填入
            BpmnExaminePersonIdReq req = new BpmnExaminePersonIdReq();
            req.setFlowId(flow);
            req.setNodeId(nodeId);
            List<BpmnExaminePersonRes> resList = roleMapper.getBpmnExaminePerson(req);
            String currentUserDepartCode = TokenUtil.getCurrentPerson().getOfficeAreaId();
            for (BpmnExaminePersonRes res : resList) {
                if (null != res.getIsOwnerOrg() && "1".equals(res.getIsOwnerOrg())) {
                    // 找出为自己部门的
                    if (currentUserDepartCode.equals(res.getOfficeId())) {
                        bpmnExaminePersonId.add(res.getUserId());
                    }
                } else {
                    // 全部
                    bpmnExaminePersonId.add(res.getUserId());
                }
            }
        }
        NodeInfos nodeInfos = new NodeInfos();
        List<NodeInfo> arrayList = new ArrayList<>();
        if (bpmnExaminePersonId.size() > 0) {
            for (String s : bpmnExaminePersonId) {
                NodeInfo nodeInfo = new NodeInfo();
                nodeInfo.setNodeId(nodeId);
                nodeInfo.setNodeHanderUser("eam" + s);
                arrayList.add(nodeInfo);
            }
        }
        nodeInfos.setList(arrayList);
        startInstanceVO.setNodeInfos(nodeInfos.toString());
        return startInstance(startInstanceVO);
    }
}
