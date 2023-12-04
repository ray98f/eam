package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultAnalyzeDO;
import com.wzmtr.eam.dto.req.bpmn.ExamineReqDTO;
import com.wzmtr.eam.dto.req.fault.AnalyzeReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultAnalyzeDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultExamineReqDTO;
import com.wzmtr.eam.dto.res.fault.AnalyzeResDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.BpmnStatus;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.fault.FaultAnalyzeMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.service.fault.AnalyzeService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private FaultAnalyzeMapper faultAnalyzeMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private BpmnService bpmnService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private IWorkFlowLogService workFlowLogService;

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
        String faultAnalysisNo = Assert.notNull(reqDTO.getFaultAnalysisNo(), "faultAnalysisNo can not be null!");
        ExamineReqDTO examineReqDTO = Assert.notNull(reqDTO.getExamineReqDTO(), "ExamineReqDTO can not be null");
        // com.baosight.wzplat.dm.fm.service.ServiceDMFM0008#submit
        List<FaultAnalyzeDO> list = faultAnalyzeMapper.getFaultAnalysisList(faultAnalysisNo, reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        FaultAnalyzeDO dmfm03 = list.get(0);
        String processId = null;
        String bpmnFlow = BpmnFlowEnum.FAULT_ANALIZE.value();
        if (StringUtils.isEmpty(dmfm03.getWorkFlowInstId())) {
            try {
                String submitNodeId = roleMapper.getSubmitNodeId(bpmnFlow, examineReqDTO.getRoleId());
                processId = bpmnService.commit(faultAnalysisNo, bpmnFlow, null, null, examineReqDTO.getUserIds(), submitNodeId);
                dmfm03.setWorkFlowInstStatus(submitNodeId);
                dmfm03.setWorkFlowInstId(processId);
                dmfm03.setRecStatus("20");
            } catch (Exception e) {
                log.error("故障分析流程提交错误，分析单号为-[{}]", faultAnalysisNo);
            }
        }
        // 流程日志记录
        workFlowLogService.add(WorkFlowLogBO.builder()
                .status(BpmnStatus.SUBMIT.getDesc())
                .userIds(reqDTO.getExamineReqDTO().getUserIds())
                .workFlowInstId(processId)
                .build());
        faultAnalyzeMapper.update(dmfm03);
    }

    @Override
    public void pass(FaultExamineReqDTO reqDTO) {
        String faultAnalysisNo = Assert.notNull(reqDTO.getFaultAnalysisNo(), "faultAnalysisNo can not be null!");
        FaultAnalyzeDO faultAnalyzeDO = faultAnalyzeMapper.selectOne(new QueryWrapper<FaultAnalyzeDO>().eq("FAULT_ANALYSIS_NO", faultAnalysisNo));
        String taskId = bpmnService.queryTaskIdByProcId(faultAnalyzeDO.getWorkFlowInstId());
        AnalyzeServiceImpl aop = (AnalyzeServiceImpl) AopContext.currentProxy();
        ;
        aop._agree(reqDTO, faultAnalyzeDO, taskId);
        // 流程流转日志记录
        workFlowLogService.add(WorkFlowLogBO.builder()
                .status(BpmnStatus.PASS.getDesc())
                .userIds(reqDTO.getExamineReqDTO().getUserIds())
                .workFlowInstId(faultAnalyzeDO.getWorkFlowInstId())
                .build());
    }

    @Transactional(rollbackFor = Exception.class)
    public void _agree(FaultExamineReqDTO reqDTO, FaultAnalyzeDO faultAnalyzeDO, String taskId) {
        if (roleMapper.getNodeIdsByFlowId(BpmnFlowEnum.FAULT_ANALIZE.value()).contains(faultAnalyzeDO.getWorkFlowInstStatus())) {
            // 提交部长审核指定下一流程
            String reviewOrNot = null;
            if (reqDTO.getReviewOrNot() != null && reqDTO.getReviewOrNot()) {
                reviewOrNot = CommonConstants.FAULT_ANALIZE_REVIEW_NODE;
            }
            bpmnService.agree(taskId, reqDTO.getExamineReqDTO().getOpinion(), String.join(",", reqDTO.getExamineReqDTO().getUserIds()), "{\"id\":\"" + faultAnalyzeDO.getFaultAnalysisNo() + "\"}", reviewOrNot);
            faultAnalyzeDO.setWorkFlowInstStatus(bpmnService.getNextNodeId(BpmnFlowEnum.FAULT_ANALIZE.value(), faultAnalyzeDO.getWorkFlowInstStatus()));
        }
        faultAnalyzeDO.setRecReviseTime(DateUtils.getTime());
        faultAnalyzeDO.setRecRevisor(TokenUtil.getCurrentPersonId());
        // 如果下一步没有了 再去更新
        if (StringUtils.isEmpty(bpmnService.nextTaskKey(taskId))) {
            faultAnalyzeDO.setRecStatus("30");
        }
        faultAnalyzeMapper.update(faultAnalyzeDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(FaultExamineReqDTO reqDTO) {
        String faultNo = reqDTO.getFaultNo();
        String faultWorkNo = reqDTO.getFaultWorkNo();
        String checkFaultAnalysisNo = reqDTO.getFaultAnalysisNo();
        String backOpinion = reqDTO.getExamineReqDTO().getOpinion();
        FaultAnalyzeDO dmfm03 = faultAnalyzeMapper.selectOne(new QueryWrapper<FaultAnalyzeDO>().eq("FAULT_NO", faultNo).eq("FAULT_WORK_NO", faultWorkNo).eq("FAULT_ANALYSIS_NO", checkFaultAnalysisNo));
        String processId = dmfm03.getWorkFlowInstId();
        String taskId = bpmnService.queryTaskIdByProcId(processId);
        bpmnService.reject(taskId, backOpinion);
        dmfm03.setRecStatus("10");
        dmfm03.setWorkFlowInstStatus("");
        faultAnalyzeMapper.update(dmfm03);
        workFlowLogService.add(WorkFlowLogBO.builder()
                .status(BpmnStatus.REJECT.getDesc())
                .userIds(reqDTO.getExamineReqDTO().getUserIds())
                .workFlowInstId(dmfm03.getWorkFlowInstId())
                .build());
    }
}
