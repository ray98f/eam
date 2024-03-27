package com.wzmtr.eam.utils.mq;

import com.alibaba.fastjson.JSON;
import com.wzmtr.eam.config.RabbitMqConfig;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.FaultErrorReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFlowReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportOpenReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 故障消费
 * @author  Ray
 * @version 1.0
 * @date 2024/02/19
 */
@Slf4j
@Component
public class FaultReceiver {
    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private FaultQueryMapper faultQueryMapper;

    @Autowired
    private FaultReportMapper faultReportMapper;

    /**
     * 故障队列消费
     * @param fault 故障信息
     */
    @RabbitListener(queues = RabbitMqConfig.FAULT_QUEUE)
    @RabbitHandler
    public void faultProcess(FaultReportOpenReqDTO fault) {
        try {
            if (!Objects.isNull(fault)) {
                FaultReportReqDTO req = new FaultReportReqDTO();
                if (StringUtils.isNotEmpty(fault.getEquipCode())) {
                    EquipmentResDTO equipment = equipmentMapper.getEquipmentDetailByCode(fault.getEquipCode());
                    req = req.toReportReqFromEquipment(equipment);
                }
                req.setCompanyName(fault.getSysName());
                if (StringUtils.isEmpty(fault.getFaultLevel())) {
                    req.setFaultDetail("故障详情：" + fault.getFaultDetail());
                } else {
                    req.setFaultDetail("故障等级：" + fault.getFaultLevel() + "，故障详情：" + fault.getFaultDetail());
                }
                if (StringUtils.isEmpty(fault.getAlamTime())) {
                    req.setDiscoveryTime(DateUtils.getCurrentTime());
                } else {
                    req.setDiscoveryTime(fault.getAlamTime());
                }
                req.setFaultType(fault.getFaultType());
                req.setFaultStatus(fault.getFaultStatus());
                req.setPartCode(fault.getPartCode());
                FaultInfoDO faultInfoDO = req.toFaultInfoInsertDO(req);
                // 来源系统名称填充创建人
                faultInfoDO.setRecCreator(fault.getSysName());
                faultInfoDO.setDiscovererName(fault.getSysName());
                faultInfoDO.setFillinUserName(fault.getSysName());
                insertToFaultInfo(faultInfoDO, fault.getFaultNo());
                FaultOrderDO faultOrderDO = req.toFaultOrderInsertDO(req);
                // 来源系统名称填充创建人
                faultOrderDO.setRecCreator(fault.getSysName());
                insertToFaultOrder(faultOrderDO, fault.getFaultNo(), fault.getFaultWorkNo());
                addFaultFlow(fault.getFaultNo(), fault.getFaultWorkNo());
            }
        } catch (Exception e) {
            faultReportMapper.addFaultError(buildFaultError(fault, e.getMessage()));
            log.error("故障提报失败：" + e.getMessage());
        }
    }

    /**
     * 新增故障数据
     * @param faultInfo 故障信息
     * @param faultNo 故障编号
     */
    private void insertToFaultInfo(FaultInfoDO faultInfo, String faultNo) {
        faultInfo.setFaultNo(faultNo);
        faultInfo.setRecId(TokenUtils.getUuId());
        faultInfo.setDeleteFlag("0");
        faultInfo.setFillinTime(DateUtils.getCurrentTime());
        faultInfo.setFillinDeptCode(TokenUtils.getCurrentPerson().getOfficeId());
        faultInfo.setRecCreateTime(DateUtils.getCurrentTime());
        faultReportMapper.addToFaultInfo(faultInfo);
    }

    /**
     * 新增故障工单数据
     * @param faultOrder 故障工单信息
     * @param faultNo 故障编号
     * @param faultWorkNo 故障工单编号
     */
    private void insertToFaultOrder(FaultOrderDO faultOrder, String faultNo, String faultWorkNo) {
        faultOrder.setFaultWorkNo(faultWorkNo);
        faultOrder.setFaultNo(faultNo);
        faultOrder.setDeleteFlag("0");
        faultOrder.setRecId(TokenUtils.getUuId());
        faultOrder.setRecCreateTime(DateUtils.getCurrentTime());
        faultReportMapper.addToFaultOrder(faultOrder);

    }

    /**
     * 新增工单流程
     * @param faultNo 故障编号
     * @param faultWorkNo 故障工单编号
     */
    private void addFaultFlow(String faultNo, String faultWorkNo) {
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

    /**
     * 故障错误信息拼装
     * @param fault 故障信息
     * @return 故障错误信息
     */
    private FaultErrorReqDTO buildFaultError(FaultReportOpenReqDTO fault, String msg) {
        FaultErrorReqDTO faultError = new FaultErrorReqDTO();
        faultError.setRecId(TokenUtils.getUuId());
        faultError.setFaultNo(fault.getFaultNo());
        faultError.setFaultWorkNo(fault.getFaultWorkNo());
        faultError.setFaultInfo(JSON.toJSONString(fault));
        faultError.setErrorMsg(msg);
        faultError.setRecCreator(TokenUtils.getCurrentPersonId());
        faultError.setRecCreateTime(DateUtils.getCurrentTime());
        return faultError;
    }

}