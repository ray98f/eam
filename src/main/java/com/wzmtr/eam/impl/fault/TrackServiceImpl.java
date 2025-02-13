package com.wzmtr.eam.impl.fault;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.common.collect.Lists;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.bizobject.export.FaultTrackWorkExportBO;
import com.wzmtr.eam.constant.ColsConstants;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dataobject.FaultTrackDO;
import com.wzmtr.eam.dto.req.bpmn.ExamineReqDTO;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.common.FlowRoleResDTO;
import com.wzmtr.eam.dto.res.fault.TrackResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.enums.*;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultTrackMapper;
import com.wzmtr.eam.mapper.fault.FaultTrackWorkMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.service.fault.TrackService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Author: Li.Wang
 * Date: 2023/8/10 9:49
 */
@Service
@Slf4j
public class TrackServiceImpl implements TrackService {
    @Autowired
    private FaultTrackMapper faultTrackMapper;
    @Autowired
    private FaultTrackWorkMapper faultTrackWorkMapper;
    @Autowired
    private BpmnService bpmnService;
    @Autowired
    private FaultQueryMapper faultQueryMapper;
    @Autowired
    private OverTodoService overTodoService;
    @Autowired
    private IDictionariesService dictService;
    @Autowired
    private IWorkFlowLogService workFlowLogService;
    @Autowired
    private RoleMapper roleMapper;
    private static final List<String> IGNORE_STATE = Arrays.asList("10", "20", "30");

    @Override
    public Page<TrackResDTO> list(TrackReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<TrackResDTO> list = faultTrackWorkMapper.query(reqDTO.of(), reqDTO.getFaultTrackNo(), reqDTO.getFaultTrackWorkNo(), reqDTO.getRecStatus(), reqDTO.getEquipTypeCode(), reqDTO.getMajorCode(), reqDTO.getObjectName(), reqDTO.getObjectCode(), reqDTO.getSystemCode());
        if (StringUtils.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        return list;
    }

    @Override
    public TrackResDTO detail(SidEntity reqDTO) {
        return faultTrackWorkMapper.detail(reqDTO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void report(TrackReportReqDTO reqDTO) {
        // EAM/service/DMFM0011/ReportRow
        reqDTO.setTrackReportTime(DateUtils.getCurrentTime());
        reqDTO.setTrackReporterId(TokenUtils.getCurrentPersonId());
        reqDTO.setRecStatus("30");
        faultTrackWorkMapper.report(reqDTO);
    }

    @Override
    public void close(TrackCloseReqDTO reqDTO) {
        reqDTO.setTrackCloseTime(DateUtils.getCurrentTime());
        reqDTO.setTrackCloserId(TokenUtils.getCurrentPersonId());
        reqDTO.setRecStatus("40");
//        DMUtil.overTODO((String)((Map)dm03List.get(0)).get("recId"), "关闭");
        faultTrackWorkMapper.close(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repair(TrackRepairReqDTO reqDTO) {
//        dmfm22.setWorkerGroupCode(repairDeptCode);
        reqDTO.setDispatchUserId(TokenUtils.getCurrentPersonId());
        reqDTO.setDispatchTime(DateUtils.getCurrentTime());
        overTodoService.overTodo(reqDTO.getRecId(), "派工完毕", CommonConstants.ONE_STRING);
        reqDTO.setRecStatus("20");
        faultTrackWorkMapper.repair(reqDTO);
    }

    @Override
    public void transmit(TrackTransmitReqDTO reqDTO) {
        FaultOrderDO res = faultQueryMapper.queryOneFaultOrder(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        if (res == null) {
            log.error("未查询到相关数据!");
            return;
        }
        String workFlowInstId;
        if (StringUtils.isNotEmpty(res.getWorkFlowInstId())) {
            workFlowInstId = res.getWorkFlowInstId();
        } else {
            workFlowInstId = res.getRecId() + "_" + res.getFaultWorkNo();
        }
        overTodoService.overTodo(workFlowInstId, "跟踪工单", CommonConstants.ONE_STRING);
        FaultTrackDO bo = BeanUtils.convert(res, FaultTrackDO.class);
        bo.setFaultTrackNo(reqDTO.getFaultTrackNo());
        bo.setRecStatus("20");
        bo.setExt1(workFlowInstId);
        // todo 发短信
        // String stepUserGroup = "DM_013";
        // EiInfo conditionInfo1 = new EiInfo();
        // String content1 = "工班人员跟踪观察，跟踪工单号：" + faultTrackNo + "，请关注。";
        // conditionInfo1.set("groupName", stepUserGroup);
        // conditionInfo1.set("content", content1);
        // conditionInfo1.set("orgCode", dmfm02.getWorkClass());
        // conditionInfo1.set("faultWorkNo", faultWorkNo);
        // ISendMessage.senMessageByGroupAndOrgCode(conditionInfo1);
        // 更新故障跟踪表
        faultTrackMapper.transmit(bo);
    }

    @Override
    public void export(TrackExportReqDTO reqDTO, HttpServletResponse response) throws IOException {
        List<TrackResDTO> resList = faultTrackWorkMapper.export(reqDTO);
        if (StringUtils.isEmpty(resList)) {
            return;
        }
        List<FaultTrackWorkExportBO> exportList = Lists.newArrayList();
        resList.forEach(a -> {
            Dictionaries dictionaries = dictService.queryOneByItemCodeAndCodesetCode("dm.faultTrackWorkStatus", a.getRecStatus());
            FaultTrackWorkExportBO exportBO = BeanUtils.convert(a, FaultTrackWorkExportBO.class);
            exportBO.setTrackStatus(dictionaries.getItemCname());
            exportList.add(exportBO);
        });
        EasyExcelUtils.export(response, "跟踪查询信息", exportList);
    }

    @Override
    public void commit(FaultExamineReqDTO reqDTO) {
        ExamineReqDTO examineReqDTO = Assert.notNull(reqDTO.getExamineReqDTO(), "下一步参与者不能为空！");
        // faultTrackDO.query  com.baosight.wzplat.dm.fm.service.ServiceDMFM0010#submit
        List<FaultTrackDO> dmfm9List = faultTrackMapper.queryList(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo(), reqDTO.getFaultAnalysisNo(), reqDTO.getFaultTrackNo());
        Assert.notEmpty(dmfm9List,ErrorCode.RESOURCE_NOT_EXIST);
        FaultTrackDO faultTrackDO = dmfm9List.get(0);
        //判断当前状态是否已是审核中，防止重复提交
        Assert.isFalse("40".equals(faultTrackDO.getRecStatus()),"当前状态已变更，不允许重复提交!");
        String processId;
        // dmfm01.query
        TrackServiceImpl aop = (TrackServiceImpl) AopContext.currentProxy();
        processId = aop.commit(reqDTO, faultTrackDO, examineReqDTO, null);
        // 记录日志
        workFlowLogService.add(WorkFlowLogBO.builder()
                .status(BpmnStatus.SUBMIT.getDesc())
                .userIds(examineReqDTO.getUserIds())
                .workFlowInstId(processId)
                .build());
    }
    @Transactional(rollbackFor = Exception.class)
    public String commit(FaultExamineReqDTO reqDTO, FaultTrackDO faultTrackDO, ExamineReqDTO examineReqDTO, String processId) {
        if (StringUtils.isEmpty(faultTrackDO.getWorkFlowInstId())) {
            String faultTrackNo = faultTrackDO.getFaultTrackNo();
            try {
                // 根据前端选择的roleId获取下一步的流程走向
                String submitNodeId = roleMapper.getSubmitNodeId(BpmnFlowEnum.FAULT_TRACK.value(), examineReqDTO.getRoleId());
                String flag = FaultTrackFlow.TECHNICAL_LEAD.getCode().equals(submitNodeId)
                        ? CommonConstants.TWO_STRING : CommonConstants.ONE_STRING;
                processId = bpmnService.commit(faultTrackNo, BpmnFlowEnum.FAULT_TRACK.value(), "{\"id\":\"" + faultTrackNo + "\",\"flag\":\"" + flag + "\"}", null, examineReqDTO.getUserIds(), submitNodeId);
                // 保存流程相关信息
                faultTrackDO.setWorkFlowInstStatus(submitNodeId);
                faultTrackDO.setWorkFlowInstId(processId);
                Optional.ofNullable(reqDTO.getTrackCloserId())
                        .ifPresent(faultTrackDO::setTrackCloserId);
                Optional.ofNullable(reqDTO.getTrackResult())
                        .ifPresent(faultTrackDO::setTrackResult);
                Optional.ofNullable(reqDTO.getTrackCloseTime())
                        .ifPresent(faultTrackDO::setTrackCloseTime);
                Optional.ofNullable(reqDTO.getTrackReportTime())
                        .ifPresent(faultTrackDO::setTrackReportTime);
                faultTrackDO.setExt5(reqDTO.getLine());
                // 待办发送
                overTodoService.overTodo(processId, "故障跟踪", CommonConstants.ONE_STRING);
                faultTrackDO.setRecStatus("40");
            } catch (Exception e) {
                log.error("送审失败！", e);
                throw new CommonException(ErrorCode.NORMAL_ERROR, "送审失败!", faultTrackNo);
            }
        }
        faultTrackMapper.update(faultTrackDO,
                new UpdateWrapper<FaultTrackDO>().eq(ColsConstants.FAULT_TRACK_NO, reqDTO.getFaultTrackNo()));
        return processId;
    }

    @Override
    public void pass(FaultExamineReqDTO reqDTO) {
        Assert.notNull(reqDTO.getExamineReqDTO(), ErrorCode.PARAM_ERROR);
        String faultTrackNo = reqDTO.getFaultTrackNo();
        FaultTrackDO dmfm09 = faultTrackMapper.selectOne(new QueryWrapper<FaultTrackDO>().eq("FAULT_TRACK_NO", faultTrackNo));
        Assert.isTrue(!IGNORE_STATE.contains(dmfm09.getRecStatus()), "跟踪单状态不为审核中时，不允许审核");
        String processId = dmfm09.getWorkFlowInstId();
        workFlowLogService.ifReviewer(processId);
        String taskId = bpmnService.queryTaskIdByProcId(processId);
        // 获取Aop对象，事务逻辑轻量化
        TrackServiceImpl trackService = (TrackServiceImpl) AopContext.currentProxy();
        trackService.agree(reqDTO, dmfm09, taskId, faultTrackNo);
        workFlowLogService.add(WorkFlowLogBO.builder()
                .status(BpmnStatus.PASS.getDesc())
                .userIds(reqDTO.getExamineReqDTO().getUserIds())
                .workFlowInstId(processId)
                .build());
    }

    @Transactional(rollbackFor = Exception.class)
    public void agree(FaultExamineReqDTO reqDTO, FaultTrackDO faultTrackDO, String taskId, String faultTrackNo) {
        try {
            if (roleMapper.getNodeIdsByFlowId(BpmnFlowEnum.FAULT_TRACK.value()).contains(faultTrackDO.getWorkFlowInstStatus())) {
                String reviewOrNot = null;
                String flag = null;
                // 需要部长审核时 flag固定为1
                if (null != reqDTO.getReviewOrNot()) {
                    if (reqDTO.getReviewOrNot()) {
                        reviewOrNot = FaultTrackFlow.REVIEW_NODE.getCode();
                        flag = CommonConstants.ONE_STRING;
                    } else {
                        // 不需要部长审核 直接变更为结束状态
                        faultTrackDO.setRecStatus("50");
                        reviewOrNot = FaultTrackFlow.END_NODE.getCode();
                        flag = CommonConstants.ZERO_STRING;
                    }
                }
                FlowRoleResDTO nextNode = bpmnService.getNextNode(BpmnFlowEnum.FAULT_TRACK.value(), faultTrackDO.getWorkFlowInstStatus(), faultTrackDO.getExt5());
                String nodeId = nextNode.getNodeId();
                // 下一步为结束节点时，变更状态
                if (nodeId.equals(FaultAnalizeFlow.FAULT_ANALIZE_END_NODE.getCode())) {
                    faultTrackDO.setRecStatus("50");
                }
                Optional.ofNullable(reqDTO.getTrackCloserId())
                        .ifPresent(faultTrackDO::setTrackCloserId);
                Optional.ofNullable(reqDTO.getTrackResult())
                        .ifPresent(faultTrackDO::setTrackResult);
                Optional.ofNullable(reqDTO.getTrackCloseTime())
                        .ifPresent(faultTrackDO::setTrackCloseTime);
                Optional.ofNullable(reqDTO.getTrackReportTime())
                        .ifPresent(faultTrackDO::setTrackReportTime);
                bpmnService.agree(taskId, null, null, "{\"id\":\"" + faultTrackNo + "\",\"review\":\"" + flag + "\"}", reviewOrNot);
                faultTrackDO.setWorkFlowInstStatus(nodeId);
                faultTrackDO.setExt5(nextNode.getLine());
            }
            faultTrackDO.setRecRevisor(TokenUtils.getCurrentPersonId());
            faultTrackDO.setRecReviseTime(DateUtils.getCurrentTime());
            faultTrackMapper.update(faultTrackDO, new UpdateWrapper<FaultTrackDO>().eq(ColsConstants.FAULT_TRACK_NO, reqDTO.getFaultTrackNo()));
        } catch (Exception e) {
            log.error("agree error", e);
            throw new CommonException(ErrorCode.NORMAL_ERROR, "agree error");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returns(FaultExamineReqDTO reqDTO) {
        List<FaultTrackDO> list = faultTrackMapper.queryList(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo(), null, reqDTO.getFaultTrackNo());
        FaultTrackDO dmfm09 = list.get(0);
        String processId = dmfm09.getWorkFlowInstId();
        workFlowLogService.ifReviewer(processId);
        // String taskId = bpmnService.queryTaskIdByProcId(processId);
        bpmnService.reject(processId, reqDTO.getExamineReqDTO().getOpinion());
        dmfm09.setRecStatus("10");
        dmfm09.setWorkFlowInstId(CommonConstants.EMPTY);
        dmfm09.setWorkFlowInstStatus(CommonConstants.EMPTY);
        faultTrackMapper.update(dmfm09, new UpdateWrapper<FaultTrackDO>().eq(ColsConstants.FAULT_TRACK_NO, reqDTO.getFaultTrackNo()));
        workFlowLogService.add(WorkFlowLogBO.builder()
                .status(BpmnStatus.REJECT.getDesc())
                .userIds(reqDTO.getExamineReqDTO().getUserIds())
                .workFlowInstId(processId)
                .build());
    }
}
