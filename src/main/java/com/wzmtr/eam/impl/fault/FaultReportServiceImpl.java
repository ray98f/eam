package com.wzmtr.eam.impl.fault;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import com.wzmtr.eam.enums.OrderStatus;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.fault.FaultInfoMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.fault.FaultQueryService;
import com.wzmtr.eam.service.fault.FaultReportService;
import com.wzmtr.eam.service.fault.TrackQueryService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.DateUtil;
import com.wzmtr.eam.utils.TokenUtil;
import com.wzmtr.eam.utils.__BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
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
    @Override
    // @Transactional(rollbackFor = Exception.class)
    public void addToEquip(FaultReportReqDTO reqDTO) {
        String maxFaultNo = faultReportMapper.getFaultInfoFaultNoMaxCode();
        String maxFaultWorkNo = faultReportMapper.getFaultOrderFaultWorkNoMaxCode();
        // 获取AOP代理对象
        // FaultReportServiceImpl aop = (FaultReportServiceImpl) AopContext.currentProxy();
        FaultInfoDO faultInfoDO = reqDTO.toFaultInfoInsertDO(reqDTO);
        String nextFaultNo = CodeUtils.getNextCode(maxFaultNo, "GZ");
        _insertToFaultInfo(faultInfoDO, nextFaultNo);
        FaultOrderDO faultOrderDO = reqDTO.toFaultOrderInsertDO(reqDTO);
        _insertToFaultOrder(faultOrderDO, nextFaultNo, maxFaultWorkNo);
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

    private void _insertToFaultInfo(FaultInfoDO faultInfoDO, String nextFaultNo) {
        faultInfoDO.setFaultNo(nextFaultNo);
        faultReportMapper.addToFaultInfo(faultInfoDO);
    }

    private void _insertToFaultOrder(FaultOrderDO faultOrderDO, String nextFaultNo, String maxFaultWorkNo) {
        String nextFaultWorkNo = CodeUtils.getNextCode(maxFaultWorkNo, "GD");
        faultOrderDO.setFaultWorkNo(nextFaultWorkNo);
        faultOrderDO.setFaultNo(nextFaultNo);
        faultReportMapper.addToFaultOrder(faultOrderDO);
    }

    @Override
    public Page<FaultReportResDTO> list(FaultReportPageReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNo(), reqDTO.getPageSize());
        // and d.MAJOR_CODE NOT IN('07','06')
        Page<FaultReportResDTO> list = faultReportMapper.list(reqDTO.of(), reqDTO.getFaultNo(), reqDTO.getObjectCode(), reqDTO.getObjectName(), reqDTO.getFaultModule(), reqDTO.getMajorCode(), reqDTO.getSystemCode(), reqDTO.getEquipTypeCode(), reqDTO.getFillinTimeStart(), reqDTO.getFillinTimeEnd());
        if (CollectionUtil.isEmpty(list.getRecords())) {
            return new Page<>();
        }
        List<FaultReportResDTO> filter = list.getRecords().stream()
                .filter(a -> !a.getMajorCode().equals("07") && !a.getMajorCode().equals("06"))
                .sorted(Comparator.comparing(FaultReportResDTO::getFaultNo).reversed()).collect(Collectors.toList());
        filter.forEach(a -> {
            a.setRepairDeptName(organizationMapper.getExtraOrgByAreaId(a.getRepairDeptCode()));
            a.setFillinDeptName(organizationMapper.getOrgById(a.getFillinDeptCode()));
        });
        return list;
    }


    @Override
    public void addToMajor(FaultReportToMajorReqDTO reqDTO) {
        String maxFaultNo = faultReportMapper.getFaultInfoFaultNoMaxCode();
        String maxFaultWorkNo = faultReportMapper.getFaultOrderFaultWorkNoMaxCode();
        FaultInfoDO faultInfoDO = __BeanUtil.convert(reqDTO, FaultInfoDO.class);
        faultInfoDO.setRecId(TokenUtil.getUuId());
        faultInfoDO.setRecCreator(TokenUtil.getCurrentPerson().getPersonId());
        faultInfoDO.setRecCreateTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
        _insertToFaultInfo(faultInfoDO, maxFaultNo);
        FaultOrderDO faultOrderDO = __BeanUtil.convert(reqDTO, FaultOrderDO.class);
        faultOrderDO.setRecId(TokenUtil.getUuId());
        if (reqDTO.getRepairDeptCode() != null) {
            faultOrderDO.setWorkClass(reqDTO.getRepairDeptCode());
        }
        faultOrderDO.setRecCreator(TokenUtil.getCurrentPerson().getPersonId());
        faultOrderDO.setRecCreateTime(DateUtil.current(DateUtil.YYYY_MM_DD_HH_MM_SS));
        _insertToFaultOrder(faultOrderDO, maxFaultNo, maxFaultWorkNo);
    }

    @Override
    public FaultDetailResDTO detail(FaultDetailReqDTO reqDTO) {
        return trackQueryService.faultDetail(reqDTO);
    }

    @Override
    public void delete(FaultCancelReqDTO reqDTO) {
        // 已提报故障单撤销 逻辑删 涉及faultinfo和faultorder两张表
        faultReportMapper.cancelOrder(reqDTO);
        faultReportMapper.cancelInfo(reqDTO);
    }

    @Override
    public void cancel(FaultCancelReqDTO reqDTO) {
        //faultWorkNo的recId
        String faultWorkNo = reqDTO.getFaultWorkNo();
        FaultOrderDO faultOrderDO = faultQueryMapper.queryOneFaultOrder(null,faultWorkNo);
        faultOrderDO.setRecRevisor(TokenUtil.getCurrentPersonId());
        faultOrderDO.setRecReviseTime(DateUtil.getCurrentTime());
        //order表作废状态
        faultOrderDO.setOrderStatus(OrderStatus.ZUO_FEI.getCode());
        faultReportMapper.updateFaultOrder(faultOrderDO);
        String faultNo = reqDTO.getFaultNo();
        //info表更新
        FaultInfoDO faultInfoDO = faultQueryMapper.queryOneFaultInfo(faultNo);
        faultInfoDO.setRecReviseTime(DateUtil.getCurrentTime());
        faultInfoDO.setRecRevisor(TokenUtil.getCurrentPersonId());
        faultReportMapper.updateFaultInfo(faultInfoDO);
        //取消待办
        overTodoService.cancelTODO(reqDTO.getOrderRecId());
    }

    @Override
    public void update(FaultReportReqDTO reqDTO) {
        // 修改已提报故障单  修改时间 修改人， 各属性的值
        FaultInfoDO infoUpdate = __BeanUtil.convert(reqDTO, FaultInfoDO.class);
        infoUpdate.setRecRevisor(TokenUtil.getCurrentPersonId());
        infoUpdate.setRecReviseTime(DateUtil.getCurrentTime());
        faultReportMapper.updateFaultInfo(infoUpdate);
        FaultOrderDO orderUpdate = __BeanUtil.convert(reqDTO, FaultOrderDO.class);
        orderUpdate.setRecRevisor(TokenUtil.getCurrentPersonId());
        orderUpdate.setRecReviseTime(DateUtil.getCurrentTime());
        faultReportMapper.updateFaultOrder(orderUpdate);
    }



    public static void main(String[] args) {
        System.out.println(DateUtil.getCurrentTime());
    }
}

