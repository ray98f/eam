package com.wzmtr.eam.impl.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.req.overhaul.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.basic.WoRuleResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulPlanResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplDetailResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.WoRuleMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.overhaul.*;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.overhaul.OverhaulPlanService;
import com.wzmtr.eam.service.overhaul.OverhaulWorkRecordService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

    @Override
    public Page<OverhaulPlanResDTO> pageOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, PageReqDTO pageReqDTO) {
        overhaulPlanListReqDTO.setTrialStatus("'20','10','30'");
        overhaulPlanListReqDTO.setObjectFlag("1");
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<OverhaulPlanResDTO> page = overhaulPlanMapper.pageOverhaulPlan(pageReqDTO.of(), overhaulPlanListReqDTO);
        List<OverhaulPlanResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (OverhaulPlanResDTO res : list) {
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
        return faultQueryMapper.queryDeptCode(lineNo, subjectCode, "20");
    }

    @Override
    public void addOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        if (df.parse(df.format(new Date())).getTime() >= df.parse(overhaulPlanReqDTO.getFirstBeginTime()).getTime()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "首次开始时间必须大于当前时间！");
        }
        if (StringUtils.isBlank(overhaulPlanReqDTO.getRuleCode()) || StringUtils.isBlank(overhaulPlanReqDTO.getFirstBeginTime())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选计划中有标红必填项未填写");
        }
        if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulPlanMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        overhaulPlanReqDTO.setRecId(TokenUtil.getUuId());
        overhaulPlanReqDTO.setTrialStatus("10");
        overhaulPlanReqDTO.setPlanStatus("10");
        overhaulPlanReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        overhaulPlanReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        String planCode = CodeUtils.getNextCode(overhaulPlanMapper.getMaxCode(), 2);
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
        if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulPlanMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        if (!CommonConstants.TEN_STRING.equals(overhaulPlanReqDTO.getTrialStatus()) && !"90".equals(overhaulPlanReqDTO.getTrialStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        if (StringUtils.isBlank(overhaulPlanReqDTO.getRuleCode()) ||
                StringUtils.isBlank(overhaulPlanReqDTO.getFirstBeginTime()) ||
                StringUtils.isBlank(overhaulPlanReqDTO.getPlanStatus())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选计划中有标红必填项未填写");
        }
        overhaulPlanReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulPlanReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulPlanReqDTO.setExt1(" ");
        overhaulPlanMapper.modifyOverhaulPlan(overhaulPlanReqDTO);
    }

    @Override
    public void deleteOverhaulPlan(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                OverhaulPlanResDTO resDTO = overhaulPlanMapper.getOverhaulPlanDetail(id, "1");
                if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
                    if (Objects.isNull(resDTO.getSubjectCode())) {
                        throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                    }
                    List<String> code = overhaulPlanMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
                    if (Objects.isNull(code) || code.isEmpty() || !code.contains(resDTO.getSubjectCode())) {
                        throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                    }
                }
                if (!CommonConstants.TEN_STRING.equals(resDTO.getTrialStatus()) && !"90".equals(resDTO.getTrialStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                overhaulPlanMapper.deleteOverhaulPlanDetail(null, id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                overhaulPlanMapper.deleteOverhaulPlan(id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
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
        if (checkHasNotOrder(overhaulPlanReqDTO.getPlanCode())) {
            if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
                if (Objects.isNull(overhaulPlanReqDTO.getSubjectCode())) {
                    throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                }
                List<String> code = overhaulPlanMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
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
        if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulPlanMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        if (!CommonConstants.TEN_STRING.equals(overhaulPlanReqDTO.getTrialStatus()) && !"90".equals(overhaulPlanReqDTO.getTrialStatus())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "只有编辑和驳回状态的数据才能够进行送审！");
        }
        if (StringUtils.isBlank(overhaulPlanReqDTO.getRuleCode())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选计划中有标红必填项未填写");
        }
        List<OverhaulObjectResDTO> list12 = overhaulPlanMapper.listOverhaulObject(overhaulPlanReqDTO.getPlanCode(), null, null, null, null, "flag");
        if (list12 == null || list12.size() <= 0) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选计划中没有检修对象和检修模板！");
        }
        overhaulPlanReqDTO.setRecDeleteTime(overhaulPlanReqDTO.getOpinion());
        submitOrderPlan(overhaulPlanReqDTO);
    }

    @Override
    public void examineOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) {
        if (overhaulPlanReqDTO.getExamineReqDTO().getExamineStatus() == 0) {
            if (CommonConstants.THIRTY_STRING.equals(overhaulPlanReqDTO.getTrialStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_DONE);
            }
            if (CommonConstants.TEN_STRING.equals(overhaulPlanReqDTO.getTrialStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_NOT_DONE);
            }
            String processId = overhaulPlanReqDTO.getWorkFlowInstId();
            String taskId = bpmnService.queryTaskIdByProcId(processId);
            bpmnService.agree(taskId, overhaulPlanReqDTO.getExamineReqDTO().getOpinion(), null, "{\"id\":\"" + overhaulPlanReqDTO.getPlanCode() + "\"}", null);
            overhaulPlanReqDTO.setWorkFlowInstStatus("已完成");
            overhaulPlanReqDTO.setTrialStatus("30");
        } else {
            if (!CommonConstants.TWENTY_STRING.equals(overhaulPlanReqDTO.getTrialStatus())) {
                throw new CommonException(ErrorCode.REJECT_ERROR);
            } else {
                String processId = overhaulPlanReqDTO.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.reject(taskId, overhaulPlanReqDTO.getExamineReqDTO().getOpinion());
                overhaulPlanReqDTO.setWorkFlowInstId("");
                overhaulPlanReqDTO.setWorkFlowInstStatus("");
                overhaulPlanReqDTO.setTrialStatus("10");
            }
        }
        overhaulPlanReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulPlanReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
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
        String relationCode = TokenUtil.getUuId();
        for (int j = 0; j < dmer21TopOrder.size(); j++) {
            OverhaulPlanReqDTO reqDTO = new OverhaulPlanReqDTO();
            reqDTO.setRecId(dmer21TopOrder.get(j).getRecId());
            reqDTO.setRelationCode(relationCode);
            reqDTO.setNodeLevel(dmer21TopOrder.size() - j);
            if (j == 0) {
                reqDTO.setCountFlag(1);
            } else {
                int max = dmer21TopOrder.get(j - 1).getPeriod();
                int min = dmer21TopOrder.get(j).getPeriod();
                if (max % min != 0 || max == min) {
                    throw new CommonException(ErrorCode.NORMAL_ERROR, "所有关联计划之间的规则周期必须为倍数关系");
                }
                int max1 = dmer21TopOrder.get(j - 1).getPeriod();
                int min1 = dmer21TopOrder.get(j).getPeriod();
                reqDTO.setCountFlag(max1 / min1 - 1);
                reqDTO.setParentNodeRecId(dmer21TopOrder.get(j - 1).getExt1());
            }
            overhaulPlanMapper.modifyOverhaulPlan(reqDTO);
        }
    }

    @Override
    public void switchsOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulPlanMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulPlanReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        if (StringUtils.isBlank(overhaulPlanReqDTO.getPlanStatus())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "勾选的检修计划中计划状态不能为空！");
        }
        overhaulPlanMapper.updatePlanSta(overhaulPlanReqDTO);
    }

    @Override
    public void importOverhaulPlan(MultipartFile file) {
        // todo excel导入
    }

    @Override
    public void exportOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "计划编号", "计划名称", "对象名称", "线路", "审批状态", "位置一",
                "专业", "系统", "设备类别", "规则", "作业工班", "启用状态", "首次开始日期", "是否关联", "预警里程");
        overhaulPlanListReqDTO.setTrialStatus("'20','10','30'");
        overhaulPlanListReqDTO.setObjectFlag("1");
        List<OverhaulPlanResDTO> overhaulTplResDTOList = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (overhaulTplResDTOList != null && !overhaulTplResDTOList.isEmpty()) {
            for (OverhaulPlanResDTO resDTO : overhaulTplResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("计划编号", resDTO.getPlanCode());
                map.put("计划名称", resDTO.getPlanName());
                map.put("对象名称", resDTO.getExt1());
                map.put("线路", "01".equals(resDTO.getLineNo()) ? "S1线" : "S2线");
                map.put("审批状态", CommonConstants.TEN_STRING.equals(resDTO.getTrialStatus()) ? "编辑" : CommonConstants.TWENTY_STRING.equals(resDTO.getTrialStatus()) ? "审核中" : "审核通过");
                map.put("位置一", resDTO.getPosition1Name());
                map.put("专业", resDTO.getSubjectName());
                map.put("系统", resDTO.getSystemName());
                map.put("设备类别", resDTO.getEquipTypeName());
                map.put("规则", resDTO.getRuleName());
                map.put("作业工班", organizationMapper.getNamesById(resDTO.getWorkerGroupCode()));
                map.put("启用状态", CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus()) ? "启用" : "禁用");
                map.put("首次开始日期", resDTO.getFirstBeginTime());
                map.put("是否关联", resDTO.getDeleteFlag());
                map.put("预警里程", resDTO.getExt2());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("检修计划（中车）信息", listName, list, null, response);
    }

    public boolean checkHasNotOrder(String planCode) {
        OverhaulOrderListReqDTO overhaulOrderListReqDTO = new OverhaulOrderListReqDTO();
        overhaulOrderListReqDTO.setPlanCode(planCode);
        overhaulOrderListReqDTO.setNewTime("flag");
        List<OverhaulOrderResDTO> list = overhaulOrderMapper.listOverhaulOrder(overhaulOrderListReqDTO);
        return list == null || list.size() <= 0;
    }

    public void triggerOne(String planCode) {
        if (checkHasNotOrder(planCode)) {
            createInsepectRecordByPlanCode(new String[]{planCode});
            if (querySonOrder(planCode) != null && checkHasNotOrder(querySonOrder(planCode))) {
                triggerOne(querySonOrder(planCode));
            }
        }
    }

    public String querySonOrder(String planCode) {
        OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
        overhaulPlanListReqDTO.setTrialStatus1("30");
        overhaulPlanListReqDTO.setFirstBegin("flag");
        overhaulPlanListReqDTO.setParentNode(planCode);
        List<OverhaulPlanResDTO> dmer11List = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        if (dmer11List != null && dmer11List.size() > 0) {
            return dmer11List.get(0).getPlanCode();
        }
        return null;
    }

    public String createInsepectRecordByPlanCode(String[] planCodes) {
        String flag = "0";
        if (planCodes.length > 1 && "1".equals(planCodes[1])) {
            flag = "1";
        }
        String planCode = planCodes[0];
        List<OverhaulTplDetailResDTO> tplDetailList = overhaulPlanMapper.getOrderIsValid(planCode);
        if (tplDetailList == null || tplDetailList.size() <= 0) {
            return "2";
        }
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        String orderCode = overhaulOrderMapper.getMaxCode();
        if (StringUtils.isEmpty(orderCode) || !orderCode.substring(2, 10).equals(day.format(System.currentTimeMillis()))) {
            orderCode = "JX" + day.format(System.currentTimeMillis()).substring(2) + "0001";
        } else {
            orderCode = CodeUtils.getNextCode(orderCode, 10);
        }
        try {
            insertInspectPlan1(planCode, new String[]{orderCode, flag});
            insertInspectObject(planCode, orderCode);
            insertWorker(planCode, orderCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        overhaulPlanMapper.updateTrigerTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()), planCode);
        return "1";
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
        if (list11 != null && list11.size() > 0 && !"".equals(list11.get(0).getWorkerGroupCode().trim())) {
            trigerTime = list11.get(0).getTrigerTime();
            insertMap.setWorkerGroupCode(list11.get(0).getWorkerGroupCode());
            insertMap.setWorkerCode(TokenUtil.getCurrentPersonId());
            insertMap.setWorkerName(TokenUtil.getCurrentPerson().getPersonName());
            OverhaulOrderReqDTO dmer21 = new OverhaulOrderReqDTO();
            dmer21.setOrderCode(orderCode);
            dmer21.setPlanCode(planCode);
            dmer21.setWorkerGroupCode(list11.get(0).getWorkerGroupCode());
            dmer21.setWorkerCode(TokenUtil.getCurrentPersonId());
            dmer21.setWorkerName(TokenUtil.getCurrentPerson().getPersonName());
            dmer21.setRecId(dmer21.getOrderCode());
            dmer21.setWorkStatus("1");
            dmer21.setSubjectCode(list11.get(0).getSubjectCode());
            dmer21.setLineNo(list11.get(0).getLineNo());
            try {
                overhaulWorkRecordService.insertRepair(dmer21);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        insertMap.setRealStartTime(" ");
        insertMap.setRealEndTime(" ");
        insertMap.setExt1(" ");
        if (orderCodes.length > 1) {
            if ("1".equals(orderCodes[1])) {
                insertMap.setPlanStartTime(orderCode.substring(2, 10));
            } else {
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd");
                String nowDate = dateTimeFormat.format(new Date());
                List<WoRuleResDTO.WoRuleDetail> ruleList = woRuleMapper.queryRuleList(planCode, nowDate.substring(nowDate.length() - 4));
                int beforeDay = ruleList.get(0).getBeforeTime();
                if (trigerTime == null || "".equals(trigerTime.trim()) || "0".equals(trigerTime)) {
                    trigerTime = orderCode.substring(2, 10);
                } else {
                    trigerTime = trigerTime.substring(0, 8);
                }
                Date date = dateTimeFormat.parse(trigerTime);
                Calendar ca = Calendar.getInstance();
                ca.setTime(date);
                ca.add(Calendar.DAY_OF_YEAR, beforeDay);
                insertMap.setPlanStartTime(dateTimeFormat.format(ca.getTime()));
            }
        } else {
            insertMap.setPlanStartTime(orderCode.substring(2, 10));
        }
        try {
            List<OverhaulPlanResDTO> planList = overhaulPlanMapper.listOverhaulPlan(queryMap1);
            if (planList != null && planList.size() > 0) {
                for (OverhaulPlanResDTO plan : planList) {
                    plan.setRecCreator(TokenUtil.getCurrentPersonId());
                    plan.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                    plan.setRecRevisor("");
                    plan.setRecReviseTime("");
                    BeanUtils.copyProperties(plan, insertMap);
                    insertMap.setRecId(TokenUtil.getUuId());
                    overhaulOrderMapper.addOverhaulOrder(insertMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertInspectObject(String planCode, String orderCode) {
        List<OverhaulObjectResDTO> objects = overhaulPlanMapper.listOverhaulObject(planCode, null, null, null, null, null);
        for (OverhaulObjectResDTO object : objects) {
            String dmer22uuid = TokenUtil.getUuId();
            List<OverhaulTplDetailResDTO> objectIsValid = overhaulTplMapper.listOverhaulTplDetail(object.getTemplateId());
            if (objectIsValid != null && objectIsValid.size() > 0) {
                String objectCode = object.getObjectCode();
                String objectName = object.getObjectName();
                String templateId = object.getTemplateId();
                insertInspectObjectItem(orderCode, objectCode, objectName, templateId, dmer22uuid);
                try {
                    List<OverhaulObjectResDTO> list = overhaulPlanMapper.listOverhaulObject(planCode, object.getRecId(), null, objectCode, null, null);
                    if (list != null && list.size() > 0) {
                        for (OverhaulObjectResDTO resDTO : list) {
                            resDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                            resDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
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
                    e.printStackTrace();
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
            if (overhaulTplDetailList != null && overhaulTplDetailList.size() > 0) {
                for (OverhaulTplDetailResDTO overhaulTplDetail : overhaulTplDetailList) {
                    BeanUtils.copyProperties(overhaulTplDetail, reqDTO);
                    reqDTO.setTdmer02Id(overhaulTplDetail.getRecId());
                    reqDTO.setRecId(TokenUtil.getUuId());
                    overhaulItemMapper.insert(reqDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        if (workCode.length() > 2) {
            String[] workerCodes = workCode.split(",");
            if (workerCodes.length > 0) {
                for (String workerCode : workerCodes) {
                    dmer24.setRecId(TokenUtil.getUuId());
                    dmer24.setWorkerCode(workerCode);
                    try {
                        overhaulWorkRecordMapper.insert(dmer24);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void submitOrderPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) throws Exception {
        // OrderPlan  submitOrderPlan
        // /iam/org/getZcOverhaulPlanExamineUser接口获取审核人
        String processId = bpmnService.commit(overhaulPlanReqDTO.getPlanCode(), BpmnFlowEnum.ORDER_PLAN_SUBMIT.value(), null, null, overhaulPlanReqDTO.getExamineReqDTO().getUserIds(), null);
        if (processId == null || "-1".equals(processId)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败");
        }
        overhaulPlanReqDTO.setWorkFlowInstId(processId);
        overhaulPlanReqDTO.setWorkFlowInstStatus(roleMapper.getSubmitNodeId(BpmnFlowEnum.ORDER_PLAN_SUBMIT.value(),null));
        overhaulPlanReqDTO.setTrialStatus("20");
        overhaulPlanReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulPlanReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulPlanReqDTO.setExt1(" ");
        overhaulPlanMapper.modifyOverhaulPlan(overhaulPlanReqDTO);
    }

    @Override
    public List<OverhaulTplDetailResDTO> getTemplates(String planCode) {
        List<OverhaulTplDetailResDTO> resList = new ArrayList<>();
        OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
        overhaulPlanListReqDTO.setPlanCode(planCode);
        List<OverhaulPlanResDTO> list = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
        if (list != null && !list.isEmpty()) {
            resList = overhaulTplMapper.queryTemplate(list.get(0).getLineNo(), list.get(0).getSubjectCode(),
                    list.get(0).getSystemCode(), list.get(0).getEquipTypeCode(), "30");
        }
        return resList;
    }

    @Override
    public Page<OverhaulObjectResDTO> pageOverhaulObject(String planCode, String planName, String objectCode, String objectName, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
        if (list.size() <= 0) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
        }
        overhaulObjectReqDTO.setRecId(TokenUtil.getUuId());
        overhaulObjectReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        overhaulObjectReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
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
        if (list.size() <= 0) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
        }
        overhaulObjectReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulObjectReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulPlanMapper.modifyOverhaulObject(overhaulObjectReqDTO);
    }

    @Override
    public void deleteOverhaulObject(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                OverhaulObjectResDTO resDTO = overhaulPlanMapper.getOverhaulObjectDetail(id);
                OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
                overhaulPlanListReqDTO.setPlanCode(resDTO.getPlanCode());
                overhaulPlanListReqDTO.setTrialStatus("'10'");
                List<OverhaulPlanResDTO> list = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
                if (list.size() <= 0) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
                }
                overhaulPlanMapper.deleteOverhaulObject(id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        }
    }

    @Override
    public void exportOverhaulObject(String planCode, String planName, String objectCode, String objectName, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "对象编号", "对象名称/车号", "检修项模板", "作业内容", "作业需求", "作业备注");
        List<OverhaulObjectResDTO> overhaulObjectResDTOList = overhaulPlanMapper.listOverhaulObject(planCode, null, planName, objectCode, objectName, null);
        List<Map<String, String>> list = new ArrayList<>();
        if (overhaulObjectResDTOList != null && !overhaulObjectResDTOList.isEmpty()) {
            for (OverhaulObjectResDTO resDTO : overhaulObjectResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("对象编号", resDTO.getObjectCode());
                map.put("对象名称/车号", resDTO.getObjectName());
                map.put("检修项模板", resDTO.getTemplateName());
                map.put("作业内容", resDTO.getTaskContent());
                map.put("作业需求", resDTO.getTaskRequest());
                map.put("作业备注", resDTO.getTaskRemark());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("检修对象（中车）信息", listName, list, null, response);
    }

}
