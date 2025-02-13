package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionDetailReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionListReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionReqDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionDetailResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionResDTO;
import com.wzmtr.eam.dto.res.mea.excel.ExcelSubmissionDetailResDTO;
import com.wzmtr.eam.dto.res.mea.excel.ExcelSubmissionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.BpmnStatus;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.mea.SubmissionMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.service.mea.SubmissionService;
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
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private BpmnService bpmnService;

    @Autowired
    private IWorkFlowLogService workFlowLogService;

    @Override
    public Page<SubmissionResDTO> pageSubmission(SubmissionListReqDTO submissionListReqDTO, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return submissionMapper.pageSubmission(pageReqDTO.of(), submissionListReqDTO);
    }

    @Override
    public SubmissionResDTO getSubmissionDetail(String id) {
        return submissionMapper.getSubmissionDetail(id);
    }

    @Override
    public void addSubmission(SubmissionReqDTO submissionReqDTO) {
        String sendVerifyNo = submissionMapper.getMaxCode();
        if (StringUtils.isEmpty(sendVerifyNo) || !(CommonConstants.TWENTY_STRING + sendVerifyNo.substring(CommonConstants.TWO, CommonConstants.EIGHT)).equals(DateUtils.getNoDate())) {
            sendVerifyNo = "JW" + DateUtils.getNoDate().substring(2) + "0001";
        } else {
            sendVerifyNo = CodeUtils.getNextCode(sendVerifyNo, 8);
        }
        submissionReqDTO.setRecId(TokenUtils.getUuId());
        submissionReqDTO.setSendVerifyNo(sendVerifyNo);
        submissionReqDTO.setSendVerifyStatus("10");
        submissionReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        submissionReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        submissionReqDTO.setArchiveFlag("0");
        submissionMapper.addSubmission(submissionReqDTO);
    }

    @Override
    public void modifySubmission(SubmissionReqDTO submissionReqDTO) {
        SubmissionResDTO res = submissionMapper.getSubmissionDetail(submissionReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!res.getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(res.getSendVerifyStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        submissionReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        submissionReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        submissionMapper.modifySubmission(submissionReqDTO);
    }

    @Override
    public void deleteSubmission(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                SubmissionResDTO res = submissionMapper.getSubmissionDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                if (!res.getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!CommonConstants.TEN_STRING.equals(res.getSendVerifyStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                submissionMapper.deleteSubmissionDetail(null, res.getSendVerifyNo(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
                if (StringUtils.isNotBlank(res.getWorkFlowInstId())) {
                    BpmnExamineDTO bpmnExamineDTO = new BpmnExamineDTO();
                    bpmnExamineDTO.setTaskId(res.getWorkFlowInstId());
                    bpmnService.rejectInstance(bpmnExamineDTO);
                }
                submissionMapper.deleteSubmission(id, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void submitSubmission(SubmissionReqDTO submissionReqDTO) throws Exception {
        // ServiceDMAM0101
        SubmissionResDTO res = submissionMapper.getSubmissionDetail(submissionReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!CommonConstants.TEN_STRING.equals(res.getSendVerifyStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "提交");
        } else {
            List<SubmissionDetailResDTO> result = submissionMapper.listSubmissionDetail(res.getSendVerifyNo());
            if (result.isEmpty()) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "此送检单不存在计划明细，无法提交");
            }
            String processId = bpmnService.commit(res.getSendVerifyNo(), BpmnFlowEnum.SUBMISSION_SUBMIT.value(), null, null, submissionReqDTO.getExamineReqDTO().getUserIds(), null);
            if (processId == null || CommonConstants.PROCESS_ERROR_CODE.equals(processId)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败");
            }
            SubmissionReqDTO reqDTO = new SubmissionReqDTO();
            BeanUtils.copyProperties(res, reqDTO);
            reqDTO.setWorkFlowInstId(processId);
            reqDTO.setWorkFlowInstStatus(roleMapper.getSubmitNodeId(BpmnFlowEnum.SUBMISSION_SUBMIT.value(),null));
            reqDTO.setSendVerifyStatus("20");
            reqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
            reqDTO.setRecReviseTime(DateUtils.getCurrentTime());
            submissionMapper.modifySubmission(reqDTO);
            // 记录日志
            workFlowLogService.add(WorkFlowLogBO.builder()
                    .status(BpmnStatus.SUBMIT.getDesc())
                    .userIds(submissionReqDTO.getExamineReqDTO().getUserIds())
                    .workFlowInstId(processId)
                    .build());
        }
    }

    @Override
    public void examineSubmission(SubmissionReqDTO submissionReqDTO) {
        SubmissionResDTO res = submissionMapper.getSubmissionDetail(submissionReqDTO.getRecId());
        SubmissionReqDTO reqDTO = new SubmissionReqDTO();
        BeanUtils.copyProperties(res, reqDTO);
        workFlowLogService.ifReviewer(res.getWorkFlowInstId());
        if (submissionReqDTO.getExamineReqDTO().getExamineStatus() == 0) {
            if (CommonConstants.THIRTY_STRING.equals(res.getSendVerifyStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_DONE);
            } else {
                String processId = res.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.agree(taskId, submissionReqDTO.getExamineReqDTO().getOpinion(), null, "{\"id\":\"" + res.getSendVerifyNo() + "\"}", null);
                reqDTO.setWorkFlowInstStatus("已完成");
                reqDTO.setSendVerifyStatus("30");
                // 记录日志
                workFlowLogService.add(WorkFlowLogBO.builder()
                        .status(BpmnStatus.PASS.getDesc())
                        .userIds(submissionReqDTO.getExamineReqDTO().getUserIds())
                        .workFlowInstId(processId)
                        .build());
            }
        } else {
            if (!CommonConstants.TWENTY_STRING.equals(res.getSendVerifyStatus())) {
                throw new CommonException(ErrorCode.REJECT_ERROR);
            } else {
                String processId = res.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.reject(taskId, submissionReqDTO.getExamineReqDTO().getOpinion());
                reqDTO.setWorkFlowInstId(CommonConstants.EMPTY);
                reqDTO.setWorkFlowInstStatus(CommonConstants.EMPTY);
                reqDTO.setSendVerifyStatus("10");
                // 记录日志
                workFlowLogService.add(WorkFlowLogBO.builder()
                        .status(BpmnStatus.REJECT.getDesc())
                        .userIds(submissionReqDTO.getExamineReqDTO().getUserIds())
                        .workFlowInstId(processId)
                        .build());
            }
        }
        reqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        reqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        submissionMapper.modifySubmission(reqDTO);
    }

    @Override
    public void exportSubmission(List<String> ids, HttpServletResponse response) throws IOException {
        List<SubmissionResDTO> checkPlanList = submissionMapper.exportSubmission(ids);
        if (checkPlanList != null && !checkPlanList.isEmpty()) {
            List<ExcelSubmissionResDTO> list = new ArrayList<>();
            for (SubmissionResDTO resDTO : checkPlanList) {
                ExcelSubmissionResDTO res = new ExcelSubmissionResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "送检单信息", list);
        }
    }

    @Override
    public Page<SubmissionDetailResDTO> pageSubmissionDetail(String sendVerifyNo, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return submissionMapper.pageSubmissionDetail(pageReqDTO.of(), sendVerifyNo);
    }

    @Override
    public SubmissionDetailResDTO getSubmissionDetailDetail(String id) {
        return submissionMapper.getSubmissionDetailDetail(id);
    }

    @Override
    public void addSubmissionDetail(SubmissionDetailReqDTO submissionDetailReqDTO) {
        SubmissionListReqDTO submissionListReqDTO = new SubmissionListReqDTO();
        submissionListReqDTO.setSendVerifyNo(submissionDetailReqDTO.getSendVerifyNo());
        List<SubmissionResDTO> list = submissionMapper.listSubmission(submissionListReqDTO);
        if (!list.isEmpty()) {
            if (!list.get(0).getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
            }
            if (!CommonConstants.TEN_STRING.equals(list.get(0).getSendVerifyStatus())) {
                throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "新增");
            }
        }
        submissionDetailReqDTO.setRecId(TokenUtils.getUuId());
        submissionDetailReqDTO.setPlanDetailRecId(TokenUtils.getUuId());
        submissionDetailReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        submissionDetailReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        submissionDetailReqDTO.setArchiveFlag("0");
        submissionMapper.addSubmissionDetail(submissionDetailReqDTO);
    }

    @Override
    public void modifySubmissionDetail(SubmissionDetailReqDTO submissionDetailReqDTO) {
        SubmissionListReqDTO submissionListReqDTO = new SubmissionListReqDTO();
        submissionListReqDTO.setSendVerifyNo(submissionDetailReqDTO.getSendVerifyNo());
        List<SubmissionResDTO> list = submissionMapper.listSubmission(submissionListReqDTO);
        if (!list.isEmpty()) {
            if (!list.get(0).getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
            }
            if (!CommonConstants.TEN_STRING.equals(list.get(0).getSendVerifyStatus())) {
                throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
            }
        }
        submissionDetailReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        submissionDetailReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        submissionMapper.modifySubmissionDetail(submissionDetailReqDTO);
    }

    @Override
    public void deleteSubmissionDetail(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                SubmissionDetailResDTO res = submissionMapper.getSubmissionDetailDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                SubmissionListReqDTO submissionListReqDTO = new SubmissionListReqDTO();
                submissionListReqDTO.setSendVerifyNo(id);
                List<SubmissionResDTO> list = submissionMapper.listSubmission(submissionListReqDTO);
                if (!list.isEmpty()) {
                    if (!list.get(0).getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                        throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                    }
                    if (!CommonConstants.TEN_STRING.equals(list.get(0).getSendVerifyStatus())) {
                        throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
                    }
                }
                submissionMapper.deleteSubmissionDetail(id, null, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportSubmissionDetail(String sendVerifyNo, HttpServletResponse response) throws IOException {
        List<SubmissionDetailResDTO> submissionDetail = submissionMapper.listSubmissionDetail(sendVerifyNo);
        if (submissionDetail != null && !submissionDetail.isEmpty()) {
            List<ExcelSubmissionDetailResDTO> list = new ArrayList<>();
            for (SubmissionDetailResDTO resDTO : submissionDetail) {
                ExcelSubmissionDetailResDTO res = new ExcelSubmissionDetailResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "送检单明细信息", list);
        }
    }

}
