package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.wzmtr.eam.dataobject.FaultTrackDO;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.PersonResDTO;
import com.wzmtr.eam.dto.res.bpmn.FlowRes;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.fault.TrackResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.SubmitType;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.TrackMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.service.fault.TrackService;
import com.wzmtr.eam.utils.DateUtil;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.__BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:49
 */
@Service
@Slf4j
public class TrackServiceImpl implements TrackService {
    @Autowired
    private TrackMapper trackMapper;
    @Autowired
    private BpmnService bpmnService;
    @Autowired
    private FaultQueryMapper faultQueryMapper;
    @Autowired
    private OverTodoService overTodoService;
    @Autowired
    private IDictionariesService dictService;
    @Autowired
    private FaultQueryService faultQueryService;
    private static final Map<String, String> processMap = new HashMap<>();

    static {
        processMap.put("A30", "跟踪报告编制");
        processMap.put("A40", "技术主管审核");
        processMap.put("A50", "维保经理审核");
        processMap.put("A60", "运营公司专业工程师审核");
        processMap.put("A70", "中铁通科长审核");
        processMap.put("A80", "中铁通部长审核");
        processMap.put("A90", "部长审核");
    }

    @Override
    public Page<TrackResDTO> list(TrackReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<TrackResDTO> list = trackMapper.query(reqDTO.of(), reqDTO.getFaultTrackNo(), reqDTO.getFaultTrackWorkNo(), reqDTO.getRecStatus(), reqDTO.getEquipTypeCode(), reqDTO.getMajorCode(), reqDTO.getObjectName(), reqDTO.getObjectCode(), reqDTO.getSystemCode());
        if (CollectionUtil.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        return list;
    }

    @Override
    public TrackResDTO detail(SidEntity reqDTO) {
        return trackMapper.detail(reqDTO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void report(TrackReportReqDTO reqDTO) {
        reqDTO.setTrackReportTime(DateUtil.current("yyyy-MM-dd HH:mm:ss"));
        reqDTO.setTrackReporterId(TokenUtil.getCurrentPersonId());
        reqDTO.setRecStatus("30");
        trackMapper.report(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void close(TrackCloseReqDTO reqDTO) {
        reqDTO.setTrackCloseTime(DateUtil.current("yyyy-MM-dd HH:mm:ss"));
        reqDTO.setTrackCloserId(TokenUtil.getCurrentPersonId());
        reqDTO.setRecStatus("40");
        trackMapper.close(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repair(TrackRepairReqDTO reqDTO) {
        // /* 107 */     dmfm22.setWorkerGroupCode(repairDeptCode);
        reqDTO.setDispatchUserId(TokenUtil.getCurrentPersonId());
        reqDTO.setDispatchTime(DateUtil.current("yyyy-MM-dd HH:mm:ss"));
        reqDTO.setRecStatus("20");
        trackMapper.repair(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returns(FaultSubmitReqDTO reqDTO) {
        List<FaultTrackDO> list = trackMapper.queryOne(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo(), null, reqDTO.getFaultTrackNo());
        FaultTrackDO dmfm09 = list.get(0);
        // String userId = UserUtil.getLoginId();
        String processId = dmfm09.getWorkFlowInstId();
        String task = bpmnService.nextTaskKey(processId);
        if (StringUtils.isEmpty(task)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "您无权审核");
        } else {
            bpmnService.reject(task, reqDTO.getBackOpinion(), null);
            dmfm09.setRecStatus("30");
            dmfm09.setWorkFlowInstStatus("驳回成功");
            trackMapper.update(dmfm09);
        }
    }
    @Override
    public void transmit(FaultQueryReqDTO reqDTO) {
        TrackQueryResDTO res = faultQueryMapper.queryOneByFaultWorkNoAndFaultNo(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        if (res == null) {
            return;
        }
        String workFlowInstId;
        if (StringUtils.isNotEmpty(res.getWorkFlowInstId())) {
            workFlowInstId = res.getWorkFlowInstId();
        } else {
            workFlowInstId = res.getRecId() + "_" + res.getFaultWorkNo();
        }
        overTodoService.overTodo(workFlowInstId, "跟踪工单");
        FaultTrackDO bo = __BeanUtil.convert(res, FaultTrackDO.class);
        bo.setRecStatus("20");
        bo.setExt1(workFlowInstId);
        // todo 发短信
        // String stepUserGroup = "DM_013";
        // /*  598 */       EiInfo conditionInfo1 = new EiInfo();
        // /*  599 */       String content1 = "工班人员跟踪观察，跟踪工单号：" + faultTrackNo + "，请关注。";
        // /*  600 */       conditionInfo1.set("groupName", stepUserGroup);
        // /*  601 */       conditionInfo1.set("content", content1);
        // /*  602 */       conditionInfo1.set("orgCode", dmfm02.getWorkClass());
        // /*  603 */       conditionInfo1.set("faultWorkNo", faultWorkNo);
        // /*  604 */       ISendMessage.senMessageByGroupAndOrgCode(conditionInfo1);
        trackMapper.transmit(bo);
    }

    @Override
    public void submit(FaultSubmitReqDTO reqDTO) {
        // dmfm09.query
        List<FaultTrackDO> dmfm9List = trackMapper.queryOne(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo(), reqDTO.getFaultAnalysisNo(), reqDTO.getFaultTrackNo());
        if (CollectionUtil.isEmpty(dmfm9List)) {
            return;
        }
        FaultTrackDO dmfm09 = dmfm9List.get(0);
        // dmfm01.query
        List<FaultDetailResDTO> dmfm01List = faultQueryMapper.list(FaultQueryReqDTO.builder().faultNo(reqDTO.getFaultNo()).faultWorkNo(reqDTO.getFaultWorkNo()).build());
        FaultDetailResDTO faultDetailResDTO = dmfm01List.get(0);
        SubmitType type = reqDTO.getType();
        String majorCode = faultDetailResDTO.getMajorCode();
        String userId = TokenUtil.getCurrentPersonId();
        List<FlowRes> taskList = null;
        try {
            taskList = bpmnService.queryFlowList(null, null);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "获取任务列表异常");
        }
        Dictionaries dictionaries = dictService.queryOneByItemCodeAndCodesetCode("dm.vehicleSpecialty", "01");
        String itemEname = dictionaries.getItemEname();
        List<String> cos = Arrays.asList(itemEname.split(","));
        if (dmfm09.getWorkFlowInstId().trim().isEmpty() && SubmitType.COMMIT.equals(type)) {
            Map<String, Object> variables = new HashMap<>();
            // EiInfo eiInfo = new EiInfo();
            // eiInfo.set("processKey", "DMFM02");
            // eiInfo.set(EiConstant.serviceId, "S_EW_38");
            // EiInfo out = XServiceManager.call(eiInfo);
            // List<Map> processList = (List) out.get("processKey");
            JSONArray userJson = null;
            for (int i = 0; i < processMap.values().size(); i++) {
                String nodeKey = processMap.get("nodeKey");
                if ("A40".equals(nodeKey) && cos.contains(majorCode)) {
                    String cocode = "ZC";
                    userJson = JSON.parseArray(processMap.get("userList"));
                    variables.put("CO_CODE", cocode);
                    break;
                }
                if ("A70".equals(nodeKey) && !cos.contains(majorCode)) {
                    String cocode = "ZTT";
                    userJson = JSONArray.parseArray(processMap.get("userList"));
                    variables.put("CO_CODE", cocode);
                    break;
                }
            }
            List<String> userCode = new ArrayList<>();
            for (int j = 0; j < userJson.size(); j++) {
                userCode.add((String) ((JSONObject) userJson.get(j)).get("userId"));
            }
            String orgCode = dmfm09.getWorkClass();
            Set<String> userCodeSet = new HashSet<>(userCode);
            List<PersonResDTO> nextUser = new ArrayList<>();
            if ("ZC".equals(variables.get("CO_CODE"))) {
                // TODO 根据group获取用户
                // nextUser = InterfaceHelper.getUserHelpe().getUserByGroup("DM_010");
                for (PersonResDTO res : nextUser) {
                    res.setUserId(res.getLoginName());
                }
            } else if ("ZTT".equals(variables.get("CO_CODE"))) {
                nextUser = faultQueryService.queryUserList(userCodeSet, orgCode);
            }
            if (nextUser.isEmpty()) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "下一步参与者不存在");
            }
            String stuts = null;
            try {
                String processId = bpmnService.commit(dmfm09.getWorkFlowInstId(), BpmnFlowEnum.FAULT_TRACK.value(), null, null);
                stuts = bpmnService.nextTaskKey(processId);
            } catch (Exception e) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "送审失败！流程提交失败。");
            }
//             String processId = WorkflowHelper.start("DMFM02", (String) dmfm09.get("workFlowInstId"), userId, (String) dmfm09.get("faultTrackNo"), nextUser, variables);
            dmfm09.setWorkFlowInstStatus("提交审核");
            String workFlowIns = dmfm09.getWorkFlowInstId();
            overTodoService.overTodo(workFlowIns, "故障跟踪");
            dmfm09.setWorkFlowInstStatus(stuts);
            dmfm09.setRecStatus("40");
        } else if (SubmitType.PASS.equals(reqDTO.getType())) {
            if (CollectionUtil.isEmpty(taskList)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "您无权审核");
            } else {
                FlowRes task = taskList.get(0);
                String taskId = task.getDefId();
                String taskDefKey = task.getDefKey();
                if (!"A60".equals(taskDefKey)) {
                    // TODO: 2023/9/7 获取所有的流程
                    // /*  769 */           EiInfo eiInfo = new EiInfo();
                    // /*  770 */           eiInfo.set("userId", userId);
                    // /*  771 */           eiInfo.set("taskId", taskId);
                    // /*  772 */           eiInfo.set(EiConstant.serviceId, "S_EW_28");
                    // EiInfo out = XServiceManager.call(eiInfo);
                    List<Map<String, String>> processList = Lists.newArrayList();
                    JSONArray userJson = null;
                    Map<String, String> map = processList.get(0);
                    String userList = map.get("userList");
                    List<Object> userCode = new ArrayList();
                    for (int i = 0; i < userJson.size(); i++) {
                        userCode.add(((JSONObject) userJson.get(i)).get("userId"));
                    }
                    if ("A40".equals(taskDefKey)) {
                        String groupName = "DM_005";
                        List<Object> nextUser = Lists.newArrayList();
                        // List<Map<String, String>> nextUser = InterfaceHelper.getUserHelpe().getUserByGroup(groupName);
                        // for (Map<String, String> user : nextUser) {
                        //     user.put("userId", user.get("loginName"));
                        // }
                        if (CollectionUtil.isEmpty(nextUser)) {
                            throw new CommonException(ErrorCode.NORMAL_ERROR, "下一步参与者不存在");
                        }
                        // submit
                        try {
                            bpmnService.commit(taskId, BpmnFlowEnum.FAULT_TRACK.value(), null, null);
                        } catch (Exception e) {
                            log.error("commit error", e);
                        }
                        // submtStatus = WorkflowHelper.submit(taskId, userId, comment, "", nextUser, null);
                        dmfm09.setRecStatus("40");
                        dmfm09.setWorkFlowInstStatus("A60");
                    } else if ("A70".equals(taskDefKey)) {
                        String orgCode = dmfm09.getWorkClass();
                        String groupName = "DM_026";
                        List<Object> nextUser = Lists.newArrayList();
                        // List nextUser = InterfaceHelper.getUserHelpe().getUserByGroupNameAndOrg(groupName, orgCode);
                        if (CollectionUtil.isEmpty(nextUser)) {
                            throw new CommonException(ErrorCode.NORMAL_ERROR, "下一步参与者不存在");
                        }
                        // submtStatus = WorkflowHelper.submit(taskId, userId, comment, "", nextUser, null);
                        try {
                            bpmnService.commit(taskId, BpmnFlowEnum.FAULT_TRACK.value(), null, null);
                        } catch (Exception e) {
                            log.error("commit error", e);
                        }
                        dmfm09.setRecStatus("40");
                        dmfm09.setWorkFlowInstStatus("A80");
                    } else if ("A50".equals(taskDefKey)) {
                        String orgCode = faultDetailResDTO.getRespDeptCode();
                        String[] ates = {"DM_005", "DM_004"};
                        List<String> group = Arrays.asList(ates);
                        List nextUser = null;
                        for (int j = 0; j < group.size(); j++) {
                            // nextUser = InterfaceHelper.getUserHelpe().getUserByGroupNameAndOrg(group.get(j), orgCode);
                            if (nextUser != null) {
                                break;
                            }
                        }
                        if (CollectionUtil.isEmpty(nextUser)) {
                            throw new CommonException(ErrorCode.NORMAL_ERROR, "下一步参与者不存在");
                        }
                        try {
                            bpmnService.commit(taskId, BpmnFlowEnum.FAULT_TRACK.value(), null, null);
                        } catch (Exception e) {
                            log.error("commit error", e);
                        }
                        dmfm09.setRecStatus("40");
                        dmfm09.setWorkFlowInstStatus("A60");
                    } else if ("A80".equals(taskDefKey)) {
                        String orgCode = faultDetailResDTO.getRespDeptCode();
                        String[] ates = {"DM_005", "DM_004"};
                        List<String> group = Arrays.asList(ates);
                        List nextUser = null;
                        // for (int j = 0; j < group.size(); j++) {
                        //     nextUser = InterfaceHelper.getUserHelpe().getUserByGroupNameAndOrg(group.get(j), orgCode);
                        //     if (nextUser != null) {
                        //         break;
                        //     }
                        // }
                        // if (CollectionUtil.isEmpty(nextUser)) {
                        //     throw new CommonException(ErrorCode.NORMAL_ERROR, "下一步参与者不存在");
                        // }
                        try {
                            bpmnService.commit(taskId, BpmnFlowEnum.FAULT_TRACK.value(), null, null);
                        } catch (Exception e) {
                            log.error("commit error", e);
                        }
                        dmfm09.setRecStatus("40");
                        dmfm09.setWorkFlowInstStatus("A60");
                    } else {
                        List<Map<String, Object>> nextUser = new ArrayList<>();
                        // submtStatus = WorkflowHelper.submit(taskId, userId, comment, "", nextUser, null);
                        dmfm09.setRecStatus("50");
                        dmfm09.setWorkFlowInstStatus("A60");
                    }
                } else {
                    Map<String, Object> variables = new HashMap<>();
                    // if ("yes".equals(condition)) {
                    //     // EiInfo eiInfo = new EiInfo();
                    //     // eiInfo.set("userId", userId);
                    //     // eiInfo.set("taskId", taskId);
                    //     // eiInfo.set(EiConstant.serviceId, "S_EW_28");
                    //     // EiInfo out = XServiceManager.call(eiInfo);
                    //     // List<Map> processList = (List) out.get("result");
                    //     // JSONArray userJson = JSONArray.fromObject(((Map) processList.get(0)).get("userList"));
                    //     List<Object> userCode = new ArrayList<>();
                    //     // for (int i = 0; i < userJson.size(); i++) {
                    //     //     userCode.add(((JSONObject) userJson.get(i)).get("userId"));
                    //     // }
                    //     Map<Object, Object> orgMap = new HashMap<>();
                    //     orgMap.put("userCode", userCode.toArray());
                    //     String orgCode = faultDetailResDTO.getRespDeptCode();
                    //     String groupName = "DM_029";
                    //     // List nextUser = InterfaceHelper.getUserHelpe().getUserByGroupNameAndOrg(groupName, orgCode);
                    //     // if (nextUser.size() == 0) {
                    //     //     throw new CommonException(ErrorCode.NORMAL_ERROR, "下一步参与者不存在");
                    //     // }
                    //     variables.put("isCommit", "true");
                    //     // submtStatus = WorkflowHelper.submit(taskId, userId, comment, "", nextUser, variables);
                    //     try {
                    //         bpmnService.commit(taskId, BpmnFlowEnum.FAULT_TRACK.value(), null, null);
                    //     } catch (Exception e) {
                    //         log.error("commit error", e);
                    //     }
                    //     dmfm09.setRecStatus("40");
                    //     dmfm09.setWorkFlowInstStatus("A90");
                    // }
                    variables.put("isCommit", "false");
                    // submtStatus = WorkflowHelper.submit(taskId, userId,  comment, "", nextUser, variables);
                    try {
                        bpmnService.commit(taskId, BpmnFlowEnum.FAULT_TRACK.value(), null, null);
                    } catch (Exception e) {
                        log.error("commit error", e);
                    }
                    dmfm09.setRecStatus("50");
                    dmfm09.setWorkFlowInstStatus("End");
                }
            }
        } else if (reqDTO.getType() == SubmitType.COMMIT && !dmfm09.getWorkFlowInstId().trim().isEmpty()) {
            if (CollectionUtil.isEmpty(taskList)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "您无权审核");
            } else {
                FlowRes task = taskList.get(0);
                String taskId = task.getDefId();
                // EiInfo eiInfo = new EiInfo();
                // eiInfo.set("userId", userId);
                // eiInfo.set("taskId", taskId);
                // eiInfo.set(EiConstant.serviceId, "S_EW_28");
                // EiInfo out = XServiceManager.call(eiInfo);
                // List<Map> processList = (List) out.get("result");
                JSONArray userJson = null;
                Map<String, Object> variables = new HashMap<>();
                // for (int i = 0; i < processList.size(); i++) {
                //     String nodeKey = (String) processMap.get("nodeKey");
                //     if ("A40".equals(nodeKey) && cos.contains(majorCode)) {
                //         String cocode = "ZC";
                //         userJson = JSONArray.fromObject(processMap.get("userList"));
                //         variables.put("CO_CODE", cocode);
                //         break;
                //     }
                //     if ("A70".equals(nodeKey) && !cos.contains(majorCode)) {
                //         String cocode = "ZTT";
                //         userJson = JSONArray.parseArray(processMap.get("userList"));
                //         variables.put("CO_CODE", cocode);
                //         break;
                //     }
                // }
                List<Object> userCode = new ArrayList();
                for (int j = 0; j < userJson.size(); j++) {
                    userCode.add(((JSONObject) userJson.get(j)).get("userId"));
                }
                Map<Object, Object> orgMap = new HashMap<>();
                String orgCode = dmfm09.getWorkClass();
                orgMap.put("orgCode", orgCode);
                orgMap.put("userCode", userCode.toArray());
                List<Map<String, String>> nextUser = new ArrayList<>();
                // if ("ZC".equals(variables.get("CO_CODE"))) {
                //     nextUser = InterfaceHelper.getUserHelpe().getUserByGroup("DM_010");
                //     for (Map<String, String> user : nextUser) {
                //         user.put("userId", user.get("loginName"));
                //     }
                // } else if ("ZTT".equals(variables.get("CO_CODE"))) {
                //     nextUser = queryUserList(orgMap);
                // }
                if (CollectionUtil.isEmpty(nextUser)) {
                    throw new CommonException(ErrorCode.NORMAL_ERROR, "下一步参与者不存在");
                }
                // submtStatus = WorkflowHelper.submit(taskId, userId, comment, "", nextUser, variables);
                try {
                    bpmnService.commit(taskId, BpmnFlowEnum.FAULT_TRACK.value(), null, null);
                } catch (Exception e) {
                    log.error("commit error", e);
                }
                dmfm09.setRecStatus("40");
                dmfm09.setWorkFlowInstStatus("跟踪报告");
                log.info("跟踪报告送审成功！");
            }
        } else {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "当前流程处于开始阶段，无法直接审核通过");
        }
        trackMapper.update(dmfm09);
    }


}
