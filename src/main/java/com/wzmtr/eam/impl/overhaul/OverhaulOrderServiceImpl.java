package com.wzmtr.eam.impl.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bo.FaultInfoBO;
import com.wzmtr.eam.bo.FaultOrderBO;
import com.wzmtr.eam.dto.req.*;
import com.wzmtr.eam.dto.req.fault.FaultInfoReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultOrderReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
import com.wzmtr.eam.dto.res.*;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.WoRuleMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulItemMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulOrderMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulPlanMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulStateMapper;
import com.wzmtr.eam.service.overhaul.OverhaulOrderService;
import com.wzmtr.eam.service.overhaul.OverhaulWorkRecordService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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
    public void exportOverhaulOrder(OverhaulOrderListReqDTO overhaulOrderListReqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "工单编号", "计划名称", "计划编号", "对象名称", "工单状态", "检修情况",
                "异常数量", "工器具", "计划开始时间", "计划完成时间", "作业工班", "作业人员", "实际开始时间", "实际完成时间",
                "线路", "位置", "专业", "系统", "派工人", "确认人", "施工计划号", "最后修改人", "备注");
        List<OverhaulOrderResDTO> overhaulOrderResDTOList = overhaulOrderMapper.listOrder(overhaulOrderListReqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (overhaulOrderResDTOList != null && !overhaulOrderResDTOList.isEmpty()) {
            for (OverhaulOrderResDTO resDTO : overhaulOrderResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("工单编号", resDTO.getOrderCode());
                map.put("计划名称", resDTO.getPlanName());
                map.put("计划编号", resDTO.getPlanCode());
                map.put("对象名称", resDTO.getExt1());
                map.put("工单状态", resDTO.getWorkStatus());
                map.put("检修情况", resDTO.getWorkFinishStatus());
                map.put("异常数量", resDTO.getAbnormalNumber());
                map.put("工器具", resDTO.getRecDeletor());
                map.put("计划开始时间", resDTO.getPlanStartTime());
                map.put("计划完成时间", resDTO.getPlanEndTime());
                map.put("作业工班", resDTO.getWorkGroupName());
                map.put("作业人员", resDTO.getWorkerName());
                map.put("实际开始时间", resDTO.getRealStartTime());
                map.put("实际完成时间", resDTO.getRealEndTime());
                map.put("线路", resDTO.getLineName());
                map.put("位置", resDTO.getPosition1Name());
                map.put("专业", resDTO.getSubjectName());
                map.put("系统", resDTO.getSystemName());
                map.put("派工人", resDTO.getSendPersonName());
                map.put("确认人", resDTO.getAckPersonName());
                map.put("施工计划号", resDTO.getSystemName());
                map.put("最后修改人", resDTO.getRecRevisor());
                map.put("备注", resDTO.getRemark());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("检修工单信息", listName, list, null, response);
    }

    @Override
    public void dispatchWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) {
        if (!"admin".equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulOrderMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        checkOrderState(overhaulOrderReqDTO, "1,2,3", "请求、已下达、已分配");
        if ("1".equals(overhaulOrderReqDTO.getWorkStatus())) {
            overhaulOrderReqDTO.setWorkStatus("2");
            overhaulOrderReqDTO.setRecDeletor(TokenUtil.getCurrentPerson().getPersonName() + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            overhaulOrderReqDTO.setSendPersonId(TokenUtil.getCurrentPersonId());
            overhaulOrderReqDTO.setSendPersonName(TokenUtil.getCurrentPerson().getPersonName());
            overhaulOrderReqDTO.setSendTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            overhaulOrderReqDTO.setWorkStatus(TokenUtil.getCurrentPersonId());
        }
        overhaulWorkRecordService.insertRepair(overhaulOrderReqDTO);
        overhaulOrderMapper.modifyOverhaulOrder(overhaulOrderReqDTO);
    }

    @Override
    public void auditWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) {
        if (!"admin".equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulOrderMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        if ("07".equals(overhaulOrderReqDTO.getSubjectCode()) && !overhaulOrderReqDTO.getPlanName().contains("二级修")) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "只有工单为车辆二级修才能进行该操作。");
        }
        checkOrderState(overhaulOrderReqDTO, "4", "完工");
        overhaulOrderReqDTO.setWorkStatus("6");
        overhaulOrderReqDTO.setRecDeleteTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulOrderReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulOrderReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulOrderReqDTO.setExt1(" ");
        overhaulOrderMapper.modifyOverhaulOrder(overhaulOrderReqDTO);
        // todo ServiceDMER0201  auditWorkers
    }

    @Override
    public void confirmWorkers(OverhaulOrderReqDTO overhaulOrderReqDTO) throws ParseException {
        if (!"admin".equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulOrderMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulOrderReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        if (!overhaulOrderReqDTO.getPlanName().contains("二级修")) {
            checkOrderState(overhaulOrderReqDTO, "4", "完工");
        } else {
            checkOrderState(overhaulOrderReqDTO, "6", "完工审核");
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
                    if ("07".equals(overhaulOrderReqDTO.getSubjectCode())) {
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
                    overhaulPlanMapper.modifyOverhaulPlan(overhaulPlanReqDTO);
                }
            }
        }
        // todo ServiceDMER0201  confirmWorkers
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
    public Page<OverhaulOrderDetailResDTO> pageOverhaulObject(String orderCode, String planCode, String planName, String objectCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulOrderMapper.pageOverhaulObject(pageReqDTO.of(), orderCode, planCode, planName, objectCode);
    }

    @Override
    public OverhaulOrderDetailResDTO getOverhaulObjectDetail(String id) {
        return overhaulOrderMapper.getOverhaulObjectDetail(id);
    }

    @Override
    public void exportOverhaulObject(String orderCode, String planCode, String planName, String objectCode, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "对象编号", "对象名称", "检修情况", "检修情况说明", "开始时间", "完成时间", "异常数量", "作业人员", "备注");
        List<OverhaulOrderDetailResDTO> overhaulObject = overhaulOrderMapper.listOverhaulObject(orderCode, planCode, planName, objectCode);
        List<Map<String, String>> list = new ArrayList<>();
        if (overhaulObject != null && !overhaulObject.isEmpty()) {
            for (OverhaulOrderDetailResDTO resDTO : overhaulObject) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("对象编号", resDTO.getObjectCode());
                map.put("对象名称", resDTO.getObjectName());
                map.put("检修情况", resDTO.getRepairStatus());
                map.put("检修情况说明", resDTO.getRepairDetail());
                map.put("开始时间", resDTO.getStartTime());
                map.put("完成时间", resDTO.getCompliteTime());
                map.put("异常数量", resDTO.getAbnormalNumber());
                map.put("作业人员", resDTO.getTaskPersonName());
                map.put("备注", resDTO.getTaskRemark());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("检修对象信息", listName, list, null, response);
    }

    @Override
    public void checkjx(String orderCode) {
        OverhaulOrderListReqDTO overhaulOrderListReqDTO = new OverhaulOrderListReqDTO();
        overhaulOrderListReqDTO.setOrderCode(orderCode);
        List<OverhaulOrderResDTO> list = overhaulOrderMapper.listOverhaulOrder(overhaulOrderListReqDTO);
        if (list != null && list.size() > 0) {
            String planNmae = list.get(0).getPlanName();
            String orderstatus = list.get(0).getWorkStatus();
            if (planNmae != null && !"".equals(planNmae.trim()) && planNmae.contains("二级修") && "4".equals(orderstatus)) {
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
    public void exportOverhaulItem(OverhaulItemListReqDTO overhaulItemListReqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "检修模块", "检修项目", "技术要求", "检修项类型", "车组号", "检修结果", "上限", "下限", "单位", "检修人", "附件");
        List<OverhaulItemResDTO> overhaulItem = overhaulItemMapper.listOverhaulItem(overhaulItemListReqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (overhaulItem != null && !overhaulItem.isEmpty()) {
            for (OverhaulItemResDTO resDTO : overhaulItem) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("检修模块", resDTO.getModelName());
                map.put("检修项目", resDTO.getItemName());
                map.put("技术要求", resDTO.getExt1());
                map.put("检修项类型", resDTO.getItemType());
                map.put("车组号", resDTO.getTrainNumber());
                map.put("检修结果", resDTO.getWorkResult());
                map.put("上限", resDTO.getMaxValue());
                map.put("下限", resDTO.getMinValue());
                map.put("单位", resDTO.getItemUnit());
                map.put("检修人", resDTO.getWorkUserName());
                map.put("附件", resDTO.getDocId());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("检修项信息", listName, list, null, response);
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
    public void exportOverhaulState(String objectCode, String itemName, String orderCode, String tdmer23RecId, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "检修项目", "检修结果", "问题描述", "处理意见", "跟踪结果", "故障单号");
        List<OverhaulStateResDTO> overhaulState = overhaulStateMapper.listOverhaulState(objectCode, itemName, orderCode, tdmer23RecId);
        List<Map<String, String>> list = new ArrayList<>();
        if (overhaulState != null && !overhaulState.isEmpty()) {
            for (OverhaulStateResDTO resDTO : overhaulState) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("检修项目", resDTO.getItemName());
                map.put("检修结果", resDTO.getFaultStatus());
                map.put("问题描述", resDTO.getProblemDescription());
                map.put("处理意见", resDTO.getHandlingSuggestion());
                map.put("跟踪结果", resDTO.getFollowStatus());
                map.put("故障单号", resDTO.getFaultCode());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("检修异常信息", listName, list, null, response);
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
        StringBuilder detail = new StringBuilder();
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
        BeanUtils.copyProperties(overhaulUpStateReqDTO.getResDTO(), dmfm01);
        String fillinUserId = overhaulUpStateReqDTO.getResDTO().getFillinUserId();
        dmfm01.setExt2(queryNowUser(fillinUserId));
        dmfm01.setRecId(UUID.randomUUID().toString());
        if (objectCode != null && !"".equals(objectCode.trim()) && objectCode.startsWith("9")) {
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
        BeanUtils.copyProperties(overhaulUpStateReqDTO.getResDTO(), dmfm02);
        dmfm02.setRecId(TokenUtil.getUuId());
        dmfm02.setFaultWorkNo(faultWorkNo);
        dmfm02.setFaultNo(faultNo);
        dmfm02.setOrderStatus("30");
        dmfm02.setWorkClass(list.get(0).getWorkerGroupCode());
        // BeanUtils.copyProperties(dmfm01, f1);
        FaultInfoBO f1 = __BeanUtil.convert(dmfm01, FaultInfoBO.class);
        faultReportMapper.addToFaultInfo(f1);
        // FaultReportReqDTO f2 = new FaultReportReqDTO();
        // BeanUtils.copyProperties(dmfm02, f2);
        FaultOrderBO f2 = __BeanUtil.convert(dmfm02, FaultOrderBO.class);
        faultReportMapper.addToFaultOrder(f2);
        overhaulOrderMapper.updateone(faultWorkNo, "30", overhaulUpStateReqDTO.getRecId());
        String content = "【市铁投集团】检修升级故障，请及时处理并在EAM系统填写维修报告，工单号：" + faultWorkNo + "，请知晓。";
        // todo ServiceDMER0205 insertUpFaultMessage
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
