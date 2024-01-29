package com.wzmtr.eam.impl.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.FaultInfoReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultOrderReqDTO;
import com.wzmtr.eam.dto.req.overhaul.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.basic.WoRuleResDTO;
import com.wzmtr.eam.dto.res.bpmn.BpmnExaminePersonRes;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.overhaul.*;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulItemResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulOrderResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulStateResDTO;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.Role;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.EquipmentCategoryMapper;
import com.wzmtr.eam.mapper.basic.WoRuleMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulItemMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulOrderMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulPlanMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulStateMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.common.UserGroupMemberService;
import com.wzmtr.eam.service.overhaul.OverhaulOrderService;
import com.wzmtr.eam.service.overhaul.OverhaulWorkRecordService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author frp
 */
@Service
@Slf4j
public class OverhaulOrderServiceImpl implements OverhaulOrderService {

    @Autowired
    private OverhaulOrderMapper overhaulOrderMapper;

    @Autowired
    private EquipmentCategoryMapper equipmentCategoryMapper;

    @Autowired
    private OverhaulWorkRecordService overhaulWorkRecordService;

    @Autowired
    private OverhaulPlanMapper overhaulPlanMapper;

    @Autowired
    private WoRuleMapper woRuleMapper;

    @Autowired
    private OverhaulItemMapper overhaulItemMapper;

    @Autowired
    private OverhaulStateMapper overhaulStateMapper;

    @Autowired
    private FaultReportMapper faultReportMapper;

    @Autowired
    private OverTodoService overTodoService;

    @Autowired
    private DictionariesMapper dictionariesMapper;

    @Autowired
    private FaultQueryMapper faultQueryMapper;

    @Autowired
    private UserGroupMemberService userGroupMemberService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<OverhaulOrderResDTO> pageOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        overhaulOrderListReqDTO.setObjectFlag("1");
        return overhaulOrderMapper.pageOrder(pageReqDTO.of(), overhaulOrderListReqDTO);
    }

    @Override
    public Page<OverhaulOrderResDTO> openApiPageOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, PageReqDTO pageReqDTO) {
        String csm = "NCSM";
        if (overhaulOrderListReqDTO.getTenant().contains(csm)) {
            PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
            overhaulOrderListReqDTO.setObjectFlag("1");
            return overhaulOrderMapper.pageOrder(pageReqDTO.of(), overhaulOrderListReqDTO);
        } else {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "您无权访问这个接口");
        }
    }

    @Override
    public OverhaulOrderResDTO getOverhaulOrderDetail(String id) {
        return overhaulOrderMapper.getOrder(id, "1");
    }

    @Override
    public void exportOverhaulOrder(List<String> ids, HttpServletResponse response) throws IOException {
        List<OverhaulOrderResDTO> overhaulOrderResDTOList = overhaulOrderMapper.getOrderByIds(ids, "1");
        if (overhaulOrderResDTOList != null && !overhaulOrderResDTOList.isEmpty()) {
            List<ExcelOverhaulOrderResDTO> resList = new ArrayList<>();
            for (OverhaulOrderResDTO resDTO : overhaulOrderResDTOList) {
                ExcelOverhaulOrderResDTO res = ExcelOverhaulOrderResDTO.builder()
                        .recId(resDTO.getRecId())
                        .orderCode(resDTO.getOrderCode())
                        .planCode(resDTO.getPlanCode())
                        .planName(resDTO.getPlanName())
                        .objectName(resDTO.getExt1())
                        .workStatus(dictionariesMapper.queryOneByItemCodeAndCodesetCode("dm.er.recStatus", resDTO.getWorkStatus()).getItemCname())
                        .workFinishStatus(resDTO.getWorkFinishStatus())
                        .abnormalNumber(resDTO.getAbnormalNumber())
                        .tools(resDTO.getRecDeletor())
                        .planStartTime(resDTO.getPlanStartTime())
                        .planEndTime(resDTO.getPlanEndTime())
                        .workGroupName(organizationMapper.getNameById(resDTO.getWorkerGroupCode()))
                        .workerName(resDTO.getWorkerName())
                        .realStartTime(resDTO.getRealStartTime())
                        .realEndTime(resDTO.getRealEndTime())
                        .lineNo(CommonConstants.LINE_CODE_ONE.equals(resDTO.getLineNo()) ? "S1线" : "S2线")
                        .position1Name(resDTO.getPosition1Name())
                        .subjectName(resDTO.getSubjectName())
                        .systemName(resDTO.getSystemName())
                        .equipTypeName(resDTO.getEquipTypeName())
                        .sendPersonName(resDTO.getSendPersonName())
                        .ackPersonName(resDTO.getAckPersonName())
                        .constructionPlanNo(resDTO.getSystemName())
                        .remark(resDTO.getRemark())
                        .build();
                resList.add(res);
            }
            EasyExcelUtils.export(response, "检修工单信息", resList);
        }
    }

    @Override
    public List<FaultRepairDeptResDTO> queryDept(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
        OverhaulOrderResDTO order = overhaulOrderMapper.getOrder(id, "1");
        if (Objects.isNull(order)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        String userId = TokenUtil.getCurrentPersonId();
        List<Role> roleList = roleMapper.getLoginRole(userId);
        if (StringUtils.isNotEmpty(roleList)) {
            List<String> roles = roleList.stream().map(Role::getRoleCode).collect(Collectors.toList());
            if (CommonConstants.ONE_STRING.equals(order.getWorkStatus())) {
                if (!roles.contains("DM_007") && !CommonConstants.ADMIN.equals(userId) && !roles.contains("DM_037")) {
                    throw new CommonException(ErrorCode.NORMAL_ERROR, "首次派工必须是调度派工给工班长！");
                }
            } else if (!roles.contains("DM_012") && !CommonConstants.ADMIN.equals(userId)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "已下达、已分配状态必须由工班长派工！");
            }
        }
        return faultQueryMapper.queryDeptCode(order.getLineNo(), order.getSubjectCode(), "10");
    }

    @Override
    public List<OrganMajorLineType> queryWorker(String workStatus, String workerGroupCode) {
        if (StringUtils.isEmpty(workerGroupCode)) {
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
        String groupCname = "DM_013";
        if (CommonConstants.ONE_STRING.equals(workStatus)) {
            groupCname = "DM_012";
        }
        return userGroupMemberService.getDepartmentUserByGroupName(groupCname, workerGroupCode);
    }

    @Override
    public void dispatchWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulOrderMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        checkOrderState(overhaulOrderReqDTO, "1,2,3", "请求、已下达、已分配");
        if (CommonConstants.ONE_STRING.equals(overhaulOrderReqDTO.getWorkStatus())) {
            overhaulOrderReqDTO.setWorkStatus("2");
            overhaulOrderReqDTO.setRecDeletor(TokenUtil.getCurrentPerson().getPersonName() + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            overhaulOrderReqDTO.setSendPersonId(TokenUtil.getCurrentPersonId());
            overhaulOrderReqDTO.setSendPersonName(TokenUtil.getCurrentPerson().getPersonName());
            overhaulOrderReqDTO.setSendTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            overhaulOrderReqDTO.setWorkStatus("3");
        }
        overhaulOrderReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulOrderReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulWorkRecordService.insertRepair(overhaulOrderReqDTO);
        overhaulOrderMapper.modifyOverhaulOrder(overhaulOrderReqDTO);
    }

    @Override
    public void auditWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulOrderMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        if (CommonConstants.CAR_SUBJECT_CODE.equals(overhaulOrderReqDTO.getSubjectCode()) && !overhaulOrderReqDTO.getPlanName().contains(CommonConstants.SECOND_REPAIR_SHIFT)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "只有工单为车辆二级修才能进行该操作。");
        }
        checkOrderState(overhaulOrderReqDTO, "4", "完工");
        overhaulOrderReqDTO.setWorkStatus("6");
        overhaulOrderReqDTO.setRecDeleteTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulOrderReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulOrderReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulOrderReqDTO.setExt1(" ");
        overhaulOrderMapper.modifyOverhaulOrder(overhaulOrderReqDTO);
        // ServiceDMER0201  auditWorkers
        overTodoService.overTodo(overhaulOrderReqDTO.getRecId(), "");
        // 根据角色获取用户列表
        List<BpmnExaminePersonRes> userList = roleMapper.getUserBySubjectAndLineAndRole(overhaulOrderReqDTO.getSubjectCode(), overhaulOrderReqDTO.getLineNo(), "DM_007");
        for (BpmnExaminePersonRes map2 : userList) {
            overTodoService.insertTodo("检修工单流转", overhaulOrderReqDTO.getRecId(), overhaulOrderReqDTO.getOrderCode(),
                    map2.getUserId(), "检修工单完工确认", "DMER0200", TokenUtil.getCurrentPersonId());
        }
    }

    @Override
    public void confirmWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) throws ParseException {
        if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulOrderMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        if (!overhaulOrderReqDTO.getPlanName().contains(CommonConstants.SECOND_REPAIR_SHIFT)) {
            checkOrderState(overhaulOrderReqDTO, "4", "完工");
        } else {
            checkOrderState(overhaulOrderReqDTO, "6", "验收");
        }
        if (StringUtils.isBlank(overhaulOrderReqDTO.getRealEndTime())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "该工单没有实际完成时间，无法完工确认！");
        }
        overhaulOrderReqDTO.setWorkStatus("5");
        overhaulOrderReqDTO.setAckPersonId(TokenUtil.getCurrentPersonId());
        overhaulOrderReqDTO.setAckPersonName(TokenUtil.getCurrentPerson().getPersonName());
        overhaulOrderReqDTO.setConfirTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulOrderReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulOrderReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulOrderReqDTO.setExt1(" ");
        overhaulOrderMapper.modifyOverhaulOrder(overhaulOrderReqDTO);
        modifyOverhaulPlanByOrder(overhaulOrderReqDTO);
        // ServiceDMER0201  confirmWorkers
        overTodoService.overTodo(overhaulOrderReqDTO.getRecId(), "");
    }

    /**
     * 根据检修工单修改检修计划（中车）
     * @param overhaulOrderReqDTO 检修工单信息
     * @throws ParseException
     */
    private void modifyOverhaulPlanByOrder(OverhaulOrderReqDTO overhaulOrderReqDTO) throws ParseException {
        OverhaulOrderListReqDTO listReqDTO = new OverhaulOrderListReqDTO();
        listReqDTO.setPlanCode(overhaulOrderReqDTO.getPlanCode());
        List<OverhaulOrderResDTO> list = overhaulOrderMapper.listOverhaulOrder(listReqDTO);
        if (StringUtils.isNotEmpty(list) && overhaulOrderReqDTO.getOrderCode().equals(list.get(0).getOrderCode())) {
            OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
            overhaulPlanListReqDTO.setPlanCode(overhaulOrderReqDTO.getPlanCode());
            List<OverhaulPlanResDTO> plans = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
            if (StringUtils.isNotEmpty(plans)) {
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat dateTimeFormat1 = new SimpleDateFormat("yyyyMMddHH");
                String nowDate = dateTimeFormat.format(new Date());
                String substring = nowDate.substring(nowDate.length() - 4);
                List<WoRuleResDTO.WoRuleDetail> rules = woRuleMapper.listWoRuleDetail(plans.get(0).getRuleCode(), substring, substring);
                int trigerMiles = 0;
                if (StringUtils.isNotEmpty(rules)) {
                    if (CommonConstants.CAR_SUBJECT_CODE.equals(overhaulOrderReqDTO.getSubjectCode())) {
                        List<String> queryObjMiles = overhaulOrderMapper.queryObjMiles(plans.get(0).getPlanCode());
                        if (StringUtils.isNotEmpty(queryObjMiles)) {
                            int mileage = Integer.parseInt(rules.get(0).getExt1());
                            int totalMiles = Integer.parseInt(queryObjMiles.get(0));
                            trigerMiles = mileage + totalMiles;
                        }
                    }
                    Date realEndTime = getRealEndTime(overhaulOrderReqDTO, rules);
                    String realEndTimeStr = dateTimeFormat1.format(realEndTime);
                    OverhaulPlanReqDTO overhaulPlanReqDTO = new OverhaulPlanReqDTO();
                    overhaulPlanReqDTO.setRecId(plans.get(0).getRecId());
                    overhaulPlanReqDTO.setTrigerTime(realEndTimeStr);
                    overhaulPlanReqDTO.setLastActionTime(String.valueOf(trigerMiles));
                    overhaulPlanReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
                    overhaulPlanReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                    overhaulPlanMapper.modifyOverhaulPlan(overhaulPlanReqDTO);
                }
            }
        }
    }

    /**
     * 获取实际结束时间
     * @param overhaulOrderReqDTO 检修工单信息
     * @param rules 规则
     * @return 结束时间
     * @throws ParseException 异常
     */
    @NotNull
    private static Date getRealEndTime(OverhaulOrderReqDTO overhaulOrderReqDTO, List<WoRuleResDTO.WoRuleDetail> rules) throws ParseException {
        long period = rules.get(0).getPeriod();
        long beforeTime = rules.get(0).getBeforeTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
        Date realEndTime1 = format.parse(overhaulOrderReqDTO.getRealEndTime().substring(0, 13));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(realEndTime1);
        calendar.add(Calendar.HOUR_OF_DAY, Math.toIntExact(period));
        calendar.add(Calendar.DAY_OF_YEAR, Math.toIntExact(-beforeTime));
        return calendar.getTime();
    }

    @Override
    public void cancellWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) {
        overhaulOrderReqDTO.setWorkStatus("8");
        overhaulOrderReqDTO.setCancelPersonId(TokenUtil.getCurrentPersonId());
        overhaulOrderReqDTO.setCancelPersonName(TokenUtil.getCurrentPerson().getPersonName());
        overhaulOrderReqDTO.setCancelTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulOrderReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulOrderReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulOrderReqDTO.setExt1(" ");
        overhaulOrderMapper.modifyOverhaulOrder(overhaulOrderReqDTO);
    }

    public void checkOrderState(OverhaulOrderReqDTO overhaulOrderReqDTO, String orderStates, String msg) {
        OverhaulOrderListReqDTO overhaulOrderListReqDTO = new OverhaulOrderListReqDTO();
        overhaulOrderListReqDTO.setOrderCode(overhaulOrderReqDTO.getOrderCode());
        List<OverhaulOrderResDTO> list = overhaulOrderMapper.listOverhaulOrder(overhaulOrderListReqDTO);
        if (StringUtils.isNotEmpty(list)) {
            if (!orderStates.contains(list.get(0).getWorkStatus())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "只有工单为" + msg + "的状态才能进行该操作。");
            }
        } else {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "在数据库未查询到该工单，请刷新后重试。");
        }
    }

    @Override
    public void pageMaterial() {
        // todo 材料列表
    }

    @Override
    public void receiveMaterial(HttpServletResponse response) throws IOException {
        response.sendRedirect(dictionariesMapper.queryOneByItemCodeAndCodesetCode("DM_ER_ADDRESS", "11").getItemCname());
    }

    @Override
    public void returnMaterial() {
        // todo 退回材料
    }

    @Override
    public Page<ConstructionResDTO> construction(String orderCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return faultQueryMapper.construction(pageReqDTO.of(), orderCode);
    }

    @Override
    public Page<ConstructionResDTO> cancellation(String orderCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return faultQueryMapper.cancellation(pageReqDTO.of(), orderCode);
    }

    @Override
    public Page<OverhaulOrderDetailResDTO> pageOverhaulObject(String orderCode, String planCode, String planName, String objectCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<OverhaulOrderDetailResDTO> page = overhaulOrderMapper.pageOverhaulObject(pageReqDTO.of(), orderCode, planCode, planName, objectCode);
        List<OverhaulOrderDetailResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (OverhaulOrderDetailResDTO res : list) {
                if (StringUtils.isEmpty(res.getTaskPersonName())) {
                    OverhaulItemListReqDTO req = new OverhaulItemListReqDTO();
                    req.setObjectCode(res.getObjectCode());
                    req.setObjectCode(res.getObjectCode());
                    List<OverhaulItemResDTO> overhaulItem = overhaulItemMapper.listOverhaulItem(req);
                    if (StringUtils.isNotEmpty(overhaulItem)) {
                        Set<String> nameSet = overhaulItem.stream().map(OverhaulItemResDTO::getWorkUserName).filter(Objects::nonNull).collect(Collectors.toSet());
                        String result = Joiner.on(",").join(nameSet);
                        List<String> names = Arrays.stream(result.split(",")).distinct().filter(Objects::nonNull).collect(Collectors.toList());
                        result = Joiner.on(",").join(names);
                        res.setTaskPersonName(result);
                    }
                }
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public OverhaulOrderDetailResDTO getOverhaulObjectDetail(String id) {
        OverhaulOrderDetailResDTO res = overhaulOrderMapper.getOverhaulObjectDetail(id);
        if (!Objects.isNull(res)) {
            if (StringUtils.isEmpty(res.getTaskPersonName())) {
                OverhaulItemListReqDTO req = new OverhaulItemListReqDTO();
                req.setObjectCode(res.getObjectCode());
                req.setObjectCode(res.getObjectCode());
                List<OverhaulItemResDTO> overhaulItem = overhaulItemMapper.listOverhaulItem(req);
                if (StringUtils.isNotEmpty(overhaulItem)) {
                    Set<String> nameSet = overhaulItem.stream().map(OverhaulItemResDTO::getWorkUserName).filter(Objects::nonNull).collect(Collectors.toSet());
                    String result = Joiner.on(",").join(nameSet);
                    List<String> names = Arrays.stream(result.split(",")).distinct().filter(Objects::nonNull).collect(Collectors.toList());
                    result = Joiner.on(",").join(names);
                    res.setTaskPersonName(result);
                }
            }
        }
        return res;
    }

    @Override
    public void exportOverhaulObject(String orderCode, String planCode, String planName, String objectCode, HttpServletResponse response) throws IOException {
        List<OverhaulOrderDetailResDTO> overhaulObject = overhaulOrderMapper.listOverhaulObject(orderCode, planCode, planName, objectCode);
        if (overhaulObject != null && !overhaulObject.isEmpty()) {
            List<ExcelOverhaulObjectResDTO> list = new ArrayList<>();
            for (OverhaulOrderDetailResDTO resDTO : overhaulObject) {
                ExcelOverhaulObjectResDTO res = new ExcelOverhaulObjectResDTO();
                org.springframework.beans.BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "检修对象信息", list);
        }
    }

    @Override
    public void checkjx(String orderCode) {
        OverhaulOrderListReqDTO overhaulOrderListReqDTO = new OverhaulOrderListReqDTO();
        overhaulOrderListReqDTO.setOrderCode(orderCode);
        List<OverhaulOrderResDTO> list = overhaulOrderMapper.listOverhaulOrder(overhaulOrderListReqDTO);
        if (StringUtils.isNotEmpty(list)) {
            String planName = list.get(0).getPlanName();
            String orderStatus = list.get(0).getWorkStatus();
            if (StringUtils.isNotEmpty(planName) && planName.contains(CommonConstants.SECOND_REPAIR_SHIFT) && CommonConstants.FOUR_STRING.equals(orderStatus)) {
                return;
            }
            throw new CommonException(ErrorCode.NORMAL_ERROR, "只有二级修工单可以进行模块验收！");
        }
    }

    @Override
    public Page<OverhaulItemResDTO> pageOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulItemMapper.pageOverhaulItem(pageReqDTO.of(), overhaulItemListReqDTO);
    }

    /**
     * 获取检修项检修模块列表
     * @param objectCode 对象编号
     * @param orderCode 工单编号
     * @return 检修模块列表
     */
    @Override
    public List<OverhaulItemResDTO> listOverhaulItemModel(String objectCode, String orderCode) {
        List<OverhaulItemResDTO> list = overhaulItemMapper.listOverhaulItemModel(objectCode, orderCode);
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 根据检修模块获取检修项列表
     * @param objectCode 对象编号
     * @param orderCode 工单编号
     * @param modelName 模块名称
     * @return 检修项列表
     */
    @Override
    public List<OverhaulItemResDTO> listOverhaulItem(String objectCode, String orderCode, String modelName) {
        return overhaulItemMapper.listOverhaulItemByModel(objectCode, orderCode, modelName);
    }

    /**
     * 获取检修项检修模块与检修项列表
     * @param objectCode 对象编号
     * @param orderCode 工单编号
     * @return 检修项列表
     */
    @Override
    public List<OverhaulItemTreeResDTO> listOverhaulItemTree(String objectCode, String orderCode) {
        List<OverhaulItemResDTO> modelList = overhaulItemMapper.listOverhaulItemModel(objectCode, orderCode);
        List<OverhaulItemTreeResDTO> models = new ArrayList<>();
        if (StringUtils.isNotEmpty(modelList)) {
            modelList = modelList.stream().distinct().collect(Collectors.toList());
            for (OverhaulItemResDTO model : modelList) {
                OverhaulItemTreeResDTO res = new OverhaulItemTreeResDTO();
                org.springframework.beans.BeanUtils.copyProperties(model, res);
                res.setItemList(overhaulItemMapper.listOverhaulItemByModel(objectCode, orderCode, model.getModelName()));
                models.add(res);
            }
        }
        return models;
    }

    @Override
    public OverhaulItemResDTO getOverhaulItemDetail(String id) {
        return overhaulItemMapper.getOverhaulItemDetail(id);
    }

    @Override
    public void exportOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO, HttpServletResponse response) throws IOException {
        List<OverhaulItemResDTO> overhaulItem = overhaulItemMapper.listOverhaulItem(overhaulItemListReqDTO);
        if (StringUtils.isNotEmpty(overhaulItem)) {
            List<ExcelOverhaulItemResDTO> list = new ArrayList<>();
            for (OverhaulItemResDTO resDTO : overhaulItem) {
                ExcelOverhaulItemResDTO res = new ExcelOverhaulItemResDTO();
                org.springframework.beans.BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "检修项信息", list);
        }
    }

    /**
     * 排查检修项
     * @param troubleshootReqDTO 排查检修项信息
     */
    @Override
    public void troubleshootOverhaulItem(OverhaulItemTroubleshootReqDTO troubleshootReqDTO) {
        if (StringUtils.isNotEmpty(troubleshootReqDTO.getOverhaulItemList())) {
            for (OverhaulItemResDTO res : troubleshootReqDTO.getOverhaulItemList()) {
                overhaulItemMapper.troubleshootOverhaulItem(res, troubleshootReqDTO.getWorkUserId(), troubleshootReqDTO.getWorkUserName());
            }
            overhaulItemMapper.finishedOverhaulOrder(troubleshootReqDTO.getObjectCode(), troubleshootReqDTO.getOrderCode());
        } else {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
    }

    @Override
    public Page<OverhaulStateResDTO> pageOverhaulState(String objectCode, String itemName, String orderCode, String tdmer23RecId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulStateMapper.pageOverhaulState(pageReqDTO.of(), objectCode, itemName, orderCode, tdmer23RecId);
    }

    @Override
    public OverhaulStateResDTO getOverhaulStateDetail(String id) {
        return overhaulStateMapper.getOverhaulStateDetail(id);
    }

    @Override
    public void exportOverhaulState(String objectCode, String itemName, String orderCode, String tdmer23RecId, HttpServletResponse response) throws IOException {
        List<OverhaulStateResDTO> overhaulState = overhaulStateMapper.listOverhaulState(objectCode, itemName, orderCode, tdmer23RecId);
        if (overhaulState != null && !overhaulState.isEmpty()) {
            List<ExcelOverhaulStateResDTO> list = new ArrayList<>();
            for (OverhaulStateResDTO resDTO : overhaulState) {
                ExcelOverhaulStateResDTO res = new ExcelOverhaulStateResDTO();
                org.springframework.beans.BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "检修异常信息", list);
        }
    }

    @Override
    public OverhaulStateOrderResDTO queryOrderInfo(String orderCode) {
        OverhaulOrderListReqDTO overhaulOrderListReqDTO = new OverhaulOrderListReqDTO();
        overhaulOrderListReqDTO.setOrderCode(orderCode);
        List<OverhaulOrderResDTO> list = overhaulOrderMapper.listOrder(overhaulOrderListReqDTO);
        OverhaulStateOrderResDTO res = new OverhaulStateOrderResDTO();
        if (StringUtils.isNotEmpty(list)) {
            res.setLineNo(list.get(0).getLineNo());
            res.setPosition1Name(list.get(0).getPosition1Name());
            res.setPosition1Code(list.get(0).getPosition1Code());
            res.setSubjectCode(list.get(0).getSubjectCode());
            res.setSubjectName(list.get(0).getSubjectName());
            res.setUserId(TokenUtil.getCurrentPersonId());
            String userName = TokenUtil.getCurrentPerson().getPersonName();
            String discovererPhone = TokenUtil.getCurrentPerson().getPhone();
            String currentUser = TokenUtil.getCurrentPerson().getPersonName();
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String orgCode = TokenUtil.getCurrentPerson().getOfficeId();
            String orgName = TokenUtil.getCurrentPerson().getOfficeName();
            res.setFillinUserId(currentUser);
            res.setFillinUserName(userName);
            res.setDiscovererId(userName);
            res.setDiscovererName(currentUser);
            res.setDiscovererPhone(discovererPhone);
            res.setDiscoveryTime(dateTimeFormat.format(new Date()));
            res.setFillinTime(dateTimeFormat.format(new Date()));
            res.setFillinDeptName(orgName);
            res.setFillinDeptCode(orgCode);
        }
        return res;
    }

    @Override
    public void upState(OverhaulUpStateReqDTO overhaulUpStateReqDTO) {
        String currentUser = TokenUtil.getCurrentPerson().getPersonName();
        String orderCode = overhaulUpStateReqDTO.getOrderCode();
        String objectCode = overhaulUpStateReqDTO.getObjectCode();
        OverhaulOrderListReqDTO overhaulOrderListReqDTO = new OverhaulOrderListReqDTO();
        overhaulOrderListReqDTO.setOrderCode(orderCode);
        List<OverhaulOrderResDTO> list = overhaulOrderMapper.listOrder(overhaulOrderListReqDTO);
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<OverhaulOrderDetailResDTO> list2 = overhaulOrderMapper.listOverhaulObject(orderCode, list.get(0).getPlanCode(), null, objectCode);
        FaultInfoReqDTO dmfm01 = new FaultInfoReqDTO();
        org.springframework.beans.BeanUtils.copyProperties(overhaulUpStateReqDTO.getResDTO(), dmfm01);
        String fillinUserId = overhaulUpStateReqDTO.getResDTO().getFillinUserId();
        buildFaultInfo(dmfm01, fillinUserId, objectCode, list2, list);
        String maxFaultNo = faultReportMapper.getFaultInfoFaultNoMaxCode();
        String maxFaultWorkNo = faultReportMapper.getFaultOrderFaultWorkNoMaxCode();
        String faultNo = CodeUtils.getNextCode(maxFaultNo, "GZ");
        String faultWorkNo = CodeUtils.getNextCode(maxFaultWorkNo, "GD");
        dmfm01.setFaultNo(faultNo);
        FaultOrderReqDTO dmfm02 = new FaultOrderReqDTO();
        org.springframework.beans.BeanUtils.copyProperties(overhaulUpStateReqDTO.getResDTO(), dmfm02);
        dmfm02.setRecId(TokenUtil.getUuId());
        dmfm02.setFaultWorkNo(faultWorkNo);
        dmfm02.setFaultNo(faultNo);
        dmfm02.setOrderStatus("30");
        dmfm02.setWorkClass(list.get(0).getWorkerGroupCode());
        FaultInfoDO f1 = BeanUtils.convert(dmfm01, FaultInfoDO.class);
        faultReportMapper.addToFaultInfo(f1);
        FaultOrderDO f2 = BeanUtils.convert(dmfm02, FaultOrderDO.class);
        faultReportMapper.addToFaultOrder(f2);
        overhaulOrderMapper.updateone(faultWorkNo, "30", overhaulUpStateReqDTO.getRecId());
        String content = "【市铁投集团】检修升级故障，请及时处理并在EAM系统填写维修报告，工单号：" + faultWorkNo + "，请知晓。";
        // ServiceDMER0205 insertUpFaultMessage
        overTodoService.insertTodoWithUserGroupAndOrg("【" + equipmentCategoryMapper.listEquipmentCategory(null, list.get(0).getSubjectCode(), null).get(0).getNodeName() + "】故障管理流程",
                dmfm02.getRecId(), faultWorkNo, "DM_013", list.get(0).getWorkerGroupCode(), "故障维修", "DMFM0001", currentUser, content);
    }

    /**
     * 故障信息拼装
     * @param faultInfo 故障信息
     * @param fillinUserId 提报人
     * @param objectCode 对象编号
     * @param overhaulOrderDetailList 检修对象列表
     * @param overhaulOrderList 检修工单列表
     */
    public void buildFaultInfo(FaultInfoReqDTO faultInfo, String fillinUserId, String objectCode,
                               List<OverhaulOrderDetailResDTO> overhaulOrderDetailList, List<OverhaulOrderResDTO> overhaulOrderList) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        faultInfo.setExt2(queryNowUser(fillinUserId));
        faultInfo.setRecId(UUID.randomUUID().toString());
        if (StringUtils.isNotEmpty(objectCode) && objectCode.startsWith(CommonConstants.NINE_STRING)) {
            faultInfo.setObjectName(overhaulOrderDetailList.get(0).getObjectName());
            faultInfo.setObjectCode(overhaulOrderDetailList.get(0).getObjectCode());
        }
        faultInfo.setFaultType("30");
        faultInfo.setMajorCode(overhaulOrderList.get(0).getSubjectCode());
        faultInfo.setMajorName(overhaulOrderList.get(0).getSubjectName());
        faultInfo.setSystemCode(overhaulOrderList.get(0).getSystemCode());
        faultInfo.setSystemName(overhaulOrderList.get(0).getSystemName());
        faultInfo.setEquipTypeCode(overhaulOrderList.get(0).getEquipTypeCode());
        faultInfo.setEquipTypeName(overhaulOrderList.get(0).getEquipTypeName());
        faultInfo.setPositionName(overhaulOrderList.get(0).getPosition1Name());
        faultInfo.setPositionCode(overhaulOrderList.get(0).getPosition1Code());
        faultInfo.setRecCreator(TokenUtil.getCurrentPerson().getPersonName());
        faultInfo.setRecCreateTime(dateTimeFormat.format(new Date()));
        faultInfo.setDiscoveryTime(dateTimeFormat.format(new Date()));
        faultInfo.setFillinTime(dateTimeFormat.format(new Date()));
    }

    public String queryNowUser(String userCode) {
//        EiInfo eiInfo1 = new EiInfo();
//        eiInfo1.set(EiConstant.serviceId, "S_XS_14");
//        eiInfo1.set("loginName", userCode);
//        eiInfo1.set("groupType", "NORMAL");
//        EiInfo outInfo = XServiceManager.call(eiInfo1);
//        List<Map<String, String>> prolist = (List<Map<String, String>>)outInfo.get("result");
//        String groupEname;
//        List<String> groups = new ArrayList<>();
//        for (Map<String, String> stringStringMap : prolist) {
//            groupEname = (String) ((Map) stringStringMap).get("groupEname");
//            groups.add(groupEname);
//        }
        String ext2 = "";
//        if (groups.contains("DM_012") || groups.contains("DM_013")) {
//            ext2 = "DM_013";
//        } else if (groups.contains("DM_007")) {
//            ext2 = "DM_007";
//        } else if (groups.contains("DM_006")) {
//            ext2 = "DM_006";
//        } else {
//            ext2 = "";
//        }
        return ext2;
    }

}
