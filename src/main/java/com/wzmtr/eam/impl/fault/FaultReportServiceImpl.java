package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.basic.query.RegionQuery;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.basic.OrgMajorResDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.LineCode;
import com.wzmtr.eam.enums.OrderStatus;
import com.wzmtr.eam.mapper.basic.OrgMajorMapper;
import com.wzmtr.eam.mapper.basic.RegionMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.PersonMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.common.UserAccountService;
import com.wzmtr.eam.service.fault.FaultReportService;
import com.wzmtr.eam.service.fault.TrackQueryService;
import com.wzmtr.eam.shiro.model.Person;
import com.wzmtr.eam.utils.*;
import com.wzmtr.eam.utils.mq.FaultSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 19:17
 */
@Service
@Slf4j
public class FaultReportServiceImpl implements FaultReportService {

    @Resource
    private UserAccountService userAccountService;

    @Autowired
    private FaultReportMapper faultReportMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private TrackQueryService trackQueryService;
    @Autowired
    private FaultQueryMapper faultQueryMapper;
    @Autowired
    private OverTodoService overTodoService;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    private OrgMajorMapper orgMajorMapper;
    @Autowired
    FaultSender faultSender;
    @Autowired
    private PersonMapper personMapper;
    private static final List<String> zcList = Arrays.asList("06", "07");
    @Autowired
    private FaultQueryServiceImpl faultQueryServiceImpl;

    @Override
    public String addToFault(FaultReportReqDTO reqDTO) {
        String maxFaultNo = faultReportMapper.getFaultInfoFaultNoMaxCode();
        String maxFaultWorkNo = faultReportMapper.getFaultOrderFaultWorkNoMaxCode();
        // 获取AOP代理对象
        FaultInfoDO faultInfoDO = reqDTO.toFaultInfoInsertDO(reqDTO);
        String nextFaultNo = CodeUtils.getNextCode(maxFaultNo, "GZ");
        insertToFaultInfo(faultInfoDO, nextFaultNo);
        FaultOrderDO faultOrderDO = reqDTO.toFaultOrderInsertDO(reqDTO);
        String nextFaultWorkNo = CodeUtils.getNextCode(maxFaultWorkNo, "GD");
        insertToFaultOrder(faultOrderDO, nextFaultNo, nextFaultWorkNo);
        // 添加流程记录
        addFaultFlow(nextFaultNo, nextFaultWorkNo);
        String majorCode = reqDTO.getMajorCode();
        // 中铁通 且不是行车调度的故障类型 直接变更为已派工状态 并给该工班下的人发待办
        if (!zcList.contains(majorCode)) {
            if (!"10".equals(reqDTO.getFaultType())) {
                String positionCode = reqDTO.getPositionCode();
                if (StringUtils.isNotEmpty(positionCode) && StringUtils.isNotEmpty(majorCode)) {
                    // 专业和位置查维修部门
                    OrgMajorResDTO organ = orgMajorMapper.getOrganByStationAndMajor(positionCode, majorCode);
                    if (organ != null) {
                        faultInfoDO.setRepairDeptCode(organ.getOrgCode());
                        // 负责人为中铁通工班长角色
                        Person person = personMapper.searchLeader(majorCode, positionCode, "DM_051");
                        faultOrderDO.setRepairRespUserId(person.getLoginName());
                        // 默认为紧急
                        faultInfoDO.setExt1("01");
                        faultInfoDO.setRecRevisor(TokenUtils.getCurrentPersonId());
                        faultInfoDO.setRecReviseTime(DateUtils.getCurrentTime());
                        faultOrderDO.setOrderStatus(OrderStatus.PAI_GONG.getCode());
                        faultOrderDO.setRecRevisor(TokenUtils.getCurrentPersonId());
                        faultOrderDO.setRecReviseTime(DateUtils.getCurrentTime());
                        faultReportMapper.updateFaultOrder(faultOrderDO);
                        faultReportMapper.updateFaultInfo(faultInfoDO);
                        overTodoService.insertTodoWithUserGroup(String.format(CommonConstants.TODO_GD_TPL, nextFaultWorkNo, "故障"), faultOrderDO.getRecId(), nextFaultWorkNo, organ.getOrgCode(), "故障派工", " ? ", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.FAULT_REPORT_QUERY.value());
                    }
                }
            } else {
                // 行车调度的故障类型 且中铁通的给生产调度发待办
                toZTTSCDD(reqDTO, faultOrderDO, nextFaultWorkNo);
            }
        } else {
            // 中车给中车检调
            toZCJD(reqDTO, faultOrderDO, nextFaultWorkNo);
        }
        return nextFaultNo;
        // TODO: 2023/8/24 知会OCC调度
//        if ("Y".equals(maintenance)) {
//            String groupName = "DM_021";
//            String content2 = userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "提报了一条" + majorName + "故障，工单号：" + faultWorkNo + "的故障，请知晓。";
//            EiInfo eiInfo1 = new EiInfo();
//            eiInfo1.set("group", groupName);
//            eiInfo1.set("content", content2);
//            ISendMessage.sendMessageByGroup(eiInfo1);
//            ISendMessage.sendMoblieMessageByGroup(eiInfo1);
//        }
    }

    private void toZCJD(FaultReportReqDTO reqDTO, FaultOrderDO faultOrderDO, String nextFaultWorkNo) {
        faultOrderDO.setOrderStatus(OrderStatus.XIA_FA.getCode());
        faultOrderDO.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultOrderDO.setRecReviseTime(DateUtils.getCurrentTime());
        faultReportMapper.updateFaultOrder(faultOrderDO);
        List<String> users = faultQueryServiceImpl.getUsersByCompanyAndRole(reqDTO.getMajorCode(), null, "ZCJD");
        if (CollectionUtil.isNotEmpty(users)) {
            overTodoService.insertTodoWithUserList(users, String.format(CommonConstants.TODO_GD_TPL, nextFaultWorkNo, "故障"), faultOrderDO.getRecId(), nextFaultWorkNo, "故障已下发", "?", TokenUtils.getCurrentPersonId(), null, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        }
    }

    private void toZTTSCDD(FaultReportReqDTO reqDTO, FaultOrderDO faultOrderDO, String nextFaultWorkNo) {
        faultOrderDO.setOrderStatus(OrderStatus.XIA_FA.getCode());
        faultOrderDO.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultOrderDO.setRecReviseTime(DateUtils.getCurrentTime());
        faultReportMapper.updateFaultOrder(faultOrderDO);
        List<String> users = faultQueryServiceImpl.getUsersByCompanyAndRole(reqDTO.getMajorCode(), "DM_007", null);
        if (CollectionUtil.isNotEmpty(users)) {
            overTodoService.insertTodoWithUserList(users, String.format(CommonConstants.TODO_GD_TPL, nextFaultWorkNo, "故障"), faultOrderDO.getRecId(), nextFaultWorkNo, "故障已下发", "?", TokenUtils.getCurrentPersonId(), null, BpmnFlowEnum.FAULT_REPORT_QUERY.value());
        }
    }

    @Override
    public void changeReport(FaultReportReqDTO reqDTO) {
        // 获取AOP代理对象
        FaultInfoDO faultInfoDO = reqDTO.toFaultInfoInsertDO(reqDTO);

        //更新故障信息
        modifyToFaultInfo(faultInfoDO);
        FaultOrderDO faultOrderDO = reqDTO.toFaultOrderChangeDO(reqDTO);
        faultOrderDO.setRecRevisor(TokenUtils.getCurrentPerson().getPersonId());
        faultOrderDO.setRecReviseTime(DateUtils.getCurrentTime());
        faultReportMapper.updateFaultOrder(faultOrderDO);

        // 添加流程记录
        //中铁通 且是行车调度的故障类型 直接变更为已派工状态 并给该工班下的人发待办
        addFaultFlow(reqDTO.getFaultNo(), reqDTO.getFaultWorkNo());
        String majorCode = reqDTO.getMajorCode();
        if (!zcList.contains(majorCode) && !"10".equals(reqDTO.getFaultType())) {
            String positionCode = reqDTO.getPositionCode();
            if (StringUtils.isNotEmpty(positionCode) && StringUtils.isNotEmpty(majorCode)) {
                // 专业和位置查维修部门
                OrgMajorResDTO organ = orgMajorMapper.getOrganByStationAndMajor(positionCode, majorCode);
                faultInfoDO.setRepairDeptCode(organ.getOrgCode());
                // 负责人为中铁通工班长角色
                Person person = personMapper.searchLeader(majorCode, positionCode, "DM_051");
                faultOrderDO.setRepairRespUserId(person.getLoginName());
                // 默认为紧急
                faultInfoDO.setExt1("01");
                faultOrderDO.setOrderStatus(OrderStatus.PAI_GONG.getCode());
                faultInfoDO.setRecRevisor(TokenUtils.getCurrentPersonId());
                faultInfoDO.setRecReviseTime(DateUtils.getCurrentTime());
                faultOrderDO.setRecRevisor(TokenUtils.getCurrentPersonId());
                faultOrderDO.setRecReviseTime(DateUtils.getCurrentTime());
                faultReportMapper.updateFaultOrder(faultOrderDO);
                faultReportMapper.updateFaultInfo(faultInfoDO);
                overTodoService.insertTodoWithUserGroup(String.format(CommonConstants.TODO_GD_TPL,reqDTO.getFaultWorkNo(),"故障"), faultOrderDO.getRecId(), reqDTO.getFaultWorkNo(), organ.getOrgCode(), "故障派工", " ? ", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.FAULT_REPORT_QUERY.value());
            }
        }
    }

    @Override
    public String addToFaultOpen(FaultReportOpenReqDTO reqDTO) {
        String maxFaultNo = faultReportMapper.getFaultInfoFaultNoMaxCode();
        String maxFaultWorkNo = faultReportMapper.getFaultOrderFaultWorkNoMaxCode();
        String nextFaultNo = CodeUtils.getNextCode(maxFaultNo, "GZ");
        String nextFaultWorkNo = CodeUtils.getNextCode(maxFaultWorkNo, "GD");
        reqDTO.setFaultNo(nextFaultNo);
        reqDTO.setFaultWorkNo(nextFaultWorkNo);
        // 推送消息至mq
        faultSender.sendFault(reqDTO);
        return nextFaultNo;
    }

    public void insertToFaultInfo(FaultInfoDO faultInfoDO, String nextFaultNo) {
        faultInfoDO.setFaultNo(nextFaultNo);
        faultInfoDO.setRecId(TokenUtils.getUuId());
        faultInfoDO.setDeleteFlag("0");
        faultInfoDO.setFillinTime(DateUtils.getCurrentTime());
        faultInfoDO.setFillinUserId(TokenUtils.getCurrentPerson().getPersonId());
        faultInfoDO.setFillinDeptCode(TokenUtils.getCurrentPerson().getOfficeId());
        faultInfoDO.setRecCreator(TokenUtils.getCurrentPerson().getPersonId());
        faultInfoDO.setRecCreateTime(DateUtils.getCurrentTime());
        faultReportMapper.addToFaultInfo(faultInfoDO);
    }

    public void insertToFaultOrder(FaultOrderDO faultOrderDO, String nextFaultNo, String nextFaultWorkNo) {
        faultOrderDO.setFaultWorkNo(nextFaultWorkNo);
        faultOrderDO.setFaultNo(nextFaultNo);
        faultOrderDO.setDeleteFlag("0");
        faultOrderDO.setRecId(TokenUtils.getUuId());
        faultOrderDO.setRecCreator(TokenUtils.getCurrentPerson().getPersonId());
        faultOrderDO.setRecCreateTime(DateUtils.getCurrentTime());
        faultReportMapper.addToFaultOrder(faultOrderDO);
    }

    //变更故障信息
    public void modifyToFaultInfo(FaultInfoDO faultInfoDO) {
        faultInfoDO.setDeleteFlag("0");
        faultInfoDO.setFillinTime(DateUtils.getCurrentTime());
        faultInfoDO.setFillinUserId(TokenUtils.getCurrentPerson().getPersonId());
        faultInfoDO.setFillinDeptCode(TokenUtils.getCurrentPerson().getOfficeId());
        faultInfoDO.setRecRevisor(TokenUtils.getCurrentPerson().getPersonId());
        faultInfoDO.setRecReviseTime(DateUtils.getCurrentTime());
        faultReportMapper.updateToFaultInfo(faultInfoDO);
    }

    @Override
    public Page<FaultReportResDTO> list(FaultReportPageReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());

        // 专业未筛选时，按当前用户专业隔离数据  获取当前用户所属组织专业
        List<String> userMajorList = null;
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId()) && StringUtils.isEmpty(reqDTO.getMajorCode())) {
            userMajorList = userAccountService.listUserMajor();
        }

        Page<FaultReportResDTO> list = faultReportMapper.list(reqDTO.of(), reqDTO.getFaultNo(),
                reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(),
                reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(),
                reqDTO.getFillinTimeEnd(), reqDTO.getPositionCode(), reqDTO.getOrderStatus(),reqDTO.getFaultWorkNo(), reqDTO.getLineCode(),userMajorList);
        List<FaultReportResDTO> records = list.getRecords();
        if (StringUtils.isEmpty(records)) {
            return new Page<>();
        }
        buildRes(records);
        return list;
    }

    @Override
    public Page<FaultReportResDTO> openApiList(FaultReportPageReqDTO reqDTO) {
        if (StringUtils.isNotEmpty(reqDTO.getPositionName())) {
            List<RegionResDTO> regionRes = regionMapper.selectByQuery(RegionQuery.builder().nodeName(reqDTO.getPositionName()).build());
            Set<String> nodeCodes = regionRes.stream().map(RegionResDTO::getNodeCode).collect(Collectors.toSet());
            reqDTO.setPositionCodes(nodeCodes);
        }
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<FaultReportResDTO> list = faultReportMapper.openApiList(reqDTO.of(), reqDTO);
        List<FaultReportResDTO> records = list.getRecords();
        if (StringUtils.isEmpty(records)) {
            return new Page<>();
        }
        buildRes(records);
        return list;
    }

    @Override
    public Page<FaultReportResDTO> carReportList(FaultReportPageReqDTO reqDTO) {
        PageMethod.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());

        // 专业未筛选时，按当前用户专业隔离数据  获取当前用户所属组织专业
        List<String> userMajorList = null;
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId()) && StringUtils.isEmpty(reqDTO.getMajorCode())) {
            userMajorList = userAccountService.listUserMajor();
        }

        Page<FaultReportResDTO> list = faultReportMapper.carFaultReportList(reqDTO.of(), reqDTO.getFaultNo(),
                reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(),
                reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(),
                reqDTO.getFillinTimeEnd(), reqDTO.getPositionCode(), reqDTO.getOrderStatus(),userMajorList);
        List<FaultReportResDTO> records = list.getRecords();
        if (StringUtils.isEmpty(records)) {
            return new Page<>();
        }
        buildRes(records);
        return list;
    }
    private void buildRes(List<FaultReportResDTO> records) {
        Set<String> positionCodes = StreamUtils.mapToSet(records, FaultReportResDTO::getPositionCode);
        List<RegionResDTO> regionRes = regionMapper.selectByQuery(RegionQuery.builder().nodeCodes(positionCodes).build());
        Map<String, RegionResDTO> regionMap = StreamUtils.toMap(regionRes, RegionResDTO::getNodeCode);
        records.forEach(a -> {
            LineCode line = LineCode.getByCode(a.getLineCode());
            if (StringUtils.isNotEmpty(a.getDocId())) {
                a.setDocFile(fileMapper.selectFileInfo(Arrays.asList(a.getDocId().split(","))));
            }
            a.setLineName(line == null ? a.getLineCode() : line.getDesc());
            if (StringUtils.isNotEmpty(a.getRepairDeptCode())) {
                a.setRepairDeptName(organizationMapper.getNamesById(a.getRepairDeptCode()));
            }
            if (regionMap.containsKey(a.getPositionCode())) {
                a.setPositionName(regionMap.get(a.getPositionCode()).getNodeName());
            }
            if (StringUtils.isNotEmpty(a.getFillinDeptCode())) {
                a.setFillinDeptName(organizationMapper.getNamesById(a.getFillinDeptCode()));
            }
        });
    }


    @Override
    public FaultDetailResDTO detail(FaultDetailReqDTO reqDTO) {
        return trackQueryService.faultDetail(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(FaultCancelReqDTO reqDTO) {
        // 已提报故障单撤销 逻辑删 涉及faultinfo和faultorder两张表
        faultReportMapper.cancelOrder(reqDTO);
        faultReportMapper.cancelInfo(reqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(FaultCancelReqDTO reqDTO) {
        // faultWorkNo的recId
        String faultWorkNo = reqDTO.getFaultWorkNo();
        FaultOrderDO faultOrderDO = faultQueryMapper.queryOneFaultOrder(null, faultWorkNo);
        faultOrderDO.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultOrderDO.setRecReviseTime(DateUtils.getCurrentTime());
        // order表作废状态
        faultOrderDO.setOrderStatus(OrderStatus.ZUO_FEI.getCode());
        faultReportMapper.updateFaultOrder(faultOrderDO);
        String faultNo = reqDTO.getFaultNo();
        // info表更新
        FaultInfoDO faultInfoDO = faultQueryMapper.queryOneFaultInfo(faultNo, faultWorkNo);
        faultInfoDO.setRecReviseTime(DateUtils.getCurrentTime());
        faultInfoDO.setRecRevisor(TokenUtils.getCurrentPersonId());
        faultReportMapper.updateFaultInfo(faultInfoDO);
        // 取消待办
        overTodoService.cancelTodo(reqDTO.getOrderRecId());
        // 添加流程记录
        addFaultFlow(faultNo, faultWorkNo);
    }

    @Override
    public void update(FaultReportReqDTO reqDTO) {
        Assert.isNotEmpty(reqDTO.getFaultNo(), "参数缺失[故障编号]不能为空!");
        // 修改已提报故障单  修改时间 修改人， 各属性的值
        FaultInfoDO infoUpdate = BeanUtils.convert(reqDTO, FaultInfoDO.class);
        infoUpdate.setRecRevisor(TokenUtils.getCurrentPersonId());
        infoUpdate.setRecReviseTime(DateUtils.getCurrentTime());
        FaultOrderDO orderUpdate = BeanUtils.convert(reqDTO, FaultOrderDO.class);
        orderUpdate.setRecRevisor(TokenUtils.getCurrentPersonId());
        orderUpdate.setRecReviseTime(DateUtils.getCurrentTime());
        if (StringUtils.isEmpty(reqDTO.getDocId())) {
            // 前端传的是个空值，特殊处理下
            infoUpdate.setDocId(" ");
            orderUpdate.setDocId(" ");
        }
        if (null != reqDTO.getMaintenance()){
             infoUpdate.setExt4(reqDTO.getMaintenance().toString());
        }
        if (CommonConstants.ZERO_STRING.equals(orderUpdate.getOrderStatus())) {
            orderUpdate.setOrderStatus("10");
            infoUpdate.setFaultStatus("20");
        }
        faultReportMapper.updateFaultInfo(infoUpdate);
        faultReportMapper.updateFaultOrder(orderUpdate);
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

