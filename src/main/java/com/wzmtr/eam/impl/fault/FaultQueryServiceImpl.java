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
        // 返回true 则代表不能进行操作
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
                // 检查s集合和hs集合的大小是否都为1则为相同的major和lineCode，并且hhs为空。如果满足这些条件，则返回false.
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
        // todo
        // Dictionaries dictionaries = dictService.queryOneByItemCodeAndCodesetCode("dm.vehicleSpecialty", "01");
        // String itemEname = dictionaries.getItemEname();
        // String resultType = null;
        // List<String> cos = Arrays.asList(itemEname.split(","));
        // FaultOrderDO faultOrderDO = new FaultOrderDO();
        // if (reqDTO != null) {
        //     faultOrderDO.setRepairStartTime(reqDTO.getRepairStartTime());
        //     faultOrderDO.setRepairEndTime(reqDTO.getRepairEndTime());
        //     faultOrderDO.setFaultProcessResult(reqDTO.getFaultProcessResult());
        //     faultOrderDO.setFaultAffect(reqDTO.getFaultAffect());
        //     faultOrderDO.setFaultReasonCode(reqDTO.getFaultReasonCode());
        //     faultOrderDO.setFaultReasonDetail(reqDTO.getFaultReasonDetail());
        //     faultOrderDO.setFaultActionCode(reqDTO.getFaultActionCode());
        //     faultOrderDO.setFaultActionDetail(reqDTO.getFaultActionDetail());
        //     faultOrderDO.setReportUserId(reqDTO.getReportUserId());
        //     faultOrderDO.setReportUserName(reqDTO.getReportUserName());
        //     resultType = reqDTO.getFaultProcessResult();
        // }
        // SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // if (reqDTO.getOrderStatus() != null) {
        //     if (reqDTO.getOrderStatus().equals("40")) {
        //         faultOrderDO.setReportStartUserId(TokenUtil.getCurrentPersonId());
        //         faultOrderDO.setReportStartTime(dateTimeFormat.format(new Date()));
        //     } else if (reqDTO.getOrderStatus().equals("50")) {
        //         faultOrderDO.setReportFinishUserId(TokenUtil.getCurrentPersonId());
        //         faultOrderDO.setReportFinishTime(dateTimeFormat.format(new Date()));
        //         // map.putAll(reportMap);
        //     } else if (reqDTO.getOrderStatus().equals("60")) {
        //         faultOrderDO.setConfirmUserId(TokenUtil.getCurrentPersonId());
        //         faultOrderDO.setConfirmTime(dateTimeFormat.format(new Date()));
        //     } else if (reqDTO.getOrderStatus().equals("55")) {
        //         faultOrderDO.setCheckUserId(TokenUtil.getCurrentPersonId());
        //         faultOrderDO.setCheckTime(dateTimeFormat.format(new Date()));
        //     }
        //     reportMapper.updateFaultOrder(faultOrderDO);
        // }
        // String faultNo = reqDTO.getFaultNo();
        // String faultWorkNo = reqDTO.getFaultWorkNo();
        // FaultInfoDO faultInfo = faultQueryMapper.queryOneFaultInfo(faultNo);
        // FaultOrderDO dmfm02 = faultQueryMapper.queryOneFaultOrder(faultWorkNo);
        // String stationCode = dmfm02.getExt1();
        // String ext2 = dmfm02.getExt2();
        // String respDeptCode = faultInfo.getRespDeptCode();
        // String lineCode = faultInfo.getLineCode();
        // String workClass = dmfm02.getWorkClass();
        // // if (reqDTO.getType() == FaultOperateType.LOWERHAIR) {
        // //     DMUtil.overTODO(dmfm02.getRecId(), "提报成功，准备下发");
        // //     content = "【市铁投集团】" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "下发一条" + majorName + "故障，工单号：" + faultWorkNo + "，尽快派工。";
        // //     if (cos.contains(majorCode)) {
        // //         String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "04", "1");
        // //         status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_007", zcStepOrg, "故障派工", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
        // //     } else {
        // //         String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "03", "1");
        // //         status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_007", zcStepOrg, "故障派工", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
        // //     }
        // // }
        //     String bussId = dmfm02.getRecId() + "_" + faultWorkNo;
        //     DMUtil.overTODO(dmfm02.getRecId(), "故障维修");
        //     if ("02".equals(resultType)) {
        //         content = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已提报完工，请及时在EAM系统验收！";
        //         String content4 = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已提报完工，但需要故障跟踪，请及时在EAM系统跟踪观察！";
        //         if (cos.contains(majorCode)) {
        //             String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "05", "1");
        //             status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", bussId, faultWorkNo, "DM_006", zcStepOrg, "故障跟踪", "DMFM0010", currentUser, majorCode, lineCode, "10", content4);
        //             status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_009", zcStepOrg, "故障验收", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
        //         } else {
        //             status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", bussId, faultWorkNo, "DM_006", dmfm02.getWorkClass(), "故障跟踪", "DMFM0010", currentUser, majorCode, lineCode, "10", content4);
        //             if (isToSubmit != null && isToSubmit.equals("ToSubmit")) {
        //                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "DM_006", workClass, "故障验收", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
        //             } else {
        //                 String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "03", "1");
        //                 String content3 = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已验收，请及时在EAM系统完工确认！";
        //                 status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "DM_007", zcStepOrg, "故障完工确认", "DMFM0001", currentUser, majorCode, lineCode, "30", content3);
        //             }
        //         }
        //     } else if ("03".equals(resultType)) {
        //         content = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "未处理，请在EAM系统完工确认，并对故障及时处理！";
        //         if (cos.contains(majorCode)) {
        //             String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "04", "1");
        //             status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_007", zcStepOrg, "故障派工", "DMFM0001", currentUser, majorCode, lineCode, "10", content);
        //         } else {
        //             String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "03", "1");
        //             status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_007", zcStepOrg, "故障派工", "DMFM0001", currentUser, majorCode, lineCode, "10", content);
        //         }
        //     } else if ("04".equals(resultType)) {
        //         String groupName = "DM_006";
        //         String orgCode = null;
        //         if (cos.contains(majorCode)) {
        //             orgCode = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "05", "1");
        //         } else {
        //             orgCode = dmfm02.getWorkClass();
        //         }
        //         String content1 = "工班人员报告故障无法处理，工单号：" + faultWorkNo + "，请关注。";
        //         conditionInfo1.set("group", groupName);
        //         conditionInfo1.set("content", content1);
        //         conditionInfo1.set("orgCode", orgCode);
        //         ISendMessage.senMessageByGroupAndOrgCode(conditionInfo1);
        //         content = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "无法处理，请在EAM系统完工确认，并对故障及时处理！";
        //         if (cos.contains(majorCode)) {
        //             String stepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "01", "1");
        //             status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_007", stepOrg, "故障完工确认", "DMFM0001", currentUser, majorCode, lineCode, "30", content);
        //         } else {
        //             String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "03", "1");
        //             status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "DM_007", zcStepOrg, "故障完工确认", "DMFM0001", currentUser, majorCode, lineCode, "30", content);
        //         }
        //     } else {
        //         content = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已提报完工，请及时在EAM系统验收！";
        //         if (cos.contains(majorCode)) {
        //             String stepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "05", "1");
        //             status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", dmfm02.getRecId(), faultWorkNo, "DM_009", stepOrg, "故障验收", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
        //         }
        //         else if (isToSubmit != null && isToSubmit.equals("ToSubmit")) {
        //             status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "DM_006", workClass, "故障验收", "DMFM0001", currentUser, majorCode, lineCode, "20", content);
        //         }
        //         else {
        //             String zcStepOrg = CodeFactory.getCodeService().getCodeEName("dm.matchControl", "03", "1");
        //             String content3 = "【市铁投集团】工单号：" + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已验收，请及时在EAM系统完工确认！";
        //             status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + "】故障管理流程", queryFaultWorkRecId(faultWorkNo), faultWorkNo, "DM_007", zcStepOrg, "故障完工确认", "DMFM0001", currentUser, majorCode, lineCode, "30", content3);
        //     }
        // }
    }

    @Override
    public void faultListExport(FaultQueryReqDTO reqDTO) {

    }

    @Override
    public void sendWork(FaultSendWorkReqDTO reqDTO) {
        // 派工
        FaultOrderDO faultOrderDO = new FaultOrderDO();
        String workerGroupCode = reqDTO.getWorkerGroupCode();
        FaultDetailResDTO faultDetailResDTO = faultQueryMapper.queryDetail(FaultQueryDetailReqDTO.builder().faultWorkNo(reqDTO.getFaultWorkNo()).build());
        String majorName = faultDetailResDTO.getMajorName();
        if (StringUtils.isNotEmpty(reqDTO.getLevelFault())) {
            faultOrderDO.setExt1(reqDTO.getLevelFault());
        }
        reportMapper.updateFaultOrder(faultOrderDO);
        FaultInfoDO faultInfoDO = new FaultInfoDO();
        faultInfoDO.setRepairDeptCode(workerGroupCode);
        if (StringUtils.isNotEmpty(reqDTO.getIsTiKai()) & reqDTO.getIsTiKai().equals("08")) {
            faultInfoDO.setExt3("08");
        }
        reportMapper.updateFaultInfo(faultInfoDO);
        overTodoService.overTodo(reqDTO.getFaultOrderRecId(), "故障维修");
        // todo 发短信
        // String content = "【市铁投集团】" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "向您指派了一条故障工单，故障位置：" + positionName + "," + position2Name + "，设备名称：" + objectName + ",故障现象：" + faultDisplayDetail + "请及时处理并在EAM系统填写维修报告，工单号：" + faultWorkNo + "，请知晓。";
        overTodoService.insertTodoWithUserGroupAndOrg("【" + majorName + "】故障管理流程", reqDTO.getFaultOrderRecId(), reqDTO.getFaultWorkNo(), "DM_013", reqDTO.getWorkClass(), "故障维修", "DMFM0001", TokenUtil.getCurrentPersonId(), null);
        Dictionaries dictionaries = dictService.queryOneByItemCodeAndCodesetCode("dm.matchControl", "01");
        String zcStepOrg = dictionaries.getItemEname();
        if (!faultOrderDO.getWorkClass().contains(zcStepOrg)) {
            // todo
            sendContractFault(faultOrderDO);
        }
    }

    public void sendContractFault(FaultOrderDO dmfm02) {
        // /* 557 */
        // Map<Object, Object> map = new HashMap<>();
        // /* 558 */
        // map.put("faultNo", dmfm02.getFaultNo());
        // /* 559 */
        // List<Map> listFm01 = this.dao.query("DMFM01.query", map, 0, -999999);
        //
        // /* 560 */
        // if (listFm01 != null && listFm01.size() > 0) {
        //     FaultOrderDO dmfm01 = new FaultOrderDO();
        //     // dmfm01.fromMap(listFm01.get(0));
        //     Message message = new Message();
        //     message.setWorkNo(dmfm02.getFaultWorkNo());
        //     message.setWorkType("");
        //     message.setObjectName(dmfm01.getObjectName());
        //     message.setLineName(CodeFactory.getCodeService().getCodeCName("line", "01", "1"));
        //     message.setPosName(InterfaceHelper.getPositionHelpe().getPositionNameToCode(dmfm01.getPositionCode()));
        //     message.setEquipType1(InterfaceHelper.getIEquipKindIntf().getEquipmentMsg(dmfm01.getMajorCode()));
        //     message.setEquipType2(dmfm01.getSystemCode());
        //     message.setEquipType3(dmfm01.getEquipTypeCode());
        //     message.setFaultType(dmfm01.getFaultType());
        //     message.setFaultDetail(dmfm01.getFaultDetail());
        //     message.setDiscoverer(dmfm01.getDiscovererName());
        //     message.setDiscoveryTime(dmfm01.getDiscoveryTime());
        //     message.setFaultStatus(dmfm01.getFaultType());
        //     message.setDispatchUser(dmfm02.getDispatchUserName());
        //     message.setDispatchTime(dmfm02.getDispatchTime());
        //     message.setGroupCode(dmfm02.getWorkClass());
        //     message.setWorkClass("");
        // List<Map<String, String>> workerGroupList = InterfaceHelper.getUserHelpe().getWorkerGroupNameByWorkerGroupCode(dmfm02.getWorkClass());
        // if (workerGroupList != null && workerGroupList.size() > 0) {
        //     message.setWorkClass((String) ((Map) workerGroupList.get(0)).get("orgName"));
        // }
        // todo sendContractOrder(message);
    }
}

