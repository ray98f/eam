package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wzmtr.eam.dataobject.FaultAnalyzeDO;
import com.wzmtr.eam.dto.req.fault.AnalyzeReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultAnalyzeDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultSubmitReqDTO;
import com.wzmtr.eam.dto.res.common.PersonResDTO;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.bpmn.FlowRes;
import com.wzmtr.eam.dto.res.fault.AnalyzeResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.SubmitType;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.fault.AnalyzeMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.user.UserHelperMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.service.fault.AnalyzeService;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Author: Li.Wang
 * Date: 2023/8/14 16:31
 */
@Service
@Slf4j
public class AnalyzeServiceImpl implements AnalyzeService {
    @Autowired
    private AnalyzeMapper mapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private IDictionariesService dictionaryService;
    @Autowired
    private FaultQueryService faultQueryService;
    @Autowired
    private BpmnService bpmnService;
    @Autowired
    private UserHelperMapper userHelperMapper;
    @Autowired
    private FaultQueryMapper faultQueryMapper;

    @Override
    public Page<AnalyzeResDTO> list(AnalyzeReqDTO reqDTO) {
        Page<AnalyzeResDTO> query = mapper.query(reqDTO.of(), reqDTO.getFaultNo(), reqDTO.getMajorCode(), reqDTO.getRecStatus(), reqDTO.getLineCode(), reqDTO.getFrequency(), reqDTO.getPositionCode(), reqDTO.getDiscoveryStartTime(), reqDTO.getDiscoveryEndTime(), reqDTO.getRespDeptCode(), reqDTO.getAffectCodes());
        List<AnalyzeResDTO> records = query.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new Page<>();
        }
        records.forEach(a -> a.setRespDeptName(organizationMapper.getOrgById(a.getRespDeptCode())));
        return query;
    }

    @Override
    public void export(String faultAnalysisNo, String faultNo, String faultWorkNo, HttpServletResponse response) {
        List<AnalyzeResDTO> resList = mapper.list(faultAnalysisNo, faultNo, faultWorkNo);
        List<String> listName = Arrays.asList("故障分析编号", "故障编号", "故障工单编号", "专业", "故障发现时间", "线路", "频次", "位置", "牵头部门", "故障等级", "故障影响", "故障现象", "故障原因", "故障调查及处置情况", "系统", "本次故障暴露的问题", "整改措施");
        if (CollectionUtil.isEmpty(resList)) {
            return;
        }
        List<Map<String, String>> list = new ArrayList<>();
        for (AnalyzeResDTO resDTO : resList) {
            String respDept = organizationMapper.getOrgById(resDTO.getRespDeptCode());
            Map<String, String> map = new HashMap<>();
            map.put("故障分析编号", resDTO.getFaultAnalysisNo());
            map.put("故障编号", resDTO.getFaultNo());
            map.put("故障工单编号", resDTO.getFaultWorkNo());
            map.put("专业", resDTO.getMajorName());
            map.put("故障发现时间", resDTO.getDiscoveryTime());
            map.put("线路", resDTO.getLineCode());
            map.put("频次", resDTO.getFrequency());
            map.put("位置", resDTO.getPositionName());
            map.put("牵头部门", StringUtils.isEmpty(respDept) ? resDTO.getRespDeptCode() : respDept);
            map.put("故障等级", resDTO.getFaultLevel());
            map.put("故障影响", resDTO.getAffectCodes());
            map.put("故障现象", resDTO.getFaultDisplayDetail());
            map.put("故障原因", resDTO.getFaultReasonDetail());
            map.put("故障调查及处置情况", resDTO.getFaultProcessDetail());
            map.put("系统", resDTO.getSystemCode());
            map.put("本次故障暴露的问题", resDTO.getProblemDescr());
            map.put("整改措施", resDTO.getImproveDetail());
            list.add(map);
        }
        ExcelPortUtil.excelPort("故障调查及处置情况", listName, list, null, response);
    }

    @Override
    public AnalyzeResDTO detail(FaultAnalyzeDetailReqDTO reqDTO) {
        AnalyzeResDTO detail = mapper.detail(reqDTO);
        if (detail == null) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        return detail;
    }

    @Override
    public void submit(FaultSubmitReqDTO reqDTO) {
        List<FaultAnalyzeDO> list = mapper.getFaultAnalysisList(reqDTO.getFaultAnalysisNo(), reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        if (CollectionUtil.isEmpty(list)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<FaultDetailResDTO> dmfm01List = faultQueryMapper.list(FaultQueryDetailReqDTO.builder().faultNo(reqDTO.getFaultNo()).faultWorkNo(reqDTO.getFaultWorkNo()).build());
        if (CollectionUtil.isEmpty(dmfm01List)) {
            log.info("dmfm01List is empty!");
            return;
        }
        List<FlowRes> taskList = null;
        try {
            taskList = bpmnService.queryFlowList(null, null);
        } catch (Exception e) {
            log.error("获取任务列表失败");
        }
        FaultAnalyzeDO dmfm03 = list.get(0);
        String majorCode = dmfm03.getMajorCode();
        Dictionaries dictionaries = dictionaryService.queryOneByItemCodeAndCodesetCode("dm.vehicleSpecialty", "01");
        String itemEname = dictionaries.getItemEname();
        List<String> cos = Arrays.asList(itemEname.split(","));
        if (dmfm03.getWorkFlowInstId().trim().isEmpty() && reqDTO.getType() == SubmitType.COMMIT) {
            // EiInfo eiInfo = new EiInfo();
            // eiInfo.set("processKey", "DMFM01");
            // eiInfo.set(EiConstant.serviceId, "S_EW_38");
            // EiInfo out = XServiceManager.call(eiInfo);
            // List<Map> processList = (List) out.get("processKey");
            // JSONArray userJson = null;
            List<Map<String, String>> processList = Lists.newArrayList();
            Map<String, Object> variables = new HashMap<>();
            for (int i = 0; i < processList.size(); i++) {
                Map processMap = processList.get(i);
                String nodeKey = (String) processMap.get("nodeKey");
                if ("A30".equals(nodeKey) && cos.contains(majorCode)) {
                    String cocode = "ZC";
                    // userJson = JSONArray.fromObject(processMap.get("userList"));
                    variables.put("CO_CODE", cocode);
                    break;
                }
                if ("A60".equals(nodeKey) && !cos.contains(majorCode)) {
                    String cocode = "ZTT";
                    // userJson = JSONArray.fromObject(processMap.get("userList"));
                    variables.put("CO_CODE", cocode);
                    break;
                }
            }
            List<String> userCode = new ArrayList();
            // for (int j = 0; j < userJson.size(); j++) {
            //     userCode.add(((JSONObject) userJson.get(j)).get("userId"));
            // }
            List<PersonResDTO> nextUser = new ArrayList<>();
            if ("ZC".equals(variables.get("CO_CODE"))) {
                nextUser = userHelperMapper.getUserByGroup("DM_010");
                for (PersonResDTO user : nextUser) {
                    user.setUserId(user.getLoginName());
                }
            } else if ("ZTT".equals(variables.get("CO_CODE"))) {
                nextUser = faultQueryService.queryUserList(new HashSet<>(userCode), dmfm03.getWorkClass());
            }
            if (CollectionUtil.isEmpty(nextUser)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "下一步参与者不存在");
            }
            String processId = null;
            // String processId = WorkflowHelper.start("DMFM01", dmfm03.getWorkFlowInstId(), userId, dmfm03.getFaultAnalysisNo(), nextUser, variables);
            dmfm03.setWorkFlowInstStatus("提交审核");
            // if (processId == null || "-1".equals(processId)) {
            //     submtStatus = -1;
            // }
            dmfm03.setWorkFlowInstId(processId);
            String stuts = bpmnService.nextTaskKey(processId);
            if (stuts == null) {
                log.error("送审失败");
            }
            dmfm03.setWorkFlowInstStatus(stuts);
            dmfm03.setRecStatus("20");
        } else if (reqDTO.getType() == SubmitType.PASS && !dmfm03.getWorkFlowInstId().trim().isEmpty()) {

            String processId = dmfm03.getWorkFlowInstId();
            if (CollectionUtil.isEmpty(taskList)) {
                log.error("您无权审核!");
            } else {
                FlowRes flowRes = taskList.get(0);
                String taskId = flowRes.getDefId();
                String taskDefKey = flowRes.getDefKey();
                if (!"A50".equals(taskDefKey)) {
                    // EiInfo eiInfo = new EiInfo();
                    // eiInfo.set("userId", userId);
                    // eiInfo.set("taskId", taskId);
                    // eiInfo.set(EiConstant.serviceId, "S_EW_28");
                    // EiInfo out = XServiceManager.call(eiInfo);
                    // List<Map> processList = (List) out.get("result");
                    // JSONArray userJson = null;
                    // userJson = JSONArray.fromObject(((Map) processList.get(0)).get("userList"));
                    List<String> userCode = new ArrayList();
                    // for (int i = 0; i < userJson.size(); i++) {
                    //     userCode.add(((JSONObject) userJson.get(i)).get("userId"));
                    // }
                    Map<Object, Object> orgMap = new HashMap<>();
                    orgMap.put("userCode", userCode.toArray());
                    if ("A30".equals(taskDefKey)) {
                        String groupName = "DM_005";
                        List<PersonResDTO> nextUser = userHelperMapper.getUserByGroup(groupName);
                        for (PersonResDTO user : nextUser) {
                            user.setUserId(user.getLoginName());
                        }
                        if (CollectionUtil.isEmpty(userCode)) {
                            log.error("下一步参与者不存在");
                        }
                        // submtStatus = WorkflowHelper.submit(taskId, userId, comment, "", nextUser, null);
                        dmfm03.setRecStatus("20");
                        dmfm03.setWorkFlowInstStatus("A50");
                    } else if ("A40".equals(taskDefKey)) {
                        String orgCode = dmfm01List.get(0).getRespDeptCode();
                        String[] ates = {"DM_005", "DM_004"};
                        List<String> group = Arrays.asList(ates);
                        List<PersonResDTO> nextUser = null;
                        for (int j = 0; j < group.size(); j++) {
                            nextUser = userHelperMapper.getUserByGroupNameAndOrg(group.get(j), orgCode);
                            if (nextUser != null) {
                                break;
                            }
                        }
                        if (CollectionUtil.isEmpty(userCode)) {
                            log.error("下一步参与者不存在");
                        }
                        // submtStatus = WorkflowHelper.submit(taskId, userId, comment, "", nextUser, null);
                        dmfm03.setRecStatus("20");
                        dmfm03.setWorkFlowInstStatus("A50");
                    } else if ("A60".equals(taskDefKey)) {
                        String orgCode = dmfm03.getWorkClass();
                        String groupName = "DM_026";
                        List<PersonResDTO> nextUser = userHelperMapper.getUserByGroupNameAndOrg(groupName, orgCode);
                        if (CollectionUtil.isEmpty(nextUser)) {
                            log.error("下一步参与者不存在");
                        }
                        // submtStatus = WorkflowHelper.submit(taskId, userId, comment, "", nextUser, null);
                        dmfm03.setRecStatus("20");
                        dmfm03.setWorkFlowInstStatus("A70");
                    } else if ("A70".equals(taskDefKey)) {
                        Dictionaries orgCode = dictionaryService.queryOneByItemCodeAndCodesetCode("dm.matchControl", "06");
                        String itemEname1 = orgCode.getItemEname();
                        String groupName = "DM_028";
                        List<PersonResDTO> nextUser = userHelperMapper.getUserByGroupNameAndOrg(groupName, itemEname1);
                        if (CollectionUtil.isEmpty(nextUser)) {
                            throw new CommonException(ErrorCode.NORMAL_ERROR, "下一步参与者不存在");
                        }
                        // submtStatus = WorkflowHelper.submit(taskId, userId, comment, "", nextUser, null);
                        dmfm03.setRecStatus("20");
                        dmfm03.setWorkFlowInstStatus("A80");
                    } else if ("A80".equals(taskDefKey)) {
                        String orgCode = dmfm01List.get(0).getRespDeptCode();
                        String[] ates = {"DM_005", "DM_004"};
                        List<String> group = Arrays.asList(ates);
                        List nextUser = null;
                        for (int j = 0; j < group.size(); j++) {
                            nextUser = userHelperMapper.getUserByGroupNameAndOrg(group.get(j), orgCode);
                            if (nextUser != null) {
                                break;
                            }
                        }
                        if (CollectionUtil.isEmpty(nextUser)) {
                            throw new CommonException(ErrorCode.NORMAL_ERROR, "下一步参与者不存在");
                        }
                        dmfm03.setRecStatus("20");
                        dmfm03.setWorkFlowInstStatus("A50");
                    } else {
                        orgMap.put("orgCode", dmfm03.getWorkClass());

                        dmfm03.setRecStatus("30");
                        dmfm03.setWorkFlowInstStatus("A50");
                    }
                } else {
                    // if ("yes".equals(condition)) {
                    //     // EiInfo eiInfo = new EiInfo();
                    //     // eiInfo.set("userId", userId);
                    //     // eiInfo.set("taskId", taskId);
                    //     // eiInfo.set(EiConstant.serviceId, "S_EW_28");
                    //     // EiInfo out = XServiceManager.call(eiInfo);
                    //     // List<Map> processList = (List) out.get("result");
                    //     // JSONArray userJson = JSONArray.fromObject(((Map) processList.get(0)).get("userList"));
                    //     List<Object> userCode = new ArrayList();
                    //     // for (int i = 0; i < userJson.size(); i++) {
                    //     //     userCode.add(((JSONObject) userJson.get(i)).get("userId"));
                    //     // }
                    //     Map<Object, Object> orgMap = new HashMap<>();
                    //     orgMap.put("userCode", userCode.toArray());
                    //     String respDeptCode = dmfm01List.get(0).getRespDeptCode();
                    //     String groupName = "DM_029";
                    //     List<PersonResDTO> nextUser = userHelperMapper.getUserByGroupNameAndOrg(groupName, respDeptCode);
                    //     if (CollectionUtil.isEmpty(nextUser)) {
                    //         throw new CommonException(ErrorCode.NORMAL_ERROR, "下一步参与者不存在");
                    //         return;
                    //     }
                    //     dmfm03.setRecStatus("20");
                    //     dmfm03.setWorkFlowInstStatus("End");
                    // } else {
                    //     dmfm03.setRecStatus("30");
                    //     dmfm03.setWorkFlowInstStatus("End");
                }
            }
        } else if (reqDTO.getType() == SubmitType.COMMIT) {
            String processId = dmfm03.getWorkFlowInstId();
            // FlowRes flowRes = taskList.get(0);
            // String taskId = (String) task.get("id");
            //     String taskDefKey = (String) task.get("taskDefKey");
            // EiInfo eiInfo = new EiInfo();
            // eiInfo.set("userId", userId);
            // eiInfo.set("taskId", taskId);
            // eiInfo.set(EiConstant.serviceId, "S_EW_28");
            // EiInfo out = XServiceManager.call(eiInfo);
            // List<Map> processList = (List) out.get("result");
            JSONArray userJson = null;
            Map<String, Object> variables = new HashMap<>();
            // for (int i = 0; i < processList.size(); i++) {
            //     Map processMap = processList.get(i);
            //     String nodeKey = (String) processMap.get("nodeKey");
            //     if ("A30".equals(nodeKey) && cos.contains(majorCode)) {
            //         String cocode = "ZC";
            //         // userJson = JSONArray.fromObject(processMap.get("userList"));
            //         variables.put("CO_CODE", cocode);
            //         break;
            //     }
            //     if ("A60".equals(nodeKey) && !cos.contains(majorCode)) {
            //         String cocode = "ZTT";
            //         // userJson = JSONArray.fromObject(processMap.get("userList"));
            //         variables.put("CO_CODE", cocode);
            //         break;
            //     }
            // }
            List<String> userCode = new ArrayList();
            // for (int j = 0; j < userJson.size(); j++) {
            //     userCode.add(((JSONObject) userJson.get(j)).get("userId"));
            // }
            Set<String> userSet = new HashSet<>(userCode);
            List<PersonResDTO> nextUser = new ArrayList<>();
            if ("ZC".equals(variables.get("CO_CODE"))) {
                nextUser = userHelperMapper.getUserByGroup("DM_010");
                for (PersonResDTO user : nextUser) {
                    user.setUserId(user.getLoginName());
                }
            } else if ("ZTT".equals(variables.get("CO_CODE"))) {
                nextUser = faultQueryService.queryUserList(userSet, dmfm03.getWorkClass());
            }
            if (CollectionUtil.isEmpty(nextUser)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "下一步参与者不存在");
            }
            dmfm03.setRecStatus("20");
            dmfm03.setWorkFlowInstStatus("提交审核");
        } else {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "\"当前流程处于开始阶段，无法直接审核通过\"");
        }
        try {
            bpmnService.commit(dmfm03.getFaultAnalysisNo(), BpmnFlowEnum.FAULT_ANALIZE.value(), null, null);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败");
        }
        mapper.update(dmfm03);
    }

}
