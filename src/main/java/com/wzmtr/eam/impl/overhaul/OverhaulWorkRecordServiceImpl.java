package com.wzmtr.eam.impl.overhaul;

import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.overhaul.OverhaulOrderReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulWorkRecordReqDTO;
import com.wzmtr.eam.dto.res.bpmn.BpmnExaminePersonRes;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulWorkRecordMapper;
import com.wzmtr.eam.service.bpmn.OverTodoService;
import com.wzmtr.eam.service.overhaul.OverhaulWorkRecordService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author frp
 */
@Service
@Slf4j
public class OverhaulWorkRecordServiceImpl implements OverhaulWorkRecordService {

    @Autowired
    private OverhaulWorkRecordMapper overhaulWorkRecordMapper;

    @Autowired
    private OverTodoService overTodoService;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void insertRepair(OverhaulOrderReqDTO overhaulOrderReqDTO) {
        // ExamineRepairOrder insertRepair DMUtil.overTODO
        overTodoService.overTodo(overhaulOrderReqDTO.getRecId(), "", CommonConstants.ONE_STRING);
        String workCode = overhaulOrderReqDTO.getWorkerCode();
        overhaulWorkRecordMapper.deleteByOrderCode(overhaulOrderReqDTO);
        if (StringUtils.isNotEmpty(workCode)) {
            String[] workerCodes = workCode.split(",");
            if (StringUtils.isNotEmpty(workerCodes)) {
                for (String workerCode : workerCodes) {
                    // 新增工作记录
                    insertWorkRecord(overhaulOrderReqDTO, workerCode);
                    // 流程流转
                    try {
                        if (CommonConstants.TWO_STRING.equals(overhaulOrderReqDTO.getWorkStatus())) {
                            overTodoService.insertTodo(String.format(CommonConstants.TODO_GD_TPL, overhaulOrderReqDTO.getOrderCode(), "检修"),
                                    overhaulOrderReqDTO.getRecId(), overhaulOrderReqDTO.getOrderCode(), workerCode,
                                    "检修工单分配", "DMER0200", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.OVERHAUL_ORDER.value());
                        } else if (CommonConstants.ONE_STRING.equals(overhaulOrderReqDTO.getWorkStatus())) {
                            // 根据角色获取用户列表
                            List<BpmnExaminePersonRes> userList = roleMapper.getUserBySubjectAndLineAndRole(overhaulOrderReqDTO.getSubjectCode(),
                                    overhaulOrderReqDTO.getLineNo(), CommonConstants.DM_007);
                            for (BpmnExaminePersonRes map : userList) {
                                overTodoService.insertTodo(String.format(CommonConstants.TODO_GD_TPL, overhaulOrderReqDTO.getOrderCode(), "检修"),
                                        overhaulOrderReqDTO.getRecId(), overhaulOrderReqDTO.getOrderCode(), map.getUserId(),
                                        "检修工单下达", "DMER0200", TokenUtils.getCurrentPersonId(), BpmnFlowEnum.OVERHAUL_ORDER.value());
                            }
                        }
                    } catch (Exception e) {
                        log.error("exception message", e);
                    }
                }
            }
        }
    }

    /**
     * 新增工作记录
     * @param overhaulOrderReqDTO 工单参数
     * @param workerCode 作业人员
     */
    public void insertWorkRecord(OverhaulOrderReqDTO overhaulOrderReqDTO, String workerCode) {
        OverhaulWorkRecordReqDTO workRecord = new OverhaulWorkRecordReqDTO();
        workRecord.setRecId(TokenUtils.getUuId());
        workRecord.setOrderCode(overhaulOrderReqDTO.getOrderCode());
        workRecord.setPlanCode(overhaulOrderReqDTO.getPlanCode());
        workRecord.setWorkerGroupCode(overhaulOrderReqDTO.getWorkerGroupCode());
        workRecord.setWorkerCode(workerCode);
        workRecord.setUploadTime(DateUtils.getCurrentTime());
        workRecord.setDownloadTime(overhaulOrderReqDTO.getSendPersonId());
        workRecord.setExt5(overhaulOrderReqDTO.getSendTime());
        //TODO 20240331 先注释这段 EXT1字段的含义未知
        /*String[] workerMsg = workerNames[i].split("-");
        workRecord.setWorkerName(workerMsg[0]);
        if (workerMsg.length > 1) {
            workRecord.setExt1(workerMsg[1]);
        }*/
        overhaulWorkRecordMapper.insert(workRecord);
    }

}
