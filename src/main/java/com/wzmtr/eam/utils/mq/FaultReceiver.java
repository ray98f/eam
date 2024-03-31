package com.wzmtr.eam.utils.mq;

import com.alibaba.fastjson.JSON;
import com.wzmtr.eam.config.RabbitMqConfig;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.FaultInfoDO;
import com.wzmtr.eam.dataobject.FaultOrderDO;
import com.wzmtr.eam.dto.req.fault.FaultErrorReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultFlowReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportOpenReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
import com.wzmtr.eam.dto.res.basic.OrgMajorResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.OrderStatus;
import com.wzmtr.eam.mapper.basic.OrgMajorMapper;
import com.wzmtr.eam.mapper.common.PersonMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.fault.FaultQueryMapper;
import com.wzmtr.eam.mapper.fault.FaultReportMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.shiro.model.Person;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 故障消费
 *
 * @author Ray
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
    private static final List<String> zcList = Arrays.asList("07", "06");
    @Autowired
    private OverTodoService overTodoService;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private OrgMajorMapper orgMajorMapper;

    /**
     * 故障队列消费
     *
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
                String majorCode = req.getMajorCode();
                FaultOrderDO faultOrderDO = req.toFaultOrderInsertDO(req);
                String faultWorkNo = fault.getFaultWorkNo();
                // 来源系统名称填充创建人
                faultOrderDO.setRecCreator(fault.getSysName());
                insertToFaultOrder(faultOrderDO, fault.getFaultNo(), faultWorkNo);

                // 中铁通的直接变更为派工状态并发送待办给工班人员
                zttSendOverTodo(majorCode, req, faultInfoDO, faultOrderDO, faultWorkNo);

                addFaultFlow(fault.getFaultNo(), faultWorkNo);
            }
        } catch (Exception e) {
            faultReportMapper.addFaultError(buildFaultError(fault, e.getMessage()));
            log.error("故障提报失败：" + e.getMessage());
        }
    }

    private void zttSendOverTodo(String majorCode, FaultReportReqDTO req, FaultInfoDO faultInfoDO, FaultOrderDO faultOrderDO, String faultWorkNo) {
        if (!zcList.contains(majorCode)) {
            String positionCode = req.getPositionCode();
            if (StringUtils.isNotEmpty(positionCode) && StringUtils.isNotEmpty(majorCode)) {
                // 专业和位置查维修部门
                OrgMajorResDTO organ = orgMajorMapper.getOrganByStationAndMajor(positionCode, majorCode);
                faultInfoDO.setRepairDeptCode(organ.getOrgCode());
                // 负责人为中铁通工班长角色
                Person person = personMapper.searchLeader(majorCode, positionCode, "DM_051");
                faultOrderDO.setRepairRespUserId(person.getLoginName());
                // 默认为紧急
                faultInfoDO.setFaultLevel("01");
                faultOrderDO.setOrderStatus(OrderStatus.PAI_GONG.getCode());
                faultInfoDO.setRecRevisor(TokenUtils.getCurrentPersonId());
                faultInfoDO.setRecReviseTime(DateUtils.getCurrentTime());
                faultOrderDO.setRecRevisor(TokenUtils.getCurrentPersonId());
                faultOrderDO.setRecReviseTime(DateUtils.getCurrentTime());
                faultReportMapper.updateFaultOrder(faultOrderDO);
                faultReportMapper.updateFaultInfo(faultInfoDO);
                overTodoService.insertTodoWithUserGroup(String.format(CommonConstants.TODO_GD_TPL,faultWorkNo,"故障"), faultOrderDO.getRecId(), faultWorkNo, organ.getOrgCode(), "故障派工", "?", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.FAULT_REPORT_QUERY.value());
            }
        }
    }

    /**
     * 新增故障数据
     *
     * @param faultInfo 故障信息
     * @param faultNo   故障编号
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
     *
     * @param faultOrder  故障工单信息
     * @param faultNo     故障编号
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
     *
     * @param faultNo     故障编号
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
     *
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