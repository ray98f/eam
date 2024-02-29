package com.wzmtr.eam.impl.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.DetectionPlanDetailDO;
import com.wzmtr.eam.dto.req.detection.DetectionPlanDetailReqDTO;
import com.wzmtr.eam.dto.req.detection.DetectionPlanReqDTO;
import com.wzmtr.eam.dto.res.detection.DetectionPlanDetailResDTO;
import com.wzmtr.eam.dto.res.detection.DetectionPlanResDTO;
import com.wzmtr.eam.dto.res.detection.excel.ExcelDetectionPlanDetailResDTO;
import com.wzmtr.eam.dto.res.detection.excel.ExcelDetectionPlanResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.BpmnStatus;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.detection.DetectionPlanDetailMapper;
import com.wzmtr.eam.mapper.detection.DetectionPlanMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.service.detection.DetectionPlanService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class DetectionPlanServiceImpl implements DetectionPlanService {

    @Autowired
    private DetectionPlanMapper detectionPlanMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private BpmnService bpmnService;
    @Autowired
    private DetectionPlanDetailMapper detectionPlanDetailMapper;

    @Autowired
    private IWorkFlowLogService workFlowLogService;

    @Override
    public Page<DetectionPlanResDTO> pageDetectionPlan(String instrmPlanNo, String planStatus, String editDeptCode,
                                                       String assetKindCode, String planPeriodMark, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<DetectionPlanResDTO> page = detectionPlanMapper.pageDetectionPlan(pageReqDTO.of(), instrmPlanNo, planStatus, editDeptCode, assetKindCode, planPeriodMark);
        List<DetectionPlanResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (DetectionPlanResDTO resDTO : list) {
                resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
                resDTO.setSecOrgName(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
                resDTO.setEditDeptName(organizationMapper.getExtraOrgByAreaId(resDTO.getEditDeptCode()));
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public DetectionPlanResDTO getDetectionPlanDetail(String id) {
        DetectionPlanResDTO resDTO = detectionPlanMapper.getDetectionPlanDetail(id);
        if (Objects.isNull(resDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
        resDTO.setSecOrgName(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
        resDTO.setEditDeptName(organizationMapper.getExtraOrgByAreaId(resDTO.getEditDeptCode()));
        return resDTO;
    }

    @Override
    public void addDetectionPlan(DetectionPlanReqDTO specialEquipReqDTO) {
        specialEquipReqDTO.setRecId(TokenUtils.getUuId());
        specialEquipReqDTO.setArchiveFlag("0");
        specialEquipReqDTO.setPlanStatus("10");
        specialEquipReqDTO.setEditDeptCode(TokenUtils.getCurrentPerson().getOfficeAreaId() == null ? " " : TokenUtils.getCurrentPerson().getOfficeAreaId());
        specialEquipReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        specialEquipReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        String instrmPlanNo = detectionPlanMapper.getMaxCode();
        if (StringUtils.isEmpty(instrmPlanNo) || !(CommonConstants.TWENTY_STRING + instrmPlanNo.substring(CommonConstants.TWO, CommonConstants.EIGHT)).equals(DateUtils.getNoDate())) {
            instrmPlanNo = "TP" + DateUtils.getNoDate().substring(2) + "0001";
        } else {
            instrmPlanNo = CodeUtils.getNextCode(instrmPlanNo, 8);
        }
        specialEquipReqDTO.setInstrmPlanNo(instrmPlanNo);
        if (StringUtils.isEmpty(specialEquipReqDTO.getPlanPeriodMark())) {
            specialEquipReqDTO.setPlanPeriodMark(" ");
        }
        detectionPlanMapper.addDetectionPlan(specialEquipReqDTO);
    }

    @Override
    public void modifyDetectionPlan(DetectionPlanReqDTO specialEquipReqDTO) {
        DetectionPlanResDTO resDTO = detectionPlanMapper.getDetectionPlanDetail(specialEquipReqDTO.getRecId());
        if (Objects.isNull(resDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!resDTO.getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        if (!specialEquipReqDTO.getAssetKindCode().equals(resDTO.getAssetKindCode()) &&
                !detectionPlanMapper.hasDetail(specialEquipReqDTO.getInstrmPlanNo()).isEmpty()) {
            throw new CommonException(ErrorCode.PLAN_HAS_DETAIL);
        }
        specialEquipReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        specialEquipReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        detectionPlanMapper.modifyDetectionPlan(specialEquipReqDTO);
    }

    @Override
    public void deleteDetectionPlan(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                DetectionPlanResDTO resDTO = detectionPlanMapper.getDetectionPlanDetail(id);
                if (!resDTO.getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                detectionPlanMapper.deleteDetectionPlanDetail(resDTO.getInstrmPlanNo(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
                detectionPlanMapper.deleteDetectionPlan(id, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void submitDetectionPlan(DetectionPlanReqDTO detectionPlanReqDTO) throws Exception {
        // ServiceDMSE0001
        DetectionPlanResDTO res = detectionPlanMapper.getDetectionPlanDetail(detectionPlanReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<DetectionPlanDetailResDTO> result = detectionPlanMapper.listDetectionPlanDetail(res.getInstrmPlanNo());
        if (result.isEmpty()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "此计划不存在计划明细，无法提交");
        }
        if (!CommonConstants.TEN_STRING.equals(res.getPlanStatus())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "非编辑状态无法提交");
        } else {
            String processId = bpmnService.commit(res.getInstrmPlanNo(), BpmnFlowEnum.DETECTION_PLAN_SUBMIT.value(), null, null, detectionPlanReqDTO.getExamineReqDTO().getUserIds(), null);
            if (processId == null || CommonConstants.PROCESS_ERROR_CODE.equals(processId)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败");
            }
            DetectionPlanReqDTO reqDTO = new DetectionPlanReqDTO();
            BeanUtils.copyProperties(res, reqDTO);
            reqDTO.setWorkFlowInstId(processId);
            reqDTO.setWorkFlowInstStatus(roleMapper.getSubmitNodeId(BpmnFlowEnum.DETECTION_PLAN_SUBMIT.value(), null));
            reqDTO.setPlanStatus("20");
            reqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
            reqDTO.setRecReviseTime(DateUtils.getCurrentTime());
            detectionPlanMapper.modifyDetectionPlan(reqDTO);
            // 记录日志
            workFlowLogService.add(WorkFlowLogBO.builder()
                    .status(BpmnStatus.SUBMIT.getDesc())
                    .userIds(detectionPlanReqDTO.getExamineReqDTO().getUserIds())
                    .workFlowInstId(processId)
                    .build());
        }
    }

    @Override
    public void examineDetectionPlan(DetectionPlanReqDTO detectionPlanReqDTO) {
        Assert.notNull(detectionPlanReqDTO.getExamineReqDTO(),ErrorCode.PARAM_ERROR);
        DetectionPlanResDTO res = detectionPlanMapper.getDetectionPlanDetail(detectionPlanReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        DetectionPlanReqDTO reqDTO = new DetectionPlanReqDTO();
        BeanUtils.copyProperties(res, reqDTO);
        workFlowLogService.ifReviewer(res.getWorkFlowInstId());
        if (detectionPlanReqDTO.getExamineReqDTO().getExamineStatus() == 0) {
            if (CommonConstants.THIRTY_STRING.equals(reqDTO.getPlanStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_DONE);
            }
            String processId = res.getWorkFlowInstId();
            String taskId = bpmnService.queryTaskIdByProcId(processId);
            if (roleMapper.getNodeIdsByFlowId(BpmnFlowEnum.DETECTION_PLAN_SUBMIT.value()).contains(reqDTO.getWorkFlowInstStatus())) {
                bpmnService.agree(taskId, detectionPlanReqDTO.getExamineReqDTO().getOpinion(), String.join(",", detectionPlanReqDTO.getExamineReqDTO().getUserIds()), "{\"id\":\"" + res.getInstrmPlanNo() + "\"}", null);
                reqDTO.setWorkFlowInstStatus(bpmnService.getNextNodeId(BpmnFlowEnum.DETECTION_PLAN_SUBMIT.value(), reqDTO.getWorkFlowInstStatus()));
                reqDTO.setPlanStatus("20");
            } else {
                bpmnService.agree(taskId, detectionPlanReqDTO.getExamineReqDTO().getOpinion(), null, "{\"id\":\"" + res.getInstrmPlanNo() + "\"}", null);
                reqDTO.setWorkFlowInstStatus("已完成");
                reqDTO.setPlanStatus("30");
            }
            // 记录日志
            workFlowLogService.add(WorkFlowLogBO.builder()
                    .status(BpmnStatus.PASS.getDesc())
                    .userIds(detectionPlanReqDTO.getExamineReqDTO().getUserIds())
                    .workFlowInstId(processId)
                    .build());
        } else {
            if (!CommonConstants.TWENTY_STRING.equals(reqDTO.getPlanStatus())) {
                throw new CommonException(ErrorCode.REJECT_ERROR);
            } else {
                String processId = res.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.reject(taskId, detectionPlanReqDTO.getExamineReqDTO().getOpinion());
                reqDTO.setWorkFlowInstId("");
                reqDTO.setWorkFlowInstStatus("");
                reqDTO.setPlanStatus("10");
                // 记录日志
                workFlowLogService.add(WorkFlowLogBO.builder()
                        .status(BpmnStatus.REJECT.getDesc())
                        .userIds(detectionPlanReqDTO.getExamineReqDTO().getUserIds())
                        .workFlowInstId(processId)
                        .build());
            }
        }
        reqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        reqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        detectionPlanMapper.modifyDetectionPlan(reqDTO);
    }

    @Override
    public void exportDetectionPlan(String instrmPlanNo, String planStatus, String editDeptCode,
                                    String assetKindCode, String planPeriodMark, HttpServletResponse response) throws IOException {
        List<DetectionPlanResDTO> detectionPlanResDTOList = detectionPlanMapper.listDetectionPlan(instrmPlanNo, planStatus, editDeptCode, assetKindCode, planPeriodMark);
        if (detectionPlanResDTOList != null && !detectionPlanResDTOList.isEmpty()) {
            List<ExcelDetectionPlanResDTO> list = new ArrayList<>();
            for (DetectionPlanResDTO resDTO : detectionPlanResDTOList) {
                ExcelDetectionPlanResDTO res = new ExcelDetectionPlanResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setManageOrg(organizationMapper.getExtraOrgByAreaId(resDTO.getManageOrg()));
                res.setSecOrg(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
                res.setEditDeptCode(organizationMapper.getExtraOrgByAreaId(resDTO.getEditDeptCode()));
                list.add(res);
            }
            EasyExcelUtils.export(response, "检测计划信息", list);
        }
    }

    @Override
    public Page<DetectionPlanDetailResDTO> pageDetectionPlanDetail(String instrmPlanNo, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return detectionPlanMapper.pageDetectionPlanDetail(pageReqDTO.of(), instrmPlanNo);
    }

    @Override
    public DetectionPlanDetailResDTO getDetectionPlanDetailDetail(String id) {
        return detectionPlanMapper.getDetectionPlanDetailDetail(id);
    }

    @Override
    public void addDetectionPlanDetail(DetectionPlanDetailReqDTO detectionPlanDetailReqDTO) {
        List<DetectionPlanResDTO> list = detectionPlanMapper.listDetectionPlan(detectionPlanDetailReqDTO.getInstrmPlanNo(), null, null, null, null);
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        DetectionPlanResDTO resDTO = list.get(0);
        if (!resDTO.getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        detectionPlanDetailReqDTO.setRecId(TokenUtils.getUuId());
        detectionPlanDetailReqDTO.setArchiveFlag("0");
        detectionPlanDetailReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        detectionPlanDetailReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        detectionPlanMapper.addDetectionPlanDetail(detectionPlanDetailReqDTO);
    }

    @Override
    public void modifyDetectionPlanDetail(DetectionPlanDetailReqDTO detectionPlanDetailReqDTO) {
        List<DetectionPlanResDTO> list = detectionPlanMapper.listDetectionPlan(detectionPlanDetailReqDTO.getInstrmPlanNo(), null, null, null, null);
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        DetectionPlanResDTO resDTO = list.get(0);
        if (!resDTO.getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        detectionPlanDetailReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        detectionPlanDetailReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        detectionPlanMapper.modifyDetectionPlanDetail(detectionPlanDetailReqDTO);
    }

    @Override
    public void deleteDetectionPlanDetail(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                DetectionPlanDetailResDTO detailResDTO = detectionPlanMapper.getDetectionPlanDetailDetail(id);
                if (Objects.isNull(detailResDTO)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                List<DetectionPlanResDTO> list = detectionPlanMapper.listDetectionPlan(detailResDTO.getInstrmPlanNo(), null, null, null, null);
                if (Objects.isNull(list) || list.isEmpty()) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                DetectionPlanResDTO resDTO = list.get(0);
                if (!resDTO.getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                DetectionPlanDetailDO detectionPlanDetailDO = new DetectionPlanDetailDO();
                detectionPlanDetailDO.setRecId(id).setDeleteFlag(CommonConstants.ONE_STRING).setRecDeletor(TokenUtils.getCurrentPersonId()).setRecDeleteTime(DateUtils.getCurrentTime());
                detectionPlanDetailMapper.updateById(detectionPlanDetailDO);
                // detectionPlanMapper.deleteDetectionPlanDetail(resDTO.getInstrmPlanNo(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportDetectionPlanDetail(String instrmPlanNo, HttpServletResponse response) throws IOException {
        List<DetectionPlanDetailResDTO> detectionPlanDetailResDTOList = detectionPlanMapper.listDetectionPlanDetail(instrmPlanNo);
        if (detectionPlanDetailResDTOList != null && !detectionPlanDetailResDTOList.isEmpty()) {
            List<ExcelDetectionPlanDetailResDTO> list = new ArrayList<>();
            for (DetectionPlanDetailResDTO resDTO : detectionPlanDetailResDTOList) {
                ExcelDetectionPlanDetailResDTO res = new ExcelDetectionPlanDetailResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "检测计划明细信息", list);
        }
    }
}
