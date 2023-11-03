package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dataobject.FaultAnalyzeDO;
import com.wzmtr.eam.dto.req.fault.AnalyzeReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultAnalyzeDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultExamineReqDTO;
import com.wzmtr.eam.dto.res.bpmn.FlowRes;
import com.wzmtr.eam.dto.res.common.PersonResDTO;
import com.wzmtr.eam.dto.res.fault.AnalyzeResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.fault.FaultAnalyzeMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.service.fault.AnalyzeService;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.service.user.UserHelperService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: Li.Wang
 * Date: 2023/8/14 16:31
 */
@Service
@Slf4j
public class AnalyzeServiceImpl implements AnalyzeService {
    @Autowired
    private FaultAnalyzeMapper faultAnalyzeMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private IDictionariesService dictionaryService;
    @Autowired
    private FaultQueryService faultQueryService;
    @Autowired
    private BpmnService bpmnService;
    @Autowired
    private UserHelperService userHelperService;
    @Autowired
    private FaultQueryMapper faultQueryMapper;

    @Override
    public Page<AnalyzeResDTO> list(AnalyzeReqDTO reqDTO) {
        Page<AnalyzeResDTO> query = faultAnalyzeMapper.query(reqDTO.of(), reqDTO.getFaultNo(), reqDTO.getMajorCode(), reqDTO.getRecStatus(), reqDTO.getLineCode(), reqDTO.getFrequency(), reqDTO.getPositionCode(), reqDTO.getDiscoveryStartTime(), reqDTO.getDiscoveryEndTime(), reqDTO.getRespDeptCode(), reqDTO.getAffectCodes());
        List<AnalyzeResDTO> records = query.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new Page<>();
        }
        records.forEach(a -> a.setRespDeptName(organizationMapper.getNamesById(a.getRespDeptCode())));
        return query;
    }

    @Override
    public void export(String faultAnalysisNo, String faultNo, String faultWorkNo, HttpServletResponse response) {
        List<AnalyzeResDTO> resList = faultAnalyzeMapper.list(faultAnalysisNo, faultNo, faultWorkNo);
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
        AnalyzeResDTO detail = faultAnalyzeMapper.detail(reqDTO);
        if (detail == null) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        return detail;
    }

    @Override
    public void submit(FaultExamineReqDTO reqDTO) {
        if (CollectionUtil.isEmpty(reqDTO.getUserIds())) {
            throw new CommonException(ErrorCode.PARAM_ERROR, "下一步参与者不存在");
        }
        // com.baosight.wzplat.dm.fm.service.ServiceDMFM0008#submit
        List<FaultAnalyzeDO> list = faultAnalyzeMapper.getFaultAnalysisList(reqDTO.getFaultAnalysisNo(), reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        FaultAnalyzeDO dmfm03 = list.get(0);
        if (StringUtils.isEmpty(dmfm03.getWorkFlowInstId())) {
            String processId = null;
            try {
                processId = bpmnService.commit(reqDTO.getFaultAnalysisNo(), BpmnFlowEnum.FAULT_ANALIZE.value(), null, null, reqDTO.getUserIds());
                dmfm03.setWorkFlowInstStatus("提交审核");
                dmfm03.setWorkFlowInstId(processId);
                dmfm03.setRecStatus("20");
            } catch (Exception e) {
                log.error("开始流程错误",e);
            }
        }
        // 送审
        else {
            try {
                bpmnService.commit(dmfm03.getFaultAnalysisNo(), BpmnFlowEnum.FAULT_ANALIZE.value(), null, null, null);
                dmfm03.setRecStatus("20");
                dmfm03.setWorkFlowInstStatus("提交审核");
            } catch (Exception e) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败");
            }
        }
        faultAnalyzeMapper.update(dmfm03);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(FaultExamineReqDTO reqDTO) {
        String faultAnalysisNo = reqDTO.getFaultAnalysisNo();
        FaultAnalyzeDO faultAnalyzeDO = faultAnalyzeMapper.selectOne(new QueryWrapper<FaultAnalyzeDO>().eq("FAULT_ANALYSIS_NO", faultAnalysisNo));
        String taskId = bpmnService.queryTaskIdByProcId(faultAnalyzeDO.getWorkFlowInstId());
        bpmnService.agree(taskId, reqDTO.getOpinion(), null, null);
        faultAnalyzeDO.setRecStatus("30");
        faultAnalyzeDO.setRecReviseTime(DateUtils.getTime());
        faultAnalyzeDO.setWorkFlowInstStatus("End");
        faultAnalyzeDO.setRecRevisor(TokenUtil.getCurrentPersonId());
        faultAnalyzeMapper.update(faultAnalyzeDO);
    }
}
