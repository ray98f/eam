package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.FaultTrackCols;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dataobject.FaultTrackDO;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.fault.TrackResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.fault.FaultInfoMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultTrackMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.dict.IDictionariesService;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.service.fault.TrackService;
import com.wzmtr.eam.service.user.UserHelperService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
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
    private BpmnService bpmnService;
    @Autowired
    private FaultQueryMapper faultQueryMapper;
    @Autowired
    private OverTodoService overTodoService;
    @Autowired
    private IDictionariesService dictService;
    @Autowired
    private IWorkFlowLogService workFlowLogService;

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
        Page<TrackResDTO> list = faultTrackMapper.query(reqDTO.of(), reqDTO.getFaultTrackNo(), reqDTO.getFaultTrackWorkNo(), reqDTO.getRecStatus(), reqDTO.getEquipTypeCode(), reqDTO.getMajorCode(), reqDTO.getObjectName(), reqDTO.getObjectCode(), reqDTO.getSystemCode());
        if (CollectionUtil.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        return list;
    }

    @Override
    public TrackResDTO detail(SidEntity reqDTO) {
        return faultTrackMapper.detail(reqDTO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void report(TrackReportReqDTO reqDTO) {
        // EAM/service/DMFM0011/ReportRow
        reqDTO.setTrackReportTime(DateUtil.current("yyyy-MM-dd HH:mm:ss"));
        reqDTO.setTrackReporterId(TokenUtil.getCurrentPersonId());
        reqDTO.setRecStatus("30");
        faultTrackMapper.report(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void close(TrackCloseReqDTO reqDTO) {
        reqDTO.setTrackCloseTime(DateUtil.current("yyyy-MM-dd HH:mm:ss"));
        reqDTO.setTrackCloserId(TokenUtil.getCurrentPersonId());
        reqDTO.setRecStatus("40");
        // /* 136 */       DMUtil.overTODO((String)((Map)dm03List.get(0)).get("recId"), "关闭");
        faultTrackMapper.close(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void repair(TrackRepairReqDTO reqDTO) {
        // /* 107 */     dmfm22.setWorkerGroupCode(repairDeptCode);
        reqDTO.setDispatchUserId(TokenUtil.getCurrentPersonId());
        reqDTO.setDispatchTime(DateUtil.current("yyyy-MM-dd HH:mm:ss"));
        overTodoService.overTodo(reqDTO.getRecId(), "派工完毕");
        reqDTO.setRecStatus("20");
        faultTrackMapper.repair(reqDTO);
    }
    // TODO com.baosight.wzplat.dm.fm.service.ServiceDMFM0010#buildWork 看着像是生成一个跟踪工单，暂时还不知道在哪触发

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returns(FaultExamineReqDTO reqDTO) {
        List<FaultTrackDO> list = faultTrackMapper.queryOne(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo(), null, reqDTO.getFaultTrackNo());
        FaultTrackDO dmfm09 = list.get(0);
        // String userId = UserUtil.getLoginId();
        String processId = dmfm09.getWorkFlowInstId();
        String taskId = bpmnService.queryTaskIdByProcId(processId);
        bpmnService.reject(taskId, reqDTO.getExamineReqDTO().getOpinion());
        dmfm09.setRecStatus("30");
        dmfm09.setWorkFlowInstStatus("驳回成功");
        faultTrackMapper.update(dmfm09, new UpdateWrapper<FaultTrackDO>().eq(FaultTrackCols.FAULT_TRACK_NO, reqDTO.getFaultTrackNo()));
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
    @Transactional(rollbackFor = Exception.class)
    public void commit(FaultExamineReqDTO reqDTO) {
        // dmfm09.query  com.baosight.wzplat.dm.fm.service.ServiceDMFM0010#submit
        List<FaultTrackDO> dmfm9List = faultTrackMapper.queryOne(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo(), reqDTO.getFaultAnalysisNo(), reqDTO.getFaultTrackNo());
        if (CollectionUtil.isEmpty(dmfm9List)) {
            return;
        }
        FaultTrackDO dmfm09 = dmfm9List.get(0);
        String processId = null;
        // dmfm01.query
        if (StringUtils.isEmpty(dmfm09.getWorkFlowInstId())) {
            try {
                processId = bpmnService.commit(dmfm09.getWorkFlowInstId(), BpmnFlowEnum.FAULT_TRACK.value(), null, null, reqDTO.getExamineReqDTO().getUserIds());
                dmfm09.setWorkFlowInstStatus("提交审核");
                dmfm09.setWorkFlowInstId(processId);
                String workFlowIns = dmfm09.getWorkFlowInstId();
                overTodoService.overTodo(workFlowIns, "故障跟踪");
                dmfm09.setRecStatus("40");
            } catch (Exception e) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "送审失败！流程提交失败。");
            }
        } else {
            try {
                bpmnService.commit(reqDTO.getFaultTrackNo(), BpmnFlowEnum.FAULT_TRACK.value(), null, null, reqDTO.getExamineReqDTO().getUserIds());
                dmfm09.setRecStatus("40");
                dmfm09.setWorkFlowInstStatus("跟踪报告送审");
                log.info("跟踪报告送审成功！");
            } catch (Exception e) {
                log.error("commit error", e);
            }
        }
        // 记录日志
        workFlowLogService.add(WorkFlowLogBO.builder()
                .status("报告提交")
                .userIds(reqDTO.getExamineReqDTO().getUserIds())
                .workFlowInstId(processId)
                .build());
        faultTrackMapper.update(dmfm09, new UpdateWrapper<FaultTrackDO>().eq(FaultTrackCols.FAULT_TRACK_NO, reqDTO.getFaultTrackNo()));

    }

    @Override
    public void export(TrackExportReqDTO reqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("跟踪单工单号", "跟踪单号", "对象编码", "对象名称", "跟踪状态", "派工人", "跟踪报告人", "派工时间", "跟踪结果", "备注", "跟踪报告时间", "跟踪关闭人", "跟踪关闭时间");
        List<TrackResDTO> resList = faultTrackMapper.query(reqDTO);
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
    public void pass(FaultExamineReqDTO reqDTO) {
        String faultTrackNo = reqDTO.getFaultTrackNo();
        FaultTrackDO dmfm09 = faultTrackMapper.selectOne(new QueryWrapper<FaultTrackDO>().eq("FAULT_TRACK_NO", faultTrackNo));
        String processId = dmfm09.getWorkFlowInstId();
        String taskId = bpmnService.queryTaskIdByProcId(processId);
        // submit
        try {
            bpmnService.agree(taskId, reqDTO.getExamineReqDTO().getOpinion(), null, "{\"id\":\"" + faultTrackNo + "\"}");
        } catch (Exception e) {
            log.error("commit error", e);
        }
        // submtStatus = WorkflowHelper.submit(taskId, userId, comment, "", nextUser, null);
        dmfm09.setRecStatus("40");
        dmfm09.setWorkFlowInstStatus("审核通过");
        faultTrackMapper.update(dmfm09, new UpdateWrapper<FaultTrackDO>().eq(FaultTrackCols.FAULT_TRACK_NO, reqDTO.getFaultTrackNo()));
    }
}
