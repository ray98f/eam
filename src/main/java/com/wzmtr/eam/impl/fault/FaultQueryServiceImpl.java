package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.common.collect.Lists;
import com.wzmtr.eam.bizobject.PartBO;
import com.wzmtr.eam.bizobject.export.FaultExportBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.bpmn.BpmnExaminePersonRes;
import com.wzmtr.eam.dto.res.common.MemberResDTO;
import com.wzmtr.eam.dto.res.common.PersonResDTO;
import com.wzmtr.eam.dto.res.common.UserCenterInfoResDTO;
import com.wzmtr.eam.dto.res.common.UserRoleResDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultOrderResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.enums.*;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.PartMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.common.UserAccountMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.fault.FaultAnalyzeMapper;
import com.wzmtr.eam.mapper.fault.FaultInfoMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.common.OrganizationService;
import com.wzmtr.eam.service.common.UserAccountService;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 17:02
 */
@Service
@Slf4j
public class FaultQueryServiceImpl implements FaultQueryService {
    public static final String IS_TIKAI_CODE = "08";
    @Resource
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private FaultQueryMapper faultQueryMapper;
    @Autowired
    private FaultReportMapper faultReportMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private OverTodoService overTodoService;
    @Autowired
    private DictionariesMapper dictionariesMapper;
    @Autowired
    private FaultAnalyzeMapper faultAnalyzeMapper;
    @Autowired
    private PartMapper partMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FaultInfoMapper faultInfoMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private OrganizationService organizationService;

    @Override
    public Page<FaultDetailResDTO> list(FaultQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        SysOffice office = userAccountMapper.getUserOrg(TokenUtils.getCurrentPersonId());
        // 专业未筛选时，按当前用户专业隔离数据  获取当前用户所属组织专业
        List<String> userMajorList = null;
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId()) && StringUtils.isEmpty(reqDTO.getMajorCode()) &&
        StringUtils.isNotNull(office) && !office.getNames().contains(CommonConstants.PASSENGER_TRANSPORT_DEPT)) {
            userMajorList = userAccountService.listUserMajor();
        }
        if (StringUtils.isNotEmpty(reqDTO.getOrderStatus()) && reqDTO.getOrderStatus().contains(CommonConstants.COMMA)) {
            reqDTO.setOrderStatusList(Arrays.asList(reqDTO.getOrderStatus().split(CommonConstants.COMMA)));
            reqDTO.setOrderStatus(null);
        }
        Page<FaultDetailResDTO> page;
        //获取用户当前角色
        List<UserRoleResDTO> userRoles = userAccountService.getUserRolesById(TokenUtils.getCurrentPersonId());
        //admin 中铁通生产调度 中车生产调度可以查看本专业的所有数据外 ，其他的角色根据 提报、派工 、验收阶段人员查看
        if (CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_007))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_048))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_004))) {
            page = faultQueryMapper.query(reqDTO.of(), reqDTO, userMajorList);
        } else {
            page = faultQueryMapper.queryByUser(reqDTO.of(), reqDTO, userMajorList, TokenUtils.getCurrentPersonId(), TokenUtils.getCurrentPerson().getOfficeAreaId());
        }
        List<FaultDetailResDTO> list = page.getRecords();
        // 如果用户的角色中包含中车、中铁通专业工程师，获取状态为完工验收之后的数据
        if (userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_032))
                || userRoles.stream().anyMatch(x -> x.getRoleCode().equals(CommonConstants.DM_006))) {
            List<FaultDetailResDTO> other = faultQueryMapper.queryByEngineer(userMajorList);
            if (StringUtils.isNotEmpty(other)) {
                list.addAll(other);
                list = list.stream().distinct()
                        .sorted(Comparator.comparing(FaultDetailResDTO::getFaultNo).reversed()
                                .thenComparing(FaultDetailResDTO::getFaultWorkNo).reversed())
                        .collect(Collectors.toList());
            }
        }
        for (FaultDetailResDTO res : list) {
            buildRes(res);
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public List<FaultDetailResDTO> queryLimit() {
        List<String> userMajorList = null;
        String userDept = null;
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            UserCenterInfoResDTO userinfo = userAccountService.getUserDetail();
            userDept = userinfo.getOfficeId();
            if (StringUtils.isNotEmpty(userinfo.getUserMajors())) {
                userMajorList = userinfo.getUserMajors();
            }
        }
        return faultQueryMapper.queryLimit(userDept, userMajorList);
    }

    @Override
    public Page<FaultDetailResDTO> statustucList(FaultQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<FaultDetailResDTO> page = faultQueryMapper.statustucQuery(reqDTO.of(), reqDTO);
        List<FaultDetailResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (FaultDetailResDTO res : list) {
                buildRes(res);
            }
        }
        page.setRecords(list);
        return page;
    }

    private void buildRes(FaultDetailResDTO a) {
        if (StringUtils.isNotEmpty(a.getDocId())) {
            a.setDocFile(fileMapper.selectFileInfo(Arrays.asList(a.getDocId().split(","))));
        }
        if (StringUtils.isNotEmpty(a.getRepairDeptCode())) {
            a.setRepairDeptName(organizationMapper.getNamesById(a.getRepairDeptCode()));
        }
        if (StringUtils.isNotEmpty(a.getFillinDeptCode())) {
            a.setFillinDeptName(organizationMapper.getNamesById(a.getFillinDeptCode()));
        }
    }

    @Override
    public String queryOrderStatus(SidEntity reqDTO) {
        List<String> status = faultQueryMapper.queryOrderStatus(reqDTO);
        return StringUtils.isEmpty(status) ? null : status.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issue(FaultDetailReqDTO reqDTO) {
        String status = queryOrderStatus(SidEntity.builder().id(reqDTO.getFaultWorkNo()).build());
        FaultOrderDO faultOrder = BeanUtils.convert(reqDTO, FaultOrderDO.class);
        switch (status) {
            case "40":
                faultOrder.setReportStartUserId(TokenUtils.getCurrentPersonId());
                faultOrder.setReportStartTime(DateUtils.getCurrentTime());
                break;
            // 完工
            case "50":
                faultOrder.setReportFinishUserId(TokenUtils.getCurrentPersonId());
                faultOrder.setReportFinishTime(DateUtils.getCurrentTime());
                break;
            // 完工确认
            case "60":
                faultOrder.setConfirmUserId(TokenUtils.getCurrentPersonId());
                faultOrder.setConfirmTime(DateUtils.getCurrentTime());
                break;
            // 验收
            case "55":
                faultOrder.setCheckUserId(TokenUtils.getCurrentPersonId());
                faultOrder.setCheckTime(DateUtils.getCurrentTime());
                break;
            default:
                break;
        }
        faultOrder.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultOrder.setRecReviseTime(DateUtils.getCurrentTime());
        faultOrder.setOrderStatus(OrderStatus.XIA_FA.getCode());

        //TODO 只更新下发状态?
        faultReportMapper.updateFaultOrder(faultOrder);

        FaultInfoDO faultInfoUpdate = BeanUtils.convert(reqDTO, FaultInfoDO.class);
        faultInfoUpdate.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultInfoUpdate.setRecReviseTime(DateUtils.getCurrentTime());
        faultReportMapper.updateFaultInfo(faultInfoUpdate);
        // ServiceDMFM0001 update
        FaultInfoDO faultInfo = faultQueryMapper.queryOneFaultInfo(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        List<FaultOrderResDTO> faultOrderList = faultReportMapper.listOrderByNoAndWorkNo(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        if (StringUtils.isNotEmpty(faultOrderList)) {
            BeanUtils.copy(faultOrderList.get(0), faultOrder);
        }

        //TODO 工班长接收待办信息
        overTodoService.overTodo(faultOrder.getRecId(), "提报成功，准备下发");
        String content = "【市铁投集团】" + TokenUtils.getCurrentPerson().getOfficeName() + "的" + TokenUtils.getCurrentPerson().getPersonName() +
                "下发一条" + faultInfo.getMajorName() + "故障，工单号：" + reqDTO.getFaultWorkNo() + "，尽快派工。";
        Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_VEHICLE_SPECIALTY_CODE, "01");
        String codeName = dictionaries.getItemEname();
        List<String> cos = Arrays.asList(codeName.split(","));
        if (cos.contains(faultInfo.getMajorCode())) {
            dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "04");
        } else {
            dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "03");
        }
        String zcStepOrg = dictionaries.getItemEname();
        overTodoService.insertTodoWithUserGroupAndAllOrg("【" + faultInfo.getMajorName() + CommonConstants.FAULT_CONTENT_END, faultOrder.getRecId(),
                reqDTO.getFaultWorkNo(), CommonConstants.DM_007, zcStepOrg, "故障派工", "DMFM0001", TokenUtils.getCurrentPersonId(),
                faultInfo.getMajorCode(), faultInfo.getLineCode(), "20", content, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        // 添加流程记录
        addFaultFlow(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo(), null);
    }

    @Override
    public void export(FaultExportReqDTO reqDTO, HttpServletResponse response) {
        // com.baosight.wzplat.dm.fm.service.ServiceDMFM0001#export
        List<FaultDetailResDTO> faultDetailRes = faultQueryMapper.export(reqDTO);
        if (StringUtils.isEmpty(faultDetailRes)) {
            return;
        }
        List<FaultExportBO> exportList = Lists.newArrayList();
        faultDetailRes.forEach(resDTO -> exportList.add(buildExportBO(resDTO)));
        try {
            EasyExcelUtils.export(response, "故障信息", exportList);
        } catch (Exception e) {
            log.error("导出失败!", e);
            throw new CommonException(ErrorCode.EXPORT_ERROR);
        }
    }

    @NotNull
    private FaultExportBO buildExportBO(FaultDetailResDTO resDTO) {
        FaultExportBO export = BeanUtils.convert(resDTO, FaultExportBO.class);
        OrderStatus orderStatus = OrderStatus.getByCode(resDTO.getOrderStatus());
        FaultAffect faultAffect = FaultAffect.getByCode(resDTO.getFaultAffect());
        FaultLevel faultLevel = FaultLevel.getByCode(resDTO.getOrderStatus());
        DealerUnit dealerUnit = DealerUnit.getByCode(resDTO.getDealerUnit());
        LineCode lineCode = LineCode.getByCode(resDTO.getLineCode());
        FaultType faultType = FaultType.getByCode(resDTO.getFaultType());
        Dictionaries position2 = dictionariesMapper.queryOneByItemCodeAndCodesetCode("dm.station2", Objects.isNull(resDTO.getPosition2Code()) ? "" : resDTO.getPosition2Code());
        String repairDept = null;
        String fillinDept = null;
        if (StringUtils.isNotEmpty(resDTO.getRepairDeptCode())) {
            repairDept = organizationMapper.getNamesById(resDTO.getRepairDeptCode());
        }
        if (StringUtils.isNotEmpty(resDTO.getFillinDeptCode())) {
            fillinDept = organizationMapper.getNamesById(resDTO.getFillinDeptCode());
        }
//        export.setReplacementName(dealerUnit != null ? dealerUnit.getDesc() : resDTO.getDealerUnit());
        if (StringUtils.isNotEmpty(fillinDept)) {
            export.setFillinDept(Optional.ofNullable(fillinDept).orElse(CommonConstants.EMPTY));
            export.setRepairDept(Optional.ofNullable(repairDept).orElse(CommonConstants.EMPTY));
        }
        if (!Objects.isNull(position2)) {
            export.setPosition2Name(Optional.ofNullable(position2.getItemCname()).orElse(CommonConstants.EMPTY));
        }
        export.setOrderStatus(orderStatus != null ? orderStatus.getDesc() : resDTO.getOrderStatus());
        export.setFaultType(faultType == null ? resDTO.getFaultType() : faultType.getDesc());
        export.setFaultLevel(faultLevel == null ? resDTO.getFaultLevel() : faultLevel.getDesc());
        export.setFaultAffect(faultAffect != null ? faultAffect.getDesc() : resDTO.getFaultAffect());
        export.setLineName(lineCode != null ? lineCode.getDesc() : resDTO.getLineCode());
        PartBO partBO = partMapper.queryPartByFaultWorkNo(resDTO.getFaultWorkNo());
        if (null != partBO) {
            export.setReplacementName(partBO.getReplacementName());
            export.setOldRepNo(partBO.getOldRepNo());
            export.setNewRepNo(partBO.getNewRepNo());
            export.setOperateCostTime(partBO.getOperateCostTime());
        }
        return export;
    }

    @Override
    public Page<ConstructionResDTO> construction(FaultQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<ConstructionResDTO> list = faultQueryMapper.construction(reqDTO.of(), reqDTO.getFaultWorkNo());
        List<ConstructionResDTO> records = list.getRecords();
        if (StringUtils.isEmpty(records)) {
            return new Page<>();
        }
        return list;
    }

    @Override
    public Page<ConstructionResDTO> cancellation(FaultQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<ConstructionResDTO> list = faultQueryMapper.cancellation(reqDTO.of(), reqDTO.getFaultWorkNo());
        List<ConstructionResDTO> records = list.getRecords();
        if (StringUtils.isEmpty(records)) {
            return new Page<>();
        }
        return list;
    }


    @Override
    public List<PersonResDTO> queryUserList(Set<String> userCode, String organCode) {
        List<PersonResDTO> orgUsers = faultAnalyzeMapper.getOrgUsers(userCode, organCode);
        if (StringUtils.isEmpty(orgUsers)) {
            List<PersonResDTO> parentList = faultAnalyzeMapper.queryCoParent(organCode);
            if (StringUtils.isEmpty(parentList)) {
                return orgUsers;
            }
            PersonResDTO parent = parentList.get(0);
            String orgCode = parent.getOrgCode();
            if (StringUtils.isEmpty(orgCode)) {
                return orgUsers;
            }
            return queryUserList(userCode, orgCode);
        }
        return orgUsers;
    }

    @Override
    public Boolean compareRows(CompareRowsReqDTO req) {
        // 只选中一条直接返回T
        if (StringUtils.isEmpty(req.getFaultNos()) || req.getFaultNos().size() == 1) {
            return true;
        }
        List<FaultInfoDO> list = faultInfoMapper.list(req.getFaultNos());
        // 不为null且长度大于1。如果满足条件，则返回1，表示为真；否则返回0，表示为假
        if (StringUtils.isNotEmpty(list)) {
            Set<String> majorCodelist = list.stream().map(FaultInfoDO::getMajorCode).collect(Collectors.toSet());
            Set<String> lineCodelist = list.stream().map(FaultInfoDO::getLineCode).collect(Collectors.toSet());
            // 检查majorCodelist和lineCodelist的大小是否都为1则为相同的major和lineCode
            return majorCodelist.size() != 1 || lineCodelist.size() != 1;
            // if (s.size() == 1 && hs.size() == 1 && hhs.isEmpty()) {
        } else {
            return true;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendWork(FaultSendWorkReqDTO reqDTO) {
        // 派工 ServiceDMFM0002 update
        if (StringUtils.isEmpty(reqDTO.getWorkerGroupCode())) {
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
        List<String> faultWorkNos = Arrays.asList(reqDTO.getFaultWorkNo().split(","));
        faultWorkNos.forEach(a -> {
            String workerGroupCode = reqDTO.getWorkerGroupCode();
            FaultOrderDO faultOrder = faultQueryMapper.queryOneFaultOrder(null, a);
            if (faultOrder != null) {
                if (StringUtils.isNotEmpty(reqDTO.getFaultLevel())) {
                    faultOrder.setExt1(reqDTO.getFaultLevel());
                }
                faultOrder.setFaultAffect(reqDTO.getFaultAffect());
                faultOrder.setWorkArea(reqDTO.getWorkArea());
                faultOrder.setPlanRecoveryTime(reqDTO.getPlanRecoveryTime());
                faultOrder.setRepairRespUserId(reqDTO.getRepairRespUserId());
                faultOrder.setDispatchUserId(TokenUtils.getCurrentPersonId());
                faultOrder.setDispatchTime(DateUtils.getCurrentTime());
                faultOrder.setRecRevisor(TokenUtils.getCurrentPersonId());
                faultOrder.setRecReviseTime(DateUtils.getCurrentTime());
                faultOrder.setOrderStatus(OrderStatus.PAI_GONG.getCode());
                faultReportMapper.updateFaultOrder(faultOrder);
                FaultInfoDO faultInfo = faultQueryMapper.queryOneFaultInfo(faultOrder.getFaultNo(), faultOrder.getFaultWorkNo());
                faultInfo.setRepairDeptCode(workerGroupCode);
                if (StringUtils.isNotEmpty(reqDTO.getIsTiKai()) && IS_TIKAI_CODE.equals(reqDTO.getIsTiKai())) {
                    faultInfo.setExt3("08");
                }
                faultInfo.setRecRevisor(TokenUtils.getCurrentPersonId());
                faultInfo.setRecReviseTime(DateUtils.getCurrentTime());
                faultInfo.setFaultNo(faultOrder.getFaultNo());
                faultReportMapper.updateFaultInfo(faultInfo);
                sendDispatchTodoMessage(faultOrder, workerGroupCode, null);

                Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "01");
                String zcStepOrg = dictionaries.getItemEname();
                if (StringUtils.isNotEmpty(faultOrder.getWorkClass()) && !faultOrder.getWorkClass().contains(zcStepOrg)) {
                    // todo 调用施工调度接口
                    sendContractFault(faultOrder);
                }
                // 添加流程记录
                addFaultFlow(faultOrder.getFaultNo(), reqDTO.getFaultWorkNo(), null);
            }
        });
    }

    /**
     * 故障派工待办推送
     * @param faultOrder 故障工单信息
     * @param workerGroupCode 作业工班编号
     * @param userId 人员id
     */
    private void sendDispatchTodoMessage(FaultOrderDO faultOrder, String workerGroupCode, String userId) {
        List<BpmnExaminePersonRes> userList = new ArrayList<>();
        if (StringUtils.isNotEmpty(userId)) {
            BpmnExaminePersonRes res = new BpmnExaminePersonRes();
            res.setUserId(userId);
            userList.add(res);
        } else {
            String newId = organizationMapper.getIdByAreaId(workerGroupCode);
            userList = roleMapper.getUserByOrgAndRole(newId, null);
        }
        String faultWorkNo = faultOrder.getFaultWorkNo();
        overTodoService.overTodo(faultWorkNo);
        if (CollectionUtil.isNotEmpty(userList)) {
            for (BpmnExaminePersonRes map2 : userList) {
                overTodoService.insertTodo(String.format(CommonConstants.TODO_GD_TPL, faultWorkNo, "故障"), faultOrder.getRecId(), faultWorkNo, map2.getUserId(), "故障派工", "DMFM0001", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.FAULT_REPORT_QUERY.value());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishWork(FaultFinishWorkReqDTO reqDTO) {
        // 完工 ServiceDMFM0002 finishWork
        if (StringUtils.isNotEmpty(reqDTO.getOrderStatus())) {
            FaultOrderDO faultOrder = BeanUtils.convert(reqDTO, FaultOrderDO.class);
            faultOrder.setReportFinishUserId(TokenUtils.getCurrentPersonId());
            faultOrder.setReportFinishUserName(TokenUtils.getCurrentPerson().getPersonName());
            faultOrder.setReportFinishTime(DateUtils.getCurrentTime());
            faultOrder.setRecRevisor(TokenUtils.getCurrentPersonId());
            faultOrder.setRecReviseTime(DateUtils.getCurrentTime());
            faultOrder.setOrderStatus(OrderStatus.WAN_GONG.getCode());
            faultReportMapper.updateFaultOrder(faultOrder);
            FaultInfoDO faultInfo = BeanUtils.convert(reqDTO, FaultInfoDO.class);
            faultInfo.setRecRevisor(TokenUtils.getCurrentPersonId());
            faultInfo.setRecReviseTime(DateUtils.getCurrentTime());
            faultReportMapper.updateFaultInfo(faultInfo);
            // 添加流程记录
            addFaultFlow(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo(), null);
        }
        finishWorkSendMessage(reqDTO);
    }

    @Override
    public void eqCheck(FaultEqCheckReqDTO reqDTO) {
        String faultWorkNo = reqDTO.getFaultWorkNo();
        String currentUser = TokenUtils.getCurrentPersonId();
        String faultNo = reqDTO.getFaultNo();
        FaultInfoDO faultInfoDO = faultQueryMapper.queryOneFaultInfo(faultNo, faultWorkNo);
        String majorCode = faultInfoDO.getMajorCode();
        String majorName = faultInfoDO.getMajorName();
        String ext2 = faultInfoDO.getExt2();
        String fillinUserId = faultInfoDO.getFillinUserId();
        FaultOrderDO faultOrderDO = faultQueryMapper.queryOneFaultOrder(null, faultWorkNo);
        String workClass = faultOrderDO.getWorkClass();
        overTodoService.overTodo(faultOrderDO.getRecId(), CommonConstants.FAULT_TUNING_CONFIRM_CN);
        Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_VEHICLE_SPECIALTY_CODE, "01");
        String itemEname = dictionaries.getItemEname();
        String[] cos01 = itemEname.split(",");
        List<String> cos = Arrays.asList(cos01);
        if (CommonConstants.DM_013.equals(ext2)) {
            overTodoService.overTodo(faultOrderDO.getRecId(), CommonConstants.FAULT_TUNING_CONFIRM_CN);
            // String content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已设调确认，请及时在EAM系统关闭工单！";
            overTodoService.insertTodoWithUserRoleAndOrg("【" + majorName + CommonConstants.FAULT_CONTENT_END, faultOrderDO.getRecId(), faultWorkNo, "DM_013", workClass, "故障关闭", "DMFM0001", currentUser, null, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        }
        // else if (ext2.equals(CommonConstants.DM_006)) {
        //     overTodoService.overTodo(faultOrderDO.getRecId(), CommonConstants.FAULT_TUNING_CONFIRM_CN);
        //     if (cos.contains(majorCode)) {
        //         if (majorCode.equals("07")) {
        //             content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已完工确认，请及时在EAM系统关闭工单！";
        //             String stepOrg = CodeFactory.getCodeService().getCodeEName(CommonConstants.DM_MATCH_CONTROL_CODE, "04", "1");
        //             status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, CommonConstants.DM_007, stepOrg, "故障关闭", "DMFM0001", currentUser, majorCode, lineCode, "30", content);
        //         } else if (majorCode.equals("06")) {
        //             content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已完工确认，请及时在EAM系统关闭工单！";
        //             String stepOrg = CodeFactory.getCodeService().getCodeEName(CommonConstants.DM_MATCH_CONTROL_CODE, "05", "1");
        //             status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, CommonConstants.DM_037, stepOrg, "故障关闭", "DMFM0001", currentUser, majorCode, lineCode, "30", content);
        //         }
        //     } else {
        //         content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已完工确认，请及时在EAM系统关闭工单！";
        //         status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + CommonConstants.FAULT_CONTENT_END, queryFaultWorkRecId(faultWorkNo), faultWorkNo, CommonConstants.DM_006, respDeptCode, "故障关闭", "DMFM0001", currentUser, majorCode, lineCode, "30", content);
        //     }
        // }
        // else if (ext2.equals(CommonConstants.DM_007)) {
        //  _close
        // }
        // else if (ext2.equals("DM_031")) {
        //     overTodoService.overTodo(faultOrderDO.getRecId(), CommonConstants.FAULT_TUNING_CONFIRM_CN);
        //     String faultProcessResult = dmfm02.getFaultProcessResult();
        //     content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已完工确认，请及时在EAM系统关闭工单！";
        //     if (CommonConstants.LINE_CODE_ONE.equals(faultProcessResult) || CommonConstants.LINE_CODE_TWO.equals(faultProcessResult)) {
        //         if (stationCode != null && !"".equals(stationCode.trim())) {
        //             Map<Object, Object> map = new HashMap<>();
        //             map.put("stationCode", stationCode);
        //             faultNo5 = this.dao.query("DMBM13.queryStation", map);
        //             for (Map<String, String> map1 : faultNo5) {
        //                 EiInfo out = DMUtil.insertTODO("【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, map1.get("userId"), "故障关闭", "DMFM0001", currentUser);
        //                 status = out.getStatus();
        //                 if (map1.get("mobile") != null && !"".equals(map1.get("mobile"))) {
        //                     // ISendMessage.messageCons(map1.get("mobile"), content);
        //                 }
        //                 throw new CommonException(ErrorCode.NORMAL_ERROR,"该人员无电话号码");
        //             }
        //         }
        //     }
        // }
        // else if (ext2.equals("DM_020") || ext2.equals("DM_044") || ext2.equals("DM_030")) {
        //     overTodoService.overTodo(faultOrderDO.getRecId(), CommonConstants.FAULT_TUNING_CONFIRM_CN);
        //     status = DMUtil.insertTODOWithUserGroup("【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, ext2, "故障关闭", "DMFM0001", currentUser);
        //     // EiInfo eiInfo = new EiInfo();
        //     // eiInfo.set("group", ext2);
        //     // eiInfo.set("content", content);
        //     // ISendMessage.sendMoblieMessageByGroup(eiInfo);
        // } else {
        overTodoService.insertTodo("【" + majorName + CommonConstants.FAULT_CONTENT_END, faultOrderDO.getRecId(), faultWorkNo, fillinUserId, "故障关闭", "?", currentUser, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
    }

    public void sendContractFault(FaultOrderDO dmfm02) {
        // Map<Object, Object> map = new HashMap<>();
        // map.put("faultNo", dmfm02.getFaultNo());
        // List<Map> listFm01 = this.dao.query("DMFM01.query", map, 0, -999999);
        //
        // if (listFm01 != null && listFm01.size() > 0) {
        //     FaultOrderDO dmfm01 = new FaultOrderDO();
        //     // dmfm01.fromMap(listFm01.get(0));
        //     Message message = new Message();
        //     message.setWorkNo(dmfm02.getFaultWorkNo());
        //     message.setWorkType("");
        //     message.setObjectName(dmfm01.getObjectName());
        //     message.setLineName(CodeFactory.getCodeService().getCodeCName("line", "01", "1"));
        //     message.setPosName(InterfaceHelper.getPositionHelpe().getPositionNameToCode(dmfm01.getPositionCode()));
        //     message.setEquipType1(InterfaceHelper.getIEquipKindIntf().getEquipmentMsg(dmfm01.getMajorCode()));
        //     message.setEquipType2(dmfm01.getSystemCode());
        //     message.setEquipType3(dmfm01.getEquipTypeCode());
        //     message.setFaultType(dmfm01.getFaultType());
        //     message.setFaultDetail(dmfm01.getFaultDetail());
        //     message.setDiscoverer(dmfm01.getDiscovererName());
        //     message.setDiscoveryTime(dmfm01.getDiscoveryTime());
        //     message.setFaultStatus(dmfm01.getFaultType());
        //     message.setDispatchUser(dmfm02.getDispatchUserName());
        //     message.setDispatchTime(dmfm02.getDispatchTime());
        //     message.setGroupCode(dmfm02.getWorkClass());
        //     message.setWorkClass("");
        // List<Map<String, String>> workerGroupList = InterfaceHelper.getUserHelpe().getWorkerGroupNameByWorkerGroupCode(dmfm02.getWorkClass());
        // if (workerGroupList != null && workerGroupList.size() > 0) {
        //     message.setWorkClass((String) ((Map) workerGroupList.get(0)).get("orgName"));
        // }
        // todo sendContractOrder(message);
    }

    @Override
    public void updateHandler(FaultNosFaultWorkNosReqDTO reqDTO) {
        if (null == reqDTO.getType() || StringUtils.isEmpty(reqDTO.getFaultNos())) {
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
        List<FaultDetailResDTO> list = faultQueryMapper.list(FaultQueryDetailReqDTO.builder().faultNos(reqDTO.getFaultNos()).build());
        if (StringUtils.isEmpty(list)) {
            log.error("未查询到数据，丢弃修改!");
            return;
        }
        Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_VEHICLE_SPECIALTY_CODE, "01");
        String itemEname = dictionaries.getItemEname();
        List<String> cos = Arrays.asList(itemEname.split(","));
        String currentUser = TokenUtils.getCurrentPersonId();
        String current = DateUtils.getCurrentTime();
        switch (reqDTO.getType()) {
            case YAN_SHOU:
                check(list, currentUser, current, reqDTO.getExamineStatus(), reqDTO.getExamineOpinion());
                break;
            case WAN_GONG_QUE_REN:
                finishWorkConfirm(list, cos, currentUser, current, reqDTO.getExamineStatus(), reqDTO.getExamineOpinion());
                break;
            case GUAN_BI:
                close(list);
                break;
            case ZUO_FEI:
                cancel(list, currentUser, current);
                break;
            default:
                break;
        }

    }

    @Override
    public List<FaultRepairDeptResDTO> querydept(String faultNo) {
        if (StringUtils.isEmpty(faultNo)) {
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
        //线路和专业查维修部门
        FaultInfoDO faultInfoDO = faultQueryMapper.queryOneFaultInfo(faultNo, null);
        return faultQueryMapper.queryDeptCode(faultInfoDO.getLineCode(), faultInfoDO.getMajorCode(), "20");
    }

    @Override
    public List<OrganMajorLineType> queryWorker(String workerGroupCode) {
        if (StringUtils.isEmpty(workerGroupCode)) {
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
//        return userGroupMemberService.getDepartmentUserByGroupName("DM_012", workerGroupCode);
        List<OrganMajorLineType> list = new ArrayList<>();
        List<MemberResDTO> memberList = organizationService.listMember(workerGroupCode);
        if (StringUtils.isNotEmpty(memberList)) {
            for (MemberResDTO member : memberList) {
                OrganMajorLineType res = new OrganMajorLineType();
                res.setLoginName(member.getId());
                res.setUserName(member.getName());
                list.add(res);
            }
        }
        return list;
    }


    private void cancel(List<FaultDetailResDTO> list, String currentUser, String current) {
        list.forEach(a -> {
            String faultNo = a.getFaultNo();
            String faultWorkNo = a.getFaultWorkNo();
            // 更新order表
            FaultOrderDO faultOrder = faultQueryMapper.queryOneFaultOrder(a.getFaultNo(), a.getFaultWorkNo());
            faultOrder.setFaultWorkNo(faultWorkNo);
            faultOrder.setFaultNo(faultNo);
            faultOrder.setOrderStatus(OrderStatus.ZUO_FEI.getCode());
            faultOrder.setRecRevisor(currentUser);
            faultOrder.setRecReviseTime(current);
            faultReportMapper.updateFaultOrder(faultOrder);
            // 更新info表
            FaultInfoDO faultInfo = faultQueryMapper.queryOneFaultInfo(faultNo, faultWorkNo);
            faultInfo.setRecRevisor(currentUser);
            faultInfo.setRecReviseTime(current);
            faultReportMapper.updateFaultInfo(faultInfo);
            // 取消代办
            overTodoService.cancelTodo(faultOrder.getRecId());
            // 添加流程记录
            addFaultFlow(a.getFaultNo(), a.getFaultWorkNo(), null);
        });
    }

    /**
     * 完工推送待办消息和短信消息
     * PS 根据原系统代码封装 这坨屎 谁来 都得说一句好吃
     * @param reqDTO 故障完工返回类
     */
    private void finishWorkSendMessage(FaultFinishWorkReqDTO reqDTO) {
        String faultNo = reqDTO.getFaultNo();
        String faultWorkNo = reqDTO.getFaultWorkNo();
        Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_VEHICLE_SPECIALTY_CODE, "01");
        List<String> cos = Arrays.asList(dictionaries.getItemEname().split(","));
        List<FaultOrderResDTO> listOrder = faultReportMapper.listOrderByNoAndWorkNo(faultNo, faultWorkNo);
        if (StringUtils.isNotEmpty(listOrder)) {
            FaultOrderResDTO order = listOrder.get(0);
            overTodoService.overTodo(order.getFaultWorkNo());
            // 根据处理结果类型进行消息推送 好吃的一坨屎 根本没用
            // switch (reqDTO.getFaultProcessResult()) {
            //     case "02":
            //         observeSend(faultWorkNo, reqDTO, cos, order);
            //         break;
            //     case "03":
            //         untreatedSend(faultWorkNo, reqDTO, cos, order);
            //         break;
            //     case "04":
            //         unProcessSend(faultWorkNo, reqDTO, cos, order);
            //         break;
            //     default:
            processedSend(faultWorkNo, reqDTO, cos, order);
            // break;
        }
    }

    /**
     * 处理结果（已处理）消息推送
     * @param faultWorkNo 工单编号
     * @param reqDTO      故障完工返回类
     * @param cos         cos
     * @param order       工单信息
     */
    public void processedSend(String faultWorkNo, FaultFinishWorkReqDTO reqDTO, List<String> cos, FaultOrderResDTO order) {
        // 基础参数
        String userId = TokenUtils.getCurrentPersonId();
        String userOfficeName = TokenUtils.getCurrentPerson().getOfficeName();
        String userName = TokenUtils.getCurrentPerson().getPersonName();
        String content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "已验收，请及时在EAM" + "系统完工确认！";
        // 中铁通发给中铁通专业工程师  中车发给中车专业工程师
        // 专业工程师(中铁通) DM_006 专业工程师（中车）DM_032
        List<String> users = getUsersByCompanyAndRole(reqDTO.getMajorCode(), "DM_006", "DM_032");
        overTodoService.insertTodoWithUserList(users, content, order.getRecId()
                , faultWorkNo, "故障完工确认", "DMFM0001", userId, TokenUtils.getCurrentPersonId(), BpmnFlowEnum.FAULT_REPORT_QUERY.value());
    }

    /**
     * 根据中车or中铁通 以及角色返回用户列表
     */
    public List<String> getUsersByCompanyAndRole(String majorCode, String zttRole, String zcRole) {
        List<String> userIds = Lists.newArrayList();
        if (!CommonConstants.ZC_LIST.contains(majorCode)) {
            if (StringUtils.isNotEmpty(zttRole)) {
                List<BpmnExaminePersonRes> userList = roleMapper.getUserBySubjectAndLineAndRole(null, null, zttRole);
                userIds = userList.stream().map(BpmnExaminePersonRes::getUserId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            }
        } else {
            if (StringUtils.isNotEmpty(zcRole)) {
                List<BpmnExaminePersonRes> userList = roleMapper.getUserBySubjectAndLineAndRole(null, null, zcRole);
                userIds = userList.stream().map(BpmnExaminePersonRes::getUserId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            }
        }
        return userIds;
    }

    /**
     * 处理结果（跟踪观察）消息推送
     * @param faultWorkNo 工单编号
     * @param reqDTO      故障完工返回类
     * @param cos         cos
     * @param order       工单信息
     */
    public void observeSend(String faultWorkNo, FaultFinishWorkReqDTO reqDTO, List<String> cos, FaultOrderResDTO order) {
        // 基础参数
        String content;
        String userId = TokenUtils.getCurrentPersonId();
        String userOfficeName = TokenUtils.getCurrentPerson().getOfficeName();
        String userName = TokenUtils.getCurrentPerson().getPersonName();
        String workClass = order.getWorkClass();
        String bussId = order.getRecId() + "_" + faultWorkNo;

        content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "已提报完工，请及时在EAM系统验收！";
        String content4 = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "已提报完工，但需要故障跟踪，请及时在EAM系统跟踪观察！";
        if (cos.contains(reqDTO.getMajorCode())) {
            String content37;
            String zcStepOrg = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "05").getItemEname();
            switch (reqDTO.getMajorCode()) {
                case "07":
                    overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, bussId,
                            faultWorkNo, CommonConstants.DM_006, zcStepOrg, "故障跟踪", "DMFM0010",
                            userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "10", content4, BpmnFlowEnum.FAULT_TRACK.value());
                    overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(),
                            faultWorkNo, "DM_009", zcStepOrg, "故障验收", "DMFM0001",
                            userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                case "06":
                    overTodoService.insertTodoWithUserGroup("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, bussId, faultWorkNo,
                            CommonConstants.DM_037, "故障跟踪", "DMFM0010", userId, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    // todo 发送短信
//                    content37 = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "已提报完工，但需要故障跟踪，请及时在EAM系统完工确认并跟踪观察！";
//                    messageInfo = new EiInfo();
//                    messageInfo.set("group", CommonConstants.DM_037);
//                    messageInfo.set("content", content37);
//                    ISendMessage.sendMoblieMessageByGroup(messageInfo);
                    overTodoService.insertTodoWithUserGroup("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, bussId, faultWorkNo,
                            CommonConstants.DM_037, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001", userId, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                default:
                    break;
            }
        } else if (CommonConstants.EQUIP_CATE_ENGINEER_CAR_CODE.equals(reqDTO.getMajorCode())) {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "07");
            String zttStepOrg = dictionaries.getItemEname();
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, bussId, faultWorkNo,
                    CommonConstants.DM_045, zttStepOrg, "故障跟踪", "DMFM0010", userId,
                    reqDTO.getMajorCode(), reqDTO.getLineCode(), "10", content4, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(), faultWorkNo,
                    CommonConstants.DM_045, zttStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001", userId,
                    reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        } else if (StringUtils.isNotEmpty(reqDTO.getIsToSubmit()) && CommonConstants.ONE_STRING.equals(reqDTO.getIsToSubmit())) {
            if (StringUtils.isNotEmpty(reqDTO.getUserIds())) {
                overTodoService.insertTodoWithUserList(reqDTO.getUserIds(), "【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END,
                        queryFaultWorkRecId(faultWorkNo), faultWorkNo, "故障验收", "DMFM0001", userId, content, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                overTodoService.insertTodoWithUserList(reqDTO.getUserIds(), "【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END,
                        queryFaultWorkRecId(faultWorkNo), faultWorkNo, "故障跟踪", "DMFM0001", userId, content4, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
            } else {
                overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, queryFaultWorkRecId(faultWorkNo),
                        faultWorkNo, CommonConstants.DM_006, workClass, "故障验收", "DMFM0001",
                        userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content, BpmnFlowEnum.FAULT_TRACK.value());
                overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, bussId,
                        faultWorkNo, CommonConstants.DM_006, order.getWorkClass(), "故障跟踪", "DMFM0010",
                        userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "10", content4, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
            }
        } else {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "03");
            String zcStepOrg = dictionaries.getItemEname();
            String content3 = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "已验收，请及时在EAM系统完工确认！";
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END,
                    queryFaultWorkRecId(faultWorkNo), faultWorkNo, CommonConstants.DM_007, zcStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001",
                    userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "30", content3, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        }
    }

    /**
     * 处理结果（未处理）消息推送
     * @param faultWorkNo 工单编号
     * @param reqDTO      故障完工返回类
     * @param cos         cos
     * @param order       工单信息
     */
    public void untreatedSend(String faultWorkNo, FaultFinishWorkReqDTO reqDTO, List<String> cos, FaultOrderResDTO order) {
        // 基础参数
        String content;
        String userId = TokenUtils.getCurrentPersonId();
        String userOfficeName = TokenUtils.getCurrentPerson().getOfficeName();
        String userName = TokenUtils.getCurrentPerson().getPersonName();

        content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "未处理，请在EAM" + "系统完工确认，并对故障及时处理！";
        if (cos.contains(reqDTO.getMajorCode())) {
            String zcStepOrg;
            String content37;
            switch (reqDTO.getMajorCode()) {
                case "07":
                    Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "04");
                    zcStepOrg = dictionaries.getItemEname();
                    overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(),
                            faultWorkNo, CommonConstants.DM_007, zcStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_AND_DISPATCH_CN, "DMFM0001",
                            userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "10", content, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                case "06":
                    content37 = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "已提报完工，请及时在EAM系统完工确认并对故障再派工！";
                    // todo 发送短信
//                    messageInfo = new EiInfo();
//                    messageInfo.set("group", CommonConstants.DM_037);
//                    messageInfo.set("content", content37);
//                    ISendMessage.sendMoblieMessageByGroup(messageInfo);
                    overTodoService.insertTodoWithUserGroup("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(), faultWorkNo,
                            CommonConstants.DM_037, CommonConstants.FAULT_FINISHED_CONFIRM_AND_DISPATCH_CN, "DMFM0001", userId, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                default:
                    break;
            }
        } else if (CommonConstants.EQUIP_CATE_ENGINEER_CAR_CODE.equals(reqDTO.getMajorCode())) {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "07");
            String zttStepOrg = dictionaries.getItemEname();
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(),
                    faultWorkNo, CommonConstants.DM_045, zttStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_AND_DISPATCH_CN, "DMFM0001",
                    userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        } else {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "03");
            String zcStepOrg = dictionaries.getItemEname();
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(),
                    faultWorkNo, CommonConstants.DM_007, zcStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_AND_DISPATCH_CN, "DMFM0001",
                    userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "10", content, BpmnFlowEnum.FAULT_REPORT_QUERY.value());

        }
    }

    /**
     * 处理结果（无法处理）消息推送
     * @param faultWorkNo 工单编号
     * @param reqDTO      故障完工返回类
     * @param cos         cos
     * @param order       工单信息
     */
    public void unProcessSend(String faultWorkNo, FaultFinishWorkReqDTO reqDTO, List<String> cos, FaultOrderResDTO order) {
        // 基础参数
        String content;
        String userId = TokenUtils.getCurrentPersonId();
        String userOfficeName = TokenUtils.getCurrentPerson().getOfficeName();
        String userName = TokenUtils.getCurrentPerson().getPersonName();

        String groupName = CommonConstants.DM_006;
        String orgCode = null;
        if (cos.contains(reqDTO.getMajorCode())) {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "05");
            orgCode = dictionaries.getItemEname();
        } else {
            orgCode = order.getWorkClass();
        }
        String content1 = "工班人员报告故障无法处理，工单号：" + faultWorkNo + "，请关注。";
        // todo 发送短信
//        conditionInfo1.set("group", groupName);
//        conditionInfo1.set("content", content1);
//        conditionInfo1.set("orgCode", orgCode);
//        ISendMessage.senMessageByGroupAndOrgCode(conditionInfo1);
        content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "无法处理，请在EAM" + "系统完工确认，并对故障及时处理！";
        if (cos.contains(reqDTO.getMajorCode())) {
            String stepOrg;
            switch (reqDTO.getMajorCode()) {
                case "07":
                    Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "01");
                    stepOrg = dictionaries.getItemEname();
                    overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(),
                            faultWorkNo, CommonConstants.DM_007, stepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001",
                            userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "30", content, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                case "06":
                    // todo 发送短信
//                    messageInfo = new EiInfo();
//                    messageInfo.set("group", CommonConstants.DM_037);
//                    messageInfo.set("content", content);
//                    ISendMessage.sendMoblieMessageByGroup(messageInfo);
                    overTodoService.insertTodoWithUserGroup("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(), faultWorkNo,
                            CommonConstants.DM_037, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001", userId, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                default:
                    break;
            }
        } else if (CommonConstants.EQUIP_CATE_ENGINEER_CAR_CODE.equals(reqDTO.getMajorCode())) {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "07");
            String zttStepOrg = dictionaries.getItemEname();
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(),
                    faultWorkNo, CommonConstants.DM_045, zttStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001",
                    userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        } else {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "03");
            String zcStepOrg = dictionaries.getItemEname();
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, queryFaultWorkRecId(faultWorkNo),
                    faultWorkNo, CommonConstants.DM_007, zcStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001",
                    userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "30", content, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        }
    }

    private void finishWorkConfirm(List<FaultDetailResDTO> list, List<String> cos, String currentUser, String current, String examineStatus, String examineOpinion) {
        list.forEach(a -> {
            String faultWorkNo = a.getFaultWorkNo();
            FaultInfoDO faultInfo = faultQueryMapper.queryOneFaultInfo(a.getFaultNo(), a.getFaultWorkNo());
            FaultOrderDO faultOrder = faultQueryMapper.queryOneFaultOrder(null, faultWorkNo);
            faultOrder.setConfirmUserId(currentUser);
            faultOrder.setConfirmTime(current);
            if (CommonConstants.ZERO_STRING.equals(examineStatus)) {
                faultOrder.setOrderStatus(OrderStatus.WAN_GONG_QUE_REN.getCode());
            } else {
                faultOrder.setOrderStatus(OrderStatus.PAI_GONG.getCode());
            }
            faultReportMapper.updateFaultOrder(faultOrder);
            // 完工确认消息发送
            if (CommonConstants.ZERO_STRING.equals(examineStatus)) {
                finishWorkConfirmPassSendMessage(faultWorkNo, faultOrder, faultInfo);
            } else {
                finishWorkConfirmRejectSendMessage(faultOrder);
            }
            // 添加流程记录
            addFaultFlow(a.getFaultNo(), a.getFaultWorkNo(), examineOpinion);
        });
    }

    /**
     * 完工确认通过消息发送
     * @param faultWorkNo 工单编号
     * @param faultOrder  工单信息
     * @param faultInfo   故障信息
     */
    private void finishWorkConfirmPassSendMessage(String faultWorkNo, FaultOrderDO faultOrder, FaultInfoDO faultInfo) {
        String content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + "已完工确认，请及时在EAM系统关闭工单！";
        overTodoService.overTodo(faultOrder.getFaultWorkNo());
        // 谁提报的谁关闭
        overTodoService.insertTodo(content, faultOrder.getRecId(), faultWorkNo, faultInfo.getFillinUserId(),
                CommonConstants.FAULT_FINISHED_CONFIRM_CN, "?", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.FAULT_REPORT_QUERY.value());
    }

    /**
     * 完工确认驳回消息发送
     * @param faultOrder 工单信息
     */
    private void finishWorkConfirmRejectSendMessage(FaultOrderDO faultOrder) {
        overTodoService.overTodo(faultOrder.getFaultWorkNo());
        sendDispatchTodoMessage(faultOrder, null, faultOrder.getReportFinishUserId());
    }

    private void close(List<FaultDetailResDTO> list) {
        list.forEach(a -> {
            FaultOrderDO faultOrder = faultQueryMapper.queryOneFaultOrder(a.getFaultNo(), null);
            faultOrder.setOrderStatus(OrderStatus.GUAN_BI.getCode());
            faultOrder.setCloseTime(DateUtils.getCurrentTime());
            faultReportMapper.updateFaultOrder(faultOrder);
            FaultInfoDO faultInfo = faultQueryMapper.queryOneFaultInfo(a.getFaultNo(), a.getFaultWorkNo());
            faultInfo.setFaultNo(a.getFaultNo());
            faultInfo.setRecReviseTime(DateUtils.getCurrentTime());
            faultInfo.setRecRevisor(TokenUtils.getCurrentPersonId());
            faultReportMapper.updateFaultInfo(faultInfo);
            overTodoService.overTodo(faultOrder.getFaultWorkNo());
            // 添加流程记录
            addFaultFlow(a.getFaultNo(), a.getFaultWorkNo(), null);
        });
    }

    private void check(List<FaultDetailResDTO> list, String currentUser, String current, String examineStatus, String examineOpinion) {
        // 判断是否存在验收状态的数据
        Set<String> orderStatus = StreamUtils.mapToSet(list, FaultDetailResDTO::getOrderStatus);
        Assert.isFalse(orderStatus.contains(OrderStatus.YAN_SHOU.getCode()), "当前勾选的数据中存在验收状态的数据，无法进行重复操作!");
        String currentPersonId = TokenUtils.getCurrentPersonId();
        list.forEach(a -> {
            String faultNo = a.getFaultNo();
            String faultWorkNo = a.getFaultWorkNo();
            FaultOrderDO faultOrder = faultQueryMapper.queryOneFaultOrder(faultNo, faultWorkNo);
            // 根据状态变更故障数据状态
            String recId = faultOrder.getRecId();
            if (CommonConstants.ZERO_STRING.equals(examineStatus)) {
                faultOrder.setOrderStatus(OrderStatus.YAN_SHOU.getCode());
            } else {
                faultOrder.setOrderStatus(OrderStatus.PAI_GONG.getCode());
            }
            faultOrder.setCheckUserId(currentUser);
            faultOrder.setCheckTime(current);
            faultOrder.setRecReviseTime(current);
            faultOrder.setRecRevisor(currentUser);
            faultReportMapper.updateFaultOrder(faultOrder);
            FaultInfoDO faultInfo = faultQueryMapper.queryOneFaultInfo(faultNo, faultWorkNo);
            faultInfo.setFaultNo(faultNo);
            faultInfo.setRecReviseTime(current);
            faultInfo.setRecRevisor(currentUser);
            faultReportMapper.updateFaultInfo(faultInfo);
            // 完成待办
            overTodoService.overTodo(recId, "故障验收");
            if (CommonConstants.ZERO_STRING.equals(examineStatus)) {
                String majorCode = faultInfo.getMajorCode();
                String content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + "已验收，请及时在EAM系统完工确认！";
                // 中铁通的发给中铁通生产调度 DM_007
                // 行车设备类 且不是车辆故障
                if ("10".equals(faultInfo.getFaultType()) && !CommonConstants.ZC_LIST.contains(majorCode)) {
                    List<String> users = getUsersByCompanyAndRole(majorCode, "DM_007", "ZCJD");
                    overTodoService.insertTodoWithUserList(users, content, recId, faultWorkNo, CommonConstants.FAULT_FINISHED_CONFIRM_CN, currentPersonId, "?", null, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                } else {
                    //其他的发给工班
                    overTodoService.insertTodoWithUserOrgan(String.format(CommonConstants.TODO_GD_TPL, faultWorkNo, "故障"), recId, faultWorkNo, faultInfo.getRepairDeptCode(), "故障工单完工验收", "?", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                }
            } else {
                sendDispatchTodoMessage(faultOrder, null, faultOrder.getReportFinishUserId());
            }
            // 添加流程记录
            addFaultFlow(faultNo, faultWorkNo, examineOpinion);
        });
    }

    /**
     * 根据工单编号获取recId
     * @param faultWorkNo 工单编号
     * @return recId
     */
    public String queryFaultWorkRecId(String faultWorkNo) {
        List<FaultOrderResDTO> list = faultReportMapper.listOrderByNoAndWorkNo(null, faultWorkNo);
        if (StringUtils.isNotEmpty(list)) {
            return list.get(0).getRecId();
        }
        return "";
    }

    /**
     * 新增工单流程
     * @param faultNo     故障编号
     * @param faultWorkNo 故障工单编号
     * @param examineOpinion      审核意见
     */
    public void addFaultFlow(String faultNo, String faultWorkNo, String examineOpinion) {
        FaultFlowReqDTO faultFlow = new FaultFlowReqDTO();
        faultFlow.setRecId(TokenUtils.getUuId());
        faultFlow.setFaultNo(faultNo);
        faultFlow.setFaultWorkNo(faultWorkNo);
        faultFlow.setOperateUserId(TokenUtils.getCurrentPersonId());
        faultFlow.setOperateUserName(TokenUtils.getCurrentPerson().getPersonName());
        faultFlow.setOperateTime(DateUtils.getCurrentTime());
        FaultOrderDO faultOrderDO = faultQueryMapper.queryOneFaultOrder(faultNo, faultWorkNo);
        if (StringUtils.isNotNull(faultOrderDO)) {
            faultFlow.setOrderStatus(faultOrderDO.getOrderStatus());
        }
        if (StringUtils.isNotEmpty(examineOpinion)) {
            faultFlow.setRemark(examineOpinion);
        }
        faultReportMapper.addFaultFlow(faultFlow);
    }

    @Override
    public String pageMaterial(String orderCode) {
        return dictionariesMapper.queryOneByItemCodeAndCodesetCode("DM_ER_ADDRESS", "11").getItemCname() + orderCode;
    }
}
