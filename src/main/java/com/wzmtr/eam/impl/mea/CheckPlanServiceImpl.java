package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.mea.CheckPlanListReqDTO;
import com.wzmtr.eam.dto.req.mea.CheckPlanReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaInfoQueryReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaInfoReqDTO;
import com.wzmtr.eam.dto.req.bpmn.BpmnExamineDTO;
import com.wzmtr.eam.dto.res.mea.CheckPlanResDTO;
import com.wzmtr.eam.dto.res.mea.MeaInfoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.mea.CheckPlanMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.mea.CheckPlanService;
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
public class CheckPlanServiceImpl implements CheckPlanService {

    @Autowired
    private CheckPlanMapper checkPlanMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private BpmnService bpmnService;

    @Override
    public Page<CheckPlanResDTO> pageCheckPlan(CheckPlanListReqDTO checkPlanListReqDTO, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<CheckPlanResDTO> page = checkPlanMapper.pageCheckPlan(pageReqDTO.of(), checkPlanListReqDTO);
        List<CheckPlanResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
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
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        String recCreator = TokenUtil.getCurrentPersonId();
        CurrentLoginUser user = TokenUtil.getCurrentPerson();
        String userName = user.getPersonName();
        String editDeptCode = user.getOfficeAreaId() == null ? user.getOfficeId() : user.getOfficeAreaId();
        String recCreateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
        String archiveFlag = "0";
        String instrmPlanNo = checkPlanMapper.getMaxCode();
        if (StringUtils.isEmpty(instrmPlanNo) || !(CommonConstants.TWENTY_STRING + instrmPlanNo.substring(2, 8)).equals(day.format(System.currentTimeMillis()))) {
            instrmPlanNo = "JP" + day.format(System.currentTimeMillis()).substring(2) + "0001";
        } else {
            instrmPlanNo = CodeUtils.getNextCode(instrmPlanNo, 8);
        }
        checkPlanReqDTO.setRecId(TokenUtil.getUuId());
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
        if (!res.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
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
        if (planList != null && planList.size() != 0) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "该定检计划已存在");
        }
        checkPlanReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        checkPlanReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        checkPlanMapper.modifyCheckPlan(checkPlanReqDTO);
    }

    @Override
    public void deleteCheckPlan(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                CheckPlanResDTO res = checkPlanMapper.getCheckPlanDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                if (!res.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!CommonConstants.TEN_STRING.equals(res.getPlanStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                checkPlanMapper.deleteCheckPlanDetail(null, res.getInstrmPlanNo(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                if (StringUtils.isNotBlank(res.getWorkFlowInstId())) {
                    BpmnExamineDTO bpmnExamineDTO = new BpmnExamineDTO();
                    bpmnExamineDTO.setTaskId(res.getWorkFlowInstId());
                    bpmnService.rejectInstance(bpmnExamineDTO);
                }
                checkPlanMapper.deleteCheckPlan(id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    // ServiceDMAM0201
    @Override
    public void submitCheckPlan(CheckPlanReqDTO checkPlanReqDTO) throws Exception {
        CheckPlanResDTO res = checkPlanMapper.getCheckPlanDetail(checkPlanReqDTO.getRecId());
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!res.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        List<MeaInfoResDTO> result = checkPlanMapper.listInfo(null, res.getInstrmPlanNo());
        if (result.size() == 0) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "此定检计划不存在计划明细，无法提交");
        }
        if (!CommonConstants.TEN_STRING.equals(res.getPlanStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        } else {
            String processId = bpmnService.commit(res.getInstrmPlanNo(), BpmnFlowEnum.CHECK_PLAN_SUBMIT.value(), null, null, checkPlanReqDTO.getExamineReqDTO().getUserIds(), null);
            if (processId == null || "-1".equals(processId)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败");
            }
            CheckPlanReqDTO reqDTO = new CheckPlanReqDTO();
            BeanUtils.copyProperties(res, reqDTO);
            reqDTO.setWorkFlowInstId(processId);
            reqDTO.setWorkFlowInstStatus(roleMapper.getSubmitNodeId(BpmnFlowEnum.CHECK_PLAN_SUBMIT.value(),null));
            reqDTO.setPlanStatus("20");
            reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
            reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            checkPlanMapper.modifyCheckPlan(reqDTO);
        }
    }

    @Override
    public void examineCheckPlan(CheckPlanReqDTO checkPlanReqDTO) {
        CheckPlanResDTO res = checkPlanMapper.getCheckPlanDetail(checkPlanReqDTO.getRecId());
        CheckPlanReqDTO reqDTO = new CheckPlanReqDTO();
        BeanUtils.copyProperties(res, reqDTO);
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
            }
        }
        reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        checkPlanMapper.modifyCheckPlan(reqDTO);
    }

    @Override
    public void exportCheckPlan(CheckPlanListReqDTO checkPlanListReqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "定检计划号", "年月", "编制部门", "计划人", "计划状态", "备注");
        List<CheckPlanResDTO> checkPlanList = checkPlanMapper.listCheckPlan(checkPlanListReqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (checkPlanList != null && !checkPlanList.isEmpty()) {
            for (CheckPlanResDTO resDTO : checkPlanList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("定检计划号", resDTO.getInstrmPlanNo());
                map.put("年月", resDTO.getPlanPeriodMark());
                map.put("编制部门", organizationMapper.getNamesById(resDTO.getEditDeptCode()));
                map.put("计划人", resDTO.getPlanCreaterName());
                map.put("计划状态", CommonConstants.TEN_STRING.equals(resDTO.getPlanStatus()) ? "编辑" : CommonConstants.TWENTY_STRING.equals(resDTO.getPlanStatus()) ? "审核中" : "审核通过");
                map.put("备注", resDTO.getPlanNote());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("定检计划信息", listName, list, null, response);
    }

    @Override
    public Page<MeaInfoResDTO> pageCheckPlanInfo(String equipCode, String instrmPlanNo, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
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
        if (list.size() != 0) {
            if (!list.get(0).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
            }
            if (!CommonConstants.TEN_STRING.equals(list.get(0).getPlanStatus())) {
                throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "新增");
            }
        }
        meaInfoReqDTO.setRecId(TokenUtil.getUuId());
        meaInfoReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        meaInfoReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
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
        if (list.size() != 0) {
            if (!list.get(0).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
            }
            if (!CommonConstants.TEN_STRING.equals(list.get(0).getPlanStatus())) {
                throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
            }
        }
        meaInfoReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        meaInfoReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        checkPlanMapper.modifyInfo(meaInfoReqDTO);
    }

    @Override
    public void deleteCheckPlanInfo(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                MeaInfoResDTO res = checkPlanMapper.getInfoDetail(id);
                if (Objects.isNull(res)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                CheckPlanListReqDTO checkPlanListReqDTO = new CheckPlanListReqDTO();
                checkPlanListReqDTO.setInstrmPlanNo(res.getInstrmPlanNo());
                List<CheckPlanResDTO> list = checkPlanMapper.listCheckPlan(checkPlanListReqDTO);
                if (list.size() != 0) {
                    if (!list.get(0).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                        throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                    }
                    if (!CommonConstants.TEN_STRING.equals(list.get(0).getPlanStatus())) {
                        throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                    }
                }
                checkPlanMapper.deleteCheckPlanDetail(id, null, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportCheckPlanInfo(String equipCode, String instrmPlanNo, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "计划号", "计量器具编码", "名称", "出厂编号");
        List<MeaInfoResDTO> meaInfoList = checkPlanMapper.listInfo(equipCode, instrmPlanNo);
        List<Map<String, String>> list = new ArrayList<>();
        if (meaInfoList != null && !meaInfoList.isEmpty()) {
            for (MeaInfoResDTO resDTO : meaInfoList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("计划号", resDTO.getInstrmPlanNo());
                map.put("计量器具编码", resDTO.getEquipCode());
                map.put("名称", resDTO.getEquipName());
                map.put("出厂编号", resDTO.getManufactureNo());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("定检计划明细信息", listName, list, null, response);
    }

    @Override
    public Page<MeaInfoResDTO> queryCheckPlanInfo(MeaInfoQueryReqDTO meaInfoQueryReqDTO, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return checkPlanMapper.queryDetail(pageReqDTO.of(), meaInfoQueryReqDTO);
    }

}
