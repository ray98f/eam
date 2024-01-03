package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import com.wzmtr.eam.enums.LineCode;
import com.wzmtr.eam.enums.OrderStatus;
import com.wzmtr.eam.mapper.basic.RegionMapper;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

    @Override
    // @Transactional(rollbackFor = Exception.class)
    public String addToFault(FaultReportReqDTO reqDTO) {
        String maxFaultNo = faultReportMapper.getFaultInfoFaultNoMaxCode();
        String maxFaultWorkNo = faultReportMapper.getFaultOrderFaultWorkNoMaxCode();
        // 获取AOP代理对象
        // FaultReportServiceImpl aop = (FaultReportServiceImpl) AopContext.currentProxy();
        FaultInfoDO faultInfoDO = reqDTO.toFaultInfoInsertDO(reqDTO);
        String nextFaultNo = CodeUtils.getNextCode(maxFaultNo, "GZ");
        _insertToFaultInfo(faultInfoDO, nextFaultNo);
        FaultOrderDO faultOrderDO = reqDTO.toFaultOrderInsertDO(reqDTO);
        String nextFaultWorkNo = CodeUtils.getNextCode(maxFaultWorkNo, "GD");
        _insertToFaultOrder(faultOrderDO, nextFaultNo, nextFaultWorkNo);
        return nextFaultNo;
        // TODO: 2023/8/24 知会OCC调度
        // if ("Y".equals(maintenance)) {
        //     /* 1756 */             String groupName = "DM_021";
        //     /* 1757 */             String content2 = userCoInfo.getOrgName() + "的" + userCoInfo.getUserName() + "提报了一条" + majorName + "故障，工单号：" + faultWorkNo + "的故障，请知晓。";
        //     /* 1758 */             EiInfo eiInfo1 = new EiInfo();
        //     /* 1759 */             eiInfo1.set("group", groupName);
        //     /* 1760 */             eiInfo1.set("content", content2);
        //     /* 1761 */             ISendMessage.sendMessageByGroup(eiInfo1);
        //     /* 1762 */             ISendMessage.sendMoblieMessageByGroup(eiInfo1);
        //     /*      */           }
    }

    public void _insertToFaultInfo(FaultInfoDO faultInfoDO, String nextFaultNo) {
        faultInfoDO.setFaultNo(nextFaultNo);
        faultInfoDO.setRecId(TokenUtil.getUuId());
        faultInfoDO.setDeleteFlag("0");
        faultInfoDO.setFillinTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
        faultInfoDO.setFillinUserId(TokenUtil.getCurrentPerson().getPersonId());
        faultInfoDO.setFillinDeptCode(TokenUtil.getCurrentPerson().getOfficeId());
        faultInfoDO.setRecCreator(TokenUtil.getCurrentPerson().getPersonId());
        faultInfoDO.setRecCreateTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
        faultReportMapper.addToFaultInfo(faultInfoDO);
    }

    public void _insertToFaultOrder(FaultOrderDO faultOrderDO, String nextFaultNo, String nextFaultWorkNo) {
        faultOrderDO.setFaultWorkNo(nextFaultWorkNo);
        faultOrderDO.setFaultNo(nextFaultNo);
        faultOrderDO.setDeleteFlag("0");
        faultOrderDO.setRecId(TokenUtil.getUuId());
        faultOrderDO.setRecCreator(TokenUtil.getCurrentPerson().getPersonId());
        faultOrderDO.setRecCreateTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
        faultReportMapper.addToFaultOrder(faultOrderDO);
    }

    @Override
    public Page<FaultReportResDTO> list(FaultReportPageReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        // and d.MAJOR_CODE NOT IN('07','06');
        long startTime = System.nanoTime();
        Page<FaultReportResDTO> list = faultReportMapper.list(reqDTO.of(), reqDTO.getFaultNo(), reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(), reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(), reqDTO.getFillinTimeEnd(), reqDTO.getPositionCode());
        log.info("已提报故障查询耗时{}s", TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime));
        List<FaultReportResDTO> records = list.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new Page<>();
        }
       _buildRes(records);
        return list;
    }

    @Override
    public Page<FaultReportResDTO> carReportList(FaultReportPageReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        // and d.MAJOR_CODE  IN('07','06');
        long startTime = System.nanoTime();
        Page<FaultReportResDTO> list = faultReportMapper.carFaultReportList(reqDTO.of(), reqDTO.getFaultNo(), reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(), reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(), reqDTO.getFillinTimeEnd(), reqDTO.getPositionCode());
        log.info("车辆故障查询耗时-------{}s", TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime));
        List<FaultReportResDTO> records = list.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return new Page<>();
        }
        // (SELECT distinct db.NODE_NAME from SYS_REGION db where db.NODE_CODE=d.POSITION_CODE) as "positionName",
        //         (select d3.NODE_NAME from SYS_REGION d3 where d3.NODE_CODE=d.EXT1) as "stationCode",
        _buildRes(records);
        return list;
    }
    private void _buildRes(List<FaultReportResDTO> records) {
        Set<String> positionCodes = StreamUtil.mapToSet(records, FaultReportResDTO::getPositionCode);
        List<RegionResDTO> regionResDTOS = regionMapper.selectByNodeCodes(positionCodes);
        Map<String, RegionResDTO> regionMap = StreamUtil.toMap(regionResDTOS, RegionResDTO::getNodeCode);
        records.forEach(a -> {
            LineCode line = LineCode.getByCode(a.getLineCode());
            if (StringUtils.isNotEmpty(a.getDocId())) {
                a.setDocFile(fileMapper.selectFileInfo(Arrays.asList(a.getDocId().split(","))));
            }
            a.setLineName(line == null ? a.getLineCode() : line.getDesc());
            if (StringUtils.isNotEmpty(a.getRepairDeptCode())) {
                a.setRepairDeptName(organizationMapper.getNamesById(a.getRepairDeptCode()));
            }
            if (regionMap.containsKey(a.getPositionCode())){
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
        faultOrderDO.setRecRevisor(TokenUtil.getCurrentPersonId());
        faultOrderDO.setRecReviseTime(DateUtil.getCurrentTime());
        // order表作废状态
        faultOrderDO.setOrderStatus(OrderStatus.ZUO_FEI.getCode());
        faultReportMapper.updateFaultOrder(faultOrderDO);
        String faultNo = reqDTO.getFaultNo();
        // info表更新
        FaultInfoDO faultInfoDO = faultQueryMapper.queryOneFaultInfo(faultNo);
        faultInfoDO.setRecReviseTime(DateUtil.getCurrentTime());
        faultInfoDO.setRecRevisor(TokenUtil.getCurrentPersonId());
        faultReportMapper.updateFaultInfo(faultInfoDO);
        // 取消待办
        overTodoService.cancelTodo(reqDTO.getOrderRecId());
    }

    @Override
    public void update(FaultReportReqDTO reqDTO) {
        // 修改已提报故障单  修改时间 修改人， 各属性的值
        FaultInfoDO infoUpdate = BeanUtils.convert(reqDTO, FaultInfoDO.class);
        infoUpdate.setRecRevisor(TokenUtil.getCurrentPersonId());
        infoUpdate.setRecReviseTime(DateUtil.getCurrentTime());
        FaultOrderDO orderUpdate = BeanUtils.convert(reqDTO, FaultOrderDO.class);
        orderUpdate.setRecRevisor(TokenUtil.getCurrentPersonId());
        orderUpdate.setRecReviseTime(DateUtil.getCurrentTime());
        if (StringUtils.isEmpty(reqDTO.getDocId())) {
            // 前端传的是个空值，特殊处理下
            infoUpdate.setDocId(" ");
            orderUpdate.setDocId(" ");
        }
        faultReportMapper.updateFaultInfo(infoUpdate);
        faultReportMapper.updateFaultOrder(orderUpdate);
    }
}

