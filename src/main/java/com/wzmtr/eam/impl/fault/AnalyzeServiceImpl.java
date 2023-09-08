package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wzmtr.eam.dto.req.fault.AnalyzeReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultAnalyzeDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultSubmitReqDTO;
import com.wzmtr.eam.dto.res.PersonResDTO;
import com.wzmtr.eam.dto.res.fault.AnalyzeResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.SubmitType;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.fault.AnalyzeMapper;
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
        List<AnalyzeResDTO> list = mapper.getFaultAnalysisList(reqDTO.getFaultAnalysisNo(), reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        if (CollectionUtil.isEmpty(list)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        AnalyzeResDTO dmfm03 = list.get(0);
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
            List<Map<String,String>> processList = Lists.newArrayList();
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
                // nextUser = InterfaceHelper.getUserHelpe().getUserByGroup("DM_010");
                for (PersonResDTO personResDTO : nextUser) {
                    personResDTO.setUserId(personResDTO.getLoginName());
                }
            } else if ("ZTT".equals(variables.get("CO_CODE"))) {
                nextUser = faultQueryService.queryUserList(new HashSet<>(userCode),dmfm03.getWorkClass());
            }
            if (nextUser.size() == 0) {
                throw new CommonException(ErrorCode.NORMAL_ERROR,"下一步参与者不存在");
            }
            String processId = null;
            try {
                processId = bpmnService.commit(dmfm03.getFaultAnalysisNo(), BpmnFlowEnum.FAULT_ANALIZE.value(), null, null);
            } catch (Exception e) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败");
            }
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
        }
        return;
    }

}
