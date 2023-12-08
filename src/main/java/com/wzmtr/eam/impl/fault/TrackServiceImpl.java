package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.Cols;
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
import java.util.*;

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
    private static final List<String> ignoreState= Arrays.asList("10","20","30");
    @Override
    public Page<TrackResDTO> list(TrackReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<TrackResDTO> list = faultTrackWorkMapper.query(reqDTO.of(), reqDTO.getFaultTrackNo(), reqDTO.getFaultTrackWorkNo(), reqDTO.getRecStatus(), reqDTO.getEquipTypeCode(), reqDTO.getMajorCode(), reqDTO.getObjectName(), reqDTO.getObjectCode(), reqDTO.getSystemCode());
        if (CollectionUtil.isEmpty(list.getRecords())) {
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
        reqDTO.setTrackReportTime(DateUtil.current("yyyy-MM-dd HH:mm:ss"));
        reqDTO.setTrackReporterId(TokenUtil.getCurrentPersonId());
        reqDTO.setRecStatus("30");
        faultTrackWorkMapper.report(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void close(TrackCloseReqDTO reqDTO) {
        reqDTO.setTrackCloseTime(DateUtil.current("yyyy-MM-dd HH:mm:ss"));
        reqDTO.setTrackCloserId(TokenUtil.getCurrentPersonId());
        reqDTO.setRecStatus("40");
        // /* 136 */       DMUtil.overTODO((String)((Map)dm03List.get(0)).get("recId"), "关闭");
        faultTrackWorkMapper.close(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repair(TrackRepairReqDTO reqDTO) {
        // /* 107 */     dmfm22.setWorkerGroupCode(repairDeptCode);
        reqDTO.setDispatchUserId(TokenUtil.getCurrentPersonId());
        reqDTO.setDispatchTime(DateUtil.current("yyyy-MM-dd HH:mm:ss"));
        overTodoService.overTodo(reqDTO.getRecId(), "派工完毕");
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
        overTodoService.overTodo(workFlowInstId, "跟踪工单");
        FaultTrackDO bo = __BeanUtil.convert(res, FaultTrackDO.class);
        bo.setFaultTrackNo(reqDTO.getFaultTrackNo());
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
        // 更新故障跟踪表
        faultTrackMapper.transmit(bo);
    }

    @Override
    public void export(TrackExportReqDTO reqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("跟踪单工单号", "跟踪单号", "对象编码", "对象名称", "跟踪状态", "派工人", "跟踪报告人", "派工时间", "跟踪结果", "备注", "跟踪报告时间", "跟踪关闭人", "跟踪关闭时间");
        List<TrackResDTO> resList = faultTrackWorkMapper.query(reqDTO);
        if (CollectionUtil.isEmpty(resList)) {
            return;
        }
        List<Map<String, String>> list = new ArrayList<>();
        for (TrackResDTO resDTO : resList) {
            Dictionaries dictionaries = dictService.queryOneByItemCodeAndCodesetCode("dm.faultTrackWorkStatus", resDTO.getRecStatus());
            Map<String, String> map = new HashMap<>();
            map.put("跟踪单工单号", resDTO.getFaultTrackWorkNo());
            map.put("跟踪单号", resDTO.getFaultTrackNo());
            map.put("对象编码", resDTO.getObjectCode());
            map.put("对象名称", resDTO.getObjectName());
            map.put("跟踪状态", dictionaries.getItemCname());
            map.put("派工人", resDTO.getDispatchUserName());
            map.put("跟踪报告人", resDTO.getTrackReportName());
            map.put("派工时间", resDTO.getDispatchTime());
            map.put("跟踪结果", resDTO.getTrackResult());
            map.put("备注", resDTO.getRemark());
            map.put("跟踪报告时间", resDTO.getTrackReportTime());
            map.put("跟踪关闭人", resDTO.getTrackCloserName());
            map.put("跟踪关闭时间", resDTO.getTrackCloseTime());
            list.add(map);
        }
        ExcelPortUtil.excelPort("故障跟踪工单信息", listName, list, null, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void commit(FaultExamineReqDTO reqDTO) {
        ExamineReqDTO examineReqDTO = Assert.notNull(reqDTO.getExamineReqDTO(), "下一步参与者不能为空！");
        // dmfm09.query  com.baosight.wzplat.dm.fm.service.ServiceDMFM0010#submit
        List<FaultTrackDO> dmfm9List = faultTrackMapper.queryList(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo(), reqDTO.getFaultAnalysisNo(), reqDTO.getFaultTrackNo());
        if (CollectionUtil.isEmpty(dmfm9List)) {
            return;
        }
        FaultTrackDO dmfm09 = dmfm9List.get(0);
        String processId = null;
        // dmfm01.query
        if (StringUtils.isEmpty(dmfm09.getWorkFlowInstId())) {
            String faultTrackNo = dmfm09.getFaultTrackNo();
            try {
                // 根据前端选择的roleId获取下一步的流程走向
                String submitNodeId = roleMapper.getSubmitNodeId(BpmnFlowEnum.FAULT_TRACK.value(), examineReqDTO.getRoleId());
                String flag = FaultTrackFlow.TECHNICAL_LEAD.getCode().equals(submitNodeId)
                        ? CommonConstants.TWO_STRING : CommonConstants.ONE_STRING;
                processId = bpmnService.commit(faultTrackNo, BpmnFlowEnum.FAULT_TRACK.value(), "{\"id\":\"" + faultTrackNo + "\",\"flag\":\"" + flag + "\"}", null, examineReqDTO.getUserIds(), submitNodeId);
                // 保存流程相关信息
                dmfm09.setWorkFlowInstStatus(submitNodeId);
                dmfm09.setWorkFlowInstId(processId);
                dmfm09.setExt5(reqDTO.getLine());
                // 待办发送
                overTodoService.overTodo(processId, "故障跟踪");
                dmfm09.setRecStatus("40");
            } catch (Exception e) {
                log.error("送审失败！",e);
                throw new CommonException(ErrorCode.NORMAL_ERROR, "送审失败!", faultTrackNo);
            }
        }
        // 记录日志
        workFlowLogService.add(WorkFlowLogBO.builder()
                .status(BpmnStatus.SUBMIT.getDesc())
                .userIds(examineReqDTO.getUserIds())
                .workFlowInstId(processId)
                .build());
        faultTrackMapper.update(dmfm09,
                new UpdateWrapper<FaultTrackDO>().eq(Cols.FAULT_TRACK_NO, reqDTO.getFaultTrackNo()));
    }

    @Override
    public void pass(FaultExamineReqDTO reqDTO) {
        Assert.notNull(reqDTO.getExamineReqDTO(),ErrorCode.PARAM_ERROR);
        String faultTrackNo = reqDTO.getFaultTrackNo();
        FaultTrackDO dmfm09 = faultTrackMapper.selectOne(new QueryWrapper<FaultTrackDO>().eq("FAULT_TRACK_NO", faultTrackNo));
        // Assert.isFalse(ignoreState.contains(dmfm09.getRecStatus()),"跟踪单状态不为审核中时，不允许审核");
        String processId = dmfm09.getWorkFlowInstId();
        String taskId = bpmnService.queryTaskIdByProcId(processId);
        // 获取Aop对象，事务逻辑轻量化
        TrackServiceImpl trackService = (TrackServiceImpl) AopContext.currentProxy();
        trackService._agree(reqDTO, dmfm09, taskId, faultTrackNo);
        workFlowLogService.add(WorkFlowLogBO.builder()
                .status(BpmnStatus.PASS.getDesc())
                .userIds(reqDTO.getExamineReqDTO().getUserIds())
                .workFlowInstId(processId)
                .build());
    }

    @Transactional(rollbackFor = Exception.class)
    public void _agree(FaultExamineReqDTO reqDTO, FaultTrackDO dmfm09, String taskId, String faultTrackNo) {
        try {
            if (roleMapper.getNodeIdsByFlowId(BpmnFlowEnum.FAULT_TRACK.value()).contains(dmfm09.getWorkFlowInstStatus())) {
                String reviewOrNot = null;
                String flag = null;
                // 需要部长审核时 flag固定为0
                if (null != reqDTO.getReviewOrNot()) {
                    if (reqDTO.getReviewOrNot()) {
                        reviewOrNot = FaultTrackFlow.REVIEW_NODE.getCode();
                        flag = CommonConstants.ZERO_STRING;
                    } else {
                        // 不需要部长审核 直接变更为结束状态
                        dmfm09.setRecStatus("30");
                        reviewOrNot = FaultTrackFlow.END_NODE.getCode();
                        flag = CommonConstants.ONE_STRING;
                    }
                }
                FlowRoleResDTO nextNode = bpmnService.getNextNode(BpmnFlowEnum.FAULT_TRACK.value(), dmfm09.getWorkFlowInstStatus(), dmfm09.getExt5());
                String nodeId = nextNode.getNodeId();
                // 下一步为结束节点时，变更状态
                if (nodeId.equals(FaultAnalizeFlow.FAULT_ANALIZE_END_NODE.getCode())) {
                    dmfm09.setRecStatus("30");
                }
                bpmnService.agree(taskId, null, null, "{\"id\":\"" + faultTrackNo + "\",\"review\":\"" + flag + "\"}", reviewOrNot);
                dmfm09.setWorkFlowInstStatus(nodeId);
                dmfm09.setExt5(nextNode.getLine());
            }
            dmfm09.setRecRevisor(TokenUtil.getCurrentPersonId());
            dmfm09.setRecReviseTime(DateUtil.getCurrentTime());
            faultTrackMapper.update(dmfm09, new UpdateWrapper<FaultTrackDO>().eq(Cols.FAULT_TRACK_NO, reqDTO.getFaultTrackNo()));
        } catch (Exception e) {
            log.error("agree error",e);
            throw new CommonException(ErrorCode.NORMAL_ERROR, "agree error");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returns(FaultExamineReqDTO reqDTO) {
        List<FaultTrackDO> list = faultTrackMapper.queryList(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo(), null, reqDTO.getFaultTrackNo());
        FaultTrackDO dmfm09 = list.get(0);
        String processId = dmfm09.getWorkFlowInstId();
        String taskId = bpmnService.queryTaskIdByProcId(processId);
        bpmnService.reject(taskId, reqDTO.getExamineReqDTO().getOpinion());
        dmfm09.setRecStatus("30");
        dmfm09.setWorkFlowInstStatus("");
        faultTrackMapper.update(dmfm09, new UpdateWrapper<FaultTrackDO>().eq(Cols.FAULT_TRACK_NO, reqDTO.getFaultTrackNo()));
        workFlowLogService.add(WorkFlowLogBO.builder()
                .status(BpmnStatus.REJECT.getDesc())
                .userIds(reqDTO.getExamineReqDTO().getUserIds())
                .workFlowInstId(processId)
                .build());
    }
}
