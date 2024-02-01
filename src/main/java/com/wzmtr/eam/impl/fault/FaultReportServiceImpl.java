package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.FaultCancelReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportPageReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.enums.LineCode;
import com.wzmtr.eam.enums.OrderStatus;
import com.wzmtr.eam.exception.CommonException;
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
    public String addToFault(FaultReportReqDTO reqDTO) {
        String maxFaultNo = faultReportMapper.getFaultInfoFaultNoMaxCode();
        String maxFaultWorkNo = faultReportMapper.getFaultOrderFaultWorkNoMaxCode();
        // 获取AOP代理对象
        // FaultReportServiceImpl aop = (FaultReportServiceImpl) AopContext.currentProxy();
        FaultInfoDO faultInfoDO = reqDTO.toFaultInfoInsertDO(reqDTO);
        String nextFaultNo = CodeUtils.getNextCode(maxFaultNo, "GZ");
        insertToFaultInfo(faultInfoDO, nextFaultNo);
        FaultOrderDO faultOrderDO = reqDTO.toFaultOrderInsertDO(reqDTO);
        String nextFaultWorkNo = CodeUtils.getNextCode(maxFaultWorkNo, "GD");
        insertToFaultOrder(faultOrderDO, nextFaultNo, nextFaultWorkNo);
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

    public void insertToFaultInfo(FaultInfoDO faultInfoDO, String nextFaultNo) {
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

    public void insertToFaultOrder(FaultOrderDO faultOrderDO, String nextFaultNo, String nextFaultWorkNo) {
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
            PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
            Page<FaultReportResDTO> list = faultReportMapper.list(reqDTO.of(), reqDTO.getFaultNo(),
                    reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(),
                    reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(),
                    reqDTO.getFillinTimeEnd(), reqDTO.getPositionCode(), reqDTO.getOrderStatus(),reqDTO.getFaultWorkNo(),reqDTO.getLineCode());
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
        Set<String> positionCodes = StreamUtil.mapToSet(records, FaultReportResDTO::getPositionCode);
        List<RegionResDTO> regionRes = regionMapper.selectByNodeCodes(positionCodes);
        Map<String, RegionResDTO> regionMap = StreamUtil.toMap(regionRes, RegionResDTO::getNodeCode);
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
        faultOrderDO.setRecRevisor(TokenUtil.getCurrentPersonId());
        faultOrderDO.setRecReviseTime(DateUtil.getCurrentTime());
        // order表作废状态
        faultOrderDO.setOrderStatus(OrderStatus.ZUO_FEI.getCode());
        faultReportMapper.updateFaultOrder(faultOrderDO);
        String faultNo = reqDTO.getFaultNo();
        // info表更新
        FaultInfoDO faultInfoDO = faultQueryMapper.queryOneFaultInfo(faultNo, faultWorkNo);
        faultInfoDO.setRecReviseTime(DateUtil.getCurrentTime());
        faultInfoDO.setRecRevisor(TokenUtil.getCurrentPersonId());
        faultReportMapper.updateFaultInfo(faultInfoDO);
        // 取消待办
        overTodoService.cancelTodo(reqDTO.getOrderRecId());
    }

    @Override
    public void update(FaultReportReqDTO reqDTO) {
        Assert.isNotEmpty(reqDTO.getFaultNo(), "参数缺失[故障编号]不能为空!");
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
}

