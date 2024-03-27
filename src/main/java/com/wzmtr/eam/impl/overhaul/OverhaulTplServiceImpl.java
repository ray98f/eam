package com.wzmtr.eam.impl.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.bizobject.WorkFlowLogBO;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.overhaul.OverhaulMaterialReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulTplDetailReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulTplReqDTO;
import com.wzmtr.eam.dto.req.overhaul.excel.ExcelOverhaulMaterialReqDTO;
import com.wzmtr.eam.dto.req.overhaul.excel.ExcelOverhaulTplDetailReqDTO;
import com.wzmtr.eam.dto.req.overhaul.excel.ExcelOverhaulTplReqDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

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
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulTplMapper.pageOverhaulTpl(pageReqDTO.of(), templateId, templateName, lineCode, position1Code, subjectCode, systemCode, equipTypeCode, null);
    }

    @Override
    public OverhaulTplResDTO getOverhaulTplDetail(String id) {
        return overhaulTplMapper.getOverhaulTplDetail(id);
    }

    @Override
    public void addOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulTplMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        overhaulTplReqDTO.setRecId(TokenUtils.getUuId());
        overhaulTplReqDTO.setTrialStatus("10");
        overhaulTplReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        overhaulTplReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        String templateId = overhaulTplMapper.getMaxCode();
        if (StringUtils.isEmpty(templateId)) {
            templateId = "JM000000";
        }
        templateId = CodeUtils.getNextCode(templateId, 2);
        overhaulTplReqDTO.setTemplateId(templateId);
        overhaulTplMapper.addOverhaulTpl(overhaulTplReqDTO);
    }

    @Override
    public void modifyOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulTplMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
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
        overhaulTplReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulTplReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulTplMapper.modifyOverhaulTpl(overhaulTplReqDTO);
    }

    @Override
    public void deleteOverhaulTpl(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                OverhaulTplResDTO resDTO = overhaulTplMapper.getOverhaulTplDetail(id);
                OverhaulTplReqDTO overhaulTplReqDTO = new OverhaulTplReqDTO();
                BeanUtils.copyProperties(overhaulTplReqDTO, resDTO);
                if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
                    if (Objects.isNull(overhaulTplReqDTO.getSubjectCode())) {
                        throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                    }
                    List<String> code = overhaulTplMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
                    if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulTplReqDTO.getSubjectCode())) {
                        throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
                    }
                }
                overhaulTplMapper.deleteOverhaulTplDetail(null, id, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
                overhaulTplMapper.deleteOverhaulTpl(id, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void changeOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO) {
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulTplMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
            if (Objects.isNull(code) || code.isEmpty() || !code.contains(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
        }
        overhaulTplReqDTO.setTrialStatus("10");
        overhaulTplReqDTO.setWorkFlowInstId("");
        overhaulTplReqDTO.setWorkFlowInstStatus("");
        overhaulTplReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulTplReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulTplMapper.changeOverhaulTpl(overhaulTplReqDTO);
    }

    @Override
    public void submitOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO) throws Exception {
        // ServiceDMER0003
        if (!CommonConstants.ADMIN.equals(TokenUtils.getCurrentPersonId())) {
            if (Objects.isNull(overhaulTplReqDTO.getSubjectCode())) {
                throw new CommonException(ErrorCode.ONLY_OWN_SUBJECT);
            }
            List<String> code = overhaulTplMapper.getSubjectByUserId(TokenUtils.getCurrentPersonId());
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
        List<Role> roles = roleMapper.getLoginRole(TokenUtils.getCurrentPersonId());
        List<String> roleCode = new ArrayList<>();
        if (!roles.isEmpty()) {
            // roleCode = roles.stream().map(Role::getRoleCode).collect(Collectors.toList());
            roleCode = StreamUtils.mapToList(roles, r -> StringUtils.isNotEmpty(r.getRoleCode()), Role::getRoleCode);
        }
        boolean bool = !roleCode.isEmpty() && (roleCode.contains("5") || roleCode.contains("6"));
        if (bool) {
            overhaulTplReqDTO.setWorkFlowInstStatus("运营-车辆专工：" + TokenUtils.getCurrentPersonId());
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
        overhaulTplReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulTplReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
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
        overhaulTplReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulTplReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulTplMapper.modifyOverhaulTpl(overhaulTplReqDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importOverhaulTpl(MultipartFile file) {
        List<ExcelOverhaulTplReqDTO> listTpl = EasyExcelUtils.read(file, ExcelOverhaulTplReqDTO.class, 0);
        List<ExcelOverhaulTplDetailReqDTO> listTplDetail = EasyExcelUtils.read(file, ExcelOverhaulTplDetailReqDTO.class, 1);
        List<ExcelOverhaulMaterialReqDTO> listMaterial = EasyExcelUtils.read(file, ExcelOverhaulMaterialReqDTO.class, 2);
        if (StringUtils.isNotEmpty(listTpl)) {
            importOverhaulTpl(listTpl);
        }
        if (StringUtils.isNotEmpty(listTplDetail)) {
            importOverhaulTplDetail(listTplDetail);
        }
        if (StringUtils.isNotEmpty(listMaterial)) {
            importOverhaulTplMaterial(listMaterial);
        }
    }

    /**
     * 导入检修模板
     * @param listTpl 检修模板导入列表
     */
    private void importOverhaulTpl(List<ExcelOverhaulTplReqDTO> listTpl) {
        List<OverhaulTplReqDTO> temp = new ArrayList<>();
        if (!Objects.isNull(listTpl) && !listTpl.isEmpty()) {
            for (ExcelOverhaulTplReqDTO reqDTO : listTpl) {
                OverhaulTplReqDTO req = new OverhaulTplReqDTO();
                BeanUtils.copyProperties(reqDTO, req);
                req.setLineNo("S1线".equals(req.getLineName()) ? "01" : "02");
                String templateId = overhaulTplMapper.getMaxCode();
                if (StringUtils.isEmpty(templateId)) {
                    templateId = "JM000000";
                }
                templateId = CodeUtils.getNextCode(templateId, 2);
                req.setTemplateId(templateId);
                req.setRecId(TokenUtils.getUuId());
                req.setTrialStatus("10");
                req.setRecCreator(TokenUtils.getCurrentPersonId());
                req.setRecCreateTime(DateUtils.getCurrentTime());
                temp.add(req);
            }
        }
        if (StringUtils.isNotEmpty(temp)) {
            overhaulTplMapper.importOverhaulTpl(temp);
        }
    }

    /**
     * 导入检修模板检修项
     * @param listTplDetail 检修模板检修项导入列表
     */
    private void importOverhaulTplDetail(List<ExcelOverhaulTplDetailReqDTO> listTplDetail) {
        List<OverhaulTplDetailReqDTO> temp = new ArrayList<>();
        if (!Objects.isNull(listTplDetail) && !listTplDetail.isEmpty()) {
            for (ExcelOverhaulTplDetailReqDTO reqDTO : listTplDetail) {
                // 校验导入数据，删除无效数据
                if (checkImportOverhaulTplDetail(reqDTO)) {
                    continue;
                }
                OverhaulTplDetailReqDTO req = new OverhaulTplDetailReqDTO();
                BeanUtils.copyProperties(reqDTO, req);
                req.setItemType("列表".equals(req.getItemType()) ? "10" : "数值".equals(req.getItemType()) ? "20" : "30");
                req.setTemplateId(overhaulTplMapper.getTemplateId(reqDTO.getTemplateName(), reqDTO.getLineName(),
                        reqDTO.getSubjectName(), reqDTO.getSystemName(), reqDTO.getEquipTypeName()));
                req.setRecId(TokenUtils.getUuId());
                req.setRecCreator(TokenUtils.getCurrentPersonId());
                req.setRecCreateTime(DateUtils.getCurrentTime());
                if (StringUtils.isNotEmpty(req.getTrainNumber())) {
                    req.setTrainNumber(req.getTrainNumber().substring(0, 2));
                }
                temp.add(req);
            }
        }
        if (StringUtils.isNotEmpty(temp)) {
            overhaulTplMapper.importOverhaulTplDetail(temp);
        }
    }

    /**
     * 导入检修模板物料
     * @param listMaterial 检修模板物料导入模板
     */
    private void importOverhaulTplMaterial(List<ExcelOverhaulMaterialReqDTO> listMaterial) {
        List<OverhaulMaterialReqDTO> temp = new ArrayList<>();
        if (!Objects.isNull(listMaterial) && !listMaterial.isEmpty()) {
            for (ExcelOverhaulMaterialReqDTO reqDTO : listMaterial) {
                OverhaulMaterialReqDTO req = new OverhaulMaterialReqDTO();
                BeanUtils.copyProperties(reqDTO, req);
                req.setTemplateId(overhaulTplMapper.getTemplateId(reqDTO.getTemplateName(), reqDTO.getLineName(),
                        reqDTO.getSubjectName(), reqDTO.getSystemName(), reqDTO.getEquipTypeName()));
                req.setRecId(TokenUtils.getUuId());
                req.setRecCreator(TokenUtils.getCurrentPersonId());
                req.setRecCreateTime(DateUtils.getCurrentTime());
                temp.add(req);
            }
        }
        if (StringUtils.isNotEmpty(temp)) {
            overhaulTplMapper.importOverhaulMaterial(temp);
        }
    }

    @Override
    public void exportOverhaulTpl(List<String> ids, HttpServletResponse response) throws IOException {
        List<OverhaulTplResDTO> overhaulTplResDTOList = overhaulTplMapper.exportOverhaulTpl(ids);
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
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulTplMapper.pageOverhaulDetailTpl(pageReqDTO.of(), templateId);
    }

    @Override
    public OverhaulTplDetailResDTO getOverhaulTplDetailDetail(String id) {
        return overhaulTplMapper.getOverhaulTplDetailDetail(id);
    }

    @Override
    public void addOverhaulTplDetail(OverhaulTplDetailReqDTO overhaulTplDetailReqDTO) {
        // 校验新增数据
        checkOverhaulTplDetail(overhaulTplDetailReqDTO);
        List<OverhaulTplResDTO> list = overhaulTplMapper.listOverhaulTpl(overhaulTplDetailReqDTO.getTemplateId(),
                null, null, null, null, null, null, null);
        if (!Objects.isNull(list) && !list.isEmpty()) {
            overhaulTplDetailReqDTO.setTemplateName(list.get(0).getTemplateName());
        }
        overhaulTplDetailReqDTO.setRecId(TokenUtils.getUuId());
        overhaulTplDetailReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        overhaulTplDetailReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        overhaulTplMapper.addOverhaulTplDetail(overhaulTplDetailReqDTO);
    }

    @Override
    public void modifyOverhaulTplDetail(OverhaulTplDetailReqDTO overhaulTplDetailReqDTO) {
        // 校验修改数据
        checkOverhaulTplDetail(overhaulTplDetailReqDTO);
        overhaulTplDetailReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulTplDetailReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulTplMapper.modifyOverhaulTplDetail(overhaulTplDetailReqDTO);
    }

    /**
     * 校验检修项参数
     * @param reqDTO 检修项参数
     */
    private void checkOverhaulTplDetail(OverhaulTplDetailReqDTO reqDTO) {
        Pattern pattern = RegularUtils.getNumberPattern();
        if (CommonConstants.TEN_STRING.equals(reqDTO.getItemType()) && Objects.isNull(reqDTO.getInspectItemValue())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "当类型为列表时，可选值为必填项！");
        }
        boolean bool = CommonConstants.TWENTY_STRING.equals(reqDTO.getItemType()) &&
                (org.apache.commons.lang3.StringUtils.isBlank(reqDTO.getDefaultValue()) || !pattern.matcher(reqDTO.getDefaultValue()).matches());
        if (bool) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "当类型为数字时，默认值必须填数字！");
        }
        List<OverhaulTplResDTO> list = overhaulTplMapper.listOverhaulTpl(reqDTO.getTemplateId(), null, null, null, null, null, null, "10");
        if (StringUtils.isEmpty(list)) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
        }
        if (StringUtils.isNotBlank(reqDTO.getMinValue()) && !pattern.matcher(reqDTO.getMinValue()).matches()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "下限必须填数字！");
        }
        if (StringUtils.isNotBlank(reqDTO.getMaxValue()) && !pattern.matcher(reqDTO.getMaxValue()).matches()) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "上限必须填数字！");
        }
        bool = StringUtils.isNotBlank(reqDTO.getMinValue()) && StringUtils.isNotBlank(reqDTO.getMaxValue()) &&
                CommonConstants.TWENTY_STRING.equals(reqDTO.getItemType()) && Integer.parseInt(reqDTO.getMaxValue()) <= Integer.parseInt(reqDTO.getMinValue());
        if (bool) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "下限不能大于等于上限！");
        }
    }

    /**
     * 导入时校验检修项参数
     * @param reqDTO 检修项参数
     * @return 是否校验通过
     */
    private boolean checkImportOverhaulTplDetail(ExcelOverhaulTplDetailReqDTO reqDTO) {
        Pattern pattern = RegularUtils.getNumberPattern();
        if (CommonConstants.TEN_STRING.equals(reqDTO.getItemType()) && Objects.isNull(reqDTO.getInspectItemValue())) {
            return true;
        }
        boolean bool = CommonConstants.TWENTY_STRING.equals(reqDTO.getItemType()) &&
                (org.apache.commons.lang3.StringUtils.isBlank(reqDTO.getDefaultValue()) || !pattern.matcher(reqDTO.getDefaultValue()).matches());
        if (bool) {
            return true;
        }
        if (StringUtils.isNotBlank(reqDTO.getMinValue()) && !pattern.matcher(reqDTO.getMinValue()).matches()) {
            return true;
        }
        if (StringUtils.isNotBlank(reqDTO.getMaxValue()) && !pattern.matcher(reqDTO.getMaxValue()).matches()) {
            return true;
        }
        if (StringUtils.isNotBlank(reqDTO.getMinValue()) && StringUtils.isNotBlank(reqDTO.getMaxValue())) {
            return CommonConstants.TWENTY_STRING.equals(reqDTO.getItemType()) && Integer.parseInt(reqDTO.getMaxValue()) <= Integer.parseInt(reqDTO.getMinValue());
        }
        return false;
    }

    @Override
    public void deleteOverhaulTplDetail(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                OverhaulTplDetailResDTO resDTO = overhaulTplMapper.getOverhaulTplDetailDetail(id);
                if (Objects.isNull(resDTO)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                List<OverhaulTplResDTO> list = overhaulTplMapper.listOverhaulTpl(resDTO.getTemplateId(), null, null, null, null, null, null, "10");
                if (Objects.isNull(list) || list.isEmpty()) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
                }
                overhaulTplMapper.deleteOverhaulTplDetail(id, null, TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
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
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return overhaulTplMapper.pageOverhaulMaterial(pageReqDTO.of(), templateId);
    }

    @Override
    public OverhaulMaterialResDTO getOverhaulMaterialDetail(String id) {
        return overhaulTplMapper.getOverhaulMaterialDetail(id);
    }

    @Override
    public void addOverhaulMaterial(OverhaulMaterialReqDTO overhaulMaterialReqDTO) {
        List<OverhaulTplResDTO> list = overhaulTplMapper.listOverhaulTpl(overhaulMaterialReqDTO.getTemplateId(), null, null, null, null, null, null, "10");
        if (StringUtils.isEmpty(list)) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "操作");
        }
        List<OverhaulTplResDTO> listStatus = overhaulTplMapper.listOverhaulTplStatus(overhaulMaterialReqDTO.getTemplateId(), null);
        if (StringUtils.isNotEmpty(listStatus)) {
            overhaulMaterialReqDTO.setTemplateName(listStatus.get(0).getTemplateName());
        }
        overhaulMaterialReqDTO.setRecId(TokenUtils.getUuId());
        overhaulMaterialReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        overhaulMaterialReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        overhaulTplMapper.addOverhaulMaterial(overhaulMaterialReqDTO);
    }

    @Override
    public void modifyOverhaulMaterial(OverhaulMaterialReqDTO overhaulMaterialReqDTO) {
        overhaulMaterialReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        overhaulMaterialReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        overhaulTplMapper.modifyOverhaulMaterial(overhaulMaterialReqDTO);
    }

    @Override
    public void deleteOverhaulMaterial(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            overhaulTplMapper.deleteOverhaulMaterial(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
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
