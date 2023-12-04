package com.wzmtr.eam.impl.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.overhaul.OverhaulMaterialReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulTplDetailReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulTplReqDTO;
import com.wzmtr.eam.dto.res.common.PersonListResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulMaterialResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.Role;
import com.wzmtr.eam.enums.BpmnFlowEnum;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.RoleMapper;
import com.wzmtr.eam.mapper.overhaul.OverhaulTplMapper;
import com.wzmtr.eam.service.bpmn.BpmnService;
import com.wzmtr.eam.service.overhaul.OverhaulTplService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

    // ServiceDMER0003
    @Override
    public void submitOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO) throws Exception {
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
            roleCode = roles.stream().map(Role::getRoleCode).collect(Collectors.toList());
        }
        boolean bool = !roleCode.isEmpty() && (roleCode.contains("5") || roleCode.contains("6"));
        if (bool) {
            overhaulTplReqDTO.setWorkFlowInstStatus("运营-车辆专工：" + TokenUtil.getCurrentPersonId());
            overhaulTplReqDTO.setTrialStatus("30");
        } else {
            String processId = bpmnService.commit(overhaulTplReqDTO.getTemplateId(), BpmnFlowEnum.OVERHAUL_TPL_SUBMIT.value(), null, null, overhaulTplReqDTO.getExamineReqDTO().getUserIds(), null);
            overhaulTplReqDTO.setWorkFlowInstStatus(roleMapper.getSubmitNodeId(BpmnFlowEnum.OVERHAUL_TPL_SUBMIT.value()));
            if (processId == null || "-1".equals(processId)) {
                throw new CommonException(ErrorCode.NORMAL_ERROR, "提交失败！");
            }
            overhaulTplReqDTO.setWorkFlowInstId(processId);
            overhaulTplReqDTO.setTrialStatus("20");
        }
        overhaulTplReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        overhaulTplReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        overhaulTplMapper.modifyOverhaulTpl(overhaulTplReqDTO);
    }

    @Override
    public void examineOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO) {
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
                                  String majorCode, String systemCode, String equipTypeCode, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "模板编号", "模板名称", "线路", "专业", "系统", "设备类别", "审批状态");
        List<OverhaulTplResDTO> overhaulTplResDTOList = overhaulTplMapper.listOverhaulTpl(templateId, templateName, lineNo, position1Code, majorCode, systemCode, equipTypeCode, null);
        List<Map<String, String>> list = new ArrayList<>();
        if (overhaulTplResDTOList != null && !overhaulTplResDTOList.isEmpty()) {
            for (OverhaulTplResDTO resDTO : overhaulTplResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("模板编号", resDTO.getTemplateId());
                map.put("模板名称", resDTO.getTemplateName());
                map.put("线路", resDTO.getLineName());
                map.put("专业", resDTO.getSubjectName());
                map.put("系统", resDTO.getSystemName());
                map.put("设备类别", resDTO.getEquipTypeName());
                map.put("审批状态", CommonConstants.TEN_STRING.equals(resDTO.getTrialStatus()) ? "编辑" : CommonConstants.TWENTY_STRING.equals(resDTO.getTrialStatus()) ? "审核中" : "审核通过");
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("检修模板信息", listName, list, null, response);
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
    public void exportOverhaulTplDetail(String templateId, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "模块顺序", "检修模块", "检修项顺序", "车组号", "检修项", "技术要求", "检修项类型", "可选值", "默认值", "上限", "下限", "单位", "备注");
        List<OverhaulTplDetailResDTO> overhaulTplDetailResDTOList = overhaulTplMapper.listOverhaulTplDetail(templateId);
        List<Map<String, String>> list = new ArrayList<>();
        if (overhaulTplDetailResDTOList != null && !overhaulTplDetailResDTOList.isEmpty()) {
            for (OverhaulTplDetailResDTO resDTO : overhaulTplDetailResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("模块顺序", resDTO.getModelSequence());
                map.put("检修模块", resDTO.getModelName());
                map.put("检修项顺序", resDTO.getSequenceId());
                map.put("车组号", (!"".equals(resDTO.getTrainNumber()) && !" ".equals(resDTO.getTrainNumber())) ? resDTO.getTrainNumber() + "车" : "");
                map.put("检修项", resDTO.getItemName());
                map.put("技术要求", resDTO.getExt1());
                map.put("检修项类型", CommonConstants.TEN_STRING.equals(resDTO.getItemType()) ? "列表" : CommonConstants.TWENTY_STRING.equals(resDTO.getItemType()) ? "数值" : "文本");
                map.put("可选值", resDTO.getInspectItemValue());
                map.put("默认值", resDTO.getDefaultValue());
                map.put("上限", resDTO.getMaxValue());
                map.put("下限", resDTO.getMinValue());
                map.put("单位", resDTO.getItemUnit());
                map.put("备注", resDTO.getRemark());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("检修项信息", listName, list, null, response);
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
    public void exportOverhaulMaterial(String templateId, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "模板编号", "模板名称", "物资编码", "物资名称", "物资名称", "规格型号", "计量单位", "数量", "备注");
        List<OverhaulMaterialResDTO> overhaulMaterialResDTOList = overhaulTplMapper.listOverhaulMaterial(templateId);
        List<Map<String, String>> list = new ArrayList<>();
        if (overhaulMaterialResDTOList != null && !overhaulMaterialResDTOList.isEmpty()) {
            for (OverhaulMaterialResDTO resDTO : overhaulMaterialResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("模板编号", resDTO.getTemplateId());
                map.put("模板名称", resDTO.getTemplateName());
                map.put("物资编码", resDTO.getMaterialCode());
                map.put("物资名称", resDTO.getMaterialName());
                map.put("规格型号", resDTO.getMaterialSpec());
                map.put("计量单位", resDTO.getUnitName());
                map.put("数量", String.valueOf(resDTO.getQuantity()));
                map.put("备注", resDTO.getRemark());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("物料信息", listName, list, null, response);
    }

}
