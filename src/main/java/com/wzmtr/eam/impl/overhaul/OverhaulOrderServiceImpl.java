package com.wzmtr.eam.impl.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.FaultInfoReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultOrderReqDTO;
import com.wzmtr.eam.dto.req.overhaul.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.basic.WoRuleResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulItemResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulOrderResDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.overhaul.*;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulStateResDTO;
import com.wzmtr.eam.entity.OrganMajorLineType;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.EquipmentCategoryMapper;
import com.wzmtr.eam.mapper.basic.WoRuleMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Page<OverhaulOrderResDTO> pageOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulOrderMapper.pageOrder(pageReqDTO.of(), overhaulOrderListReqDTO);
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
                        .workGroupName(resDTO.getWorkGroupName())
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
        return faultQueryMapper.queryDeptCode(order.getLineNo(), order.getSubjectCode(), "20");
    }

    @Override
    public List<OrganMajorLineType> queryWorker(String workerGroupCode) {
        if (StringUtils.isEmpty(workerGroupCode)) {
            throw new CommonException(ErrorCode.PARAM_ERROR);
        }
        return userGroupMemberService.getDepartmentUserByGroupName(workerGroupCode, "DM_012");
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
            overhaulOrderReqDTO.setWorkStatus(TokenUtil.getCurrentPersonId());
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
        // todo 根据角色获取用户列表
//        List<Map<String, String>> userList = InterfaceHelper.getUserHelpe().getUserBySubjectAndLineAndGroup(dmer21.getSubjectCode(), dmer21.getLineNo(), "DM_007");
        List<Map<String, String>> userList = new ArrayList<>();
        for (Map<String, String> map2 : userList) {
            overTodoService.insertTodo("检修工单流转", overhaulOrderReqDTO.getRecId(), overhaulOrderReqDTO.getOrderCode(), map2.get("userId"), "检修工单完工确认", "DMER0200", TokenUtil.getCurrentPersonId());
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
        OverhaulOrderListReqDTO listReqDTO = new OverhaulOrderListReqDTO();
        listReqDTO.setPlanCode(overhaulOrderReqDTO.getPlanCode());
        List<OverhaulOrderResDTO> list = overhaulOrderMapper.listOverhaulOrder(listReqDTO);
        if (list != null && list.size() > 0 && overhaulOrderReqDTO.getOrderCode().equals(list.get(0).getOrderCode())) {
            OverhaulPlanListReqDTO overhaulPlanListReqDTO = new OverhaulPlanListReqDTO();
            overhaulPlanListReqDTO.setPlanCode(overhaulOrderReqDTO.getPlanCode());
            List<OverhaulPlanResDTO> plans = overhaulPlanMapper.listOverhaulPlan(overhaulPlanListReqDTO);
            if (plans.size() > 0) {
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat dateTimeFormat1 = new SimpleDateFormat("yyyyMMddHH");
                String nowDate = dateTimeFormat.format(new Date());
                List<WoRuleResDTO.WoRuleDetail> rules = woRuleMapper.listWoRuleDetail(plans.get(0).getRuleCode(), nowDate.substring(nowDate.length() - 4), nowDate.substring(nowDate.length() - 4));
                int trigerMiles = 0;
                if (rules.size() > 0) {
                    if (CommonConstants.CAR_SUBJECT_CODE.equals(overhaulOrderReqDTO.getSubjectCode())) {
                        List<String> queryObjMiles = overhaulOrderMapper.queryObjMiles(plans.get(0).getPlanCode());
                        if (queryObjMiles != null && queryObjMiles.size() > 0) {
                            int mileage = Integer.parseInt(rules.get(0).getExt1());
                            int totalMiles = Integer.parseInt(queryObjMiles.get(0));
                            trigerMiles = mileage + totalMiles;
                        }
                    }
                    int period = rules.get(0).getPeriod();
                    int beforeTime = rules.get(0).getBeforeTime();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
                    Date realEndTime1 = format.parse(overhaulOrderReqDTO.getRealEndTime().substring(0, 13));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(realEndTime1);
                    calendar.add(Calendar.HOUR_OF_DAY, period);
                    calendar.add(Calendar.DAY_OF_YEAR, -beforeTime);
                    Date realEndTime = calendar.getTime();
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
        // ServiceDMER0201  confirmWorkers
        overTodoService.overTodo(overhaulOrderReqDTO.getRecId(), "");
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
        if (list.size() > 0) {
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
        return overhaulOrderMapper.pageOverhaulObject(pageReqDTO.of(), orderCode, planCode, planName, objectCode);
    }

    @Override
    public OverhaulOrderDetailResDTO getOverhaulObjectDetail(String id) {
        return overhaulOrderMapper.getOverhaulObjectDetail(id);
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
        if (list != null && list.size() > 0) {
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

    @Override
    public OverhaulItemResDTO getOverhaulItemDetail(String id) {
        return overhaulItemMapper.getOverhaulItemDetail(id);
    }

    @Override
    public void exportOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO, HttpServletResponse response) throws IOException {
        List<OverhaulItemResDTO> overhaulItem = overhaulItemMapper.listOverhaulItem(overhaulItemListReqDTO);
        if (overhaulItem != null && !overhaulItem.isEmpty()) {
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
        if (list != null && !list.isEmpty()) {
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
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        dmfm01.setExt2(queryNowUser(fillinUserId));
        dmfm01.setRecId(UUID.randomUUID().toString());
        if (StringUtils.isNotEmpty(objectCode) && objectCode.startsWith(CommonConstants.NINE_STRING)) {
            dmfm01.setObjectName(list2.get(0).getObjectName());
            dmfm01.setObjectCode(list2.get(0).getObjectCode());
        }
        dmfm01.setFaultType("30");
        dmfm01.setMajorCode(list.get(0).getSubjectCode());
        dmfm01.setMajorName(list.get(0).getSubjectName());
        dmfm01.setSystemCode(list.get(0).getSystemCode());
        dmfm01.setSystemName(list.get(0).getSystemName());
        dmfm01.setEquipTypeCode(list.get(0).getEquipTypeCode());
        dmfm01.setEquipTypeName(list.get(0).getEquipTypeName());
        dmfm01.setPositionName(list.get(0).getPosition1Name());
        dmfm01.setPositionCode(list.get(0).getPosition1Code());
        dmfm01.setRecCreator(currentUser);
        dmfm01.setRecCreateTime(dateTimeFormat.format(new Date()));
        dmfm01.setDiscoveryTime(dateTimeFormat.format(new Date()));
        dmfm01.setFillinTime(dateTimeFormat.format(new Date()));
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
