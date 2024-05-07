package com.wzmtr.eam.impl.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.req.overhaul.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.basic.WoRuleResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulPlanResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulPlanObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulPlanResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.BpmnStatus;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.WoRuleMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.overhaul.*;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.overhaul.OverhaulPlanService;
import com.wzmtr.eam.service.overhaul.OverhaulWorkRecordService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class OverhaulPlanServiceImpl implements OverhaulPlanService {

    @Autowired
    private OverhaulPlanMapper overhaulPlanMapper;

    @Autowired
    private OverhaulOrderMapper overhaulOrderMapper;

    @Autowired
    private WoRuleMapper woRuleMapper;

    @Autowired
    private OverhaulWorkRecordService overhaulWorkRecordService;

    @Autowired
    private OverhaulWorkRecordMapper overhaulWorkRecordMapper;

    @Autowired
    private OverhaulTplMapper overhaulTplMapper;

    @Autowired
    private OverhaulItemMapper overhaulItemMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private BpmnService bpmnService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private FaultQueryMapper faultQueryMapper;

    @Autowired
    private IWorkFlowLogService workFlowLogService;

    @Autowired
    private OverTodoService overTodoService;

    @Override
    public Page<OverhaulPlanResDTO> pageOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, PageReqDTO pageReqDTO) {
        overhaulPlanListReqDTO.setTrialStatus("'20','10','30'");
        overhaulPlanListReqDTO.setObjectFlag("1");
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<OverhaulPlanResDTO> page = overhaulPlanMapper.pageOverhaulPlan(pageReqDTO.of(), overhaulPlanListReqDTO);
        List<OverhaulPlanResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (OverhaulPlanResDTO res : list) {
                if(StringUtils.isEmpty(res.getWorkFlowInstStatus())){
                    res.setWorkFlowInstStatus(" ");
                }
                if (StringUtils.isNotEmpty(res.getWorkerGroupCode())) {
                    res.setWorkGroupName(organizationMapper.getNamesById(res.getWorkerGroupCode()));
                }
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public OverhaulPlanResDTO getOverhaulPlanDetail(String id) {
        OverhaulPlanResDTO res = overhaulPlanMapper.getOverhaulPlanDetail(id, "1");
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
    public void addOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) throws ParseException {
        if (org.apache.commons.lang3.StringUtils.isBlank(overhaulPlanReqDTO.getRuleCode()) || org.apache.commons.lang3.StringUtils.isBlank(overhaulPlanReqDTO.getFirstBeginTime())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选计划中有标红必填项未填写");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        if (df.parse(df.format(new Date())).getTime() >= df.parse(overhaulPlanReqDTO.getFirstBeginTime()).getTime()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "首次开始时间必须大于当前时间！");
        }
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulPlanMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        overhaulPlanReqDTO.setRecId(TokenUtils.getUuId());
        overhaulPlanReqDTO.setTrialStatus("10");
        overhaulPlanReqDTO.setPlanStatus("10");
        overhaulPlanReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        overhaulPlanReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        String code = overhaulPlanMapper.getMaxCode();
        if (StringUtils.isEmpty(code)) {
            code = "JX00000000";
        }
        String planCode = CodeUtils.getNextCode(code, 2);
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
        overhaulPlanMapper.addOverhaulPlan(overhaulPlanReqDTO);
    }

    @Override
    public void modifyOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulPlanMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        if (!CommonConstants.TEN_STRING.equals(overhaulPlanReqDTO.getTrialStatus()) && !CommonConstants.NINETY_STRING.equals(overhaulPlanReqDTO.getTrialStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(overhaulPlanReqDTO.getRuleCode()) ||
                org.apache.commons.lang3.StringUtils.isBlank(overhaulPlanReqDTO.getFirstBeginTime()) ||
                org.apache.commons.lang3.StringUtils.isBlank(overhaulPlanReqDTO.getPlanStatus())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选计划中有标红必填项未填写");
        }
        overhaulPlanReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulPlanReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulPlanReqDTO.setExt1(" ");
        overhaulPlanMapper.modifyOverhaulPlan(overhaulPlanReqDTO);
    }

    @Override
    public void deleteOverhaulPlan(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                OverhaulPlanResDTO resDTO = overhaulPlanMapper.getOverhaulPlanDetail(id, "1");
                if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
                    if (Objects.isNull(resDTO.getSubjectCode())) {
                        throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                    }
                    List<String> code = overhaulPlanMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
                    if (Objects.isNull(code) || code.isEmpty() || !code.contains(resDTO.getSubjectCode())) {
                        throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                    }
                }
                if (!CommonConstants.TEN_STRING.equals(resDTO.getTrialStatus()) && !"90".equals(resDTO.getTrialStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
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
    public void triggerOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) {
        if (!CommonConstants.THIRTY_STRING.equals(overhaulPlanReqDTO.getTrialStatus())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "检修计划未审批完成，无法触发");
        }
        if (checkHasNotOrder(overhaulPlanReqDTO.getPlanCode())) {
            if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
                if (Objects.isNull(overhaulPlanReqDTO.getSubjectCode())) {
                    throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                }
                List<String> code = overhaulPlanMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
                if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulPlanReqDTO.getSubjectCode())) {
                    throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                }
            }
            triggerOne(overhaulPlanReqDTO.getPlanCode());
        } else {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "选择触发的检修计划中存在未做完的工单，请优先做完工单后再进行触发。");
        }
    }

    @Override
    public void submitOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) throws Exception {
        _check(overhaulPlanReqDTO);
        //为啥要设置删除时间？？
        overhaulPlanReqDTO.setRecDeleteTime(overhaulPlanReqDTO.getOpinion());
        submitOrderPlan(overhaulPlanReqDTO);
    }

    private void _check(OverhaulPlanReqDTO overhaulPlanReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulPlanMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        if (!CommonConstants.TEN_STRING.equals(overhaulPlanReqDTO.getTrialStatus()) && !CommonConstants.NINETY_STRING.equals(overhaulPlanReqDTO.getTrialStatus())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "只有编辑和驳回状态的数据才能够进行送审！");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(overhaulPlanReqDTO.getRuleCode())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选计划中有标红必填项未填写");
        }
        List<OverhaulObjectResDTO> list12 = overhaulPlanMapper.listOverhaulObject(overhaulPlanReqDTO.getPlanCode(), null, null, null, null, "flag");
        if (list12 == null || list12.size() <= 0) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选计划中没有检修对象和检修模板！");
        }
    }

    @Override
    public void examineOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) {
        workFlowLogService.ifReviewer(overhaulPlanReqDTO.getWorkFlowInstId());
        String opinion = overhaulPlanReqDTO.getExamineReqDTO().getOpinion();
        String recId = overhaulPlanReqDTO.getRecId();
        if (overhaulPlanReqDTO.getExamineReqDTO().getExamineStatus() == 0) {
            if (CommonConstants.THIRTY_STRING.equals(overhaulPlanReqDTO.getTrialStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_DONE);
            }
            if (CommonConstants.TEN_STRING.equals(overhaulPlanReqDTO.getTrialStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_NOT_DONE);
            }
            String processId = overhaulPlanReqDTO.getWorkFlowInstId();
            String taskId = bpmnService.queryTaskIdByProcId(processId);
            //TODO
            //bpmnService.agree(taskId, opinion, null, "{\"id\":\"" + overhaulPlanReqDTO.getPlanCode() + "\"}", null);
            overhaulPlanReqDTO.setWorkFlowInstStatus("已完成");
            overhaulPlanReqDTO.setTrialStatus("30");
            overTodoService.overTodo(recId,opinion);
            // 记录日志
            workFlowLogService.add(WorkFlowLogBO.builder()
                    .status(BpmnStatus.PASS.getDesc())
                    .userIds(overhaulPlanReqDTO.getExamineReqDTO().getUserIds())
                    .workFlowInstId(processId)
                    .build());
        } else {
            if (!CommonConstants.TWENTY_STRING.equals(overhaulPlanReqDTO.getTrialStatus())) {
                throw new CommonException(ErrorCode.REJECT_ERROR);
            } else {
                String processId = overhaulPlanReqDTO.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.reject(taskId, opinion);
                overTodoService.cancelTodo(recId);
                overhaulPlanReqDTO.setWorkFlowInstId("");
                overhaulPlanReqDTO.setWorkFlowInstStatus("");
                overhaulPlanReqDTO.setTrialStatus("10");
                // 记录日志
                workFlowLogService.add(WorkFlowLogBO.builder()
                        .status(BpmnStatus.REJECT.getDesc())
                        .userIds(overhaulPlanReqDTO.getExamineReqDTO().getUserIds())
                        .workFlowInstId(processId)
                        .build());
            }
        }
        overhaulPlanReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulPlanReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulPlanReqDTO.setExt1(" ");
        overhaulPlanMapper.modifyOverhaulPlan(overhaulPlanReqDTO);
    }

    @Override
    public void relationOverhaulPlan(List<OverhaulPlanReqDTO> list) {
        StringBuilder planCodeAndIn = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (!CommonConstants.THIRTY_STRING.equals(list.get(i).getTrialStatus())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "只有审批通过状态的数据才能够进行关联");
            }
            if ("关联".equals(list.get(i).getRelationCode())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "已经是关联计划的选项不能重复进行关联");
            }
            List<WoRuleResDTO> ruleList = woRuleMapper.listWoRule(list.get(i).getRuleCode(), null, null);
            if (ruleList.size() != 1) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "所有的关联计划的规则明细必须为一条");
            }
            if (i == 0) {
                planCodeAndIn = new StringBuilder("'" + list.get(i).getPlanCode() + "'");
            } else {
                planCodeAndIn.append(",'").append(list.get(i).getPlanCode()).append("'");
            }
        }
        List<WoRuleResDTO.WoRuleDetail> dmer21TopOrder = overhaulPlanMapper.queryAllRule(String.valueOf(planCodeAndIn));
        String relationCode = TokenUtils.getUuId();
        for (int j = 0; j < dmer21TopOrder.size(); j++) {
            OverhaulPlanReqDTO reqDTO = getOverhaulPlan(dmer21TopOrder, j, relationCode);
            overhaulPlanMapper.modifyOverhaulPlan(reqDTO);
        }
    }

    @NotNull
    private static OverhaulPlanReqDTO getOverhaulPlan(List<WoRuleResDTO.WoRuleDetail> dmer21TopOrder, int j, String relationCode) {
        OverhaulPlanReqDTO reqDTO = new OverhaulPlanReqDTO();
        reqDTO.setRecId(dmer21TopOrder.get(j).getRecId());
        reqDTO.setRelationCode(relationCode);
        reqDTO.setNodeLevel(dmer21TopOrder.size() - j);
        if (j == 0) {
            reqDTO.setCountFlag(1);
        } else {
            long max = dmer21TopOrder.get(j - 1).getPeriod();
            long min = dmer21TopOrder.get(j).getPeriod();
            if (max % min != 0 || max == min) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "所有关联计划之间的规则周期必须为倍数关系");
            }
            long max1 = dmer21TopOrder.get(j - 1).getPeriod();
            long min1 = dmer21TopOrder.get(j).getPeriod();
            reqDTO.setCountFlag(Math.toIntExact(max1 / min1 - 1));
            reqDTO.setParentNodeRecId(dmer21TopOrder.get(j - 1).getExt1());
        }
        return reqDTO;
    }

    @Override
    public void switchsOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulPlanMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(overhaulPlanReqDTO.getPlanStatus())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选的检修计划中计划状态不能为空！");
        }
        overhaulPlanMapper.updatePlanSta(overhaulPlanReqDTO);
    }

    @Override
    public void importOverhaulPlan(MultipartFile file) {
        // todo excel导入
    }

    @Override
    public void exportOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, HttpServletResponse response) throws IOException {
        overhaulPlanListReqDTO.setTrialStatus("'20','10','30'");
        overhaulPlanListReqDTO.setObjectFlag("1");
        List<OverhaulPlanResDTO> overhaulTplResDTOList = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        if (overhaulTplResDTOList != null && !overhaulTplResDTOList.isEmpty()) {
            List<ExcelOverhaulPlanResDTO> list = new ArrayList<>();
            for (OverhaulPlanResDTO resDTO : overhaulTplResDTOList) {
                ExcelOverhaulPlanResDTO res = new ExcelOverhaulPlanResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setLineNo(CommonConstants.LINE_CODE_ONE.equals(resDTO.getLineNo()) ? "S1线" : "S2线");
                res.setTrialStatus(CommonConstants.TEN_STRING.equals(resDTO.getTrialStatus()) ? "编辑" : CommonConstants.TWENTY_STRING.equals(resDTO.getTrialStatus()) ? "审核中" : "审核通过");
                res.setWorkerGroupCode(organizationMapper.getNamesById(resDTO.getWorkerGroupCode()));
                res.setPlanStatus(CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus()) ? "启用" : "禁用");
                list.add(res);
            }
            EasyExcelUtils.export(response, "检修计划（中车）信息", list);
        }
    }

    public boolean checkHasNotOrder(String planCode) {
        OverhaulOrderListReqDTO overhaulOrderListReqDTO = new OverhaulOrderListReqDTO();
        overhaulOrderListReqDTO.setPlanCode(planCode);
        overhaulOrderListReqDTO.setNewTime("flag");
        List<OverhaulOrderResDTO> list = overhaulOrderMapper.listOverhaulOrder(overhaulOrderListReqDTO);
        return StringUtils.isEmpty(list);
    }

    public void triggerOne(String planCode) {
        if (checkHasNotOrder(planCode)) {
            createInsepectRecordByPlanCode(new String[]{planCode});
            String str = querySonOrder(planCode);
            if (StringUtils.isNotEmpty(str) && checkHasNotOrder(str)) {
                triggerOne(str);
            }
        }
    }

    public String querySonOrder(String planCode) {
        OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
        overhaulPlanListReqDTO.setTrialStatus1("30");
        overhaulPlanListReqDTO.setFirstBegin("flag");
        overhaulPlanListReqDTO.setParentNode(planCode);
        List<OverhaulPlanResDTO> dmer11List = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        if (StringUtils.isNotEmpty(dmer11List)) {
            return dmer11List.get(0).getPlanCode();
        }
        return null;
    }

    public void createInsepectRecordByPlanCode(String[] planCodes) {
        String flag = "0";
        if (planCodes.length > 1 && CommonConstants.ONE_STRING.equals(planCodes[1])) {
            flag = "1";
        }
        String planCode = planCodes[0];
        List<OverhaulTplDetailResDTO> tplDetailList = overhaulPlanMapper.getOrderIsValid(planCode);
        if (StringUtils.isEmpty(tplDetailList)) {
            return;
        }
        String orderCode = overhaulOrderMapper.getMaxCode();
        if (StringUtils.isEmpty(orderCode) || !orderCode.substring(CommonConstants.TWO, CommonConstants.TEN).equals(DateUtils.getNoDate())) {
            orderCode = "JX" + DateUtils.getNoDate() + "0001";
        } else {
            orderCode = CodeUtils.getNextCode(orderCode, 10);
        }
        try {
            insertInspectPlan1(planCode, new String[]{orderCode, flag});
            insertInspectObject(planCode, orderCode);
            insertWorker(planCode, orderCode);
        } catch (Exception e) {
            log.error("exception message", e);
        }
        overhaulPlanMapper.updateTrigerTime(DateUtils.getCurrentTime(), planCode);
    }

    public void insertInspectPlan1(String planCode, String[] orderCodes) throws Exception {
        String orderCode = orderCodes[0];
        OverhaulPlanListReqDTO selectMap = new OverhaulPlanListReqDTO();
        selectMap.setPlanCode(planCode);
        OverhaulOrderReqDTO insertMap = new OverhaulOrderReqDTO();
        insertMap.setOrderCode(orderCode);

        insertMap.setWorkStatus("1");

        String trigerTime = "";
        OverhaulPlanListReqDTO queryMap1 = new OverhaulPlanListReqDTO();
        queryMap1.setPlanCode(planCode);
        List<OverhaulPlanResDTO> list11 = overhaulPlanMapper.listOverhaulPlan(queryMap1);
        if (StringUtils.isNotEmpty(list11) && !list11.get(0).getWorkerGroupCode().trim().isEmpty()) {
            trigerTime = list11.get(0).getTrigerTime();
            insertMap.setWorkerGroupCode(list11.get(0).getWorkerGroupCode());
            insertMap.setWorkerCode(TokenUtils.getCurrentPersonId());
            insertMap.setWorkerName(TokenUtils.getCurrentPerson().getPersonName());
            OverhaulOrderReqDTO dmer21 = buildOverhaulOrder(planCode, orderCode, list11);

            try {
                overhaulWorkRecordService.insertRepair(dmer21);
            } catch (Exception e) {
                log.error("exception message", e);
            }
        }
        insertMap.setRealStartTime(" ");
        insertMap.setRealEndTime(" ");
        insertMap.setExt1(" ");
        buildOverhaulOrderPlanStartTime(planCode, orderCodes, insertMap, trigerTime);
        addOverhaulOrder(queryMap1, insertMap);
    }

    /**
     * 组装检修工单信息
     * @param planCode 检修计划号
     * @param orderCode 检修工单号
     * @param list11 检修计划信息
     * @return 检修工单信息
     */
    @NotNull
    private OverhaulOrderReqDTO buildOverhaulOrder(String planCode, String orderCode, List<OverhaulPlanResDTO> list11) {
        OverhaulOrderReqDTO dmer21 = new OverhaulOrderReqDTO();
        dmer21.setOrderCode(orderCode);
        dmer21.setPlanCode(planCode);
        dmer21.setWorkerGroupCode(list11.get(0).getWorkerGroupCode());
        dmer21.setWorkerCode(TokenUtils.getCurrentPersonId());
        dmer21.setWorkerName(TokenUtils.getCurrentPerson().getPersonName());
        dmer21.setRecId(dmer21.getOrderCode());
        dmer21.setWorkStatus("1");
        dmer21.setSubjectCode(list11.get(0).getSubjectCode());
        dmer21.setLineNo(list11.get(0).getLineNo());
        return dmer21;
    }

    /**
     * 组装检修工单计划开始时间
     * @param planCode 检修计划号
     * @param orderCodes 检修工单号数组
     * @param insertMap 检修工单信息
     * @param trigerTime 触发时间
     * @throws ParseException 异常
     */
    private void buildOverhaulOrderPlanStartTime(String planCode, String[] orderCodes, OverhaulOrderReqDTO insertMap, String trigerTime) throws ParseException {
        if (orderCodes.length > 1) {
            if (CommonConstants.ONE_STRING.equals(orderCodes[1])) {
                insertMap.setPlanStartTime(orderCodes[0].substring(CommonConstants.TWO, CommonConstants.TEN));
            } else {
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd");
                String nowDate = dateTimeFormat.format(new Date());
                List<WoRuleResDTO.WoRuleDetail> ruleList = woRuleMapper.queryRuleList(planCode, nowDate.substring(nowDate.length() - 4));
                if (StringUtils.isNotEmpty(ruleList) && StringUtils.isNotNull(ruleList.get(0).getBeforeTime())) {
                    long beforeDay = ruleList.get(0).getBeforeTime();
                    if (StringUtils.isEmpty(trigerTime) || CommonConstants.ZERO_STRING.equals(trigerTime)) {
                        trigerTime = orderCodes[0].substring(CommonConstants.TWO, CommonConstants.TEN);
                    } else {
                        trigerTime = trigerTime.substring(0, 8);
                    }
                    Date date = dateTimeFormat.parse(trigerTime);
                    Calendar ca = Calendar.getInstance();
                    ca.setTime(date);
                    ca.add(Calendar.DAY_OF_YEAR, Math.toIntExact(beforeDay));
                    insertMap.setPlanStartTime(dateTimeFormat.format(ca.getTime()));
                } else {
                    insertMap.setPlanStartTime(orderCodes[0].substring(CommonConstants.TWO, CommonConstants.TEN));
                }
            }
        } else {
            insertMap.setPlanStartTime(orderCodes[0].substring(CommonConstants.TWO, CommonConstants.TEN));
        }
    }

    /**
     * 新增检修工单
     * @param overhaulPlanList 检修计划信息
     * @param overhaulOrder 检修工单信息
     */
    public void addOverhaulOrder(OverhaulPlanListReqDTO overhaulPlanList, OverhaulOrderReqDTO overhaulOrder) {
        try {
            List<OverhaulPlanResDTO> planList = overhaulPlanMapper.listOverhaulPlan(overhaulPlanList);
            if (StringUtils.isNotEmpty(planList)) {
                int i = 0;
                for (OverhaulPlanResDTO plan : planList) {
                    plan.setRecCreator(TokenUtils.getCurrentPersonId());
                    plan.setRecCreateTime(DateUtils.getCurrentTime());
                    plan.setRecRevisor("");
                    plan.setRecReviseTime("");
                    BeanUtils.copyProperties(plan, overhaulOrder);
                    overhaulOrder.setRecId(TokenUtils.getUuId());
                    if (i > 0) {
                        overhaulOrder.setOrderCode(CodeUtils.getNextCodeByAddNum(overhaulOrder.getOrderCode(), 10, i));
                    }
                    overhaulOrderMapper.addOverhaulOrder(overhaulOrder);
                    i++;
                }
            }
        } catch (Exception e) {
            log.error("exception message", e);
        }
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
                    object.setRecCreator(TokenUtils.getCurrentPersonId());
                    object.setRecCreateTime(DateUtils.getCurrentTime());
                    object.setRecRevisor("");
                    object.setRecReviseTime("");
                    OverhaulOrderDetailReqDTO reqDTO = new OverhaulOrderDetailReqDTO();
                    BeanUtils.copyProperties(object, reqDTO);
                    reqDTO.setOrderCode(orderCode);
                    reqDTO.setRecId(dmer22uuid);
                    reqDTO.setStartTime(" ");
                    reqDTO.setCompliteTime(" ");
                    overhaulOrderMapper.addOverhaulOrderDetail(reqDTO);
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

    public void submitOrderPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) throws Exception {
        // OrderPlan  submitOrderPlan
        // /iam/org/getZcOverhaulPlanExamineUser接口获取审核人
        List<String> userIds = overhaulPlanReqDTO.getExamineReqDTO().getUserIds();
        String planCode = overhaulPlanReqDTO.getPlanCode();
        String recId = overhaulPlanReqDTO.getRecId();
        String currentPersonId = TokenUtils.getCurrentPersonId();
        String processId = bpmnService.commit(planCode, BpmnFlowEnum.ORDER_PLAN_SUBMIT.value(), null, null, userIds, null);
        if (processId == null || CommonConstants.PROCESS_ERROR_CODE.equals(processId)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败");
        }
        overhaulPlanReqDTO.setWorkFlowInstId(processId);
        overhaulPlanReqDTO.setWorkFlowInstStatus(roleMapper.getSubmitNodeId(BpmnFlowEnum.ORDER_PLAN_SUBMIT.value(), null));
        overhaulPlanReqDTO.setTrialStatus("20");
        overhaulPlanReqDTO.setRecRevisor(currentPersonId);
        overhaulPlanReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulPlanReqDTO.setExt1(" ");
        overhaulPlanMapper.modifyOverhaulPlan(overhaulPlanReqDTO);
        // 添加待办
        overTodoService.insertTodoWithUserList(userIds, "收到一条检修计划编号为：" + planCode + "的审批流程", recId, planCode, "检修计划审核", "?", currentPersonId, null, BpmnFlowEnum.ORDER_PLAN_SUBMIT.value());
        // 记录日志
        workFlowLogService.add(WorkFlowLogBO.builder().status(BpmnStatus.SUBMIT.getDesc()).userIds(userIds).workFlowInstId(processId).build());
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
        OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
        overhaulPlanListReqDTO.setPlanCode(overhaulObjectReqDTO.getPlanCode());
        overhaulPlanListReqDTO.setTrialStatus("'10'");
        List<OverhaulPlanResDTO> list = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        if (StringUtils.isEmpty(list)) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
        }
        overhaulObjectReqDTO.setRecId(TokenUtils.getUuId());
        overhaulObjectReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        overhaulObjectReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        if ("".equals(overhaulObjectReqDTO.getObjectCode())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "在" + overhaulObjectReqDTO.getPlanName() + "计划中，有对象编号为空");
        }
        overhaulPlanMapper.addOverhaulObject(overhaulObjectReqDTO);
    }

    @Override
    public void modifyOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO) {
        OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
        overhaulPlanListReqDTO.setPlanCode(overhaulObjectReqDTO.getPlanCode());
        overhaulPlanListReqDTO.setTrialStatus("'10'");
        List<OverhaulPlanResDTO> list = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        if (StringUtils.isEmpty(list)) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
        }
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
                overhaulPlanListReqDTO.setTrialStatus("'10'");
                List<OverhaulPlanResDTO> list = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
                if (StringUtils.isEmpty(list)) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
                }
                overhaulPlanMapper.deleteOverhaulObject(id, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
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
            EasyExcelUtils.export(response, "检修对象（中车）信息", list);
        }
    }

}
