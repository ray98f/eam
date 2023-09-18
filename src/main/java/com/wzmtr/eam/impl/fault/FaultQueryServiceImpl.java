package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dataobject.FaultTrackDO;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.PersonResDTO;
import com.wzmtr.eam.dto.res.bpmn.FlowRes;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.enums.*;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.fault.AnalyzeMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.mapper.fault.TrackMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 17:02
 */
@Service
@Slf4j
public class FaultQueryServiceImpl implements FaultQueryService {
    @Autowired
    FaultQueryMapper faultQueryMapper;
    @Autowired
    FaultReportMapper reportMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private OverTodoService overTodoService;
    @Autowired
    private TrackMapper trackMapper;
    @Autowired
    private IDictionariesService dictService;
    @Autowired
    private AnalyzeMapper analyzeMapper;
    @Autowired
    private BpmnService bpmnService;

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
    public Page<FaultDetailResDTO> list(FaultQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<FaultDetailResDTO> list = faultQueryMapper.query(reqDTO.of(), reqDTO);
        List<FaultDetailResDTO> records = list.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new Page<>();
        }
        records.forEach(a -> {
            String repair = organizationMapper.getExtraOrgByAreaId(a.getRepairDeptCode());
            a.setRepairDeptName(StringUtils.isEmpty(repair) ? a.getRepairDeptCode() : repair);
        });
        return list;
    }

    @Override
    public List<FaultDetailResDTO> statisticList(FaultQueryReqDTO reqDTO) {
        return faultQueryMapper.list(reqDTO);
    }

    @Override
    public String queryOrderStatus(SidEntity reqDTO) {
        // faultWorkNo
        List<String> status = faultQueryMapper.queryOrderStatus(reqDTO);
        return CollectionUtil.isEmpty(status) ? null : status.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issue(FaultDetailReqDTO reqDTO) {
        FaultQueryServiceImpl aop = (FaultQueryServiceImpl) AopContext.currentProxy();
        String status = aop.queryOrderStatus(SidEntity.builder().id(reqDTO.getFaultWorkNo()).build());
        FaultOrderDO faultOrderDO = __BeanUtil.convert(reqDTO, FaultOrderDO.class);
        switch (status) {
            case "40":
                faultOrderDO.setReportStartUserId(TokenUtil.getCurrentPersonId());
                faultOrderDO.setReportStartTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                break;
            case "50":
                faultOrderDO.setReportFinishUserId(TokenUtil.getCurrentPersonId());
                faultOrderDO.setReportFinishTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                break;
            case "60":
                faultOrderDO.setConfirmUserId(TokenUtil.getCurrentPersonId());
                faultOrderDO.setConfirmTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                break;
            case "55":
                faultOrderDO.setCheckUserId(TokenUtil.getCurrentPersonId());
                faultOrderDO.setCheckTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
                break;
        }
        faultOrderDO.setRecRevisor(TokenUtil.getCurrentPersonId());
        faultOrderDO.setRecReviseTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
        reportMapper.updateFaultOrder(faultOrderDO);
        FaultInfoDO faultInfoDO = __BeanUtil.convert(reqDTO, FaultInfoDO.class);
        faultInfoDO.setRecRevisor(TokenUtil.getCurrentPersonId());
        faultInfoDO.setRecReviseTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
        reportMapper.updateFaultInfo(faultInfoDO);
    }

    @Override
    public void export(FaultExportReqDTO reqDTO, HttpServletResponse response) {
        // com.baosight.wzplat.dm.fm.service.ServiceDMFM0001#export
        List<String> listName = Arrays.asList("故障编号", "故障现象", "故障详情", "对象名称", "部件名称", "故障工单编号", "对象编码", "故障状态", "维修部门", "提报部门", "提报人员", "联系电话", "提报时间", "发现人", "发现时间", "故障紧急程度", "故障影响", "线路", "车底号/车厢号", "位置一", "位置二", "专业", "系统", "设备分类", "模块", "更换部件", "旧配件编号", "新配件编号", "部件更换时间", "故障处理人员", "故障处理情况", "故障处理人数");
        List<FaultDetailResDTO> faultDetailResDTOS = faultQueryMapper.export(reqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(faultDetailResDTOS)) {
            for (FaultDetailResDTO resDTO : faultDetailResDTOS) {
                String repairDept = organizationMapper.getOrgById(resDTO.getRepairDeptCode());
                String fillinDept = organizationMapper.getOrgById(resDTO.getFillinDeptCode());
                Map<String, String> map = new HashMap<>();
                OrderStatus orderStatus = OrderStatus.getByCode(resDTO.getOrderStatus());
                FaultAffect faultAffect = FaultAffect.getByCode(resDTO.getFaultAffect());
                FaultLevel faultLevel = FaultLevel.getByCode(resDTO.getOrderStatus());
                DealerUnit dealerUnit = DealerUnit.getByCode(resDTO.getDealerUnit());
                LineCode lineCode = LineCode.getByCode(resDTO.getLineCode());
                map.put("故障编号", resDTO.getFaultNo());
                map.put("故障现象", resDTO.getFaultDisplayDetail());
                map.put("故障详情", resDTO.getFaultDetail());
                map.put("对象名称", resDTO.getObjectName());
                map.put("部件名称", resDTO.getPartName());
                map.put("故障工单编号", resDTO.getFaultWorkNo());
                map.put("对象编码", resDTO.getObjectCode());
                map.put("故障状态", orderStatus != null ? orderStatus.getDesc() : resDTO.getOrderStatus());
                map.put("维修部门", StringUtils.isEmpty(repairDept) ? resDTO.getRepairDeptCode() : repairDept);
                map.put("提报部门", StringUtils.isEmpty(fillinDept) ? resDTO.getFillinDeptCode() : fillinDept);
                map.put("提报人员", resDTO.getFillinUserName());
                map.put("联系电话", resDTO.getDiscovererPhone());
                map.put("提报时间", resDTO.getFillinTime());
                map.put("发现人", resDTO.getDiscovererName());
                map.put("发现时间", resDTO.getDiscoveryTime());
                map.put("故障紧急程度", faultLevel == null ? resDTO.getFaultLevel() : faultLevel.getDesc());
                map.put("故障影响", faultAffect != null ? faultAffect.getDesc() : resDTO.getFaultAffect());
                map.put("线路", lineCode != null ? lineCode.getDesc() : resDTO.getLineCode());
                map.put("车底号/车厢号", resDTO.getTrainTrunk());
                map.put("位置一", resDTO.getPositionName());
                map.put("位置二", resDTO.getPosition2Name());
                map.put("专业", resDTO.getMajorName());
                map.put("系统", resDTO.getSystemName());
                map.put("设备分类", resDTO.getEquipTypeName());
                map.put("模块", resDTO.getFaultModule());
                map.put("故障处理人员", dealerUnit != null ? dealerUnit.getDesc() : resDTO.getDealerUnit());
                map.put("故障处理情况", resDTO.getFaultActionDetail());
                map.put("故障处理人数", resDTO.getDealerNum());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("xxxx", listName, list, null, response);
    }

    @Override
    public Page<ConstructionResDTO> construction(FaultQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<ConstructionResDTO> list = faultQueryMapper.construction(reqDTO.of(), reqDTO);
        List<ConstructionResDTO> records = list.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new Page<>();
        }
        return list;
    }

    @Override
    public Page<ConstructionResDTO> cancellation(FaultQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<ConstructionResDTO> list = faultQueryMapper.cancellation(reqDTO.of(), reqDTO);
        List<ConstructionResDTO> records = list.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new Page<>();
        }
        return list;
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
        faultQueryMapper.transmit(bo);
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
                nextUser = queryUserList(userCodeSet, orgCode);
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


    @Override
    public List<PersonResDTO> queryUserList(Set<String> userCode, String organCode) {
        List<PersonResDTO> orgUsers = analyzeMapper.getOrgUsers(userCode, organCode);
        if (CollectionUtil.isEmpty(orgUsers)) {
            List<PersonResDTO> parentList = analyzeMapper.queryCoParent(organCode);
            if (CollectionUtil.isEmpty(parentList)) {
                return orgUsers;
            }
            PersonResDTO parent = parentList.get(0);
            String orgCode = parent.getOrgCode();
            if (StringUtils.isEmpty(orgCode)) {
                return orgUsers;
            }
            return queryUserList(userCode, orgCode);
        }
        return orgUsers;
    }

    @Override
    public Boolean compareRows(CompareRowsReqDTO req) {
        //返回true 则代表不能同时进行操作
        Set<String> faultNos = req.getFaultNos();
        List<FaultDetailResDTO> list = faultQueryMapper.export(FaultExportReqDTO.builder().faultNos(faultNos).build());
        if ((((list != null) ? 1 : 0) & ((list.size() > 1) ? 1 : 0)) != 0) {
            List<String> majorCodelist = list.stream().map(FaultDetailResDTO::getMajorCode).collect(Collectors.toList());
            List<String> orderStatuslist = list.stream().map(FaultDetailResDTO::getOrderStatus).collect(Collectors.toList());
            List<String> lineCodelist = list.stream().map(FaultDetailResDTO::getLineCode).collect(Collectors.toList());
            Set<String> s = new HashSet<>(majorCodelist);
            Set<String> hs = new HashSet<>(lineCodelist);
            Set<String> hhs = new HashSet<>(orderStatuslist);
            List<String> str = Collections.singletonList("10");
            if (orderStatuslist.contains("10")) {
                hhs.removeAll(str);
                // 检查s集合和hs集合的大小是否都为1，并且hhs为空。如果满足这些条件，则返回false.
                return s.size() != 1 || hs.size() != 1 || !hhs.isEmpty();
                // if (s.size() == 1 && hs.size() == 1 && hhs.isEmpty()) {
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    // 驳回
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
    public void finishConfirm(FaultFinishConfirmReqDTO reqDTO) {
    }
}
    // @Override
//     public void finishConfirm(FaultFinishConfirmReqDTO reqDTO) {
//             StringBuffer buffer = new StringBuffer();
//             StringBuffer detail = new StringBuffer();
//             EiBlock userBlock = inInfo.getBlock("uult");
//             List<Map> nextUser = new ArrayList<>();
//             if (userBlock != null) {
//                 for (int j = 0; j < userBlock.getRowCount(); j++) {
//                     Map temp = userBlock.getRow(j);
//                     nextUser.add(temp);
//                 }
//             }
//             String currentUser = String.valueOf(UserSession.getLoginName());
//             SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//             UserCoInfo userCoInfo = UserFactory.getUserAllInfo().getUserInfo(UserUtil.getLoginId()).getUserCoInfo();
//             String content = null;
//             int updateSuccessCount = 0;
//             int updateFailCount = 0;
//             String blockId = (String) inInfo.get("blockId");
//             String type = (String) inInfo.get("type");
//             String isToSubmit = (String) inInfo.get("isToSubmit");
//             String isToSend = (String) inInfo.get("isToSend");
//             String resultType = null;
//             Map<Object, Object> reportMap = new HashMap<>();
//             if (inInfo.getBlock("report") != null) {
//                 String arrivalTime = (String) inInfo.getBlock("report").getRow(0).get("arrivalTime");
//                 String repairStartTime = (String) inInfo.getBlock("report").getRow(0).get("repairStartTime");
//                 String repairEndTime = (String) inInfo.getBlock("report").getRow(0).get("repairEndTime");
//                 String faultProcessResult = (String) inInfo.getBlock("report").getRow(0).get("faultProcessResult");
//                 String faultAffect = (String) inInfo.getBlock("report").getRow(0).get("faultAffect");
//                 String faultReasonCode = (String) inInfo.getBlock("report").getRow(0).get("faultReasonCode");
//                 String faultReasonDetail = (String) inInfo.getBlock("report").getRow(0).get("faultReasonDetail");
//                 String faultActionCode = (String) inInfo.getBlock("report").getRow(0).get("faultActionCode");
//                 String faultActionDetail = (String) inInfo.getBlock("report").getRow(0).get("faultActionDetail");
//                 String reportUserId = (String) inInfo.getBlock("report").getRow(0).get("reportUserId");
//                 String reportUserName = (String) inInfo.getBlock("report").getRow(0).get("reportUserName");
//                 String remark = (String) inInfo.getBlock("report").getRow(0).get("remark");
//                 String isFault = (String) inInfo.getBlock("report").getRow(0).get("isFault");
//                 String dealerUnit = (String) inInfo.getBlock("report").getRow(0).get("dealerUnit");
//                 String dealerNum = (String) inInfo.getBlock("report").getRow(0).get("dealerNum");
//                 if (dealerUnit != null && !dealerUnit.trim().equals("")) {
//                     reportMap.put("dealerUnit", dealerUnit);
//                 }
//                 if (dealerNum != null && !dealerNum.trim().equals("")) {
//                     reportMap.put("dealerNum", dealerNum);
//                 }
//                 reportMap.put("arrivalTime", arrivalTime);
//                 reportMap.put("repairStartTime", repairStartTime);
//                 reportMap.put("repairEndTime", repairEndTime);
//                 reportMap.put("faultProcessResult", faultProcessResult);
//                 reportMap.put("faultAffect", faultAffect);
//                 reportMap.put("faultReasonCode", faultReasonCode);
//                 reportMap.put("faultReasonDetail", faultReasonDetail);
//                 reportMap.put("faultActionCode", faultActionCode);
//                 reportMap.put("faultActionDetail", faultActionDetail);
//                 reportMap.put("reportUserId", reportUserId);
//                 reportMap.put("reportUserName", reportUserName);
//                 reportMap.put("remark", remark);
//                 reportMap.put("isFault", isFault);
//                 resultType = (String) inInfo.getBlock("report").getRow(0).get("faultProcessResult");
//             }
//             EiInfo result = null;
//             Map<Object, Object> faultMap = new HashMap<>();
//             List<DMFM02> faultList = new ArrayList();
//             List<Map<String, Object>> faultList4 = new ArrayList<>();
//             /*  947 */
//             List<Map<String, String>> faultList5 = new ArrayList<>();
//             /*  948 */
//             EiInfo conditionInfo1 = new EiInfo();
//             /*      */
//             /*  950 */
//             List<Map> cos1 = CodeFactory.getCodeService().getCode("dm.vehicleSpecialty", "01", "1");
//             /*  951 */
//             String codeName = ((Map) cos1.get(0)).get("itemEname").toString();
//             /*  952 */
//             String[] cos01 = codeName.split(",");
//             /*      */
//             /*  954 */
//             List<String> cos = Arrays.asList(cos01);
//             /*  955 */
//             for (int i = 0; i < inInfo.getBlock(blockId).getRowCount(); i++) {
//                 /*      */
//                 /*  957 */
//                 String objectCode = (String) inInfo.getBlock("report").getRow(0).get("objectCode");
//                 /*  958 */
//                 String objectName = (String) inInfo.getBlock("report").getRow(0).get("objectName");
//                 /*  959 */
//                 String partCode = (String) inInfo.getBlock("report").getRow(0).get("partCode");
//                 /*  960 */
//                 String partName = (String) inInfo.getBlock("report").getRow(0).get("partName");
//                 /*  961 */
//                 inInfo.getBlock(blockId).setCell(i, "recRevisor", currentUser);
//                 /*  962 */
//                 inInfo.getBlock(blockId).setCell(i, "recReviseTime", dateTimeFormat.format(new Date()));
//                 /*  963 */
//                 if (objectCode != null && objectCode.trim() != "") {
//                     /*  964 */
//                     inInfo.getBlock(blockId).setCell(i, "objectCode", objectCode);
//                     /*      */
//                 }
//                 /*  966 */
//                 if (objectName != null && objectName.trim() != "") {
//                     /*  967 */
//                     inInfo.getBlock(blockId).setCell(i, "objectName", objectName);
//                     /*      */
//                 }
//                 /*  969 */
//                 if (partCode != null && partCode.trim() != "") {
//                     /*  970 */
//                     inInfo.getBlock(blockId).setCell(i, "partCode", partCode);
//                     /*      */
//                 }
//                 /*  972 */
//                 if (partName != null && partName.trim() != "") {
//                     /*  973 */
//                     inInfo.getBlock(blockId).setCell(i, "partName", partName);
//                     /*      */
//                 }
//                 /*      */
//                 /*      */
//                 try {
//                     /*  977 */
//                     String faultType = (String) inInfo.getBlock(blockId).getRow(i).get("faultType");
//                     /*  978 */
//                     String traintag = (String) inInfo.getBlock(blockId).getRow(i).get("traintag");
//                     /*  979 */
//                     String faultWorkNo = (String) inInfo.getBlock(blockId).getRow(i).get("faultWorkNo");
//                     /*  980 */
//                     String faultNo = (String) inInfo.getBlock(blockId).getRow(i).get("faultNo");
//                     /*  981 */
//                     String majorName = (String) inInfo.getBlock(blockId).getRow(i).get("majorName");
//                     /*  982 */
//                     String majorCode = (String) inInfo.getBlock(blockId).getRow(i).get("majorCode");
//                     /*  983 */
//                     int status = 1;
//                     /*      */
//                     /*  985 */
//                     if (inInfo.getBlock(blockId).getRow(i).get("orderStatus") != null) {
//                         /*      */
//                         /*  987 */
//                         String orderStatus = (String) inInfo.getBlock(blockId).getRow(i).get("orderStatus");
//                         /*  988 */
//                         Map<Object, Object> map = new HashMap<>();
//                         /*  989 */
//                         map.put("faultNo", faultNo);
//                         /*  990 */
//                         map.put("faultWorkNo", faultWorkNo);
//                         /*  991 */
//                         map.put("orderStatus", orderStatus);
//                         /*      */
//                         /*  993 */
//                         map.put("reportFinishUserId", currentUser);
//                         /*  994 */
//                         map.put("reportFinishTime", dateTimeFormat.format(new Date()));
//                         /*  995 */
//                         map.putAll(reportMap);
//                         /*      */
//                         /*      */
//                         /*  998 */
//                         status = this.dao.update("DMFM02.update", map);
//                         /*      */
//                     }
//                     /*      */
//                     /* 1001 */
//                     status = this.dao.update("DMFM01.update", inInfo.getBlock("report").getRow(0));
//                     /* 1002 */
//                     faultMap.put("faultNo", faultNo);
//                     /* 1003 */
//                     faultMap.put("faultWorkNo", faultWorkNo);
//                     /* 1004 */
//                     faultList = this.dao.query("DMFM02.query", faultMap, 0, -999999);
//                     /* 1005 */
//                     faultList4 = this.dao.query("DMFM01.query", faultMap, 0, -999999);
//                     /*      */
//                     /*      */
//                     /* 1008 */
//                     String respDeptCode = ((Map) faultList4.get(0)).get("respDeptCode").toString();
//                     /* 1009 */
//                     String lineCode = (String) inInfo.getBlock(blockId).getRow(i).get("lineCode");
//                     /* 1010 */
//                     DMFM02 dmfm02 = faultList.get(0);
//                     /* 1011 */
//                     String workClass = dmfm02.getWorkClass();
//                     /* 1012 */
//                     if (type.equals("finish")) {
//                         /* 1013 */
//                         String bussId = dmfm02.getRecId() + "_" + faultWorkNo;
//                         /*      */
//                         /* 1015 */
//                         DMUtil.overTODO(dmfm02.getRecId(), "故障维修");
//                         /*      */
//                         /* 1017 */
//                         if ("02".equals(resultType)) {
//                             /*      */
//                             /* 1019 */
//                             content = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已提报完工，请及时在EAM系统验收！";
//                             /* 1020 */
//                             String content4 = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已提报完工，但需要故障跟踪，请及时在EAM系统跟踪观察！";
//                             /* 1021 */
//                             if (cos.contains(majorCode)) {
//                                 /*      */
//                                 String content37;
//                                 EiInfo messageInfo;
//                                 /* 1023 */
//                                 String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "05", "1");
//                                 /* 1024 */
//                                 switch (majorCode) {
//                                     /*      */
//                                     /*      */
//                                     /*      */
//                                     case "07":
//                                         /* 1028 */
//                                         status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", bussId, faultWorkNo, "DM_006", zcStepOrg, "故障跟踪", "DMFM0010", currentUser, majorCode, lineCode, "10", content4);
//                                         /* 1029 */
//                                         status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_009", zcStepOrg, "故障验收", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
//                                         /*      */
//                                         break;
//                                     /*      */
//                                     /*      */
//                                     /*      */
//                                     /*      */
//                                     case "06":
//                                         /* 1035 */
//                                         status = DMUtil.insertTODOWithUserGroup("【" + majorName + "】故障管理流程", bussId, faultWorkNo, "DM_037", "故障跟踪", "DMFM0010", currentUser);
//                                         /* 1036 */
//                                         content37 = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已提报完工，但需要故障跟踪，请及时在EAM系统完工确认并跟踪观察！";
//                                         /*      */
//                                         /* 1038 */
//                                         messageInfo = new EiInfo();
//                                         /* 1039 */
//                                         messageInfo.set("group", "DM_037");
//                                         /* 1040 */
//                                         messageInfo.set("content", content37);
//                                         /* 1041 */
//                                         ISendMessage.sendMoblieMessageByGroup(messageInfo);
//                                         /* 1042 */
//                                         status = DMUtil.insertTODOWithUserGroup("【" + majorName + "】故障管理流程", bussId, faultWorkNo, "DM_037", "故障完工确认", "DMFM0001", currentUser);
//                                         /*      */
//                                         break;
//                                     /*      */
//                                 }
//                                 /* 1045 */
//                             } else if (majorCode.equals("17")) {
//                                 /*      */
//                                 /*      */
//                                 /* 1048 */
//                                 String zttStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "07", "1");
//                                 /* 1049 */
//                                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", bussId, faultWorkNo, "DM_045", zttStepOrg, "故障跟踪", "DMFM0010", currentUser, majorCode, lineCode, "10", content4);
//                                 /* 1050 */
//                                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_045", zttStepOrg, "故障完工确认", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
//                                 /*      */
//                                 /*      */
//                                 /*      */
//                             }
//                             /* 1054 */
//                             else if (isToSubmit != null && isToSubmit.equals("ToSubmit")) {
//                                 /*      */
//                                 /* 1056 */
//                                 if (nextUser != null && nextUser.size() > 0) {
//                                     /* 1057 */
//                                     status = DMUtil.insertTODOWithUserList(nextUser, "【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "故障验收", "DMFM0001", currentUser);
//                                     /* 1058 */
//                                     status = DMUtil.insertTODOWithUserList(nextUser, "【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "故障跟踪", "DMFM0001", currentUser);
//                                     /*      */
//                                     /* 1060 */
//                                     EiInfo userPhoneInfo = new EiInfo();
//                                     /* 1061 */
//                                     userPhoneInfo.set("contacts", nextUser);
//                                     /* 1062 */
//                                     userPhoneInfo.set("content", content);
//                                     /* 1063 */
//                                     ISendMessage.sendMessageByPhoneList(userPhoneInfo);
//                                     /* 1064 */
//                                     EiInfo userPhoneInfo2 = new EiInfo();
//                                     /* 1065 */
//                                     userPhoneInfo2.set("contacts", nextUser);
//                                     /* 1066 */
//                                     userPhoneInfo2.set("content", content4);
//                                     /* 1067 */
//                                     ISendMessage.sendMessageByPhoneList(userPhoneInfo2);
//                                     /*      */
//                                 } else {
//                                     /* 1069 */
//                                     status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "DM_006", workClass, "故障验收", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
//                                     /* 1070 */
//                                     status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", bussId, faultWorkNo, "DM_006", dmfm02.getWorkClass(), "故障跟踪", "DMFM0010", currentUser, majorCode, lineCode, "10", content4);
//                                     /*      */
//                                 }
//                                 /*      */
//                                 /*      */
//                             }
//                             /*      */
//                             else {
//                                 /*      */
//                                 /* 1076 */
//                                 String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "03", "1");
//                                 /* 1077 */
//                                 String content3 = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已验收，请及时在EAM系统完工确认！";
//                                 /* 1078 */
//                                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "DM_007", zcStepOrg, "故障完工确认", "DMFM0001", currentUser, majorCode, lineCode, "30", content3);
//                                 /*      */
//                             }
//                             /*      */
//                             /* 1081 */
//                         } else if ("03".equals(resultType)) {
//                             /*      */
//                             /* 1083 */
//                             content = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "未处理，请在EAM系统完工确认，并对故障及时处理！";
//                             /* 1084 */
//                             if (cos.contains(majorCode)) {
//                                 /*      */
//                                 String zcStepOrg;
//                                 String content37;
//                                 EiInfo messageInfo;
//                                 /* 1086 */
//                                 switch (majorCode) {
//                                     /*      */
//                                     case "07":
//                                         /* 1088 */
//                                         zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "04", "1");
//                                         /* 1089 */
//                                         status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_007", zcStepOrg, "工单完工确认并故障再派工", "DMFM0001", currentUser, majorCode, lineCode, "10", content);
//                                         /*      */
//                                         break;
//                                     /*      */
//                                     /*      */
//                                     /*      */
//                                     case "06":
//                                         /* 1094 */
//                                         content37 = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已提报完工，请及时在EAM系统完工确认并对故障再派工！";
//                                         /* 1095 */
//                                         messageInfo = new EiInfo();
//                                         /* 1096 */
//                                         messageInfo.set("group", "DM_037");
//                                         /* 1097 */
//                                         messageInfo.set("content", content37);
//                                         /* 1098 */
//                                         ISendMessage.sendMoblieMessageByGroup(messageInfo);
//                                         /* 1099 */
//                                         status = DMUtil.insertTODOWithUserGroup("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_037", "工单完工确认并故障再派工", "DMFM0001", currentUser);
//                                         /*      */
//                                         break;
//                                     /*      */
//                                 }
//                                 /*      */
//                                 /* 1103 */
//                             } else if (majorCode.equals("17")) {
//                                 /*      */
//                                 /*      */
//                                 /* 1106 */
//                                 String zttStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "07", "1");
//                                 /* 1107 */
//                                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_045", zttStepOrg, "工单完工确认并故障再派工", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
//                                 /*      */
//                             }
//                             /*      */
//                             else {
//                                 /*      */
//                                 /* 1111 */
//                                 String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "03", "1");
//                                 /* 1112 */
//                                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_007", zcStepOrg, "工单完工确认并故障再派工", "DMFM0001", currentUser, majorCode, lineCode, "10", content);
//                                 /*      */
//                             }
//                             /* 1114 */
//                         } else if ("04".equals(resultType)) {
//                             /*      */
//                             /* 1116 */
//                             String groupName = "DM_006";
//                             /* 1117 */
//                             String orgCode = null;
//                             /* 1118 */
//                             if (cos.contains(majorCode)) {
//                                 /* 1119 */
//                                 orgCode = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "05", "1");
//                                 /*      */
//                             } else {
//                                 /* 1121 */
//                                 orgCode = dmfm02.getWorkClass();
//                                 /*      */
//                             }
//                             /*      */
//                             /* 1124 */
//                             String content1 = "工班人员报告故障无法处理，工单号：" + faultWorkNo + "，请关注。";
//                             /* 1125 */
//                             conditionInfo1.set("group", groupName);
//                             /* 1126 */
//                             conditionInfo1.set("content", content1);
//                             /* 1127 */
//                             conditionInfo1.set("orgCode", orgCode);
//                             /* 1128 */
//                             ISendMessage.senMessageByGroupAndOrgCode(conditionInfo1);
//                             /* 1129 */
//                             content = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "无法处理，请在EAM系统完工确认，并对故障及时处理！";
//                             /* 1130 */
//                             if (cos.contains(majorCode)) {
//                                 /*      */
//                                 String stepOrg;
//                                 EiInfo messageInfo;
//                                 /* 1132 */
//                                 switch (majorCode) {
//                                     /*      */
//                                     case "07":
//                                         /* 1134 */
//                                         stepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "01", "1");
//                                         /* 1135 */
//                                         status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_007", stepOrg, "故障完工确认", "DMFM0001", currentUser, majorCode, lineCode, "30", content);
//                                         /*      */
//                                         break;
//                                     /*      */
//                                     /*      */
//                                     /*      */
//                                     /*      */
//                                     case "06":
//                                         /* 1141 */
//                                         messageInfo = new EiInfo();
//                                         /* 1142 */
//                                         messageInfo.set("group", "DM_037");
//                                         /* 1143 */
//                                         messageInfo.set("content", content);
//                                         /* 1144 */
//                                         ISendMessage.sendMoblieMessageByGroup(messageInfo);
//                                         /* 1145 */
//                                         status = DMUtil.insertTODOWithUserGroup("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_037", "故障完工确认", "DMFM0001", currentUser);
//                                         /*      */
//                                         break;
//                                     /*      */
//                                 }
//                                 /*      */
//                                 /* 1149 */
//                             } else if (majorCode.equals("17")) {
//                                 /*      */
//                                 /*      */
//                                 /* 1152 */
//                                 String zttStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "07", "1");
//                                 /* 1153 */
//                                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_045", zttStepOrg, "故障完工确认", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
//                                 /*      */
//                             }
//                             /*      */
//                             else {
//                                 /*      */
//                                 /* 1157 */
//                                 String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "03", "1");
//                                 /* 1158 */
//                                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "DM_007", zcStepOrg, "故障完工确认", "DMFM0001", currentUser, majorCode, lineCode, "30", content);
//                                 /*      */
//                             }
//                             /*      */
//                             /*      */
//                         }
//                         /*      */
//                         else {
//                             /*      */
//                             /* 1164 */
//                             content = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已提报完工，请及时在EAM系统验收！";
//                             /* 1165 */
//                             if (cos.contains(majorCode)) {
//                                 /*      */
//                                 String stepOrg;
//                                 String content37;
//                                 EiInfo messageInfo;
//                                 /* 1167 */
//                                 switch (majorCode) {
//                                     /*      */
//                                     /*      */
//                                     /*      */
//                                     case "07":
//                                         /* 1171 */
//                                         stepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "05", "1");
//                                         /* 1172 */
//                                         status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_009", stepOrg, "故障验收", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
//                                         /*      */
//                                         break;
//                                     /*      */
//                                     /*      */
//                                     /*      */
//                                     /*      */
//                                     /*      */
//                                     case "06":
//                                         /* 1179 */
//                                         content37 = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已提报完工，请及时在EAM系统完工确认！";
//                                         /* 1180 */
//                                         messageInfo = new EiInfo();
//                                         /* 1181 */
//                                         messageInfo.set("group", "DM_037");
//                                         /* 1182 */
//                                         messageInfo.set("content", content37);
//                                         /* 1183 */
//                                         ISendMessage.sendMoblieMessageByGroup(messageInfo);
//                                         /* 1184 */
//                                         status = DMUtil.insertTODOWithUserGroup("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_037", "故障完工确认", "DMFM0001", currentUser);
//                                         /*      */
//                                         break;
//                                     /*      */
//                                 }
//                                 /*      */
//                                 /* 1188 */
//                             } else if (majorCode.equals("17")) {
//                                 /*      */
//                                 /*      */
//                                 /* 1191 */
//                                 String zttStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "07", "1");
//                                 /* 1192 */
//                                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_045", zttStepOrg, "故障完工确认", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
//                                 /*      */
//                                 /*      */
//                                 /*      */
//                                 /*      */
//                             }
//                             /* 1197 */
//                             else if (isToSubmit != null && isToSubmit.equals("ToSubmit")) {
//                                 /*      */
//                                 /*      */
//                                 /* 1200 */
//                                 if (nextUser != null && nextUser.size() > 0) {
//                                     /* 1201 */
//                                     status = DMUtil.insertTODOWithUserList(nextUser, "【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "故障验收", "DMFM0001", currentUser);
//                                     /* 1202 */
//                                     EiInfo userPhoneInfo = new EiInfo();
//                                     /* 1203 */
//                                     userPhoneInfo.addBlock("result");
//                                     /* 1204 */
//                                     userPhoneInfo.set("contacts", nextUser);
//                                     /* 1205 */
//                                     userPhoneInfo.set("content", content);
//                                     /* 1206 */
//                                     ISendMessage.sendMessageByPhoneList(userPhoneInfo);
//                                     /*      */
//                                 } else {
//                                     /*      */
//                                     /* 1209 */
//                                     status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "DM_006", workClass, "故障验收", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
//                                     /*      */
//                                 }
//                                 /*      */
//                                 /*      */
//                             } else {
//                                 /*      */
//                                 /* 1214 */
//                                 String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "03", "1");
//                                 /* 1215 */
//                                 String content3 = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已验收，请及时在EAM系统完工确认！";
//                                 /* 1216 */
//                                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "DM_007", zcStepOrg, "故障完工确认", "DMFM0001", currentUser, majorCode, lineCode, "30", content3);
//                                 /*      */
//                             }
//                             /*      */
//                         }
//                         /*      */
//                     }
//                     /*      */
//                     /*      */
//                     /* 1222 */
//                     updateSuccessCount++;
//                     /* 1223 */
//                     inInfo.setStatus(status);
//                     /* 1224 */
//                 } catch (Exception ex) {
//                     /* 1225 */
//                     buffer.append("更新:" + inInfo.getBlock("result").getCell(i, "faultNo") + "的记录失败\n");
//                     /* 1226 */
//                     inInfo.setStatus(-1);
//                     /* 1227 */
//                     detail.append(ex.getCause().toString());
//                     /* 1228 */
//                     updateFailCount++;
//                     /*      */
//                 }
//                 /*      */
//             }
//             /* 1231 */
//             if (updateSuccessCount > 0)
//                 /* 1232 */ buffer.insert(0, "更新成功" + updateSuccessCount + "条记录！\n");
//             /* 1233 */
//             if (updateFailCount > 0)
//                 /* 1234 */ buffer.insert(0, "更新失败" + updateFailCount + "条记录！\n");
//             /* 1235 */
//             inInfo.setMsg(buffer.toString());
//             /* 1236 */
//             inInfo.setDetailMsg(detail.toString());
//             return inInfo;
//     }
// }
