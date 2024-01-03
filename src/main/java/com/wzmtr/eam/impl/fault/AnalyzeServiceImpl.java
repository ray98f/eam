package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wzmtr.eam.bizobject.export.FaultAnalizeExportBO;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.Cols;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultAnalyzeDO;
import com.wzmtr.eam.dto.req.bpmn.ExamineReqDTO;
import com.wzmtr.eam.dto.req.fault.AnalyzeReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultAnalyzeDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultExamineReqDTO;
import com.wzmtr.eam.dto.res.common.FlowRoleResDTO;
import com.wzmtr.eam.dto.res.fault.AnalyzeResDTO;
import com.wzmtr.eam.enums.*;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.fault.FaultAnalyzeMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.service.fault.AnalyzeService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

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
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private IDictionariesService dictService;

    @Override
    public Page<AnalyzeResDTO> list(AnalyzeReqDTO reqDTO) {
        Page<AnalyzeResDTO> query = faultAnalyzeMapper.query(reqDTO.of(), reqDTO.getFaultNo(), reqDTO.getMajorCode(), reqDTO.getRecStatus(), reqDTO.getLineCode(), reqDTO.getFrequency(), reqDTO.getPositionCode(), reqDTO.getDiscoveryStartTime(), reqDTO.getDiscoveryEndTime(), reqDTO.getRespDeptCode(), reqDTO.getAffectCodes());
        List<AnalyzeResDTO> records = query.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new Page<>();
        }
        records.forEach(a -> {
                    a.setRespDeptName(organizationMapper.getNamesById(a.getRespDeptCode()));
                    if (StringUtils.isNotEmpty(a.getDocId())) {
                        a.setDocFile(fileMapper.selectFileInfo(Arrays.asList(a.getDocId().split(CommonConstants.COMMA))));
                    }
                }
        );
        return query;
    }

    @Override
    public void export(String faultAnalysisNo, String faultNo, String faultWorkNo, HttpServletResponse response) {
        List<AnalyzeResDTO> resList = faultAnalyzeMapper.list(faultAnalysisNo, faultNo, faultWorkNo);
        if (CollectionUtil.isEmpty(resList)) {
            return;
        }
        List<FaultAnalizeExportBO> exportList = Lists.newArrayList();
        resList.forEach(item -> {
                    LineCode lineCode = LineCode.getByCode(item.getLineCode());
                    FaultFrequency frequency = FaultFrequency.getByCode(item.getFrequency());
                    String respDeptName = organizationMapper.getOrgById(item.getRespDeptCode());
                    FaultAnalizeExportBO exportBO = BeanUtils.convert(item, FaultAnalizeExportBO.class);
                    exportBO.setLineCode(lineCode != null ? lineCode.getDesc() : item.getLineCode());
                    if (StringUtils.isNotEmpty(item.getFaultLevel())){
                        exportBO.setFaultLevel(dictService.queryOneByItemCodeAndCodesetCode("dm.faultLevel", item.getFaultLevel()).getItemCname());
                    }
                    exportBO.setFrequency(frequency != null ? frequency.getDesc() : item.getFrequency());
                    exportBO.setRespDeptName(respDeptName == null ? CommonConstants.EMPTY : respDeptName);
                    exportList.add(exportBO);
                }
        );
        try {
            EasyExcelUtils.export(response, "故障调查及处置情况", exportList);
        } catch (Exception e) {
            log.error("导出失败", e);
            throw new CommonException(ErrorCode.NORMAL_ERROR);
        }
    }

    @Override
    public AnalyzeResDTO detail(FaultAnalyzeDetailReqDTO reqDTO) {
        AnalyzeResDTO detail = faultAnalyzeMapper.detail(reqDTO);
        if (detail == null) {
            return null;
        }
        String respDeptCode = detail.getRespDeptCode();
        if (StringUtils.isNotEmpty(respDeptCode)) {
            detail.setRespDeptName(organizationMapper.getNamesById(respDeptCode));
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
                // 2 技术主管流程
                String flag = FaultAnalizeFlow.FAULT_ANALIZE_TECHNICAL_LEAD.getCode().equals(submitNodeId)
                        ? CommonConstants.TWO_STRING : CommonConstants.ONE_STRING;
                processId = bpmnService.commit(faultAnalysisNo, bpmnFlow, "{\"id\":\"" + faultAnalysisNo + "\",\"flag\":\"" + flag + "\"}", null, examineReqDTO.getUserIds(), submitNodeId);
                dmfm03.setWorkFlowInstStatus(submitNodeId);
                dmfm03.setWorkFlowInstId(processId);
                dmfm03.setExt5(reqDTO.getLine());
                dmfm03.setRecStatus("20");
            } catch (Exception e) {
                log.error("故障分析流程提交错误，分析单号为-[{}]", faultAnalysisNo, e);
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
        FaultAnalyzeDO faultAnalyzeDO = faultAnalyzeMapper.selectOne(new QueryWrapper<FaultAnalyzeDO>().eq(Cols.FAULT_ANALIZE_NO, faultAnalysisNo));
        workFlowLogService.ifReviewer(faultAnalyzeDO.getWorkFlowInstId());
        String taskId = bpmnService.queryTaskIdByProcId(faultAnalyzeDO.getWorkFlowInstId());
        AnalyzeServiceImpl aop = (AnalyzeServiceImpl) AopContext.currentProxy();
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
        try {
            if (roleMapper.getNodeIdsByFlowId(BpmnFlowEnum.FAULT_ANALIZE.value()).contains(faultAnalyzeDO.getWorkFlowInstStatus())) {
                // 提交部长审核指定下一流程
                String reviewOrNot = null;
                String flag = null;
                if (null != reqDTO.getReviewOrNot()) {
                    // 需要部长审核时 flag固定为0
                    if (reqDTO.getReviewOrNot()) {
                        reviewOrNot = FaultAnalizeFlow.FAULT_ANALIZE_REVIEW_NODE.getCode();
                        flag = CommonConstants.ZERO_STRING;
                    } else {
                        // 不需要部长审核 直接变更为结束状态
                        faultAnalyzeDO.setRecStatus("30");
                        reviewOrNot = FaultAnalizeFlow.FAULT_ANALIZE_END_NODE.getCode();
                        flag = CommonConstants.ONE_STRING;
                    }
                }
                // 当前审核完需要获取下一步
                FlowRoleResDTO nextNode = bpmnService.getNextNode(BpmnFlowEnum.FAULT_ANALIZE.value(), faultAnalyzeDO.getWorkFlowInstStatus(), faultAnalyzeDO.getExt5());
                String nextNodeId = nextNode.getNodeId();
                // 下一步为结束节点时，变更状态
                if (nextNodeId.equals(FaultAnalizeFlow.FAULT_ANALIZE_END_NODE.getCode())) {
                    faultAnalyzeDO.setRecStatus("30");
                }
                bpmnService.agree(taskId, reqDTO.getExamineReqDTO().getOpinion(), String.join(",", reqDTO.getExamineReqDTO().getUserIds()), "{\"id\":\"" + faultAnalyzeDO.getFaultAnalysisNo() + "\",\"review\":\"" + flag + "\"}", reviewOrNot);
                faultAnalyzeDO.setWorkFlowInstStatus(nextNodeId);
                // 保存当前属于哪条流程线
                faultAnalyzeDO.setExt5(nextNode.getLine());
            }
            faultAnalyzeDO.setRecReviseTime(DateUtil.getCurrentTime());
            faultAnalyzeDO.setRecRevisor(TokenUtil.getCurrentPersonId());
            faultAnalyzeMapper.update(faultAnalyzeDO);
        } catch (Exception e) {
            log.error("pass error", e);
            throw new CommonException(ErrorCode.NORMAL_ERROR, "pass error");
        }
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
        workFlowLogService.ifReviewer(processId);
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
