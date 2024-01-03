package com.wzmtr.eam.impl.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.overhaul.OverhaulMaterialReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulTplDetailReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulTplReqDTO;
import com.wzmtr.eam.dto.res.common.PersonListResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulMaterialResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulMaterialResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.excel.ExcelOverhaulTplResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.Role;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.BpmnStatus;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulTplMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.bpmn.IWorkFlowLogService;
import com.wzmtr.eam.service.overhaul.OverhaulTplService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author frp
 */
@Service
@Slf4j
public class OverhaulTplServiceImpl implements OverhaulTplService {

    @Autowired
    private OverhaulTplMapper overhaulTplMapper;

    @Autowired
    private BpmnService bpmnService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private IWorkFlowLogService workFlowLogService;

    @Override
    public Page<OverhaulTplResDTO> pageOverhaulTpl(String templateId, String templateName, String lineCode, String position1Code,
                                                   String subjectCode, String systemCode, String equipTypeCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulTplMapper.pageOverhaulTpl(pageReqDTO.of(), templateId, templateName, lineCode, position1Code, subjectCode, systemCode, equipTypeCode, null);
    }

    @Override
    public OverhaulTplResDTO getOverhaulTplDetail(String id) {
        return overhaulTplMapper.getOverhaulTplDetail(id);
    }

    @Override
    public void addOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulTplMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        overhaulTplReqDTO.setRecId(TokenUtil.getUuId());
        overhaulTplReqDTO.setTrialStatus("10");
        overhaulTplReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        overhaulTplReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        String templateId = CodeUtils.getNextCode(overhaulTplMapper.getMaxCode(), 2);
        overhaulTplReqDTO.setTemplateId(templateId);
        overhaulTplMapper.addOverhaulTpl(overhaulTplReqDTO);
    }

    @Override
    public void modifyOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulTplMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        OverhaulTplResDTO resDTO = overhaulTplMapper.getOverhaulTplDetail(overhaulTplReqDTO.getRecId());
        if (Objects.isNull(resDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!CommonConstants.TEN_STRING.equals(resDTO.getTrialStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        overhaulTplReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulTplReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulTplMapper.modifyOverhaulTpl(overhaulTplReqDTO);
    }

    @Override
    public void deleteOverhaulTpl(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                OverhaulTplResDTO resDTO = overhaulTplMapper.getOverhaulTplDetail(id);
                OverhaulTplReqDTO overhaulTplReqDTO = new OverhaulTplReqDTO();
                BeanUtils.copyProperties(overhaulTplReqDTO, resDTO);
                if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
                    if (Objects.isNull(overhaulTplReqDTO.getSubjectCode())) {
                        throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                    }
                    List<String> code = overhaulTplMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
                    if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulTplReqDTO.getSubjectCode())) {
                        throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                    }
                }
                overhaulTplMapper.deleteOverhaulTplDetail(null, id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                overhaulTplMapper.deleteOverhaulTpl(id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void changeOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulTplMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        overhaulTplReqDTO.setTrialStatus("10");
        overhaulTplReqDTO.setWorkFlowInstId("");
        overhaulTplReqDTO.setWorkFlowInstStatus("");
        overhaulTplReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulTplReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulTplMapper.changeOverhaulTpl(overhaulTplReqDTO);
    }

    @Override
    public void submitOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO) throws Exception {
        // ServiceDMER0003
        if (!CommonConstants.ADMIN.equals(TokenUtil.getCurrentPersonId())) {
            if (Objects.isNull(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulTplMapper.getSubjectByUserId(TokenUtil.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        if (!CommonConstants.TEN_STRING.equals(overhaulTplReqDTO.getTrialStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "送审");
        }
        List<OverhaulTplDetailResDTO> list = overhaulTplMapper.listOverhaulTplDetail(overhaulTplReqDTO.getTemplateId());
        if (list == null || list.size() <= 0) {
            throw new CommonException(ErrorCode.NO_DETAIL, "勾选模板中没有检修项！");
        }
        List<Role> roles = roleMapper.getLoginRole(TokenUtil.getCurrentPersonId());
        List<String> roleCode = new ArrayList<>();
        if (!roles.isEmpty()) {
            // roleCode = roles.stream().map(Role::getRoleCode).collect(Collectors.toList());
            roleCode = StreamUtil.mapToList(roles, r -> StringUtils.isNotEmpty(r.getRoleCode()), Role::getRoleCode);
        }
        boolean bool = !roleCode.isEmpty() && (roleCode.contains("5") || roleCode.contains("6"));
        if (bool) {
            overhaulTplReqDTO.setWorkFlowInstStatus("运营-车辆专工：" + TokenUtil.getCurrentPersonId());
            overhaulTplReqDTO.setTrialStatus("30");
        } else {
            String processId = bpmnService.commit(overhaulTplReqDTO.getTemplateId(), BpmnFlowEnum.OVERHAUL_TPL_SUBMIT.value(), null, null, overhaulTplReqDTO.getExamineReqDTO().getUserIds(), null);
            overhaulTplReqDTO.setWorkFlowInstStatus(roleMapper.getSubmitNodeId(BpmnFlowEnum.OVERHAUL_TPL_SUBMIT.value(),null));
            if (processId == null || CommonConstants.PROCESS_ERROR_CODE.equals(processId)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败！");
            }
            overhaulTplReqDTO.setWorkFlowInstId(processId);
            overhaulTplReqDTO.setTrialStatus("20");
            // 记录日志
            workFlowLogService.add(WorkFlowLogBO.builder()
                    .status(BpmnStatus.SUBMIT.getDesc())
                    .userIds(overhaulTplReqDTO.getExamineReqDTO().getUserIds())
                    .workFlowInstId(processId)
                    .build());
        }
        overhaulTplReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulTplReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulTplMapper.modifyOverhaulTpl(overhaulTplReqDTO);
    }

    @Override
    public void examineOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO) {
        workFlowLogService.ifReviewer(overhaulTplReqDTO.getWorkFlowInstId());
        if (overhaulTplReqDTO.getExamineReqDTO().getExamineStatus() == 0) {
            if (CommonConstants.THIRTY_STRING.equals(overhaulTplReqDTO.getTrialStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_DONE);
            }
            if (CommonConstants.TEN_STRING.equals(overhaulTplReqDTO.getTrialStatus())) {
                throw new CommonException(ErrorCode.EXAMINE_NOT_DONE);
            }
            String processId = overhaulTplReqDTO.getWorkFlowInstId();
            String taskId = bpmnService.queryTaskIdByProcId(processId);
            bpmnService.agree(taskId, overhaulTplReqDTO.getExamineReqDTO().getOpinion(), null, "{\"id\":\"" + overhaulTplReqDTO.getTemplateId() + "\"}", null);
            overhaulTplReqDTO.setWorkFlowInstStatus("已完成");
            overhaulTplReqDTO.setTrialStatus("30");
            // 记录日志
            workFlowLogService.add(WorkFlowLogBO.builder()
                    .status(BpmnStatus.PASS.getDesc())
                    .userIds(overhaulTplReqDTO.getExamineReqDTO().getUserIds())
                    .workFlowInstId(processId)
                    .build());
        } else {
            if (!CommonConstants.TWENTY_STRING.equals(overhaulTplReqDTO.getTrialStatus())) {
                throw new CommonException(ErrorCode.REJECT_ERROR);
            } else {
                String processId = overhaulTplReqDTO.getWorkFlowInstId();
                String taskId = bpmnService.queryTaskIdByProcId(processId);
                bpmnService.reject(taskId, overhaulTplReqDTO.getExamineReqDTO().getOpinion());
                overhaulTplReqDTO.setWorkFlowInstId("");
                overhaulTplReqDTO.setWorkFlowInstStatus("");
                overhaulTplReqDTO.setTrialStatus("10");
                // 记录日志
                workFlowLogService.add(WorkFlowLogBO.builder()
                        .status(BpmnStatus.REJECT.getDesc())
                        .userIds(overhaulTplReqDTO.getExamineReqDTO().getUserIds())
                        .workFlowInstId(processId)
                        .build());
            }
        }
        overhaulTplReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulTplReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulTplMapper.modifyOverhaulTpl(overhaulTplReqDTO);
    }

    @Override
    public void importOverhaulTpl(MultipartFile file) {
        // todo excel导入
    }

    @Override
    public void exportOverhaulTpl(String templateId, String templateName, String lineNo, String position1Code,
                                  String majorCode, String systemCode, String equipTypeCode, HttpServletResponse response) throws IOException {
        List<OverhaulTplResDTO> overhaulTplResDTOList = overhaulTplMapper.listOverhaulTpl(templateId, templateName, lineNo, position1Code, majorCode, systemCode, equipTypeCode, null);
        if (overhaulTplResDTOList != null && !overhaulTplResDTOList.isEmpty()) {
            List<ExcelOverhaulTplResDTO> list = new ArrayList<>();
            for (OverhaulTplResDTO resDTO : overhaulTplResDTOList) {
                ExcelOverhaulTplResDTO res = new ExcelOverhaulTplResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setTrialStatus(CommonConstants.TEN_STRING.equals(resDTO.getTrialStatus()) ? "编辑" : CommonConstants.TWENTY_STRING.equals(resDTO.getTrialStatus()) ? "审核中" : "审核通过");
                list.add(res);
            }
            EasyExcelUtils.export(response, "检修模板信息", list);
        }
    }

    @Override
    public Page<OverhaulTplDetailResDTO> pageOverhaulDetailTpl(String templateId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulTplMapper.pageOverhaulDetailTpl(pageReqDTO.of(), templateId);
    }

    @Override
    public OverhaulTplDetailResDTO getOverhaulTplDetailDetail(String id) {
        return overhaulTplMapper.getOverhaulTplDetailDetail(id);
    }

    @Override
    public void addOverhaulTplDetail(OverhaulTplDetailReqDTO overhaulTplDetailReqDTO) {
        Pattern pattern = RegularUtils.getNumberPattern();
        if (CommonConstants.TEN_STRING.equals(overhaulTplDetailReqDTO.getItemType()) && Objects.isNull(overhaulTplDetailReqDTO.getInspectItemValue())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "当类型为列表时，可选值为必填项！");
        }
        if (CommonConstants.TWENTY_STRING.equals(overhaulTplDetailReqDTO.getItemType())) {
            if (StringUtils.isBlank(overhaulTplDetailReqDTO.getDefaultValue()) || !pattern.matcher(overhaulTplDetailReqDTO.getDefaultValue()).matches()) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "当类型为数字时，默认值必须填数字！");
            }
        }
        List<OverhaulTplResDTO> list = overhaulTplMapper.listOverhaulTpl(overhaulTplDetailReqDTO.getTemplateId(), null, null, null, null, null, null, "10");
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
        }
        if (StringUtils.isNotBlank(overhaulTplDetailReqDTO.getMinValue()) && !pattern.matcher(overhaulTplDetailReqDTO.getMinValue()).matches()) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "下限必须填数字！");
        }
        if (StringUtils.isNotBlank(overhaulTplDetailReqDTO.getMaxValue()) && !pattern.matcher(overhaulTplDetailReqDTO.getMaxValue()).matches()) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "上限必须填数字！");
        }
        if (StringUtils.isNotBlank(overhaulTplDetailReqDTO.getMinValue()) && StringUtils.isNotBlank(overhaulTplDetailReqDTO.getMaxValue())) {
            if (CommonConstants.TWENTY_STRING.equals(overhaulTplDetailReqDTO.getItemType()) && Integer.parseInt(overhaulTplDetailReqDTO.getMaxValue()) <= Integer.parseInt(overhaulTplDetailReqDTO.getMinValue())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "下限不能大于等于上限！");
            }
        }
        list = overhaulTplMapper.listOverhaulTpl(overhaulTplDetailReqDTO.getTemplateId(), null, null, null, null, null, null, null);
        if (!Objects.isNull(list) && !list.isEmpty()) {
            overhaulTplDetailReqDTO.setTemplateName(list.get(0).getTemplateName());
        }
        overhaulTplDetailReqDTO.setRecId(TokenUtil.getUuId());
        overhaulTplDetailReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        overhaulTplDetailReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulTplMapper.addOverhaulTplDetail(overhaulTplDetailReqDTO);
    }

    @Override
    public void modifyOverhaulTplDetail(OverhaulTplDetailReqDTO overhaulTplDetailReqDTO) {
        Pattern pattern = RegularUtils.getNumberPattern();
        if (CommonConstants.TEN_STRING.equals(overhaulTplDetailReqDTO.getItemType())) {
            if (Objects.isNull(overhaulTplDetailReqDTO.getInspectItemValue())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "当类型为列表时，可选值为必填项！");
            }
        } else if (CommonConstants.TWENTY_STRING.equals(overhaulTplDetailReqDTO.getItemType()) && !pattern.matcher(overhaulTplDetailReqDTO.getDefaultValue()).matches()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "当类型为数字时，默认值必须填数字！");
        }
        List<OverhaulTplResDTO> list = overhaulTplMapper.listOverhaulTpl(overhaulTplDetailReqDTO.getTemplateId(), null, null, null, null, null, null, "10");
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
        }
        if (overhaulTplDetailReqDTO.getMaxValue() != null && overhaulTplDetailReqDTO.getMinValue() != null) {
            if (!pattern.matcher(overhaulTplDetailReqDTO.getMaxValue()).matches() || !pattern.matcher(overhaulTplDetailReqDTO.getMinValue()).matches()) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "下限、上限必须填数字！");
            }
            if (CommonConstants.TWENTY_STRING.equals(overhaulTplDetailReqDTO.getItemType()) && Integer.parseInt(overhaulTplDetailReqDTO.getMaxValue()) <= Integer.parseInt(overhaulTplDetailReqDTO.getMinValue())) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "下限不能大于等于上限！");
            }
        }
        overhaulTplDetailReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulTplDetailReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulTplMapper.modifyOverhaulTplDetail(overhaulTplDetailReqDTO);
    }

    @Override
    public void deleteOverhaulTplDetail(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                OverhaulTplDetailResDTO resDTO = overhaulTplMapper.getOverhaulTplDetailDetail(id);
                if (Objects.isNull(resDTO)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                List<OverhaulTplResDTO> list = overhaulTplMapper.listOverhaulTpl(resDTO.getTemplateId(), null, null, null, null, null, null, "10");
                if (Objects.isNull(list) || list.isEmpty()) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
                }
                overhaulTplMapper.deleteOverhaulTplDetail(id, null, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        }
    }

    @Override
    public void exportOverhaulTplDetail(String templateId, HttpServletResponse response) throws IOException {
        List<OverhaulTplDetailResDTO> overhaulTplDetailResDTOList = overhaulTplMapper.listOverhaulTplDetail(templateId);
        if (overhaulTplDetailResDTOList != null && !overhaulTplDetailResDTOList.isEmpty()) {
            List<ExcelOverhaulTplDetailResDTO> list = new ArrayList<>();
            for (OverhaulTplDetailResDTO resDTO : overhaulTplDetailResDTOList) {
                ExcelOverhaulTplDetailResDTO res = new ExcelOverhaulTplDetailResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setTrainNumber(StringUtils.isNotEmpty(resDTO.getTrainNumber()) ? resDTO.getTrainNumber() + "车" : "");
                res.setItemType(CommonConstants.TEN_STRING.equals(resDTO.getItemType()) ? "列表" : CommonConstants.TWENTY_STRING.equals(resDTO.getItemType()) ? "数值" : "文本");
                list.add(res);
            }
            EasyExcelUtils.export(response, "检修项信息", list);
        }
    }

    @Override
    public Page<OverhaulMaterialResDTO> pageOverhaulMaterial(String templateId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulTplMapper.pageOverhaulMaterial(pageReqDTO.of(), templateId);
    }

    @Override
    public OverhaulMaterialResDTO getOverhaulMaterialDetail(String id) {
        return overhaulTplMapper.getOverhaulMaterialDetail(id);
    }

    @Override
    public void addOverhaulMaterial(OverhaulMaterialReqDTO overhaulMaterialReqDTO) {
        List<OverhaulTplResDTO> list = overhaulTplMapper.listOverhaulTpl(overhaulMaterialReqDTO.getTemplateId(), null, null, null, null, null, null, "10");
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
        }
        List<OverhaulTplResDTO> listStatus = overhaulTplMapper.listOverhaulTplStatus(overhaulMaterialReqDTO.getTemplateId(), null);
        if (!Objects.isNull(listStatus) && !listStatus.isEmpty()) {
            overhaulMaterialReqDTO.setTemplateName(listStatus.get(0).getTemplateName());
        }
        overhaulMaterialReqDTO.setRecId(TokenUtil.getUuId());
        overhaulMaterialReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        overhaulMaterialReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulTplMapper.addOverhaulMaterial(overhaulMaterialReqDTO);
    }

    @Override
    public void modifyOverhaulMaterial(OverhaulMaterialReqDTO overhaulMaterialReqDTO) {
        overhaulMaterialReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulMaterialReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulTplMapper.modifyOverhaulMaterial(overhaulMaterialReqDTO);
    }

    @Override
    public void deleteOverhaulMaterial(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            overhaulTplMapper.deleteOverhaulMaterial(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        }
    }

    @Override
    public void exportOverhaulMaterial(String templateId, HttpServletResponse response) throws IOException {
        List<OverhaulMaterialResDTO> overhaulMaterialResDTOList = overhaulTplMapper.listOverhaulMaterial(templateId);
        if (overhaulMaterialResDTOList != null && !overhaulMaterialResDTOList.isEmpty()) {
            List<ExcelOverhaulMaterialResDTO> list = new ArrayList<>();
            for (OverhaulMaterialResDTO resDTO : overhaulMaterialResDTOList) {
                ExcelOverhaulMaterialResDTO res = new ExcelOverhaulMaterialResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setQuantity(String.valueOf(resDTO.getQuantity()));
                list.add(res);
            }
            EasyExcelUtils.export(response, "物料信息", list);
        }
    }

}
