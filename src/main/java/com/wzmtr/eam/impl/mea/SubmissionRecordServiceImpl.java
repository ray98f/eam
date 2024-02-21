package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordDetailReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordReqDTO;
import com.wzmtr.eam.dto.res.mea.MeaResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordResDTO;
import com.wzmtr.eam.dto.res.mea.excel.ExcelSubmissionRecordDetailResDTO;
import com.wzmtr.eam.dto.res.mea.excel.ExcelSubmissionRecordResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.BpmnStatus;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.mapper.mea.MeaMapper;
import com.wzmtr.eam.mapper.mea.SubmissionRecordMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.service.mea.SubmissionRecordService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class SubmissionRecordServiceImpl implements SubmissionRecordService {

    @Autowired
    private SubmissionRecordMapper submissionRecordMapper;

    @Autowired
    private MeaMapper meaMapper;

    @Autowired
    private BpmnService bpmnService;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private IWorkFlowLogService workFlowLogService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<SubmissionRecordResDTO> pageSubmissionRecord(String checkNo, String instrmPlanNo, String recStatus, String workFlowInstId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<SubmissionRecordResDTO> page = submissionRecordMapper.pageSubmissionRecord(pageReqDTO.of(), checkNo, instrmPlanNo, recStatus, workFlowInstId);
        List<SubmissionRecordResDTO> list = page.getRecords();
        if (!Objects.isNull(list) && !list.isEmpty()) {
            for (SubmissionRecordResDTO res : list) {
                if (StringUtils.isNotEmpty(res.getDocId())) {
                    res.setDocFile(fileMapper.selectFileInfo(Arrays.asList(res.getDocId().split(","))));
                }
                if (StringUtils.isNotEmpty(res.getVerifyDept())) {
                    res.setVerifyDeptName(organizationMapper.getNamesById(res.getVerifyDept()));
                }
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public SubmissionRecordResDTO getSubmissionRecordDetail(String id) {
        SubmissionRecordResDTO res = submissionRecordMapper.getSubmissionRecordDetail(id);
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (StringUtils.isNotEmpty(res.getDocId())) {
            res.setDocFile(fileMapper.selectFileInfo(Arrays.asList(res.getDocId().split(","))));
        }
        return res;
    }

    @Override
    public void addSubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO) {
        String recCreator = TokenUtils.getCurrentPersonId();
        CurrentLoginUser user = TokenUtils.getCurrentPerson();
        String editDeptCode = user.getOfficeAreaId() == null ? user.getOfficeId() : user.getOfficeAreaId();
        String recCreateTime = DateUtils.getCurrentTime();
        String archiveFlag = "0";
        String recStatus = "10";
        String checkNo = submissionRecordMapper.getMaxCode();
        if (StringUtils.isEmpty(checkNo) || !(CommonConstants.TWENTY_STRING + checkNo.substring(CommonConstants.TWO, CommonConstants.EIGHT)).equals(DateUtils.getNoDate())) {
            checkNo = "JJ" + DateUtils.getNoDate().substring(2) + "0001";
        } else {
            checkNo = CodeUtils.getNextCode(checkNo, 8);
        }
        submissionRecordReqDTO.setRecId(TokenUtils.getUuId());
        submissionRecordReqDTO.setCheckNo(checkNo);
        submissionRecordReqDTO.setRecCreator(recCreator);
        submissionRecordReqDTO.setRecCreateTime(recCreateTime);
        submissionRecordReqDTO.setRecStatus(recStatus);
        submissionRecordReqDTO.setArchiveFlag(archiveFlag);
        submissionRecordReqDTO.setEditDeptCode(editDeptCode);
        submissionRecordMapper.addSubmissionRecord(submissionRecordReqDTO);
    }

    @Override
    public void modifySubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO) {
        SubmissionRecordResDTO res = submissionRecordMapper.getSubmissionRecordDetail(submissionRecordReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!res.getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(res.getRecStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        submissionRecordReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        submissionRecordReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        submissionRecordMapper.modifySubmissionRecord(submissionRecordReqDTO);
    }

    @Override
    public void deleteSubmissionRecord(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                SubmissionRecordResDTO res = submissionRecordMapper.getSubmissionRecordDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                if (!res.getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!CommonConstants.TEN_STRING.equals(res.getRecStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                submissionRecordMapper.deleteSubmissionRecordDetail(null, res.getRecId(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
                if (StringUtils.isNotBlank(res.getWorkFlowInstId())) {
                    BpmnExamineDTO bpmnExamineDTO = new BpmnExamineDTO();
                    bpmnExamineDTO.setTaskId(res.getWorkFlowInstId());
                    bpmnService.rejectInstance(bpmnExamineDTO);
                }
                submissionRecordMapper.deleteSubmissionRecord(id, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void submitSubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO) throws Exception {
        // ServiceDMAM0301
        SubmissionRecordResDTO res = submissionRecordMapper.getSubmissionRecordDetail(submissionRecordReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!CommonConstants.TEN_STRING.equals(res.getRecStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "提交");
        } else {
            List<SubmissionRecordDetailResDTO> result = submissionRecordMapper.listSubmissionRecordDetail(res.getRecId());
            if (result.isEmpty()) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "此定检记录不存在计划明细，无法提交");
            }
            String processId = bpmnService.commit(res.getCheckNo(), BpmnFlowEnum.SUBMISSION_RECORD_SUBMIT.value(), null, null, submissionRecordReqDTO.getExamineReqDTO().getUserIds(), null);
            if (processId == null || CommonConstants.PROCESS_ERROR_CODE.equals(processId)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败");
            }
            SubmissionRecordReqDTO reqDTO = new SubmissionRecordReqDTO();
            BeanUtils.copyProperties(res, reqDTO);
            reqDTO.setWorkFlowInstId(processId);
            reqDTO.setWorkFlowInstStatus(roleMapper.getSubmitNodeId(BpmnFlowEnum.SUBMISSION_RECORD_SUBMIT.value(),null));
            reqDTO.setRecStatus("20");
            reqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
            reqDTO.setRecReviseTime(DateUtils.getCurrentTime());
            submissionRecordMapper.modifySubmissionRecord(reqDTO);
            // 记录日志
            workFlowLogService.add(WorkFlowLogBO.builder()
                    .status(BpmnStatus.SUBMIT.getDesc())
                    .userIds(submissionRecordReqDTO.getExamineReqDTO().getUserIds())
                    .workFlowInstId(processId)
                    .build());
        }
    }

    @Override
    public void examineSubmissionRecord(SubmissionRecordReqDTO submissionRecordReqDTO) {
        SubmissionRecordResDTO res = submissionRecordMapper.getSubmissionRecordDetail(submissionRecordReqDTO.getRecId());
        SubmissionRecordReqDTO reqDTO = new SubmissionRecordReqDTO();
        BeanUtils.copyProperties(res, reqDTO);
        workFlowLogService.ifReviewer(res.getWorkFlowInstId());
        if (submissionRecordReqDTO.getExamineReqDTO().getExamineStatus() == 0) {
            if (CommonConstants.THIRTY_STRING.equals(res.getRecStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_DONE);
            } else {
                String processId = res.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.agree(taskId, submissionRecordReqDTO.getExamineReqDTO().getOpinion(), null, "{\"id\":\"" + res.getCheckNo() + "\"}", null);
                reqDTO.setWorkFlowInstStatus("已完成");
                reqDTO.setRecStatus("30");
                // 记录日志
                workFlowLogService.add(WorkFlowLogBO.builder()
                        .status(BpmnStatus.PASS.getDesc())
                        .userIds(submissionRecordReqDTO.getExamineReqDTO().getUserIds())
                        .workFlowInstId(processId)
                        .build());
            }
        } else {
            if (!CommonConstants.TWENTY_STRING.equals(res.getRecStatus())) {
                throw new CommonException(ErrorCode.REJECT_ERROR);
            } else {
                String processId = res.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.reject(taskId, submissionRecordReqDTO.getExamineReqDTO().getOpinion());
                reqDTO.setWorkFlowInstId("");
                reqDTO.setWorkFlowInstStatus("");
                reqDTO.setRecStatus("10");
                // 记录日志
                workFlowLogService.add(WorkFlowLogBO.builder()
                        .status(BpmnStatus.REJECT.getDesc())
                        .userIds(submissionRecordReqDTO.getExamineReqDTO().getUserIds())
                        .workFlowInstId(processId)
                        .build());
            }
        }
        reqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        reqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        submissionRecordMapper.modifySubmissionRecord(reqDTO);
    }

    @Override
    public void exportSubmissionRecord(List<String> ids, HttpServletResponse response) throws IOException {
        List<SubmissionRecordResDTO> checkPlanList = submissionRecordMapper.exportSubmissionRecord(ids);
        if (checkPlanList != null && !checkPlanList.isEmpty()) {
            List<ExcelSubmissionRecordResDTO> list = new ArrayList<>();
            for (SubmissionRecordResDTO resDTO : checkPlanList) {
                if (StringUtils.isNotEmpty(resDTO.getVerifyDept())) {
                    resDTO.setVerifyDeptName(organizationMapper.getNamesById(resDTO.getVerifyDept()));
                }
                ExcelSubmissionRecordResDTO res = new ExcelSubmissionRecordResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setRecStatus(CommonConstants.TEN_STRING.equals(resDTO.getRecStatus()) ? "编辑" : CommonConstants.TWENTY_STRING.equals(resDTO.getRecStatus()) ? "审核中" : "审核通过");
                list.add(res);
            }
            EasyExcelUtils.export(response, "定检计划信息", list);
        }
    }

    @Override
    public Page<SubmissionRecordDetailResDTO> pageSubmissionRecordDetail(String testRecId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return submissionRecordMapper.pageSubmissionRecordDetail(pageReqDTO.of(), testRecId);
    }

    @Override
    public SubmissionRecordDetailResDTO getSubmissionRecordDetailDetail(String id) {
        return submissionRecordMapper.getSubmissionRecordDetailDetail(id);
    }

    @Override
    public void addSubmissionRecordDetail(SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO) {
        submissionRecordDetailReqDTO.setRecId(TokenUtils.getUuId());
        submissionRecordDetailReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        submissionRecordDetailReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        submissionRecordDetailReqDTO.setArchiveFlag("0");
        if (StringUtils.isNotEmpty(submissionRecordDetailReqDTO.getEquipName())) {
            String equipCode = equipmentMapper.getEquipCodeByName(submissionRecordDetailReqDTO.getEquipName());
            if (StringUtils.isNotEmpty(equipCode)) {
                submissionRecordDetailReqDTO.setEquipCode(equipCode);
            } else {
                throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
            }
        } else {
            throw new CommonException(ErrorCode.PARAM_NULL);
        }
        MeaResDTO meaResDTO = new MeaResDTO();
        meaResDTO.setLastVerifyDate(submissionRecordDetailReqDTO.getLastVerifyDate());
        meaResDTO.setNextVerifyDate(submissionRecordDetailReqDTO.getNextVerifyDate());
        meaResDTO.setEquipName(submissionRecordDetailReqDTO.getEquipName());
        meaResDTO.setCertificateNo(submissionRecordDetailReqDTO.getVerificationNo());
        meaResDTO.setMeasureBarcode(submissionRecordDetailReqDTO.getMeasureBarcode());
        if (submissionRecordDetailReqDTO.getVerifyPeriod() != null) {
            meaResDTO.setVerifyPeriod(Integer.valueOf(submissionRecordDetailReqDTO.getVerifyPeriod()));
        }
        meaResDTO.setVerifyDept(submissionRecordDetailReqDTO.getVerifyDept());
        meaResDTO.setEquipCode(submissionRecordDetailReqDTO.getEquipCode());
        meaResDTO.setRecCreateTime(submissionRecordDetailReqDTO.getRecCreateTime());
        meaResDTO.setRecCreator(submissionRecordDetailReqDTO.getRecCreator());
        meaMapper.updateone(meaResDTO);
        submissionRecordMapper.addSubmissionRecordDetail(submissionRecordDetailReqDTO);
    }

    @Override
    public void modifySubmissionRecordDetail(SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO) {
        List<SubmissionRecordResDTO> list = submissionRecordMapper.listSubmissionRecord(submissionRecordDetailReqDTO.getTestRecId(), null, null, null, null);
        if (!list.isEmpty()) {
            if (!list.get(0).getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
            }
            if (!CommonConstants.TEN_STRING.equals(list.get(0).getRecStatus())) {
                throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
            }
        }
        submissionRecordDetailReqDTO.setRecRevisor(TokenUtils.getUuId());
        submissionRecordDetailReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        submissionRecordMapper.modifySubmissionRecordDetail(submissionRecordDetailReqDTO);
    }

    @Override
    public void deleteSubmissionRecordDetail(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                SubmissionRecordDetailResDTO res = submissionRecordMapper.getSubmissionRecordDetailDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                List<SubmissionRecordResDTO> list = submissionRecordMapper.listSubmissionRecord(res.getTestRecId(), null, null, null, null);
                if (!list.isEmpty()) {
                    if (!list.get(0).getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                        throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                    }
                    if (!CommonConstants.TEN_STRING.equals(list.get(0).getRecStatus())) {
                        throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                    }
                }
                submissionRecordMapper.deleteSubmissionRecordDetail(id, null, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportSubmissionRecordDetail(List<String> ids, HttpServletResponse response) throws IOException {
        List<SubmissionRecordDetailResDTO> meaInfoList = submissionRecordMapper.exportSubmissionRecordDetail(ids);
        if (meaInfoList != null && !meaInfoList.isEmpty()) {
            List<ExcelSubmissionRecordDetailResDTO> list = new ArrayList<>();
            for (SubmissionRecordDetailResDTO resDTO : meaInfoList) {
                ExcelSubmissionRecordDetailResDTO res = new ExcelSubmissionRecordDetailResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "检定记录明细信息", list);
        }
    }

}
