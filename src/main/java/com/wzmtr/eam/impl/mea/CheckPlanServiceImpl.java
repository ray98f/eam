package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.req.mea.CheckPlanListReqDTO;
import com.wzmtr.eam.dto.req.mea.CheckPlanReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaInfoQueryReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaInfoReqDTO;
import com.wzmtr.eam.dto.res.mea.CheckPlanResDTO;
import com.wzmtr.eam.dto.res.mea.MeaInfoResDTO;
import com.wzmtr.eam.dto.res.mea.excel.ExcelCheckPlanResDTO;
import com.wzmtr.eam.dto.res.mea.excel.ExcelMeaInfoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.BpmnStatus;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.mea.CheckPlanMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.service.mea.CheckPlanService;
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
public class CheckPlanServiceImpl implements CheckPlanService {

    @Autowired
    private CheckPlanMapper checkPlanMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private BpmnService bpmnService;

    @Autowired
    private IWorkFlowLogService workFlowLogService;

    @Override
    public Page<CheckPlanResDTO> pageCheckPlan(CheckPlanListReqDTO checkPlanListReqDTO, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<CheckPlanResDTO> page = checkPlanMapper.pageCheckPlan(pageReqDTO.of(), checkPlanListReqDTO);
        List<CheckPlanResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (CheckPlanResDTO res : list) {
                res.setEditDeptCode(organizationMapper.getNamesById(res.getEditDeptCode()));
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public CheckPlanResDTO getCheckPlanDetail(String id) {
        CheckPlanResDTO res = checkPlanMapper.getCheckPlanDetail(id);
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        res.setEditDeptCode(organizationMapper.getNamesById(res.getEditDeptCode()));
        return res;
    }

    @Override
    public void addCheckPlan(CheckPlanReqDTO checkPlanReqDTO) {
        String recCreator = TokenUtils.getCurrentPersonId();
        CurrentLoginUser user = TokenUtils.getCurrentPerson();
        String userName = user.getPersonName();
        String editDeptCode = user.getOfficeAreaId() == null ? user.getOfficeId() : user.getOfficeAreaId();
        String recCreateTime = DateUtils.getCurrentTime();
        String archiveFlag = "0";
        String instrmPlanNo = checkPlanMapper.getMaxCode();
        if (StringUtils.isEmpty(instrmPlanNo) || !(CommonConstants.TWENTY_STRING + instrmPlanNo.substring(CommonConstants.TWO, CommonConstants.EIGHT)).equals(DateUtils.getNoDate())) {
            instrmPlanNo = "JP" + DateUtils.getNoDate().substring(2) + "0001";
        } else {
            instrmPlanNo = CodeUtils.getNextCode(instrmPlanNo, 8);
        }
        checkPlanReqDTO.setRecId(TokenUtils.getUuId());
        checkPlanReqDTO.setInstrmPlanNo(instrmPlanNo);
        checkPlanReqDTO.setPlanStatus("10");
        checkPlanReqDTO.setRecCreator(recCreator);
        checkPlanReqDTO.setPlanCreaterNo(recCreator);
        checkPlanReqDTO.setPlanCreaterName(userName);
        checkPlanReqDTO.setRecCreateTime(recCreateTime);
        checkPlanReqDTO.setArchiveFlag(archiveFlag);
        checkPlanReqDTO.setEditDeptCode(editDeptCode);
        if (StringUtils.isEmpty(checkPlanReqDTO.getPlanPeriodMark())) {
            checkPlanReqDTO.setPlanPeriodMark(" ");
        }
        if (StringUtils.isEmpty(checkPlanReqDTO.getPlanCreateTime())) {
            checkPlanReqDTO.setPlanCreateTime(" ");
        }
        checkPlanMapper.addCheckPlan(checkPlanReqDTO);
    }

    @Override
    public void modifyCheckPlan(CheckPlanReqDTO checkPlanReqDTO) {
        CheckPlanResDTO res = checkPlanMapper.getCheckPlanDetail(checkPlanReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!res.getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!CommonConstants.TEN_STRING.equals(res.getPlanStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        CheckPlanListReqDTO checkPlanListReqDTO = new CheckPlanListReqDTO();
        checkPlanListReqDTO.setInstrmPlanType(checkPlanReqDTO.getInstrmPlanType());
        checkPlanListReqDTO.setPlanPeriodMark(checkPlanReqDTO.getPlanPeriodMark());
        checkPlanListReqDTO.setEditDeptCode(checkPlanReqDTO.getEditDeptCode());
        List<CheckPlanResDTO> planList = checkPlanMapper.listCheckPlan(checkPlanListReqDTO);
        if (StringUtils.isNotEmpty(planList)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "该定检计划已存在");
        }
        checkPlanReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        checkPlanReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        checkPlanMapper.modifyCheckPlan(checkPlanReqDTO);
    }

    @Override
    public void deleteCheckPlan(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                CheckPlanResDTO res = checkPlanMapper.getCheckPlanDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                if (!res.getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!CommonConstants.TEN_STRING.equals(res.getPlanStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                checkPlanMapper.deleteCheckPlanDetail(null, res.getInstrmPlanNo(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
                if (StringUtils.isNotBlank(res.getWorkFlowInstId())) {
                    BpmnExamineDTO bpmnExamineDTO = new BpmnExamineDTO();
                    bpmnExamineDTO.setTaskId(res.getWorkFlowInstId());
                    bpmnService.rejectInstance(bpmnExamineDTO);
                }
                checkPlanMapper.deleteCheckPlan(id, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void submitCheckPlan(CheckPlanReqDTO checkPlanReqDTO) throws Exception {
        // ServiceDMAM0201
        CheckPlanResDTO res = checkPlanMapper.getCheckPlanDetail(checkPlanReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!res.getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        List<MeaInfoResDTO> result = checkPlanMapper.listInfo(null, res.getInstrmPlanNo());
        if (result.isEmpty()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "此定检计划不存在计划明细，无法提交");
        }
        if (!CommonConstants.TEN_STRING.equals(res.getPlanStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        } else {
            String processId = bpmnService.commit(res.getInstrmPlanNo(), BpmnFlowEnum.CHECK_PLAN_SUBMIT.value(), null, null, checkPlanReqDTO.getExamineReqDTO().getUserIds(), null);
            if (processId == null || CommonConstants.PROCESS_ERROR_CODE.equals(processId)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败");
            }
            CheckPlanReqDTO reqDTO = new CheckPlanReqDTO();
            BeanUtils.copyProperties(res, reqDTO);
            reqDTO.setWorkFlowInstId(processId);
            reqDTO.setWorkFlowInstStatus(roleMapper.getSubmitNodeId(BpmnFlowEnum.CHECK_PLAN_SUBMIT.value(),null));
            reqDTO.setPlanStatus("20");
            reqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
            reqDTO.setRecReviseTime(DateUtils.getCurrentTime());
            checkPlanMapper.modifyCheckPlan(reqDTO);
            // 记录日志
            workFlowLogService.add(WorkFlowLogBO.builder()
                    .status(BpmnStatus.SUBMIT.getDesc())
                    .userIds(checkPlanReqDTO.getExamineReqDTO().getUserIds())
                    .workFlowInstId(processId)
                    .build());
        }
    }

    @Override
    public void examineCheckPlan(CheckPlanReqDTO checkPlanReqDTO) {
        CheckPlanResDTO res = checkPlanMapper.getCheckPlanDetail(checkPlanReqDTO.getRecId());
        CheckPlanReqDTO reqDTO = new CheckPlanReqDTO();
        BeanUtils.copyProperties(res, reqDTO);
        workFlowLogService.ifReviewer(res.getWorkFlowInstId());
        if (checkPlanReqDTO.getExamineReqDTO().getExamineStatus() == 0) {
            if (CommonConstants.THIRTY_STRING.equals(res.getPlanStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_DONE);
            }
            if (CommonConstants.TEN_STRING.equals(res.getPlanStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_NOT_DONE);
            }
            String processId = res.getWorkFlowInstId();
            String taskId = bpmnService.queryTaskIdByProcId(processId);
            bpmnService.agree(taskId, checkPlanReqDTO.getExamineReqDTO().getOpinion(), null, "{\"id\":\"" + res.getInstrmPlanNo() + "\"}", null);
            reqDTO.setWorkFlowInstStatus("已完成");
            reqDTO.setPlanStatus("30");
            // 记录日志
            workFlowLogService.add(WorkFlowLogBO.builder()
                    .status(BpmnStatus.PASS.getDesc())
                    .userIds(checkPlanReqDTO.getExamineReqDTO().getUserIds())
                    .workFlowInstId(processId)
                    .build());
        } else {
            if (!CommonConstants.TWENTY_STRING.equals(res.getPlanStatus())) {
                throw new CommonException(ErrorCode.REJECT_ERROR);
            } else {
                String processId = res.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.reject(taskId, checkPlanReqDTO.getExamineReqDTO().getOpinion());
                reqDTO.setWorkFlowInstId("");
                reqDTO.setWorkFlowInstStatus("");
                reqDTO.setPlanStatus("10");
                // 记录日志
                workFlowLogService.add(WorkFlowLogBO.builder()
                        .status(BpmnStatus.REJECT.getDesc())
                        .userIds(checkPlanReqDTO.getExamineReqDTO().getUserIds())
                        .workFlowInstId(processId)
                        .build());
            }
        }
        reqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        reqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        checkPlanMapper.modifyCheckPlan(reqDTO);
    }

    @Override
    public void exportCheckPlan(List<String> ids, HttpServletResponse response) throws IOException {
        List<CheckPlanResDTO> checkPlanList = checkPlanMapper.exportCheckPlan(ids);
        if (checkPlanList != null && !checkPlanList.isEmpty()) {
            List<ExcelCheckPlanResDTO> list = new ArrayList<>();
            for (CheckPlanResDTO resDTO : checkPlanList) {
                ExcelCheckPlanResDTO res = new ExcelCheckPlanResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setEditDeptCode(organizationMapper.getNamesById(resDTO.getEditDeptCode()));
                res.setPlanStatus(CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus()) ? "编辑" : CommonConstants.TWENTY_STRING.equals(resDTO.getPlanStatus()) ? "审核中" : "审核通过");
                list.add(res);
            }
            EasyExcelUtils.export(response, "定检计划信息", list);
        }
    }

    @Override
    public Page<MeaInfoResDTO> pageCheckPlanInfo(String equipCode, String instrmPlanNo, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return checkPlanMapper.pageInfo(pageReqDTO.of(), equipCode, instrmPlanNo);
    }

    @Override
    public MeaInfoResDTO getCheckPlanInfoDetail(String id) {
        return checkPlanMapper.getInfoDetail(id);
    }

    @Override
    public void addCheckPlanInfo(MeaInfoReqDTO meaInfoReqDTO) {
        CheckPlanListReqDTO checkPlanListReqDTO = new CheckPlanListReqDTO();
        checkPlanListReqDTO.setInstrmPlanNo(meaInfoReqDTO.getInstrmPlanNo());
        List<CheckPlanResDTO> list = checkPlanMapper.listCheckPlan(checkPlanListReqDTO);
        if (StringUtils.isNotEmpty(list)) {
            if (!list.get(0).getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
            }
            if (!CommonConstants.TEN_STRING.equals(list.get(0).getPlanStatus())) {
                throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "新增");
            }
        }
        meaInfoReqDTO.setRecId(TokenUtils.getUuId());
        meaInfoReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        meaInfoReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        meaInfoReqDTO.setArchiveFlag("0");
        if (meaInfoReqDTO.getVerifyPeriod() == 0) {
            meaInfoReqDTO.setVerifyPeriod(null);
        }
        checkPlanMapper.addInfo(meaInfoReqDTO);
    }

    @Override
    public void modifyCheckPlanInfo(MeaInfoReqDTO meaInfoReqDTO) {
        CheckPlanListReqDTO checkPlanListReqDTO = new CheckPlanListReqDTO();
        checkPlanListReqDTO.setInstrmPlanNo(meaInfoReqDTO.getInstrmPlanNo());
        List<CheckPlanResDTO> list = checkPlanMapper.listCheckPlan(checkPlanListReqDTO);
        if (StringUtils.isNotEmpty(list)) {
            if (!list.get(0).getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
            }
            if (!CommonConstants.TEN_STRING.equals(list.get(0).getPlanStatus())) {
                throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
            }
        }
        meaInfoReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        meaInfoReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        checkPlanMapper.modifyInfo(meaInfoReqDTO);
    }

    @Override
    public void deleteCheckPlanInfo(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                MeaInfoResDTO res = checkPlanMapper.getInfoDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                CheckPlanListReqDTO checkPlanListReqDTO = new CheckPlanListReqDTO();
                checkPlanListReqDTO.setInstrmPlanNo(res.getInstrmPlanNo());
                List<CheckPlanResDTO> list = checkPlanMapper.listCheckPlan(checkPlanListReqDTO);
                if (StringUtils.isNotEmpty(list)) {
                    if (!list.get(0).getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                        throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                    }
                    if (!CommonConstants.TEN_STRING.equals(list.get(0).getPlanStatus())) {
                        throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                    }
                }
                checkPlanMapper.deleteCheckPlanDetail(id, null, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportCheckPlanInfo(List<String> ids, HttpServletResponse response) throws IOException {
        List<MeaInfoResDTO> meaInfoList = checkPlanMapper.exportInfo(ids);
        if (meaInfoList != null && !meaInfoList.isEmpty()) {
            List<ExcelMeaInfoResDTO> list = new ArrayList<>();
            for (MeaInfoResDTO resDTO : meaInfoList) {
                ExcelMeaInfoResDTO res = new ExcelMeaInfoResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "定检计划明细信息", list);
        }
    }

    @Override
    public Page<MeaInfoResDTO> queryCheckPlanInfo(MeaInfoQueryReqDTO meaInfoQueryReqDTO, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return checkPlanMapper.queryDetail(pageReqDTO.of(), meaInfoQueryReqDTO);
    }

}
