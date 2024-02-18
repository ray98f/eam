package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.basic.query.RegionQuery;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.LineCode;
import com.wzmtr.eam.enums.OrderStatus;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.basic.RegionMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.fault.FaultReportService;
import com.wzmtr.eam.service.fault.TrackQueryService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 19:17
 */
@Service
@Slf4j
public class FaultReportServiceImpl implements FaultReportService {
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
    private EquipmentMapper equipmentMapper;

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

    @Override
    public String addToFaultOpen(FaultReportOpenReqDTO reqDTO) {
        FaultReportReqDTO req = new FaultReportReqDTO();
        if (StringUtils.isNotEmpty(reqDTO.getEquipCode())) {
            EquipmentResDTO equipment = equipmentMapper.getEquipmentDetailByCode(reqDTO.getEquipCode());
            req = req.toReportReqFromEquipment(equipment);
        }
        req.setFaultDetail("故障等级：" + reqDTO.getFaultLevel() + "，故障详情：" + reqDTO.getFaultDetail());
        req.setDiscoveryTime(reqDTO.getAlamTime());
        req.setFaultType(reqDTO.getFaultType());
        req.setFaultStatus(reqDTO.getFaultStatus());
        req.setPartCode(reqDTO.getPartCode());
        return addToFault(req);
    }

    public void insertToFaultInfo(FaultInfoDO faultInfoDO, String nextFaultNo) {
        faultInfoDO.setFaultNo(nextFaultNo);
        faultInfoDO.setRecId(TokenUtils.getUuId());
        faultInfoDO.setDeleteFlag("0");
        faultInfoDO.setFillinTime(DateUtils.current(DateUtils.YYYY_MM_DD_HH_MM_SS));
        faultInfoDO.setFillinUserId(TokenUtils.getCurrentPerson().getPersonId());
        faultInfoDO.setFillinDeptCode(TokenUtils.getCurrentPerson().getOfficeId());
        faultInfoDO.setRecCreator(TokenUtils.getCurrentPerson().getPersonId());
        faultInfoDO.setRecCreateTime(DateUtils.current(DateUtils.YYYY_MM_DD_HH_MM_SS));
        faultReportMapper.addToFaultInfo(faultInfoDO);
    }

    public void insertToFaultOrder(FaultOrderDO faultOrderDO, String nextFaultNo, String nextFaultWorkNo) {
        faultOrderDO.setFaultWorkNo(nextFaultWorkNo);
        faultOrderDO.setFaultNo(nextFaultNo);
        faultOrderDO.setDeleteFlag("0");
        faultOrderDO.setRecId(TokenUtils.getUuId());
        faultOrderDO.setRecCreator(TokenUtils.getCurrentPerson().getPersonId());
        faultOrderDO.setRecCreateTime(DateUtils.current(DateUtils.YYYY_MM_DD_HH_MM_SS));
        faultReportMapper.addToFaultOrder(faultOrderDO);

    }

    @Override
    public Page<FaultReportResDTO> list(FaultReportPageReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<FaultReportResDTO> list = faultReportMapper.list(reqDTO.of(), reqDTO.getFaultNo(),
                reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(),
                reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(),
                reqDTO.getFillinTimeEnd(), reqDTO.getPositionCode(), reqDTO.getOrderStatus(),reqDTO.getFaultWorkNo(), reqDTO.getLineCode());
        List<FaultReportResDTO> records = list.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new Page<>();
        }
        buildRes(records);
        return list;
    }

    @Override
    public Page<FaultReportResDTO> openApiList(FaultReportPageReqDTO reqDTO) {
        String csm = "NCSM";
        if (reqDTO.getTenant().contains(csm)) {
            if(StringUtils.isNotEmpty(reqDTO.getPositionName())){
                List<RegionResDTO> regionResDTOS = regionMapper.selectByQuery(RegionQuery.builder().nodeName(reqDTO.getPositionName()).build());
                Set<String> nodeCodes = regionResDTOS.stream().map(RegionResDTO::getNodeCode).collect(Collectors.toSet());
                reqDTO.setPositionCodes(nodeCodes);
            }
            PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
            Page<FaultReportResDTO> list = faultReportMapper.openApiList(reqDTO.of(), reqDTO);
            List<FaultReportResDTO> records = list.getRecords();
            if (CollectionUtil.isEmpty(records)) {
                return new Page<>();
            }
            buildRes(records);
            return list;
        } else {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "您无权访问这个接口");
        }
    }

    @Override
    public Page<FaultReportResDTO> carReportList(FaultReportPageReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        Page<FaultReportResDTO> list = faultReportMapper.carFaultReportList(reqDTO.of(), reqDTO.getFaultNo(),
                reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(),
                reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(),
                reqDTO.getFillinTimeEnd(), reqDTO.getPositionCode(), reqDTO.getOrderStatus());
        List<FaultReportResDTO> records = list.getRecords();
        if (CollectionUtil.isEmpty(records)) {
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
                a.setFillinDeptCode(organizationMapper.getNamesById(a.getFillinDeptCode()));
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
        faultFlowReqDTO.setOperateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        FaultOrderDO faultOrderDO = faultQueryMapper.queryOneFaultOrder(faultNo, faultWorkNo);
        if (!Objects.isNull(faultOrderDO)) {
            faultFlowReqDTO.setOrderStatus(faultOrderDO.getOrderStatus());
        }
        faultReportMapper.addFaultFlow(faultFlowReqDTO);
    }
}

