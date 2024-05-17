package com.wzmtr.eam.impl.overhaul;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.req.equipment.EquipmentSiftReqDTO;
import com.wzmtr.eam.dto.req.overhaul.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentRoomResDTO;
import com.wzmtr.eam.dto.res.overhaul.*;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulPlanObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulPlanResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulWeekPlanResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.BpmnStatus;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentRoomMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.overhaul.*;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.overhaul.OverhaulWeekPlanService;
import com.wzmtr.eam.service.overhaul.OverhaulWorkRecordService;
import com.wzmtr.eam.soft.csm.planWork.vo.Message;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author frp
 */
@Service
@Slf4j
public class OverhaulWeekPlanServiceImpl implements OverhaulWeekPlanService {

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EquipmentRoomMapper equipmentRoomMapper;

    @Autowired
    private OverhaulWeekPlanMapper overhaulWeekPlanMapper;

    @Autowired
    private OverhaulPlanMapper overhaulPlanMapper;

    @Autowired
    private OverhaulOrderMapper overhaulOrderMapper;

    @Autowired
    private OverhaulWorkRecordService overhaulWorkRecordService;

    @Autowired
    private OverhaulTplMapper overhaulTplMapper;

    @Autowired
    private OverhaulItemMapper overhaulItemMapper;

    @Autowired
    private OverhaulWorkRecordMapper overhaulWorkRecordMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private BpmnService bpmnService;

    @Autowired
    private DictionariesMapper dictionariesMapper;

    @Autowired
    private IWorkFlowLogService workFlowLogService;

    @Autowired
    private FaultQueryMapper faultQueryMapper;

    @Autowired
    private OverTodoService overTodoService;
    @Override
    public Page<OverhaulWeekPlanResDTO> pageOverhaulWeekPlan(OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<OverhaulWeekPlanResDTO> page = overhaulWeekPlanMapper.pageOverhaulWeekPlan(pageReqDTO.of(), overhaulWeekPlanListReqDTO);
        List<OverhaulWeekPlanResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (OverhaulWeekPlanResDTO res : list) {
                res.setWorkGroupName(organizationMapper.getNamesById(res.getWorkerGroupCode()));
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public OverhaulWeekPlanResDTO getOverhaulWeekPlanDetail(String id) {
        OverhaulWeekPlanResDTO res = overhaulWeekPlanMapper.getOverhaulWeekPlanDetail(id);
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        res.setWorkGroupName(organizationMapper.getNamesById(res.getWorkerGroupCode()));
        return res;
    }

    @Override
    public List<FaultRepairDeptResDTO> queryDept(String lineNo, String subjectCode) {
        if (StringUtils.isEmpty(lineNo) || StringUtils.isEmpty(subjectCode)) {
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
        return faultQueryMapper.queryDeptCode(lineNo, subjectCode, "10");
    }

    @Override
    public void addOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) {
        if (org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getPlanName()) || org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getFirstBeginTime()) ||
                org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getSubjectCode()) || org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getWorkerGroupCode()) ||
                org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getWorkerCode())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选计划中有标红必填项未填写");
        }
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulWeekPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulWeekPlanMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulWeekPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        SimpleDateFormat month = new SimpleDateFormat("yyMM");
        String weekPlanCode = overhaulWeekPlanMapper.getMaxCode();
        if (StringUtils.isEmpty(weekPlanCode) || !weekPlanCode.substring(CommonConstants.THREE, CommonConstants.SEVEN).equals(month.format(System.currentTimeMillis()))) {
            weekPlanCode = "ZJH" + month.format(System.currentTimeMillis()) + "0001";
        } else {
            weekPlanCode = CodeUtils.getNextCode(weekPlanCode, 7);
        }
        overhaulWeekPlanReqDTO.setRecId(TokenUtils.getUuId());
        overhaulWeekPlanReqDTO.setWeekPlanCode(weekPlanCode);
        overhaulWeekPlanReqDTO.setTrialStatus("10");
        overhaulWeekPlanReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        overhaulWeekPlanReqDTO.setRecCreatorName(TokenUtils.getCurrentPerson().getCompanyName());
        overhaulWeekPlanReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        overhaulWeekPlanReqDTO.setWorkFlowInstId(" ");
        overhaulWeekPlanReqDTO.setWorkFlowInstStatus(" ");
        overhaulWeekPlanMapper.addOverhaulWeekPlan(overhaulWeekPlanReqDTO);
    }

    @Override
    public void modifyOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) {
        if (org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getPlanName()) || org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getFirstBeginTime()) ||
                org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getSubjectCode()) || org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getWorkerGroupCode()) ||
                org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getWorkerCode())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选计划中有标红必填项未填写");
        }
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulWeekPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulWeekPlanMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulWeekPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        if (!CommonConstants.TEN_STRING.equals(overhaulWeekPlanReqDTO.getTrialStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        overhaulWeekPlanReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulWeekPlanReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulWeekPlanMapper.modifyOverhaulWeekPlan(overhaulWeekPlanReqDTO);
    }

    @Override
    public void deleteOverhaulWeekPlan(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                OverhaulWeekPlanResDTO resDTO = overhaulWeekPlanMapper.getOverhaulWeekPlanDetail(id);
                if (!CommonConstants.TEN_STRING.equals(resDTO.getTrialStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
                    if (Objects.isNull(resDTO.getSubjectCode())) {
                        throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                    }
                    List<String> code = overhaulWeekPlanMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
                    if (Objects.isNull(code) || code.isEmpty() || !code.contains(resDTO.getSubjectCode())) {
                        throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                    }
                }
                if (StringUtils.isNotBlank(resDTO.getWorkFlowInstId())) {
                    BpmnExamineDTO bpmnExamineDTO = new BpmnExamineDTO();
                    bpmnExamineDTO.setTaskId(resDTO.getWorkFlowInstId());
                    bpmnService.rejectInstance(bpmnExamineDTO);
                }
                overhaulWeekPlanMapper.deleteOverhaulWeekPlan(id, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
                overhaulWeekPlanMapper.deleteOverhaulPlan(resDTO.getWeekPlanCode(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
                overhaulWeekPlanMapper.deleteOverhaulObject(resDTO.getWeekPlanCode(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void triggerOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws Exception {
        if (!CommonConstants.THIRTY_STRING.equals(overhaulWeekPlanReqDTO.getTrialStatus())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "检修周计划未审批完成，无法触发");
        }
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulWeekPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulWeekPlanMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulWeekPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        triggerOne(overhaulWeekPlanReqDTO.getWeekPlanCode());
    }

    @Override
    public void submitOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws Exception {
        // ServiceDMER0111
        String currentPersonId = TokenUtils.getCurrentPersonId();
        if (org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getPlanName()) || org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getFirstBeginTime()) ||
                org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getSubjectCode()) || org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getWorkerGroupCode()) ||
                org.apache.commons.lang3.StringUtils.isBlank(overhaulWeekPlanReqDTO.getWorkerCode())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选计划中有标红必填项未填写！");
        }
        if (!CommonConstants.ADMIN.equals(currentPersonId)) {
            if (Objects.isNull(overhaulWeekPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulWeekPlanMapper.getSubjectByUserId(currentPersonId);
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulWeekPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO = new OverhaulWeekPlanListReqDTO();
        String weekPlanCode = overhaulWeekPlanReqDTO.getWeekPlanCode();
        overhaulWeekPlanListReqDTO.setWeekPlanCode(weekPlanCode);
        List<OverhaulWeekPlanResDTO> list = overhaulWeekPlanMapper.listOverhaulWeekPlan(overhaulWeekPlanListReqDTO);
        if (!CommonConstants.TEN_STRING.equals(list.get(0).getTrialStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "送审");
        }
        List<OverhaulPlanResDTO> list11 = overhaulPlanMapper.queryWeekObj(weekPlanCode);
        if (StringUtils.isNotEmpty(list11)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选周计划中" + list11.get(0).getPlanCode() + "检修计划没有检修对象！");
        }
        OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
        overhaulPlanListReqDTO.setWeekPlanCode(weekPlanCode);
        overhaulPlanListReqDTO.setConstructionType("C2");
        List<OverhaulPlanResDTO> contractQuery = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        if (StringUtils.isNotEmpty(contractQuery)) {
            if (StringUtils.isBlank(TokenUtils.getCurrentPerson().getOfficeId())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "您的组织机构为空，请确认。");
            }
            List<String> userIds = overhaulWeekPlanReqDTO.getExamineReqDTO().getUserIds();
            String processId = bpmnService.commit(weekPlanCode, BpmnFlowEnum.OVERHAUL_WEEK_PLAN_SUBMIT.value(), null, null, userIds, null);
            overhaulWeekPlanReqDTO.setWorkFlowInstStatus(roleMapper.getSubmitNodeId(BpmnFlowEnum.OVERHAUL_WEEK_PLAN_SUBMIT.value(),null));
            if (processId == null || CommonConstants.PROCESS_ERROR_CODE.equals(processId)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "送审失败！流程提交失败。");
            } else {
                overTodoService.insertTodoWithUserList(userIds, "收到一条检修周计划编号为：" + weekPlanCode + "的审批流程", overhaulWeekPlanReqDTO.getRecId(), weekPlanCode, "检修周计划审核", "?", currentPersonId, null, BpmnFlowEnum.ORDER_PLAN_SUBMIT.value());
                overhaulWeekPlanReqDTO.setWorkFlowInstId(processId);
                overhaulWeekPlanReqDTO.setTrialStatus("20");
                // 记录日志
                workFlowLogService.add(WorkFlowLogBO.builder()
                        .status(BpmnStatus.SUBMIT.getDesc())
                        .userIds(userIds)
                        .workFlowInstId(processId)
                        .build());
            }
        } else {
            overhaulWeekPlanReqDTO.setTrialStatus("30");
            // 去除触发操作
//            triggerOne(weekPlanCode);
        }
        overhaulWeekPlanReqDTO.setRecRevisor(currentPersonId);
        overhaulWeekPlanReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulWeekPlanMapper.modifyOverhaulWeekPlan(overhaulWeekPlanReqDTO);
    }

    @Override
    public void examineOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws Exception {
        workFlowLogService.ifReviewer(overhaulWeekPlanReqDTO.getWorkFlowInstId());
        String recId = overhaulWeekPlanReqDTO.getRecId();
        String opinion = overhaulWeekPlanReqDTO.getExamineReqDTO().getOpinion();
        if (overhaulWeekPlanReqDTO.getExamineReqDTO().getExamineStatus() == 0) {
            if (CommonConstants.THIRTY_STRING.equals(overhaulWeekPlanReqDTO.getTrialStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_DONE);
            }
            if (CommonConstants.TEN_STRING.equals(overhaulWeekPlanReqDTO.getTrialStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_NOT_DONE);
            }
            overhaulWeekPlanReqDTO.setTrialStatus("30");
            // 去除触发操作
//            triggerOne(overhaulWeekPlanReqDTO.getWeekPlanCode());
            String processId = overhaulWeekPlanReqDTO.getWorkFlowInstId();
            String taskId = bpmnService.queryTaskIdByProcId(processId);
            overTodoService.overTodo(recId,opinion);

            //TODO
            //bpmnService.agree(taskId, opinion, null, "{\"id\":\"" + overhaulWeekPlanReqDTO.getWeekPlanCode() + "\"}", null);
            overhaulWeekPlanReqDTO.setWorkFlowInstStatus("已完成");
            // 记录日志
            workFlowLogService.add(WorkFlowLogBO.builder()
                    .status(BpmnStatus.PASS.getDesc())
                    .userIds(overhaulWeekPlanReqDTO.getExamineReqDTO().getUserIds())
                    .workFlowInstId(processId)
                    .build());
        } else {
            if (!CommonConstants.TWENTY_STRING.equals(overhaulWeekPlanReqDTO.getTrialStatus())) {
                throw new CommonException(ErrorCode.REJECT_ERROR);
            } else {
                String processId = overhaulWeekPlanReqDTO.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.reject(taskId, opinion);
                overhaulWeekPlanReqDTO.setWorkFlowInstId("");
                overhaulWeekPlanReqDTO.setWorkFlowInstStatus("");
                overhaulWeekPlanReqDTO.setTrialStatus("10");
                overTodoService.cancelTodo(recId);
                // 记录日志
                workFlowLogService.add(WorkFlowLogBO.builder()
                        .status(BpmnStatus.REJECT.getDesc())
                        .userIds(overhaulWeekPlanReqDTO.getExamineReqDTO().getUserIds())
                        .workFlowInstId(processId)
                        .build());
            }
        }
        overhaulWeekPlanReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulWeekPlanReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulWeekPlanMapper.modifyOverhaulWeekPlan(overhaulWeekPlanReqDTO);
    }

    @Override
    public void exportOverhaulWeekPlan(List<String> ids, HttpServletResponse response) throws IOException {
        List<OverhaulWeekPlanResDTO> overhaulWeekPlan = overhaulWeekPlanMapper.getOverhaulWeekPlanByIds(ids);
        if (overhaulWeekPlan != null && !overhaulWeekPlan.isEmpty()) {
            List<ExcelOverhaulWeekPlanResDTO> list = new ArrayList<>();
            for (OverhaulWeekPlanResDTO resDTO : overhaulWeekPlan) {
                ExcelOverhaulWeekPlanResDTO res = new ExcelOverhaulWeekPlanResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setLineNo(CommonConstants.LINE_CODE_ONE.equals(resDTO.getLineNo()) ? "S1线" : "S2线");
                res.setWorkerGroupCode(organizationMapper.getNamesById(resDTO.getWorkerGroupCode()));
                list.add(res);
            }
            EasyExcelUtils.export(response, "检修周计划（中铁通）信息", list);
        }
    }

    public void triggerOne(String weekPlanCode) throws Exception {
        OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
        overhaulPlanListReqDTO.setWeekPlanCode(weekPlanCode);
        List<OverhaulPlanResDTO> planList = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        if (StringUtils.isEmpty(planList)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "您选择触发的周计划中没有检修项！");
        }
        for (OverhaulPlanResDTO plan : planList) {
            if (!checkHasNotOrder(plan.getPlanCode())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "选择触发的检修周计划中的检修计划存在未做完的工单，请优先做完工单后再进行触发。");
            }
        }
        for (OverhaulPlanResDTO plan : planList) {
            List<OverhaulTplDetailResDTO> orderIsValid = overhaulPlanMapper.getOrderIsValid(plan.getPlanCode());
            if (StringUtils.isEmpty(orderIsValid)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "您选择触发的周计划中没有检修项！");
            }
            String orderCode = overhaulOrderMapper.getMaxCode();
            if (StringUtils.isEmpty(orderCode) || !orderCode.substring(CommonConstants.TWO, CommonConstants.TEN).equals(DateUtils.getNoDate())) {
                orderCode = "JX" + DateUtils.getNoDate() + "0001";
            } else {
                orderCode = CodeUtils.getNextCode(orderCode, 10);
            }
            insertInspectPlan(plan.getPlanCode(), new String[]{orderCode, weekPlanCode, plan.getFirstBeginTime()});
            if (StringUtils.isNotEmpty(plan.getConstructionType())) {
                plan.setExt1(orderCode);
                try {
                    sendConstrctioOrderMsg(plan);
                } catch (Exception e) {
                    OverhaulPlanReqDTO t11map = new OverhaulPlanReqDTO();
                    t11map.setRecId(plan.getRecId());
                    t11map.setArchiveFlag("1");
                    t11map.setRecRevisor(TokenUtils.getCurrentPersonId());
                    t11map.setRecReviseTime(DateUtils.getCurrentTime());
                    overhaulPlanMapper.modifyOverhaulPlan(t11map);
                    log.error("exception message", e);
                }
            }
            insertInspectObject(plan.getPlanCode(), orderCode);
            insertWorker(plan.getPlanCode(), orderCode);
        }
    }

    /**
     * 检查检修计划是否存在未做完的工单
     * @param planCode 检修计划编号
     * @return 是否存在
     */
    public boolean checkHasNotOrder(String planCode) {
        OverhaulOrderListReqDTO overhaulOrderListReqDTO = new OverhaulOrderListReqDTO();
        overhaulOrderListReqDTO.setPlanCode(planCode);
        overhaulOrderListReqDTO.setNewTime("flag");
        List<OverhaulOrderResDTO> list = overhaulOrderMapper.listOverhaulOrder(overhaulOrderListReqDTO);
        return StringUtils.isEmpty(list);
    }

    public void insertInspectPlan(String planCode, String[] orderCodes) throws Exception {
        String orderCode = orderCodes[0];
        String weekPlanCode = orderCodes[1];
        String firstBeginTime = orderCodes[2];
        OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO = new OverhaulWeekPlanListReqDTO();
        overhaulWeekPlanListReqDTO.setWeekPlanCode(weekPlanCode);
        List<OverhaulWeekPlanResDTO> weekPlan = overhaulWeekPlanMapper.listOverhaulWeekPlan(overhaulWeekPlanListReqDTO);
        if (StringUtils.isEmpty(weekPlan)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        String orgCode = weekPlan.get(0).getWorkerGroupCode();
        String userCode = weekPlan.get(0).getWorkerCode();
        String userName = weekPlan.get(0).getWorkerName();
        if (StringUtils.isEmpty(orgCode)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "该周计划中没有填写作业工班！");
        }
        if (StringUtils.isEmpty(userCode)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "该周计划中没有填写工班长！");
        }
        OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
        overhaulPlanListReqDTO.setPlanCode(planCode);
        List<OverhaulPlanResDTO> list = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        if (!Objects.isNull(list) && !list.isEmpty()) {
            int i = 0;
            for (OverhaulPlanResDTO res : list) {
                OverhaulOrderReqDTO reqDTO = new OverhaulOrderReqDTO();
                BeanUtils.copyProperties(res, reqDTO);
                reqDTO.setOrderCode(orderCode);
                reqDTO.setWorkStatus("2");
                reqDTO.setWorkerGroupCode(orgCode);
                reqDTO.setWorkerCode(userCode);
                reqDTO.setWorkerName(userName);
                reqDTO.setRealStartTime(" ");
                reqDTO.setRealEndTime(" ");
                reqDTO.setExt1(" ");
                reqDTO.setPlanStartTime(firstBeginTime);
                reqDTO.setRecId(TokenUtils.getUuId());
                reqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
                reqDTO.setRecCreateTime(DateUtils.getCurrentTime());
                reqDTO.setRecRevisor("");
                reqDTO.setRecReviseTime("");
                if (i > 0) {
                    reqDTO.setOrderCode(CodeUtils.getNextCodeByAddNum(orderCode, 10, i));
                }
                overhaulOrderMapper.addOverhaulOrder(reqDTO);
                i++;
                try {
                    overhaulWorkRecordService.insertRepair(reqDTO);
                } catch (Exception e) {
                    log.error("exception message", e);
                }
            }
        }
    }

    public void sendConstrctioOrderMsg(OverhaulPlanResDTO dmer11) {
        OverhaulOrderListReqDTO overhaulOrderListReqDTO = new OverhaulOrderListReqDTO();
        overhaulOrderListReqDTO.setOrderCode(dmer11.getExt1());
        List<OverhaulOrderResDTO> recIdList = overhaulOrderMapper.listOverhaulOrder(overhaulOrderListReqDTO);
        String workerGroupCode = recIdList.get(0).getWorkerGroupCode();
        dmer11.setWorkerGroupCode(workerGroupCode);
        dmer11.setWorkGroupName(organizationMapper.getOrgNameByOrgCode(workerGroupCode));
        Message message = new Message();
        message.setSyscode("EAM");
        message.setOperType("0");
        message.setUuid(recIdList.get(0).getRecId());
        message.setWorkNo(dmer11.getExt1());
        message.setWorkType(dmer11.getConstructionType());
        message.setPlanNo(dmer11.getPlanCode());
        message.setPlanName(dmer11.getPlanName());
        message.setPlanStartDate(dmer11.getFirstBeginTime());
        message.setPlanFinishDate(dmer11.getPlanFinishTime());
        message.setGroupName(dmer11.getWorkGroupName());
        message.setLineName(dmer11.getLineName());
        message.setPosName(dmer11.getPosition1Name());
        message.setEquipType1(dmer11.getSubjectName());
        message.setEquipType2(dmer11.getSystemName());
        message.setEquipType3(dmer11.getEquipTypeName());
        message.setGroupCode(workerGroupCode);
        List<OverhaulObjectResDTO> objectList = overhaulPlanMapper.queryObject(dmer11.getPlanCode());
        if (StringUtils.isNotEmpty(objectList)) {
            message.setObjectCodes(objectList.get(0).getObjectCode());
            message.setObjectNames(objectList.get(0).getObjectName());
        }
        // todo 暂时注释
//        sendContractOrder(message);
        OverhaulPlanReqDTO t11map = new OverhaulPlanReqDTO();
        t11map.setRecId(dmer11.getRecId());
        t11map.setArchiveFlag("2");
        t11map.setRecRevisor(TokenUtils.getCurrentPersonId());
        t11map.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulPlanMapper.modifyOverhaulPlan(t11map);
    }

    public String sendContractOrder(Message json) throws Exception {
        // IorderRecordCreator sendContractOrder
        Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode("dm.contextPath", "01");
        if (Objects.isNull(dictionaries)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        String url = dictionaries.getItemEname();
        com.wzmtr.eam.soft.csm.planWork.vo.RequestMessage requestMessage = new com.wzmtr.eam.soft.csm.planWork.vo.RequestMessage();
        requestMessage.setMessage(json);
        requestMessage.setVerb("Get");
        requestMessage.setNoun("faultInfo");
        URL wsdlLocation = new URL(url);
        com.wzmtr.eam.soft.csm.planWork.service.impl.ISetEamplanwork serverData = (new com.wzmtr.eam.soft.csm.planWork.vo.SetEamplanworkImplService(wsdlLocation)).getSetEamplanworkImplPort();
        com.wzmtr.eam.soft.csm.planWork.vo.ResponseMessage responseMessage = serverData.setEamplanwork(requestMessage);
        return JSON.toJSONString(responseMessage);
    }

    public void insertInspectObject(String planCode, String orderCode) {
        List<OverhaulObjectResDTO> objects = overhaulPlanMapper.listOverhaulObject(planCode, null, null, null, null, null);
        for (OverhaulObjectResDTO object : objects) {
            String dmer22uuid = TokenUtils.getUuId();
            List<OverhaulTplDetailResDTO> objectIsValid = overhaulTplMapper.listOverhaulTplDetail(object.getTemplateId());
            if (StringUtils.isNotEmpty(objectIsValid)) {
                String objectCode = object.getObjectCode();
                String objectName = object.getObjectName();
                String templateId = object.getTemplateId();
                insertInspectObjectItem(orderCode, objectCode, objectName, templateId, dmer22uuid);
                try {
                    List<OverhaulObjectResDTO> list = overhaulPlanMapper.listOverhaulObject(planCode, object.getRecId(), null, objectCode, null, null);
                    if (StringUtils.isNotEmpty(list)) {
                        for (OverhaulObjectResDTO resDTO : list) {
                            resDTO.setRecCreator(TokenUtils.getCurrentPersonId());
                            resDTO.setRecCreateTime(DateUtils.getCurrentTime());
                            resDTO.setRecRevisor("");
                            resDTO.setRecReviseTime("");
                            OverhaulOrderDetailReqDTO reqDTO = new OverhaulOrderDetailReqDTO();
                            BeanUtils.copyProperties(resDTO, reqDTO);
                            reqDTO.setOrderCode(orderCode);
                            reqDTO.setRecId(dmer22uuid);
                            reqDTO.setStartTime(" ");
                            reqDTO.setCompliteTime(" ");
                            overhaulOrderMapper.addOverhaulOrderDetail(reqDTO);
                        }
                    }
                } catch (Exception e) {
                    log.error("exception message", e);
                }
            }
        }
    }

    public void insertInspectObjectItem(String orderCode, String objectCode, String objectName, String templateId, String dmer22uuid) {
        OverhaulItemReqDTO reqDTO = new OverhaulItemReqDTO();
        reqDTO.setOrderCode(orderCode);
        reqDTO.setObjectCode(objectCode);
        reqDTO.setObjectName(objectName);
        reqDTO.setObjectId(dmer22uuid);
        reqDTO.setErrorFlag("1");
        try {
            List<OverhaulTplDetailResDTO> overhaulTplDetailList = overhaulTplMapper.listOverhaulTplDetail(templateId);
            if (StringUtils.isNotEmpty(overhaulTplDetailList)) {
                for (OverhaulTplDetailResDTO overhaulTplDetail : overhaulTplDetailList) {
                    BeanUtils.copyProperties(overhaulTplDetail, reqDTO);
                    reqDTO.setTdmer02Id(overhaulTplDetail.getRecId());
                    reqDTO.setRecId(TokenUtils.getUuId());
                    overhaulItemMapper.insert(reqDTO);
                }
            }
        } catch (Exception e) {
            log.error("exception message", e);
        }
    }

    public void insertWorker(String planCode, String orderCode) {
        OverhaulWorkRecordReqDTO dmer24 = new OverhaulWorkRecordReqDTO();
        OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
        overhaulPlanListReqDTO.setPlanCode(planCode);
        List<OverhaulPlanResDTO> list = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        String workCode = list.get(0).getWorkerCode();
        dmer24.setPlanCode(planCode);
        dmer24.setOrderCode(orderCode);
        dmer24.setWorkerGroupCode(list.get(0).getWorkerGroupCode());
        if (workCode.length() > CommonConstants.TWO) {
            String[] workerCodes = workCode.split(",");
            for (String workerCode : workerCodes) {
                dmer24.setRecId(TokenUtils.getUuId());
                dmer24.setWorkerCode(workerCode);
                try {
                    overhaulWorkRecordMapper.insert(dmer24);
                } catch (Exception e) {
                    log.error("exception message", e);
                }
            }
        }
    }

    @Override
    public Page<OverhaulPlanResDTO> pageOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, PageReqDTO pageReqDTO) {
        if (StringUtils.isEmpty(overhaulPlanListReqDTO.getWeekPlanCode())) {
            overhaulPlanListReqDTO.setWeekPlanCode("flag");
        }
        overhaulPlanListReqDTO.setObjectFlag("1");
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulPlanMapper.pageOverhaulPlan(pageReqDTO.of(), overhaulPlanListReqDTO);
    }

    @Override
    public OverhaulPlanResDTO getOverhaulPlanDetail(String id) {
        return overhaulPlanMapper.getOverhaulPlanDetail(id, "1");
    }

    @Override
    public void addOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) {
        if (overhaulPlanReqDTO.getWeekPlanCode() == null) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "新增检修计划必须双击检修周计划主表！");
        }
        OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO = new OverhaulWeekPlanListReqDTO();
        overhaulWeekPlanListReqDTO.setWeekPlanCode(overhaulPlanReqDTO.getWeekPlanCode());
        overhaulWeekPlanListReqDTO.setTrialStatus("10");
        List<OverhaulWeekPlanResDTO> weekPlanList = checkTrialStatus(overhaulWeekPlanListReqDTO);
        overhaulPlanReqDTO.setRecId(TokenUtils.getUuId());
        overhaulPlanReqDTO.setTrialStatus(" ");
        overhaulPlanReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        overhaulPlanReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        String planCode = overhaulPlanMapper.getMaxCode();
        if (StringUtils.isEmpty(planCode)) {
            planCode = "JX00000000";
        }
        planCode = CodeUtils.getNextCode(planCode, 2);
        overhaulPlanReqDTO.setPlanCode(planCode);
        overhaulPlanReqDTO.setExt1(" ");
        overhaulPlanReqDTO.setRelationCode(" ");
        overhaulPlanReqDTO.setWorkFlowInstId(" ");
        overhaulPlanReqDTO.setWorkFlowInstStatus(" ");
        overhaulPlanReqDTO.setTrigerTime("0");
        overhaulPlanReqDTO.setLastActionTime("0");
        overhaulPlanReqDTO.setNodeLevel(0);
        overhaulPlanReqDTO.setParentNodeRecId(" ");
        overhaulPlanReqDTO.setCountFlag(0);
        overhaulPlanReqDTO.setCount(0);
        if (Objects.isNull(overhaulPlanReqDTO.getPosition1Name())) {
            overhaulPlanReqDTO.setPosition1Name(" ");
        }
        if (Objects.isNull(overhaulPlanReqDTO.getPosition1Code())) {
            overhaulPlanReqDTO.setPosition1Code(" ");
        }
        overhaulPlanReqDTO.setSubjectCode(weekPlanList.get(0).getSubjectCode());
        overhaulPlanReqDTO.setWorkerGroupCode(weekPlanList.get(0).getWorkerGroupCode());
        overhaulPlanReqDTO.setWorkerCode(weekPlanList.get(0).getWorkerCode());
        overhaulPlanReqDTO.setWorkerName(weekPlanList.get(0).getWorkerName());
        overhaulPlanMapper.addOverhaulPlan(overhaulPlanReqDTO);
    }

    @Override
    public void modifyOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) {
        OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO = new OverhaulWeekPlanListReqDTO();
        overhaulWeekPlanListReqDTO.setWeekPlanCode(overhaulPlanReqDTO.getWeekPlanCode());
        overhaulWeekPlanListReqDTO.setTrialStatus("10");
        List<OverhaulWeekPlanResDTO> weekPlanList = checkTrialStatus(overhaulWeekPlanListReqDTO);
        overhaulPlanReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulPlanReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulPlanReqDTO.setExt1(" ");
        overhaulPlanReqDTO.setSubjectCode(weekPlanList.get(0).getSubjectCode());
        overhaulPlanReqDTO.setWorkerGroupCode(weekPlanList.get(0).getWorkerGroupCode());
        overhaulPlanReqDTO.setWorkerCode(weekPlanList.get(0).getWorkerCode());
        overhaulPlanReqDTO.setWorkerName(weekPlanList.get(0).getWorkerName());
        overhaulPlanMapper.modifyOverhaulPlan(overhaulPlanReqDTO);
    }

    @Override
    public void deleteOverhaulPlan(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                OverhaulPlanResDTO resDTO = overhaulPlanMapper.getOverhaulPlanDetail(id, "1");
                OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO = new OverhaulWeekPlanListReqDTO();
                overhaulWeekPlanListReqDTO.setWeekPlanCode(resDTO.getWeekPlanCode());
                overhaulWeekPlanListReqDTO.setTrialStatus("10");
                checkTrialStatus(overhaulWeekPlanListReqDTO);
                overhaulPlanMapper.deleteOverhaulPlanDetail(null, id, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
                overhaulPlanMapper.deleteOverhaulPlan(id, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
                if (StringUtils.isNotBlank(resDTO.getWorkFlowInstId())) {
                    BpmnExamineDTO bpmnExamineDTO = new BpmnExamineDTO();
                    bpmnExamineDTO.setTaskId(resDTO.getWorkFlowInstId());
                    bpmnService.rejectInstance(bpmnExamineDTO);
                }
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importOverhaulPlan(MultipartFile file) {
        // todo excel导入
    }

    @Override
    public void exportOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(overhaulPlanListReqDTO.getWeekPlanCode())) {
            overhaulPlanListReqDTO.setWeekPlanCode("flag");
        }
        overhaulPlanListReqDTO.setObjectFlag("1");
        List<OverhaulPlanResDTO> overhaulTplResDTOList = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        if (overhaulTplResDTOList != null && !overhaulTplResDTOList.isEmpty()) {
            List<ExcelOverhaulPlanResDTO> list = new ArrayList<>();
            for (OverhaulPlanResDTO resDTO : overhaulTplResDTOList) {
                ExcelOverhaulPlanResDTO res = new ExcelOverhaulPlanResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "检修计划（中车）信息", list);
        }
    }

    @Override
    public List<OverhaulTplDetailResDTO> getTemplates(String planCode) {
        List<OverhaulTplDetailResDTO> resList = new ArrayList<>();
        OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
        overhaulPlanListReqDTO.setPlanCode(planCode);
        List<OverhaulPlanResDTO> list = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        if (StringUtils.isNotEmpty(list)) {
            resList = overhaulTplMapper.queryTemplate(list.get(0).getLineNo(), list.get(0).getSubjectCode(),
                    list.get(0).getSystemCode(), list.get(0).getEquipTypeCode(), "30");
        }
        return resList;
    }

    public List<OverhaulWeekPlanResDTO> checkTrialStatus(OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO) {
        List<OverhaulWeekPlanResDTO> queryCount = overhaulWeekPlanMapper.listOverhaulWeekPlan(overhaulWeekPlanListReqDTO);
        if (StringUtils.isEmpty(queryCount)) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作！");
        }
        return queryCount;
    }

    @Override
    public Page<OverhaulObjectResDTO> pageOverhaulObject(String planCode, String planName, String objectCode, String objectName, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulPlanMapper.pageOverhaulObject(pageReqDTO.of(), planCode, null, planName, objectCode, objectName, null);
    }

    @Override
    public OverhaulObjectResDTO getOverhaulObjectDetail(String id) {
        return overhaulPlanMapper.getOverhaulObjectDetail(id);
    }

    @Override
    public void addOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO) {
        if (overhaulObjectReqDTO.getPlanCode() == null) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "新增检修对象必须双击检修计划");
        }
        OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO = new OverhaulWeekPlanListReqDTO();
        overhaulWeekPlanListReqDTO.setWeekPlanCode(overhaulObjectReqDTO.getWeekPlanCode());
        overhaulWeekPlanListReqDTO.setTrialStatus("10");
        checkTrialStatus(overhaulWeekPlanListReqDTO);
        overhaulObjectReqDTO.setPlanCode(overhaulObjectReqDTO.getPlanCode());
        overhaulObjectReqDTO.setRecId(TokenUtils.getUuId());
        overhaulObjectReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        overhaulObjectReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        overhaulObjectReqDTO.setObjectName(detailJudge(overhaulObjectReqDTO));
        overhaulPlanMapper.addOverhaulObject(overhaulObjectReqDTO);
    }

    @Override
    public void modifyOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO) {
        OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO = new OverhaulWeekPlanListReqDTO();
        overhaulWeekPlanListReqDTO.setWeekPlanCode(overhaulObjectReqDTO.getWeekPlanCode());
        overhaulWeekPlanListReqDTO.setTrialStatus("10");
        checkTrialStatus(overhaulWeekPlanListReqDTO);
        overhaulObjectReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulObjectReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulPlanMapper.modifyOverhaulObject(overhaulObjectReqDTO);
    }

    @Override
    public void deleteOverhaulObject(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                OverhaulObjectResDTO resDTO = overhaulPlanMapper.getOverhaulObjectDetail(id);
                OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
                overhaulPlanListReqDTO.setPlanCode(resDTO.getPlanCode());
                List<OverhaulPlanResDTO> list = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
                if (StringUtils.isNotEmpty(list)) {
                    OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO = new OverhaulWeekPlanListReqDTO();
                    overhaulWeekPlanListReqDTO.setWeekPlanCode(list.get(0).getWeekPlanCode());
                    overhaulWeekPlanListReqDTO.setTrialStatus("10");
                    checkTrialStatus(overhaulWeekPlanListReqDTO);
                    overhaulPlanMapper.deleteOverhaulObject(id, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
                }
            }
        }
    }

    @Override
    public void exportOverhaulObject(String planCode, String planName, String objectCode, String objectName, HttpServletResponse response) throws IOException {
        List<OverhaulObjectResDTO> overhaulObjectResDTOList = overhaulPlanMapper.listOverhaulObject(planCode, null, planName, objectCode, objectName, null);
        if (overhaulObjectResDTOList != null && !overhaulObjectResDTOList.isEmpty()) {
            List<ExcelOverhaulPlanObjectResDTO> list = new ArrayList<>();
            for (OverhaulObjectResDTO resDTO : overhaulObjectResDTOList) {
                ExcelOverhaulPlanObjectResDTO res = new ExcelOverhaulPlanObjectResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "检修对象（中铁通）信息", list);
        }
    }

    public String detailJudge(OverhaulObjectReqDTO overhaulObjectReqDTO) {
        if (StringUtils.isEmpty(overhaulObjectReqDTO.getObjectCode())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "在" + overhaulObjectReqDTO.getPlanName() + "计划中，有对象编号为空");
        }
        OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
        overhaulPlanListReqDTO.setPlanCode(overhaulObjectReqDTO.getPlanCode());
        List<OverhaulPlanResDTO> planList = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        if (StringUtils.isEmpty(planList)){
            return null;
        }
        if (CommonConstants.CAR_SUBJECT_CODE.equals(planList.get(0).getSubjectCode())) {
            List<OverhaulObjectResDTO> objectList = overhaulPlanMapper.listOverhaulObject(overhaulObjectReqDTO.getPlanCode(), null, null, null, null, null);
            if (StringUtils.isNotEmpty(objectList)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "车辆专业的计划只能有一条设备对象！");
            }
        }
        String subjectCode = Optional.ofNullable(planList.get(0).getSubjectCode()).orElse(CommonConstants.BLANK);
        String systemCode = "";
        String equipTypeCode = "";
        if (StringUtils.isNotEmpty(planList.get(0).getSystemCode())) {
            systemCode = planList.get(0).getSystemCode().trim();
        }
        if (StringUtils.isNotEmpty(planList.get(0).getEquipTypeCode())) {
            equipTypeCode = planList.get(0).getEquipTypeCode().trim();
        }
        String objectName = getEquipNameByCodeAndSubjects(overhaulObjectReqDTO.getObjectCode(), subjectCode, systemCode, equipTypeCode);
        if (org.apache.commons.lang3.StringUtils.isBlank(objectName)) {
            List<EquipmentRoomResDTO> equipmentRoomList = equipmentRoomMapper.listEquipmentRoom(overhaulObjectReqDTO.getObjectCode(), null, null, null, null, null);
            if (StringUtils.isNotEmpty(equipmentRoomList)) {
                objectName = equipmentRoomList.get(0).getEquipRoomName();
            }
        }
        if (StringUtils.isEmpty(objectName)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "在" + overhaulObjectReqDTO.getPlanName() + "计划中，对象编码" + overhaulObjectReqDTO.getObjectCode() + "错误");
        }
        return objectName;
    }

    public String getEquipNameByCodeAndSubjects(String code, String majorCode, String systemCode, String equipTypeCode) {
        if (StringUtils.isEmpty(code.trim())) {
            return "";
        }
        EquipmentSiftReqDTO equipmentSiftReqDTO = new EquipmentSiftReqDTO();
        equipmentSiftReqDTO.setEquipCode(code);
        equipmentSiftReqDTO.setMajorCode(majorCode);
        equipmentSiftReqDTO.setSystemCode(systemCode);
        equipmentSiftReqDTO.setEquipTypeCode(equipTypeCode);
        List<EquipmentResDTO> equipMsg = equipmentMapper.siftEquipment(equipmentSiftReqDTO);
        if (equipMsg.isEmpty()) {
            return " ";
        }
        return equipMsg.get(0).getEquipName();
    }

}
