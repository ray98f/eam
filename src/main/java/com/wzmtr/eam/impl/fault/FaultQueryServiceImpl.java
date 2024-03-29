package com.wzmtr.eam.impl.fault;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.common.collect.Lists;
import com.wzmtr.eam.bizobject.PartBO;
import com.wzmtr.eam.bizobject.StationBO;
import com.wzmtr.eam.bizobject.export.FaultExportBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.common.MemberResDTO;
import com.wzmtr.eam.dto.res.common.PersonResDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultOrderResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.enums.*;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.PartMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.StationMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.fault.FaultAnalyzeMapper;
import com.wzmtr.eam.mapper.fault.FaultInfoMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.common.OrganizationService;
import com.wzmtr.eam.service.common.UserGroupMemberService;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.service.fault.FaultReportService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public static final String Y = "Y";
    public static final String DM_006 = CommonConstants.DM_006;
    public static final String DM_007 = CommonConstants.DM_007;
    public static final String DM_013 = "DM_013";
    public static final String DM_020 = "DM_020";
    public static final String DM_030 = "DM_030";
    public static final String DM_031 = "DM_031";
    public static final String DM_044 = "DM_044";
    @Autowired
    private FaultQueryMapper faultQueryMapper;
    @Autowired
    private FaultReportMapper faultReportMapper;
    @Autowired
    private FaultReportService faultReportService;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private OverTodoService overTodoService;
    @Autowired
    private DictionariesMapper dictionariesMapper;
    @Autowired
    private FaultAnalyzeMapper faultAnalyzeMapper;
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private PartMapper partMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FaultInfoMapper faultInfoMapper;
    @Autowired
    private UserGroupMemberService userGroupMemberService;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public Page<FaultDetailResDTO> list(FaultQueryReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<FaultDetailResDTO> page = faultQueryMapper.query(reqDTO.of(), reqDTO);
        List<FaultDetailResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (FaultDetailResDTO res : list) {
                buildRes(res);
            }
        }
        page.setRecords(list);
        return page;
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
        // faultWorkNo
        List<String> status = faultQueryMapper.queryOrderStatus(reqDTO);
        return StringUtils.isEmpty(status) ? null : status.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issue(FaultDetailReqDTO reqDTO) {
        String status = queryOrderStatus(SidEntity.builder().id(reqDTO.getFaultWorkNo()).build());
        FaultOrderDO faultOrderDO = BeanUtils.convert(reqDTO, FaultOrderDO.class);
        switch (status) {
            case "40":
                faultOrderDO.setReportStartUserId(TokenUtils.getCurrentPersonId());
                faultOrderDO.setReportStartTime(DateUtils.getCurrentTime());
                break;
            // 完工
            case "50":
                faultOrderDO.setReportFinishUserId(TokenUtils.getCurrentPersonId());
                faultOrderDO.setReportFinishTime(DateUtils.getCurrentTime());
                break;
            // 完工确认
            case "60":
                faultOrderDO.setConfirmUserId(TokenUtils.getCurrentPersonId());
                faultOrderDO.setConfirmTime(DateUtils.getCurrentTime());
                break;
            // 验收
            case "55":
                faultOrderDO.setCheckUserId(TokenUtils.getCurrentPersonId());
                faultOrderDO.setCheckTime(DateUtils.getCurrentTime());
                break;
            default:
                break;
        }
        faultOrderDO.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultOrderDO.setRecReviseTime(DateUtils.getCurrentTime());
        faultOrderDO.setOrderStatus(OrderStatus.XIA_FA.getCode());

        //TODO 只更新下发状态?
        faultReportMapper.updateFaultOrder(faultOrderDO);

        FaultInfoDO faultInfoDO = BeanUtils.convert(reqDTO, FaultInfoDO.class);
        faultInfoDO.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultInfoDO.setRecReviseTime(DateUtils.getCurrentTime());
        faultReportMapper.updateFaultInfo(faultInfoDO);
        // ServiceDMFM0001 update
        FaultInfoDO faultInfo1 = faultQueryMapper.queryOneFaultInfo(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        List<FaultOrderResDTO> faultOrder = faultReportMapper.listOrderByNoAndWorkNo(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        if (StringUtils.isNotEmpty(faultOrder)) {
            BeanUtils.copy(faultOrder.get(0), faultOrderDO);
        }

        //TODO 工班长接收待办信息
        overTodoService.overTodo(faultOrderDO.getRecId(), "提报成功，准备下发");
        String content = "【市铁投集团】" + TokenUtils.getCurrentPerson().getOfficeName() + "的" + TokenUtils.getCurrentPerson().getPersonName() +
                "下发一条" + faultInfo1.getMajorName() + "故障，工单号：" + reqDTO.getFaultWorkNo() + "，尽快派工。";
        Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_VEHICLE_SPECIALTY_CODE, "01");
        String codeName = dictionaries.getItemEname();
        List<String> cos = Arrays.asList(codeName.split(","));
        if (cos.contains(faultInfo1.getMajorCode())) {
            dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "04");
        } else {
            dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "03");
        }
        String zcStepOrg = dictionaries.getItemEname();
        overTodoService.insertTodoWithUserGroupAndAllOrg("【" + faultInfo1.getMajorName() + CommonConstants.FAULT_CONTENT_END, faultOrderDO.getRecId(),
                reqDTO.getFaultWorkNo(), CommonConstants.DM_007, zcStepOrg, "故障派工", "DMFM0001", TokenUtils.getCurrentPersonId(),
                faultInfo1.getMajorCode(), faultInfo1.getLineCode(), "20", content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        // 添加流程记录
        addFaultFlow(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
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
        FaultExportBO exportBO = BeanUtils.convert(resDTO, FaultExportBO.class);
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
        exportBO.setDealerUnit(dealerUnit != null ? dealerUnit.getDesc() : resDTO.getDealerUnit());
        if (StringUtils.isNotEmpty(fillinDept)) {
            exportBO.setFillinDept(Optional.ofNullable(fillinDept).orElse(CommonConstants.EMPTY));
            exportBO.setRepairDept(Optional.ofNullable(repairDept).orElse(CommonConstants.EMPTY));
        }
        if (!Objects.isNull(position2)) {
            exportBO.setPosition2Name(Optional.ofNullable(position2.getItemCname()).orElse(CommonConstants.EMPTY));
        }
        exportBO.setOrderStatus(orderStatus != null ? orderStatus.getDesc() : resDTO.getOrderStatus());
        exportBO.setFaultType(faultType == null ? resDTO.getFaultType() : faultType.getDesc());
        exportBO.setFaultLevel(faultLevel == null ? resDTO.getFaultLevel() : faultLevel.getDesc());
        exportBO.setFaultAffect(faultAffect != null ? faultAffect.getDesc() : resDTO.getFaultAffect());
        exportBO.setLineName(lineCode != null ? lineCode.getDesc() : resDTO.getLineCode());
        PartBO partBO = partMapper.queryPartByFaultWorkNo(resDTO.getFaultWorkNo());
        if (null != partBO) {
            exportBO.setReplacementName(partBO.getReplacementName());
            exportBO.setOldRepNo(partBO.getOldRepNo());
            exportBO.setNewRepNo(partBO.getNewRepNo());
            exportBO.setOperateCostTime(partBO.getOperateCostTime());
        }
        return exportBO;
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
        if (req.getFaultNos().size() == 1 || StringUtils.isEmpty(req.getFaultNos())) {
            return true;
        }
        Set<String> faultNos = req.getFaultNos();
        List<FaultInfoDO> list = faultInfoMapper.selectList(new QueryWrapper<FaultInfoDO>().in("FAULT_NO", faultNos));
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
            FaultOrderDO faultOrder1 = faultQueryMapper.queryOneFaultOrder(null, a);
            if (faultOrder1 != null) {
                if (StringUtils.isNotEmpty(reqDTO.getFaultLevel())) {
                    faultOrder1.setExt1(reqDTO.getFaultLevel());
                }
                faultOrder1.setFaultAffect(reqDTO.getFaultAffect());
                faultOrder1.setWorkArea(reqDTO.getWorkArea());
                faultOrder1.setPlanRecoveryTime(reqDTO.getPlanRecoveryTime());
                faultOrder1.setRepairRespUserId(reqDTO.getRepairRespUserId());
                faultOrder1.setDispatchUserId(TokenUtils.getCurrentPersonId());
                faultOrder1.setDispatchTime(DateUtils.getCurrentTime());
                faultOrder1.setRecRevisor(TokenUtils.getCurrentPersonId());
                faultOrder1.setRecReviseTime(DateUtils.getCurrentTime());
                faultOrder1.setOrderStatus(OrderStatus.PAI_GONG.getCode());
                faultReportMapper.updateFaultOrder(faultOrder1);
                FaultInfoDO faultInfoDO = faultQueryMapper.queryOneFaultInfo(faultOrder1.getFaultNo(), faultOrder1.getFaultWorkNo());
                faultInfoDO.setRepairDeptCode(workerGroupCode);
                if (StringUtils.isNotEmpty(reqDTO.getIsTiKai()) && IS_TIKAI_CODE.equals(reqDTO.getIsTiKai())) {
                    faultInfoDO.setExt3("08");
                }
                faultInfoDO.setRecRevisor(TokenUtils.getCurrentPersonId());
                faultInfoDO.setRecReviseTime(DateUtils.getCurrentTime());
                faultInfoDO.setFaultNo(faultOrder1.getFaultNo());
                faultReportMapper.updateFaultInfo(faultInfoDO);
                overTodoService.overTodo(faultOrder1.getRecId(), "故障维修");
                String content = "【市铁投集团】" + TokenUtils.getCurrentPerson().getOfficeName() + "的" + TokenUtils.getCurrentPerson().getPersonName() +
                        "向您指派了一条故障工单，故障位置：" + faultInfoDO.getPositionName() + "," + faultInfoDO.getPosition2Code() +
                        "，设备名称：" + faultInfoDO.getObjectName() + ",故障现象：" + faultInfoDO.getFaultDisplayDetail() +
                        "请及时处理并在EAM系统填写维修报告，工单号：" + faultOrder1.getFaultWorkNo() + "，请知晓。";
                //待办推送  根据故障分类来判断
                overTodoService.insertTodoWithUserGroupAndOrg("【" + reqDTO.getMajorCode() + CommonConstants.FAULT_CONTENT_END,
                        faultOrder1.getRecId(), faultOrder1.getFaultWorkNo(), "DM_013", workerGroupCode, "故障维修",
                        "DMFM0001", TokenUtils.getCurrentPersonId(), content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "01");
                String zcStepOrg = dictionaries.getItemEname();
                if (StringUtils.isNotEmpty(faultOrder1.getWorkClass()) && !faultOrder1.getWorkClass().contains(zcStepOrg)) {
                    // todo 调用施工调度接口
                    sendContractFault(faultOrder1);
                }
                // 添加流程记录
                addFaultFlow(faultOrder1.getFaultNo(), reqDTO.getFaultWorkNo());
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishWork(FaultFinishWorkReqDTO reqDTO) {
        // 完工 ServiceDMFM0002 finishWork
        if (StringUtils.isNotEmpty(reqDTO.getOrderStatus())) {
            FaultOrderDO orderUpdate = BeanUtils.convert(reqDTO, FaultOrderDO.class);
            orderUpdate.setReportFinishUserId(TokenUtils.getCurrentPersonId());
            orderUpdate.setReportFinishUserName(TokenUtils.getCurrentPerson().getPersonName());
            orderUpdate.setReportFinishTime(DateUtils.getCurrentTime());
            orderUpdate.setRecRevisor(TokenUtils.getCurrentPersonId());
            orderUpdate.setRecReviseTime(DateUtils.getCurrentTime());
            orderUpdate.setOrderStatus(OrderStatus.WAN_GONG.getCode());
            faultReportMapper.updateFaultOrder(orderUpdate);
            FaultInfoDO infoUpdate = BeanUtils.convert(reqDTO, FaultInfoDO.class);
            infoUpdate.setRecRevisor(TokenUtils.getCurrentPersonId());
            infoUpdate.setRecReviseTime(DateUtils.getCurrentTime());
            faultReportMapper.updateFaultInfo(infoUpdate);
            // 添加流程记录
            addFaultFlow(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        }
        finishWorkSendMessage(reqDTO);
        //完工更新其他人的待办状态
        updateTodoStatus(reqDTO.getFaultWorkNo());
    }

    private void updateTodoStatus(String faultWorkNo) {
        overTodoService.overTodo();
    }

    @Override
    public void eqCheck(FaultEqCheckReqDTO reqDTO) {
        String faultWorkNo = reqDTO.getFaultWorkNo();
        String currentUser = TokenUtils.getCurrentPersonId();
        String faultNo = reqDTO.getFaultNo();
        FaultInfoDO faultInfoDO = faultQueryMapper.queryOneFaultInfo(faultNo, faultWorkNo);
        String majorCode = faultInfoDO.getMajorCode();
        String majorName = faultInfoDO.getMajorName();
        String stationCode = faultInfoDO.getExt1();
        String ext2 = faultInfoDO.getExt2();
        String fillinUserId = faultInfoDO.getFillinUserId();
        String respDeptCode = faultInfoDO.getRespDeptCode();
        String lineCode = faultInfoDO.getLineCode();
        FaultOrderDO faultOrderDO = faultQueryMapper.queryOneFaultOrder(null, faultWorkNo);
        String workClass = faultOrderDO.getWorkClass();
        overTodoService.overTodo(faultOrderDO.getRecId(), CommonConstants.FAULT_TUNING_CONFIRM_CN);
        Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_VEHICLE_SPECIALTY_CODE, "01");
        String itemEname = dictionaries.getItemEname();
        String[] cos01 = itemEname.split(",");
        List<String> cos = Arrays.asList(cos01);
        if (cos.contains(majorCode)) {
            overTodoService.overTodo(faultOrderDO.getRecId(), CommonConstants.FAULT_TUNING_CONFIRM_CN);
        } else if (DM_013.equals(ext2)) {
            overTodoService.overTodo(faultOrderDO.getRecId(), CommonConstants.FAULT_TUNING_CONFIRM_CN);
            // String content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已设调确认，请及时在EAM系统关闭工单！";
            overTodoService.insertTodoWithUserGroupAndOrg("【" + majorName + CommonConstants.FAULT_CONTENT_END, faultOrderDO.getRecId(), faultWorkNo, "DM_013", workClass, "故障关闭", "DMFM0001", currentUser, null,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
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
        overTodoService.overTodo(faultOrderDO.getRecId(), CommonConstants.FAULT_TUNING_CONFIRM_CN);
        List<Map<Object, Object>> userList = new ArrayList();
        Map<Object, Object> userMap = new HashMap<>();
        userMap.put("userCode", fillinUserId);
        userList.add(userMap);
        // status = DMUtil.insertTODOWithUserList(userList, "【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, "故障关闭", "DMFM0001", currentUser);
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
        // String stepOrg = CodeFactory.getCodeService().getCodeEName(CommonConstants.DM_MATCH_CONTROL_CODE, "04", "1");
        switch (reqDTO.getType()) {
            case WAN_GONG_QUE_REN:
                finishWorkConfirm(list, cos, currentUser, current);
                break;
            case YAN_SHOU:
                check(list, cos, currentUser, current, itemEname);
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
            FaultOrderDO faultOrderDO = faultQueryMapper.queryOneFaultOrder(a.getFaultNo(), a.getFaultWorkNo());
            faultOrderDO.setFaultWorkNo(faultWorkNo);
            faultOrderDO.setFaultNo(faultNo);
            faultOrderDO.setOrderStatus(OrderStatus.ZUO_FEI.getCode());
            faultOrderDO.setRecRevisor(currentUser);
            faultOrderDO.setRecReviseTime(current);
            faultReportMapper.updateFaultOrder(faultOrderDO);
            // 更新info表
            FaultInfoDO faultInfoDO = faultQueryMapper.queryOneFaultInfo(faultNo, faultWorkNo);
            faultInfoDO.setRecRevisor(currentUser);
            faultInfoDO.setRecReviseTime(current);
            faultReportMapper.updateFaultInfo(faultInfoDO);
            // 取消代办
            overTodoService.cancelTodo(faultOrderDO.getRecId());
            // 添加流程记录
            addFaultFlow(a.getFaultNo(), a.getFaultWorkNo());
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
            overTodoService.overTodo(order.getRecId(), "故障维修");
            // 根据处理结果类型进行消息推送
            switch (reqDTO.getFaultProcessResult()) {
                case "02":
                    observeSend(faultWorkNo, reqDTO, cos, order);
                    break;
                case "03":
                    untreatedSend(faultWorkNo, reqDTO, cos, order);
                    break;
                case "04":
                    unProcessSend(faultWorkNo, reqDTO, cos, order);
                    break;
                default:
                    processedSend(faultWorkNo, reqDTO, cos, order);
                    break;
            }
        }
    }

    /**
     * 处理结果（已处理）消息推送
     * @param faultWorkNo 工单编号
     * @param reqDTO 故障完工返回类
     * @param cos cos
     * @param order 工单信息
     */
    public void processedSend(String faultWorkNo, FaultFinishWorkReqDTO reqDTO, List<String> cos, FaultOrderResDTO order) {
        // 基础参数
        String content;
        String userId = TokenUtils.getCurrentPersonId();
        String userOfficeName = TokenUtils.getCurrentPerson().getOfficeName();
        String userName = TokenUtils.getCurrentPerson().getPersonName();
        String workClass = order.getWorkClass();
        content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "已提报完工，请及时在EAM系统验收！";
        if (cos.contains(reqDTO.getMajorCode())) {
            switch (reqDTO.getMajorCode()) {
                case "07":
                    Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "05");
                    String stepOrg = dictionaries.getItemEname();
                    overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(),
                            faultWorkNo, "DM_009", stepOrg, "故障验收", "DMFM0001",
                            userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                case "06":
                    // todo 发送短信
//                    String content37 = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "已提报完工，请及时在EAM系统完工确认！";
//                    messageInfo = new EiInfo();
//                    messageInfo.set("group", CommonConstants.DM_037);
//                    messageInfo.set("content", content37);
//                    ISendMessage.sendMoblieMessageByGroup(messageInfo);
                    overTodoService.insertTodoWithUserGroup("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(), faultWorkNo,
                            CommonConstants.DM_037, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001", userId,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                default:
                    break;
            }
        } else if (CommonConstants.EQUIP_CATE_ENGINEER_CAR_CODE.equals(reqDTO.getMajorCode())) {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "07");
            String zttStepOrg = dictionaries.getItemEname();
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(),
                    faultWorkNo, CommonConstants.DM_045, zttStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001",
                    userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        } else if (StringUtils.isNotEmpty(reqDTO.getIsToSubmit()) && CommonConstants.ONE_STRING.equals(reqDTO.getIsToSubmit())) {
            if (StringUtils.isNotEmpty(reqDTO.getUserIds())) {
                overTodoService.insertTodoWithUserList(reqDTO.getUserIds(), "【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END,
                        queryFaultWorkRecId(faultWorkNo), faultWorkNo, "故障验收", "DMFM0001", userId, content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
            } else {
                overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, queryFaultWorkRecId(faultWorkNo),
                        faultWorkNo, CommonConstants.DM_006, workClass, "故障验收", "DMFM0001",
                        userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
            }
        } else {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "03");
            String zcStepOrg = dictionaries.getItemEname();
            String content3 = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "已验收，请及时在EAM" + "系统完工确认！";
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END,
                    queryFaultWorkRecId(faultWorkNo), faultWorkNo, CommonConstants.DM_007, zcStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001",
                    userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "30", content3,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        }
    }

    /**
     * 处理结果（跟踪观察）消息推送
     * @param faultWorkNo 工单编号
     * @param reqDTO 故障完工返回类
     * @param cos cos
     * @param order 工单信息
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
                            userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "10", content4,BpmnFlowEnum.FAULT_TRACK.value());
                    overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(),
                            faultWorkNo, "DM_009", zcStepOrg, "故障验收", "DMFM0001",
                            userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content,BpmnFlowEnum.FAULT_TRACK.value());
                    break;
                case "06":
                    overTodoService.insertTodoWithUserGroup("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, bussId, faultWorkNo,
                            CommonConstants.DM_037, "故障跟踪", "DMFM0010", userId,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    // todo 发送短信
//                    content37 = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "已提报完工，但需要故障跟踪，请及时在EAM系统完工确认并跟踪观察！";
//                    messageInfo = new EiInfo();
//                    messageInfo.set("group", CommonConstants.DM_037);
//                    messageInfo.set("content", content37);
//                    ISendMessage.sendMoblieMessageByGroup(messageInfo);
                    overTodoService.insertTodoWithUserGroup("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, bussId, faultWorkNo,
                            CommonConstants.DM_037, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001", userId,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                default:
                    break;
            }
        } else if (CommonConstants.EQUIP_CATE_ENGINEER_CAR_CODE.equals(reqDTO.getMajorCode())) {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "07");
            String zttStepOrg = dictionaries.getItemEname();
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, bussId, faultWorkNo,
                    CommonConstants.DM_045, zttStepOrg, "故障跟踪", "DMFM0010", userId,
                    reqDTO.getMajorCode(), reqDTO.getLineCode(), "10", content4,BpmnFlowEnum.FAULT_TRACK.value());
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(), faultWorkNo,
                    CommonConstants.DM_045, zttStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001", userId,
                    reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content,BpmnFlowEnum.FAULT_TRACK.value());
        } else if (StringUtils.isNotEmpty(reqDTO.getIsToSubmit()) && CommonConstants.ONE_STRING.equals(reqDTO.getIsToSubmit())) {
            if (StringUtils.isNotEmpty(reqDTO.getUserIds())) {
                overTodoService.insertTodoWithUserList(reqDTO.getUserIds(), "【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END,
                        queryFaultWorkRecId(faultWorkNo), faultWorkNo, "故障验收", "DMFM0001", userId, content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                overTodoService.insertTodoWithUserList(reqDTO.getUserIds(), "【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END,
                        queryFaultWorkRecId(faultWorkNo), faultWorkNo, "故障跟踪", "DMFM0001", userId, content4,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
            } else {
                overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, queryFaultWorkRecId(faultWorkNo),
                        faultWorkNo, CommonConstants.DM_006, workClass, "故障验收", "DMFM0001",
                        userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content,BpmnFlowEnum.FAULT_TRACK.value());
                overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, bussId,
                        faultWorkNo, CommonConstants.DM_006, order.getWorkClass(), "故障跟踪", "DMFM0010",
                        userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "10", content4,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
            }
        } else {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "03");
            String zcStepOrg = dictionaries.getItemEname();
            String content3 = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "已验收，请及时在EAM系统完工确认！";
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END,
                    queryFaultWorkRecId(faultWorkNo), faultWorkNo, CommonConstants.DM_007, zcStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001",
                    userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "30", content3,BpmnFlowEnum.FAULT_TRACK.value());
        }
    }

    /**
     * 处理结果（未处理）消息推送
     * @param faultWorkNo 工单编号
     * @param reqDTO 故障完工返回类
     * @param cos cos
     * @param order 工单信息
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
                            userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "10", content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                case "06":
                    content37 = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userOfficeName + "的" + userName + "已提报完工，请及时在EAM系统完工确认并对故障再派工！";
                    // todo 发送短信
//                    messageInfo = new EiInfo();
//                    messageInfo.set("group", CommonConstants.DM_037);
//                    messageInfo.set("content", content37);
//                    ISendMessage.sendMoblieMessageByGroup(messageInfo);
                    overTodoService.insertTodoWithUserGroup("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(), faultWorkNo,
                            CommonConstants.DM_037, CommonConstants.FAULT_FINISHED_CONFIRM_AND_DISPATCH_CN, "DMFM0001", userId,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                default:
                    break;
            }
        } else if (CommonConstants.EQUIP_CATE_ENGINEER_CAR_CODE.equals(reqDTO.getMajorCode())) {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "07");
            String zttStepOrg = dictionaries.getItemEname();
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(),
                    faultWorkNo, CommonConstants.DM_045, zttStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_AND_DISPATCH_CN, "DMFM0001",
                    userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        } else {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "03");
            String zcStepOrg = dictionaries.getItemEname();
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(),
                    faultWorkNo, CommonConstants.DM_007, zcStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_AND_DISPATCH_CN, "DMFM0001",
                    userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "10", content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());

        }
    }

    /**
     * 处理结果（无法处理）消息推送
     * @param faultWorkNo 工单编号
     * @param reqDTO 故障完工返回类
     * @param cos cos
     * @param order 工单信息
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
                            userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "30", content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                case "06":
                    // todo 发送短信
//                    messageInfo = new EiInfo();
//                    messageInfo.set("group", CommonConstants.DM_037);
//                    messageInfo.set("content", content);
//                    ISendMessage.sendMoblieMessageByGroup(messageInfo);
                    overTodoService.insertTodoWithUserGroup("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(), faultWorkNo,
                            CommonConstants.DM_037, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001", userId,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    break;
                default:
                    break;
            }
        } else if (CommonConstants.EQUIP_CATE_ENGINEER_CAR_CODE.equals(reqDTO.getMajorCode())) {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "07");
            String zttStepOrg = dictionaries.getItemEname();
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, order.getRecId(),
                    faultWorkNo, CommonConstants.DM_045, zttStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001",
                    userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "20", content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        } else {
            Dictionaries dictionaries = dictionariesMapper.queryOneByItemCodeAndCodesetCode(CommonConstants.DM_MATCH_CONTROL_CODE, "03");
            String zcStepOrg = dictionaries.getItemEname();
            overTodoService.insertTodoWithUserGroupAndAllOrg("【" + reqDTO.getMajorName() + CommonConstants.FAULT_CONTENT_END, queryFaultWorkRecId(faultWorkNo),
                    faultWorkNo, CommonConstants.DM_007, zcStepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001",
                    userId, reqDTO.getMajorCode(), reqDTO.getLineCode(), "30", content,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        }
    }

    private void finishWorkConfirm(List<FaultDetailResDTO> list, List<String> cos, String currentUser, String current) {
        list.forEach(a -> {
            String faultWorkNo = a.getFaultWorkNo();
            FaultInfoDO faultInfo = faultQueryMapper.queryOneFaultInfo(a.getFaultNo(), a.getFaultWorkNo());
            FaultOrderDO dmfm02 = faultQueryMapper.queryOneFaultOrder(null, faultWorkNo);
            dmfm02.setConfirmUserId(currentUser);
            dmfm02.setConfirmTime(current);
            dmfm02.setOrderStatus(OrderStatus.WAN_GONG_QUE_REN.getCode());
            faultReportMapper.updateFaultOrder(dmfm02);
            // 完工确认消息发送
            finishWorkConfirmSendMessage(faultWorkNo, cos, currentUser, current, dmfm02, faultInfo);
            // 添加流程记录
            addFaultFlow(a.getFaultNo(), a.getFaultWorkNo());
        });
    }

    /**
     * 完工确认消息发送
     * @param faultWorkNo 工单编号
     * @param cos cos
     * @param currentUser 当前人
     * @param current 当前
     * @param dmfm02 工单信息
     * @param faultInfo 故障信息
     */
    private void finishWorkConfirmSendMessage(String faultWorkNo, List<String> cos, String currentUser, String current, FaultOrderDO dmfm02, FaultInfoDO faultInfo) {
        String stationCode = dmfm02.getExt1();
        String majorName = faultInfo.getMajorName();
        String majorCode = faultInfo.getMajorCode();
        String ext2 = dmfm02.getExt2();
        // String content2 = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已完工确认，请知晓！";
        // todo 发短信
        // List<Map> list = this.dao.query("DMDM59.queryMByUserGroup", faultInfo.getFillinUserId());
        // if (StringUtils.isNotEmpty(list) && isToSend.equals("ToSend") && !list.contains("DM_021")) {
        //     EiInfo eiInfo = new EiInfo();
        //     eiInfo.set("group", "DM_021");
        //     eiInfo.set("content", content2);
        //     ISendMessage.sendMoblieMessageByGroup(eiInfo);
        //     ISendMessage.sendMessageByGroup(eiInfo);
        // }
        String toOcc = faultInfo.getExt4();
        String toClose = faultInfo.getExt5();
        if (toOcc != null && !toOcc.trim().isEmpty() && Y.equals(toOcc)) {
            overTodoService.overTodo(dmfm02.getRecId(), CommonConstants.FAULT_FINISHED_CONFIRM_CN);
            overTodoService.insertTodoWithUserGroupAndOrg("【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, "DM_021", null, CommonConstants.FAULT_TUNING_CONFIRM_CN, "DMFM0001", currentUser, null,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        } else if (toClose != null && !toClose.trim().isEmpty()) {
            FaultOrderDO faultOrder1 = new FaultOrderDO();
            faultOrder1.setOrderStatus(OrderStatus.GUAN_BI.getCode());
            faultOrder1.setFaultNo(faultInfo.getFaultNo());
            faultOrder1.setCloseTime(current);
            faultOrder1.setCloseUserId(currentUser);
            faultOrder1.setFaultWorkNo(dmfm02.getFaultWorkNo());
            faultReportMapper.updateFaultOrder(faultOrder1);
            overTodoService.overTodo(dmfm02.getRecId(), "故障关闭");
        } else if (cos.contains(majorCode)) {
            overTodoService.overTodo(dmfm02.getRecId(), CommonConstants.FAULT_FINISHED_CONFIRM_CN);
        } else if (DM_013.equals(ext2)) {
            overTodoService.overTodo(dmfm02.getRecId(), CommonConstants.FAULT_FINISHED_CONFIRM_CN);
            // content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已完工确认，请及时在EAM系统关闭工单！";
            // status = overTodoService.insertTodoWithUserGroupAndOrg("【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, "DM_013", workClass, "故障关闭", "DMFM0001", currentUser, majorCode, lineCode, "30");
        } else if (DM_006.equals(ext2)) {
            overTodoService.overTodo(dmfm02.getRecId(), CommonConstants.FAULT_FINISHED_CONFIRM_CN);
            if (cos.contains(majorCode)) {
                if (CommonConstants.CAR_SUBJECT_CODE.equals(majorCode)) {
                    // content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已完工确认，请及时在EAM系统关闭工单！";
                    // String stepOrg = CodeFactory.getCodeService().getCodeEName(CommonConstants.DM_MATCH_CONTROL_CODE, "04", "1");
                    // status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, CommonConstants.DM_007, stepOrg, "故障关闭", "DMFM0001", currentUser, majorCode, lineCode, "30", content);
                } else if (CommonConstants.CAR_DEVICE_SUBJECT_CODE.equals(majorCode)) {
                    // content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已完工确认，请及时在EAM系统关闭工单！";
                    // String stepOrg = CodeFactory.getCodeService().getCodeEName(CommonConstants.DM_MATCH_CONTROL_CODE, "05", "1");
                    // status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, CommonConstants.DM_037, stepOrg, "故障关闭", "DMFM0001", currentUser, majorCode, lineCode, "30", content);
                }
            } else {
                // content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已完工确认，请及时在EAM系统关闭工单！";
                // status = DMUtil.insertTODOWithUserGroupAndAllOrg("【" + majorName + CommonConstants.FAULT_CONTENT_END, queryFaultWorkRecId(faultWorkNo), faultWorkNo, CommonConstants.DM_006, respDeptCode, "故障关闭", "DMFM0001", currentUser, majorCode, lineCode, "30", content);
            }
        } else if (DM_007.equals(ext2)) {
            overTodoService.overTodo(dmfm02.getRecId(), "故障完工确认并关闭");
            FaultOrderDO faultOrderDO = new FaultOrderDO();
            faultOrderDO.setFaultWorkNo(dmfm02.getFaultWorkNo());
            faultOrderDO.setFaultNo(dmfm02.getFaultNo());
            faultOrderDO.setOrderStatus(OrderStatus.GUAN_BI.getCode());
            faultOrderDO.setCloseUserId(currentUser);
            faultOrderDO.setCloseTime(current);
        } else if (DM_031.equals(ext2)) {
            overTodoService.overTodo(dmfm02.getRecId(), CommonConstants.FAULT_FINISHED_CONFIRM_CN);
            String faultProcessResult = dmfm02.getFaultProcessResult();
            // content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已完工确认，请及时在EAM系统关闭工单！";
            if (CommonConstants.LINE_CODE_ONE.equals(faultProcessResult) || CommonConstants.LINE_CODE_TWO.equals(faultProcessResult)) {
                if (StringUtils.isNotEmpty(stationCode)) {
                    List<StationBO> stations = stationMapper.queryStation(null, stationCode);
                    for (StationBO bo : stations) {
                        overTodoService.insertTodo("【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, bo.getUserId(), "故障关闭", "DMFM0001", currentUser,BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                        // if (map1.get("mobile") != null && !"".equals(map1.get("mobile"))) {
                        //     ISendMessage.messageCons(map1.get("mobile"), content);
                        //     continue;
                        // }
                        // throw new CommonException(ErrorCode.NORMAL_ERROR, "该人员无电话信息");
                    }
                }
            }
        } else if (DM_020.equals(ext2) || DM_044.equals(ext2) || DM_030.equals(ext2)) {
            overTodoService.overTodo(dmfm02.getRecId(), CommonConstants.FAULT_FINISHED_CONFIRM_CN);
            // todo 发短信
            // status = DMUtil.insertTODOWithUserGroup("【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, ext2, "故障关闭", "DMFM0001", currentUser);
            // EiInfo eiInfo = new EiInfo();
            // eiInfo.set("group", ext2);
            // eiInfo.set("content", content);
            // ISendMessage.sendMoblieMessageByGroup(eiInfo);
        } else {
            overTodoService.overTodo(dmfm02.getRecId(), CommonConstants.FAULT_FINISHED_CONFIRM_CN);
            List<Map<Object, Object>> userList = new ArrayList();
            Map<Object, Object> userMap = new HashMap<>();
            // userMap.put("userCode", fillinUserId);
            userList.add(userMap);
            // overTodoService.insertTodo();
            //  DMUtil.insertTODOWithUserList(userList, "【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, "故障关闭", "DMFM0001", currentUser);
        }
    }

    private void close(List<FaultDetailResDTO> list) {
        list.forEach(a -> {
            FaultOrderDO faultOrderDO = faultQueryMapper.queryOneFaultOrder(a.getFaultNo(), null);
            faultOrderDO.setOrderStatus(OrderStatus.GUAN_BI.getCode());
            faultOrderDO.setCloseTime(DateUtils.getCurrentTime());
            faultReportMapper.updateFaultOrder(faultOrderDO);
            FaultInfoDO faultInfoDO = faultQueryMapper.queryOneFaultInfo(a.getFaultNo(), a.getFaultWorkNo());
            faultInfoDO.setFaultNo(a.getFaultNo());
            faultInfoDO.setRecReviseTime(DateUtils.getCurrentTime());
            faultInfoDO.setRecRevisor(TokenUtils.getCurrentPersonId());
            faultReportMapper.updateFaultInfo(faultInfoDO);
            overTodoService.overTodo(faultOrderDO.getRecId(), "故障关闭");
            // 添加流程记录
            addFaultFlow(a.getFaultNo(), a.getFaultWorkNo());
        });
    }

    private void check(List<FaultDetailResDTO> list, List<String> cos, String currentUser, String current, String stepOrg) {
        // 判断是否存在验收状态的数据
        Set<String> orderStatus = StreamUtils.mapToSet(list, FaultDetailResDTO::getOrderStatus);
        Assert.isFalse(orderStatus.contains(OrderStatus.YAN_SHOU.getCode()), "当前勾选的数据中存在验收状态的数据，无法进行重复操作!");
        list.forEach(a -> {
            String faultNo = a.getFaultNo();
            String faultWorkNo = a.getFaultWorkNo();
            FaultOrderDO faultOrderDO = faultQueryMapper.queryOneFaultOrder(faultNo, faultWorkNo);
            // 状态变更为验收
            faultOrderDO.setOrderStatus(OrderStatus.YAN_SHOU.getCode());
            faultOrderDO.setCheckUserId(currentUser);
            faultOrderDO.setCheckTime(current);
            faultOrderDO.setRecReviseTime(current);
            faultOrderDO.setRecRevisor(currentUser);
            faultReportMapper.updateFaultOrder(faultOrderDO);
            FaultInfoDO faultInfoDO = faultQueryMapper.queryOneFaultInfo(faultNo, faultWorkNo);
            faultInfoDO.setFaultNo(faultNo);
            faultInfoDO.setRecReviseTime(current);
            faultInfoDO.setRecRevisor(currentUser);
            faultReportMapper.updateFaultInfo(faultInfoDO);
            overTodoService.overTodo(faultOrderDO.getRecId(), "故障验收");
            String majorCode = a.getMajorCode();
            String majorName = a.getMajorName();
            if (cos.contains(majorCode)) {
                if (CommonConstants.CAR_SUBJECT_CODE.equals(majorCode)) {
                    // todo
                    // content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已验收，请及时在EAM系统完工确认！";
                    // overTodoService.insertTodoWithUserGroupAndOrg("【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, CommonConstants.DM_007, stepOrg, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001", currentUser, majorCode, lineCode, "30", content);
                } else if (CommonConstants.CAR_DEVICE_SUBJECT_CODE.equals(majorCode)) {
                    // content = CommonConstants.FAULT_CONTENT_BEGIN + faultWorkNo + "的故障，" + userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "已验收，请及时在EAM系统完工确认！";
                    // ISendMessage.sendMoblieMessageByGroup(messageInfo);
                    // status = DMUtil.insertTODOWithUserGroup("【" + majorName + CommonConstants.FAULT_CONTENT_END, dmfm02.getRecId(), faultWorkNo, CommonConstants.DM_037, CommonConstants.FAULT_FINISHED_CONFIRM_CN, "DMFM0001", currentUser);
                }
            }
            // 添加流程记录
            addFaultFlow(faultNo, faultWorkNo);
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
     * @param faultNo 故障编号
     * @param faultWorkNo 故障工单编号
     */
    public void addFaultFlow(String faultNo, String faultWorkNo) {
        FaultFlowReqDTO faultFlowReqDTO = new FaultFlowReqDTO();
        faultFlowReqDTO.setRecId(TokenUtils.getUuId());
        faultFlowReqDTO.setFaultNo(faultNo);
        faultFlowReqDTO.setFaultWorkNo(faultWorkNo);
        faultFlowReqDTO.setOperateUserId(TokenUtils.getCurrentPersonId());
        faultFlowReqDTO.setOperateUserName(TokenUtils.getCurrentPerson().getPersonName());
        faultFlowReqDTO.setOperateTime(DateUtils.getCurrentTime());
        FaultOrderDO faultOrderDO = faultQueryMapper.queryOneFaultOrder(faultNo, faultWorkNo);
        if (!Objects.isNull(faultOrderDO)) {
            faultFlowReqDTO.setOrderStatus(faultOrderDO.getOrderStatus());
        }
        faultReportMapper.addFaultFlow(faultFlowReqDTO);
    }
}
