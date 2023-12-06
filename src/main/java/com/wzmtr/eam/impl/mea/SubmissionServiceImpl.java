package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionDetailReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionListReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionReqDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionDetailResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.mea.SubmissionMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.mea.SubmissionService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Override
    public Page<SubmissionResDTO> pageSubmission(SubmissionListReqDTO submissionListReqDTO, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return submissionMapper.pageSubmission(pageReqDTO.of(), submissionListReqDTO);
    }

    @Override
    public SubmissionResDTO getSubmissionDetail(String id) {
        return submissionMapper.getSubmissionDetail(id);
    }

    @Override
    public void addSubmission(SubmissionReqDTO submissionReqDTO) {
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        String sendVerifyNo = submissionMapper.getMaxCode();
        if (StringUtils.isEmpty(sendVerifyNo) || !(CommonConstants.TWENTY_STRING + sendVerifyNo.substring(CommonConstants.TWO, CommonConstants.EIGHT)).equals(day.format(System.currentTimeMillis()))) {
            sendVerifyNo = "JW" + day.format(System.currentTimeMillis()).substring(2) + "0001";
        } else {
            sendVerifyNo = CodeUtils.getNextCode(sendVerifyNo, 8);
        }
        submissionReqDTO.setRecId(TokenUtil.getUuId());
        submissionReqDTO.setSendVerifyNo(sendVerifyNo);
        submissionReqDTO.setSendVerifyStatus("10");
        submissionReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        submissionReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        submissionReqDTO.setArchiveFlag("0");
        submissionMapper.addSubmission(submissionReqDTO);
    }

    @Override
    public void modifySubmission(SubmissionReqDTO submissionReqDTO) {
        SubmissionResDTO res = submissionMapper.getSubmissionDetail(submissionReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!res.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(res.getSendVerifyStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        submissionReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        submissionReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        submissionMapper.modifySubmission(submissionReqDTO);
    }

    @Override
    public void deleteSubmission(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                SubmissionResDTO res = submissionMapper.getSubmissionDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                if (!res.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!CommonConstants.TEN_STRING.equals(res.getSendVerifyStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                submissionMapper.deleteSubmissionDetail(null, res.getSendVerifyNo(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                if (StringUtils.isNotBlank(res.getWorkFlowInstId())) {
                    BpmnExamineDTO bpmnExamineDTO = new BpmnExamineDTO();
                    bpmnExamineDTO.setTaskId(res.getWorkFlowInstId());
                    bpmnService.rejectInstance(bpmnExamineDTO);
                }
                submissionMapper.deleteSubmission(id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    // ServiceDMAM0101
    @Override
    public void submitSubmission(SubmissionReqDTO submissionReqDTO) throws Exception {
        SubmissionResDTO res = submissionMapper.getSubmissionDetail(submissionReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!CommonConstants.TEN_STRING.equals(res.getSendVerifyStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "提交");
        } else {
            List<SubmissionDetailResDTO> result = submissionMapper.listSubmissionDetail(res.getSendVerifyNo());
            if (result.size() == 0) {
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
            reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
            reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            submissionMapper.modifySubmission(reqDTO);
        }
    }

    @Override
    public void examineSubmission(SubmissionReqDTO submissionReqDTO) {
        SubmissionResDTO res = submissionMapper.getSubmissionDetail(submissionReqDTO.getRecId());
        SubmissionReqDTO reqDTO = new SubmissionReqDTO();
        BeanUtils.copyProperties(res, reqDTO);
        if (submissionReqDTO.getExamineReqDTO().getExamineStatus() == 0) {
            if (CommonConstants.THIRTY_STRING.equals(res.getSendVerifyStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_DONE);
            } else {
                String processId = res.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.agree(taskId, submissionReqDTO.getExamineReqDTO().getOpinion(), null, "{\"id\":\"" + res.getSendVerifyNo() + "\"}", null);
                reqDTO.setWorkFlowInstStatus("已完成");
                reqDTO.setRecStatus("30");
            }
        } else {
            if (!CommonConstants.TWENTY_STRING.equals(res.getRecStatus())) {
                throw new CommonException(ErrorCode.REJECT_ERROR);
            } else {
                String processId = res.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.reject(taskId, submissionReqDTO.getExamineReqDTO().getOpinion());
                reqDTO.setWorkFlowInstId("");
                reqDTO.setWorkFlowInstStatus("");
                reqDTO.setRecStatus("10");
            }
        }
        reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        submissionMapper.modifySubmission(reqDTO);
    }

    @Override
    public void exportSubmission(SubmissionListReqDTO checkPlanListReqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "送检单号", "送检委托人", "送检委托人电话", "送检接收人", "送检接收人电话",
                "送检日期", "返送人", "返送人电话", "返还日期", "返还接收人", "返还接收人电话", "送检单状态");
        List<SubmissionResDTO> checkPlanList = submissionMapper.listSubmission(checkPlanListReqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (checkPlanList != null && !checkPlanList.isEmpty()) {
            for (SubmissionResDTO resDTO : checkPlanList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("送检单号", resDTO.getSendVerifyNo());
                map.put("送检委托人", resDTO.getSendConsignerName());
                map.put("送检委托人电话", resDTO.getSendConsignerTele());
                map.put("送检接收人", resDTO.getSendReceiverName());
                map.put("送检接收人电话", resDTO.getSendReceiverTele());
                map.put("送检日期", resDTO.getSendVerifyDate());
                map.put("返送人", resDTO.getBackReturnName());
                map.put("返送人电话", resDTO.getBackReturnTele());
                map.put("返还日期", resDTO.getVerifyBackDate());
                map.put("返还接收人", resDTO.getBackReceiverName());
                map.put("返还接收人电话", resDTO.getBackConsignerTele());
                map.put("送检单状态", resDTO.getSendVerifyStatus());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("送检单信息", listName, list, null, response);
    }

    @Override
    public Page<SubmissionDetailResDTO> pageSubmissionDetail(String sendVerifyNo, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
        if (list.size() != 0) {
            if (!list.get(0).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
            }
            if (!CommonConstants.TEN_STRING.equals(list.get(0).getSendVerifyStatus())) {
                throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "新增");
            }
        }
        submissionDetailReqDTO.setRecId(TokenUtil.getUuId());
        submissionDetailReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        submissionDetailReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        submissionDetailReqDTO.setArchiveFlag("0");
        submissionMapper.addSubmissionDetail(submissionDetailReqDTO);
    }

    @Override
    public void modifySubmissionDetail(SubmissionDetailReqDTO submissionDetailReqDTO) {
        SubmissionListReqDTO submissionListReqDTO = new SubmissionListReqDTO();
        submissionListReqDTO.setSendVerifyNo(submissionDetailReqDTO.getSendVerifyNo());
        List<SubmissionResDTO> list = submissionMapper.listSubmission(submissionListReqDTO);
        if (list.size() != 0) {
            if (!list.get(0).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
            }
            if (!CommonConstants.TEN_STRING.equals(list.get(0).getSendVerifyStatus())) {
                throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
            }
        }
        submissionDetailReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        submissionDetailReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        submissionMapper.modifySubmissionDetail(submissionDetailReqDTO);
    }

    @Override
    public void deleteSubmissionDetail(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                SubmissionDetailResDTO res = submissionMapper.getSubmissionDetailDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                SubmissionListReqDTO submissionListReqDTO = new SubmissionListReqDTO();
                submissionListReqDTO.setSendVerifyNo(id);
                List<SubmissionResDTO> list = submissionMapper.listSubmission(submissionListReqDTO);
                if (list.size() != 0) {
                    if (!list.get(0).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                        throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                    }
                    if (!CommonConstants.TEN_STRING.equals(list.get(0).getSendVerifyStatus())) {
                        throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
                    }
                }
                submissionMapper.deleteSubmissionDetail(id, null, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportSubmissionDetail(String sendVerifyNo, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "送检单号", "计量器具编码", "计量器具名称", "型号规格", "出厂编号", "检定校准单位");
        List<SubmissionDetailResDTO> submissionDetail = submissionMapper.listSubmissionDetail(sendVerifyNo);
        List<Map<String, String>> list = new ArrayList<>();
        if (submissionDetail != null && !submissionDetail.isEmpty()) {
            for (SubmissionDetailResDTO resDTO : submissionDetail) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("送检单号", resDTO.getSendVerifyNo());
                map.put("计量器具编码", resDTO.getEquipCode());
                map.put("计量器具名称", resDTO.getEquipName());
                map.put("型号规格", resDTO.getMatSpecifi());
                map.put("出厂编号", resDTO.getManufactureNo());
                map.put("检定校准单位", resDTO.getInstallationUnit());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("送检单明细信息", listName, list, null, response);
    }

}
