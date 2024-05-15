package com.wzmtr.eam.impl.overhaul;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
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
import com.wzmtr.eam.dto.res.common.MemberResDTO;
import com.wzmtr.eam.dto.res.common.UserRoleResDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.overhaul.*;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulItemResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulOrderResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulStateResDTO;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.Role;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.EquipmentCategoryMapper;
import com.wzmtr.eam.mapper.basic.WoRuleMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulItemMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulOrderMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulPlanMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulStateMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.common.OrganizationService;
import com.wzmtr.eam.service.common.UserAccountService;
import com.wzmtr.eam.service.overhaul.OverhaulOrderService;
import com.wzmtr.eam.service.overhaul.OverhaulWorkRecordService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Resource
    private UserAccountService userAccountService;

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
    private FileMapper fileMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationService organizationService;

    private static final List<String> zcList = Arrays.asList("07", "06");

    @Override
    public Page<OverhaulOrderResDTO> pageOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        overhaulOrderListReqDTO.setObjectFlag("1");
        // 专业未筛选时，按当前用户专业隔离数据  获取当前用户所属组织专业
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId()) && StringUtils.isEmpty(overhaulOrderListReqDTO.getSubjectCode())) {
            overhaulOrderListReqDTO.setMajors(userAccountService.listUserMajor());
        }
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            overhaulOrderListReqDTO.setUserId(TokenUtils.getCurrentPersonId());
        }
        Page<OverhaulOrderResDTO> page = overhaulOrderMapper.pageOrder(pageReqDTO.of(), overhaulOrderListReqDTO);
        List<OverhaulOrderResDTO> list = page.getRecords();
        // 专业为车辆的检修工单填充字段
        if (StringUtils.isNotEmpty(list) && CommonConstants.CAR_SUBJECT_CODE.equals(overhaulOrderListReqDTO.getSubjectCode())) {
            for (OverhaulOrderResDTO res : list) {
                OverhaulOrderResDTO ext = overhaulOrderMapper.getCarOrderExt(res.getOrderCode(), res.getPlanCode());
                OverhaulOrderResDTO rule = overhaulOrderMapper.getCarOrderRuleExt(res.getOrderCode(), res.getPlanCode());
                if (!Objects.isNull(ext)) {
                    res.setLastMile(ext.getLastMile());
                    res.setLastDay(ext.getLastDay());
                }
                if (!Objects.isNull(rule)) {
                    res.setProvideMile(rule.getProvideMile());
                    res.setProvideTime(rule.getProvideTime());
                }
                if (!Objects.isNull(ext) && !Objects.isNull(rule)) {
                    res.setNowMile(ext.getLastMile() + rule.getProvideMile());
                    if (ext.getLastDay() != null && rule.getProvideTime() != null) {
                        res.setNowDay(DateUtils.addDateHour(ext.getLastDay(), rule.getProvideTime()));
                    }
                }
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<OverhaulOrderResDTO> openApiPageOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, PageReqDTO pageReqDTO) {
        return pageOverhaulOrder(overhaulOrderListReqDTO, pageReqDTO);
    }

    @Override
    public OverhaulOrderResDTO getOverhaulOrderDetail(String id) {
        OverhaulOrderResDTO res = overhaulOrderMapper.getOrder(id, "1");
        if (StringUtils.isNotNull(res) && StringUtils.isNotEmpty(res.getOrderCode())) {
            res.setFlows(overhaulOrderMapper.orderFlowDetail(res.getOrderCode()));
        }
        return res;
    }

    @Override
    public Page<MateBorrowResDTO> pageMateBorrow(String orderCode, String mateCode, String mateName, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulOrderMapper.pageMateBorrow(pageReqDTO.of(), orderCode, mateCode, mateName);
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
        String userId = TokenUtils.getCurrentPersonId();
        List<Role> roleList = roleMapper.getLoginRole(userId);
        if (StringUtils.isNotEmpty(roleList)) {
            List<String> roles = roleList.stream().map(Role::getRoleCode).collect(Collectors.toList());
            if (CommonConstants.ONE_STRING.equals(order.getWorkStatus())) {
                if (!roles.contains(CommonConstants.DM_007) && !CommonConstants.ADMIN.equals(userId) && !roles.contains(CommonConstants.DM_048)) {
                    throw new CommonException(ErrorCode.NORMAL_ERROR, "首次派工必须是调度派工给工班长！");
                }
            } else if (!roles.contains(CommonConstants.DM_012) && !roles.contains(CommonConstants.DM_051) && !CommonConstants.ADMIN.equals(userId)) {
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
//        String groupCname = "DM_013";
//        if (CommonConstants.ONE_STRING.equals(workStatus)) {
//            groupCname = "DM_012";
//        }
//        return userGroupMemberService.getDepartmentUserByGroupName(groupCname, workerGroupCode);
        List<OrganMajorLineType> list = new ArrayList<>();
        List<MemberResDTO> memberList = organizationService.listMember(workerGroupCode);
        if (StringUtils.isNotEmpty(memberList)) {
            for (MemberResDTO member : memberList) {
                OrganMajorLineType res = new OrganMajorLineType();
                res.setLoginName(member.getId());
                res.setUserName(member.getName());
                List<UserRoleResDTO>  userRoles = userAccountService.getUserRolesById(member.getId());
                for(UserRoleResDTO r:userRoles){
                    //是工班长:DM_012是中车工班长 DM051是中铁通工班长
                    if(CommonConstants.DM_012.equals(r.getRoleCode())|| CommonConstants.DM_051.equals(r.getRoleCode())){
                        res.setIsDM012(CommonConstants.ONE_STRING);
                    }
                }

                list.add(res);
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dispatchWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulOrderMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        try {
            checkOrderState(overhaulOrderReqDTO, "1,2", "请求、已下达");
            // 直接派工至工班长，工班人员可看到该工单，注释掉已下达，直接设置为已分配
//            overhaulOrderReqDTO.setWorkStatus("2");
//            overhaulOrderReqDTO.setRecDeletor(TokenUtils.getCurrentPerson().getPersonName() + "-" + DateUtils.getCurrentTime());
//        } else {
            // 这句有点奇怪，为什么设置删除者这个字段?TODO 后续需要排查
            overhaulOrderReqDTO.setRecDeletor(TokenUtils.getCurrentPerson().getPersonName() + "-" + DateUtils.getCurrentTime());

            overhaulOrderReqDTO.setSendPersonId(TokenUtils.getCurrentPersonId());
            overhaulOrderReqDTO.setSendPersonName(TokenUtils.getCurrentPerson().getPersonName());
            overhaulOrderReqDTO.setSendTime(DateUtils.getCurrentTime());
            overhaulOrderReqDTO.setWorkStatus("3");
            String workerGroupCode = overhaulOrderReqDTO.getWorkerGroupCode();
            if (StringUtils.isNotEmpty(workerGroupCode)) {
                // 派工 直接派工至该工班人员
                overTodoService.insertTodoWithUserOrgan(String.format(CommonConstants.TODO_GD_TPL, overhaulOrderReqDTO.getOrderCode(), "检修"), overhaulOrderReqDTO.getRecId(), overhaulOrderReqDTO.getOrderCode(), workerGroupCode, "检修工单派工", "DMER0200", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.OVERHAUL_ORDER.value());
            }
            overhaulOrderReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
            overhaulOrderReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
            overhaulWorkRecordService.insertRepair(overhaulOrderReqDTO);
            overhaulOrderMapper.modifyOverhaulOrder(overhaulOrderReqDTO);
            // 添加流程记录
            addOverhaulOrderFlow(overhaulOrderReqDTO.getOrderCode(), null);
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }

    @Override
    public void finishOrder(OverhaulOrderReqDTO req) {
        req.setRecRevisor(TokenUtils.getCurrentPersonId());
        req.setRecReviseTime(DateUtils.getCurrentTime());
        req.setRealStartTime(req.getActualStartTime());
        req.setRealEndTime(req.getActualEndTime());
        req.setRealMile(req.getEndMile());
        overhaulOrderMapper.modifyOverhaulOrder(req);
        overhaulItemMapper.finishedOverhaulOrder(req.getOrderCode());
        // 根据专业判断 车辆的是中车-》中车专业工程师 其他是中铁通 -》中铁通专业工程师
        // DM_032 专业工程师（中车)  DM_006   专业工程师(中铁通)
        String nextRole = nextRole(req, "DM_006", "DM_032");
        List<BpmnExaminePersonRes> userList = roleMapper.getUserBySubjectAndLineAndRole(null, null, nextRole);
        for (BpmnExaminePersonRes map2 : userList) {
            overTodoService.insertTodo(String.format(CommonConstants.TODO_GD_TPL,req.getOrderCode(),"检修"), req.getRecId(), req.getOrderCode(), map2.getUserId(), "检修工单完工", "DMER0200", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.OVERHAUL_ORDER.value());
        }
        // 添加流程记录
        addOverhaulOrderFlow(req.getOrderCode(), null);
    }

    @Override
    public void auditWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulOrderMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        // if (CommonConstants.CAR_SUBJECT_CODE.equals(overhaulOrderReqDTO.getSubjectCode()) && !overhaulOrderReqDTO.getPlanName().contains(CommonConstants.SECOND_REPAIR_SHIFT)) {
        //     throw new CommonException(ErrorCode.NORMAL_ERROR, "只有工单为车辆二级修才能进行该操作。");
        // }
        checkOrderState(overhaulOrderReqDTO, "4", "完工");
        if (CommonConstants.ZERO_STRING.equals(overhaulOrderReqDTO.getExamineStatus())) {
            overhaulOrderReqDTO.setWorkStatus(CommonConstants.SIX_STRING);
        } else {
            overhaulOrderReqDTO.setWorkStatus(CommonConstants.THREE_STRING);
        }
        overhaulOrderReqDTO.setRecDeleteTime(DateUtils.getCurrentTime());
        overhaulOrderReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulOrderReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulOrderReqDTO.setExt1(" ");
        overhaulOrderMapper.modifyOverhaulOrder(overhaulOrderReqDTO);
        // ServiceDMER0201  auditWorkers
        //完成该业务编号下的所有待办
        overTodoService.overTodo(overhaulOrderReqDTO.getOrderCode());
        // 根据角色获取用户列表
        if (CommonConstants.ZERO_STRING.equals(overhaulOrderReqDTO.getExamineStatus())) {
            String roleCode = nextRole(overhaulOrderReqDTO,"ZCJD","DM_30");
            List<BpmnExaminePersonRes> userList = roleMapper.getUserBySubjectAndLineAndRole(null, null, roleCode);
            if (CollectionUtil.isNotEmpty(userList)){
                for (BpmnExaminePersonRes map2 : userList) {
                    overTodoService.insertTodo(String.format(CommonConstants.TODO_GD_TPL,overhaulOrderReqDTO.getOrderCode(),"检修"), overhaulOrderReqDTO.getRecId(), overhaulOrderReqDTO.getOrderCode(),
                            map2.getUserId(), "检修工单完工确认", "DMER0200", TokenUtils.getCurrentPersonId(),BpmnFlowEnum.OVERHAUL_ORDER.value());
                }
            }
        } else {
            String workerGroupCode = overhaulOrderReqDTO.getWorkerGroupCode();
            if (StringUtils.isNotEmpty(workerGroupCode)) {
                // 派工 直接派工至该工班人员
                overTodoService.insertTodoWithUserOrgan(String.format(CommonConstants.TODO_GD_TPL, overhaulOrderReqDTO.getOrderCode(), "检修"), overhaulOrderReqDTO.getRecId(), overhaulOrderReqDTO.getOrderCode(), workerGroupCode, "检修工单派工", "DMER0200", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.OVERHAUL_ORDER.value());
            }
        }
        // 添加流程记录
        addOverhaulOrderFlow(overhaulOrderReqDTO.getOrderCode(), overhaulOrderReqDTO.getExamineOpinion());
    }

    /**
     *
     * @param overhaulOrderReqDTO
     * @param zcRole 中车角色
     * @param zttRole 中铁通角色
     * @return
     */
    private static String nextRole(OverhaulOrderReqDTO overhaulOrderReqDTO,String zcRole,String zttRole) {
        String roleCode = null;
        if (zcList.contains(overhaulOrderReqDTO.getSubjectCode())) {
            roleCode = zcRole;
        } else {
            roleCode = zttRole;
        }
        return roleCode;
    }

    @Override
    public void confirmWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) throws ParseException {
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulOrderMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        // if (!overhaulOrderReqDTO.getPlanName().contains(CommonConstants.SECOND_REPAIR_SHIFT)) {
        //     checkOrderState(overhaulOrderReqDTO, "4", "完工");
        // } else {
            checkOrderState(overhaulOrderReqDTO, "6", "验收");
        // }
        if (org.apache.commons.lang3.StringUtils.isBlank(overhaulOrderReqDTO.getRealEndTime())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "该工单没有实际完成时间，无法完工确认！");
        }
        if (CommonConstants.ZERO_STRING.equals(overhaulOrderReqDTO.getExamineStatus())) {
            overhaulOrderReqDTO.setWorkStatus(CommonConstants.FIVE_STRING);
        } else {
            overhaulOrderReqDTO.setWorkStatus(CommonConstants.THREE_STRING);
        }
        overhaulOrderReqDTO.setAckPersonId(TokenUtils.getCurrentPersonId());
        overhaulOrderReqDTO.setAckPersonName(TokenUtils.getCurrentPerson().getPersonName());
        overhaulOrderReqDTO.setConfirTime(DateUtils.getCurrentTime());
        overhaulOrderReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulOrderReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulOrderReqDTO.setExt1(" ");
        overhaulOrderMapper.modifyOverhaulOrder(overhaulOrderReqDTO);
        modifyOverhaulPlanByOrder(overhaulOrderReqDTO);
        // ServiceDMER0201  confirmWorkers
        //完成待办
        overTodoService.overTodo(overhaulOrderReqDTO.getOrderCode());
        if (CommonConstants.ONE_STRING.equals(overhaulOrderReqDTO.getExamineStatus())) {
            String workerGroupCode = overhaulOrderReqDTO.getWorkerGroupCode();
            if (StringUtils.isNotEmpty(workerGroupCode)) {
                // 派工 直接派工至该工班人员
                overTodoService.insertTodoWithUserOrgan(String.format(CommonConstants.TODO_GD_TPL, overhaulOrderReqDTO.getOrderCode(), "检修"), overhaulOrderReqDTO.getRecId(), overhaulOrderReqDTO.getOrderCode(), workerGroupCode, "检修工单派工", "DMER0200", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.OVERHAUL_ORDER.value());
            }
        }
        // 添加流程记录
        addOverhaulOrderFlow(overhaulOrderReqDTO.getOrderCode(), overhaulOrderReqDTO.getExamineOpinion());
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
                SimpleDateFormat dateTimeFormat1 = new SimpleDateFormat("yyyy-MM-dd HH");
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
                    overhaulPlanReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
                    overhaulPlanReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
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
        overhaulOrderReqDTO.setCancelPersonId(TokenUtils.getCurrentPersonId());
        overhaulOrderReqDTO.setCancelPersonName(TokenUtils.getCurrentPerson().getPersonName());
        overhaulOrderReqDTO.setCancelTime(DateUtils.getCurrentTime());
        overhaulOrderReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulOrderReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulOrderReqDTO.setExt1(" ");
        overhaulOrderMapper.modifyOverhaulOrder(overhaulOrderReqDTO);
        // 添加流程记录
        addOverhaulOrderFlow(overhaulOrderReqDTO.getOrderCode(), null);
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
    public void pageMaterial(String orderCode, HttpServletResponse response) throws IOException {
        response.sendRedirect(dictionariesMapper.queryOneByItemCodeAndCodesetCode("DM_ER_ADDRESS", "11").getItemCname() + orderCode);
    }

    @Override
    public void receiveMaterial(HttpServletResponse response) throws IOException {
        response.sendRedirect(dictionariesMapper.queryOneByItemCodeAndCodesetCode("DM_ER_ADDRESS", "11").getItemCname());
    }

    @Override
    public void returnMaterial(HttpServletResponse response) throws IOException {
        response.sendRedirect(dictionariesMapper.queryOneByItemCodeAndCodesetCode("DM_ER_ADDRESS", "12").getItemCname());
    }

    @Override
    public Page<ConstructionResDTO> construction(String orderCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return faultQueryMapper.construction(pageReqDTO.of(), orderCode);
    }

    @Override
    public Page<ConstructionResDTO> cancellation(String orderCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return faultQueryMapper.cancellation(pageReqDTO.of(), orderCode);
    }

    @Override
    public Page<OverhaulOrderDetailResDTO> pageOverhaulObject(String orderCode, String planCode, String planName, String objectCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
    public Page<OverhaulOrderDetailOpenResDTO> openPageOverhaulObject(String orderCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulOrderMapper.openPageOverhaulObject(pageReqDTO.of(), orderCode);
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
    public void modifyOverhaulObject(OverhaulOrderDetailReqDTO req) {
        req.setRepairPerson(TokenUtils.getCurrentPersonId());
        overhaulOrderMapper.modifyOverhaulObject(req);
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
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
            modelList = modelList.stream().filter(Objects::nonNull).collect(Collectors.toList());
            modelList = new ArrayList<>(modelList.stream().collect(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(OverhaulItemResDTO::getModelName)))));
            // 当模块数组为空时，添加默认模块
            if (StringUtils.isEmpty(modelList)) {
                OverhaulItemResDTO model = new OverhaulItemResDTO();
                model.setModelName("默认模块");
                modelList.add(model);
            }
            for (OverhaulItemResDTO model : modelList) {
                OverhaulItemTreeResDTO res = new OverhaulItemTreeResDTO();
                org.springframework.beans.BeanUtils.copyProperties(model, res);
                List<OverhaulItemResDTO> list = overhaulItemMapper.listOverhaulItemByModel(objectCode, orderCode, model.getModelName());
                if (StringUtils.isNotEmpty(list)) {
                    for (OverhaulItemResDTO itemRes : list) {
                        if (StringUtils.isNotEmpty(itemRes.getDocId())) {
                            itemRes.setDocFile(fileMapper.selectFileInfo(Arrays.asList(itemRes.getDocId().split(","))));
                        }
                        // 提交结果为空时填充提交处理信息
                        if (StringUtils.isNull(res.getWorkResult())) {
                            res.setWorkResult(itemRes.getWorkResult());
                            res.setWorkUserId(itemRes.getWorkUserId());
                            res.setWorkUserName(itemRes.getWorkUserName());
                        }
                    }
                }
                res.setItemList(list);
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

    @Override
    public Integer selectHadFinishedOverhaulOrder(String orderCode, String objectCode) {
        return overhaulItemMapper.selectHadFinishedOverhaulOrder(orderCode, objectCode);
    }

    @Override
    public void troubleshootOverhaulItem(OverhaulItemTroubleshootReqDTO req) {
        if (StringUtils.isNotEmpty(req.getOverhaulItemList())) {
            for (OverhaulItemResDTO res : req.getOverhaulItemList()) {
                // 列表时选异常
                boolean bool1 = CommonConstants.TEN_STRING.equals(res.getItemType()) && CommonConstants.ERROR.equals(res.getWorkResult());
                // 数字超过上下限
                boolean bool2 = CommonConstants.TWENTY_STRING.equals(res.getItemType()) &&
                                ((StringUtils.isNotBlank(res.getMinValue()) && Integer.parseInt(res.getMinValue()) > Integer.parseInt(res.getWorkResult())) ||
                                        (StringUtils.isNotBlank(res.getMaxValue()) && Integer.parseInt(res.getMaxValue()) < Integer.parseInt(res.getWorkResult())));
                // 文本内容包含异常
                boolean bool3 = CommonConstants.THIRTY_STRING.equals(res.getItemType()) && res.getWorkResult().contains(CommonConstants.ERROR);
                // 异常时添加异常数据
                if (bool1 || bool2 || bool3) {
                    // 新增异常数据
                    OverhaulStateReqDTO overhaulStateReqDTO = new OverhaulStateReqDTO();
                    org.springframework.beans.BeanUtils.copyProperties(res, overhaulStateReqDTO);
                    overhaulStateReqDTO.setTdmer23RecId(res.getRecId());
                    overhaulStateReqDTO.setWorkUserId(req.getWorkUserId());
                    overhaulStateReqDTO.setWorkUserName(req.getWorkUserName());
                    overhaulStateMapper.addOverhaulState(overhaulStateReqDTO);
                    // 修改工单异常数量
                    overhaulItemMapper.updateOverhaulOrderErrorNum(req.getOrderCode());
                    overhaulItemMapper.updateOverhaulOrderDetailErrorNum(req.getOrderCode(), req.getObjectCode());
                }
                overhaulItemMapper.troubleshootOverhaulItem(res, req.getWorkUserId(), req.getWorkUserName());
            }
        } else {
            throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
        }
    }

    @Override
    public Page<OverhaulStateResDTO> pageOverhaulState(String objectCode, String itemName, String orderCode, String tdmer23RecId, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
            res.setUserId(TokenUtils.getCurrentPersonId());
            String userName = TokenUtils.getCurrentPerson().getPersonName();
            String discovererPhone = TokenUtils.getCurrentPerson().getPhone();
            String currentUser = TokenUtils.getCurrentPerson().getPersonName();
            String orgCode = TokenUtils.getCurrentPerson().getOfficeId();
            String orgName = TokenUtils.getCurrentPerson().getOfficeName();
            res.setFillinUserId(currentUser);
            res.setFillinUserName(userName);
            res.setDiscovererId(userName);
            res.setDiscovererName(currentUser);
            res.setDiscovererPhone(discovererPhone);
            res.setDiscoveryTime(DateUtils.getCurrentTime());
            res.setFillinTime(DateUtils.getCurrentTime());
            res.setFillinDeptName(orgName);
            res.setFillinDeptCode(orgCode);
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upState(OverhaulUpStateReqDTO overhaulUpStateReqDTO) {
        String currentUser = TokenUtils.getCurrentPerson().getPersonName();
        String orderCode = overhaulUpStateReqDTO.getOrderCode();
        String objectCode = overhaulUpStateReqDTO.getObjectCode();
        OverhaulOrderListReqDTO overhaulOrderListReqDTO = new OverhaulOrderListReqDTO();
        overhaulOrderListReqDTO.setOrderCode(orderCode);
        List<OverhaulOrderResDTO> list = overhaulOrderMapper.listOrder(overhaulOrderListReqDTO);
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }

        try{
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
            dmfm02.setRecId(TokenUtils.getUuId());
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
            overTodoService.insertTodoWithUserRoleAndOrg("【" + equipmentCategoryMapper.listEquipmentCategory(null, list.get(0).getSubjectCode(), null).get(0).getNodeName() + CommonConstants.FAULT_CONTENT_END,
                    dmfm02.getRecId(), faultWorkNo, "DM_013", list.get(0).getWorkerGroupCode(), "故障维修", "DMFM0001", currentUser, content, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        }catch (Exception e){
            log.error(e.getMessage());
        }

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
        faultInfo.setRecCreator(TokenUtils.getCurrentPerson().getPersonName());
        faultInfo.setRecCreateTime(DateUtils.getCurrentTime());
        faultInfo.setDiscoveryTime(DateUtils.getCurrentTime());
        faultInfo.setFillinTime(DateUtils.getCurrentTime());
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
//        } else if (groups.contains(CommonConstants.DM_007)) {
//            ext2 = CommonConstants.DM_007;
//        } else if (groups.contains(CommonConstants.DM_006)) {
//            ext2 = CommonConstants.DM_006;
//        } else {
//            ext2 = "";
//        }
        return ext2;
    }

    /**
     * 新增工单流程
     * @param orderCode 工单编号
     * @param remark 备注
     */
    public void addOverhaulOrderFlow(String orderCode, String remark) {
        OverhaulOrderFlowReqDTO orderFlow = new OverhaulOrderFlowReqDTO();
        orderFlow.setRecId(TokenUtils.getUuId());
        orderFlow.setOrderCode(orderCode);
        orderFlow.setOperateUserId(TokenUtils.getCurrentPersonId());
        orderFlow.setOperateUserName(TokenUtils.getCurrentPerson().getPersonName());
        orderFlow.setOperateTime(DateUtils.getCurrentTime());
        OverhaulOrderListReqDTO overhaulOrderListReqDTO = new OverhaulOrderListReqDTO();
        overhaulOrderListReqDTO.setOrderCode(orderCode);
        List<OverhaulOrderResDTO> orderDetail = overhaulOrderMapper.listOverhaulOrder(overhaulOrderListReqDTO);
        if (StringUtils.isNotEmpty(orderDetail)) {
            if (StringUtils.isNotNull(orderDetail)) {
                orderFlow.setWorkStatus(orderDetail.get(0).getWorkStatus());
            }
        }
        if (StringUtils.isNotEmpty(remark)) {
            orderFlow.setRemark(remark);
        }
        overhaulOrderMapper.addOverhaulOrderFlow(orderFlow);
    }

}
