package com.wzmtr.eam.impl.specialEquip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dataobject.DetectionPlanDetailDO;
import com.wzmtr.eam.dto.req.specialEquip.DetectionPlanDetailReqDTO;
import com.wzmtr.eam.dto.req.specialEquip.DetectionPlanReqDTO;
import com.wzmtr.eam.dto.res.specialEquip.DetectionPlanDetailResDTO;
import com.wzmtr.eam.dto.res.specialEquip.DetectionPlanResDTO;
import com.wzmtr.eam.dto.res.specialEquip.excel.ExcelDetectionPlanDetailResDTO;
import com.wzmtr.eam.dto.res.specialEquip.excel.ExcelDetectionPlanResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.specialEquip.DetectionPlanDetailMapper;
import com.wzmtr.eam.mapper.specialEquip.DetectionPlanMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.specialEquip.DetectionPlanService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

    @Override
    public Page<DetectionPlanResDTO> pageDetectionPlan(String instrmPlanNo, String planStatus, String editDeptCode,
                                                       String assetKindCode, String planPeriodMark, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<DetectionPlanResDTO> page = detectionPlanMapper.pageDetectionPlan(pageReqDTO.of(), instrmPlanNo, planStatus, editDeptCode, assetKindCode, planPeriodMark);
        List<DetectionPlanResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
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
        SimpleDateFormat min = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        specialEquipReqDTO.setRecId(TokenUtil.getUuId());
        specialEquipReqDTO.setArchiveFlag("0");
        specialEquipReqDTO.setPlanStatus("10");
        specialEquipReqDTO.setEditDeptCode(TokenUtil.getCurrentPerson().getOfficeAreaId() == null ? " " : TokenUtil.getCurrentPerson().getOfficeAreaId());
        specialEquipReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        specialEquipReqDTO.setRecCreateTime(min.format(System.currentTimeMillis()));
        String instrmPlanNo = detectionPlanMapper.getMaxCode();
        if (StringUtils.isEmpty(instrmPlanNo) || !(CommonConstants.TWENTY_STRING + instrmPlanNo.substring(CommonConstants.TWO, CommonConstants.EIGHT)).equals(day.format(System.currentTimeMillis()))) {
            instrmPlanNo = "TP" + day.format(System.currentTimeMillis()).substring(2) + "0001";
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
        if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        if (!specialEquipReqDTO.getAssetKindCode().equals(resDTO.getAssetKindCode())) {
            if (detectionPlanMapper.hasDetail(specialEquipReqDTO.getInstrmPlanNo()).size() != 0) {
                throw new CommonException(ErrorCode.PLAN_HAS_DETAIL);
            }
        }
        specialEquipReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        specialEquipReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        detectionPlanMapper.modifyDetectionPlan(specialEquipReqDTO);
    }

    @Override
    public void deleteDetectionPlan(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                DetectionPlanResDTO resDTO = detectionPlanMapper.getDetectionPlanDetail(id);
                if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                detectionPlanMapper.deleteDetectionPlanDetail(resDTO.getInstrmPlanNo(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                detectionPlanMapper.deleteDetectionPlan(id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    // ServiceDMSE0001
    @Override
    public void submitDetectionPlan(DetectionPlanReqDTO detectionPlanReqDTO) throws Exception {
        DetectionPlanResDTO res = detectionPlanMapper.getDetectionPlanDetail(detectionPlanReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        List<DetectionPlanDetailResDTO> result = detectionPlanMapper.listDetectionPlanDetail(res.getInstrmPlanNo());
        if (result.size() == 0) {
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
            reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
            reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            detectionPlanMapper.modifyDetectionPlan(reqDTO);
        }
    }

    @Override
    public void examineDetectionPlan(DetectionPlanReqDTO detectionPlanReqDTO) {
        DetectionPlanResDTO res = detectionPlanMapper.getDetectionPlanDetail(detectionPlanReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        DetectionPlanReqDTO reqDTO = new DetectionPlanReqDTO();
        BeanUtils.copyProperties(res, reqDTO);
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
            }
        }
        reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
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
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
        if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        detectionPlanDetailReqDTO.setRecId(TokenUtil.getUuId());
        detectionPlanDetailReqDTO.setArchiveFlag("0");
        detectionPlanDetailReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        detectionPlanDetailReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        detectionPlanMapper.addDetectionPlanDetail(detectionPlanDetailReqDTO);
    }

    @Override
    public void modifyDetectionPlanDetail(DetectionPlanDetailReqDTO detectionPlanDetailReqDTO) {
        List<DetectionPlanResDTO> list = detectionPlanMapper.listDetectionPlan(detectionPlanDetailReqDTO.getInstrmPlanNo(), null, null, null, null);
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        DetectionPlanResDTO resDTO = list.get(0);
        if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        detectionPlanDetailReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        detectionPlanDetailReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        detectionPlanMapper.modifyDetectionPlanDetail(detectionPlanDetailReqDTO);
    }

    @Override
    public void deleteDetectionPlanDetail(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
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
                if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                DetectionPlanDetailDO detectionPlanDetailDO = new DetectionPlanDetailDO();
                detectionPlanDetailDO.setRecId(id).setDeleteFlag(CommonConstants.ONE_STRING).setRecDeletor(TokenUtil.getCurrentPersonId()).setRecDeleteTime(DateUtil.dateTimeNow());
                detectionPlanDetailMapper.updateById(detectionPlanDetailDO);
                // detectionPlanMapper.deleteDetectionPlanDetail(resDTO.getInstrmPlanNo(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
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
