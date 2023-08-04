package com.wzmtr.eam.impl.specialEquip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.DetectionPlanDetailReqDTO;
import com.wzmtr.eam.dto.req.DetectionPlanReqDTO;
import com.wzmtr.eam.dto.res.DetectionPlanDetailResDTO;
import com.wzmtr.eam.dto.res.DetectionPlanResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.specialEquip.DetectionPlanMapper;
import com.wzmtr.eam.service.specialEquip.DetectionPlanService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Override
    public Page<DetectionPlanResDTO> pageDetectionPlan(String instrmPlanNo, String planStatus, String editDeptCode,
                                                       String assetKindCode, String planPeriodMark, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<DetectionPlanResDTO> page = detectionPlanMapper.pageDetectionPlan(pageReqDTO.of(), instrmPlanNo, planStatus, editDeptCode, assetKindCode, planPeriodMark);
        List<DetectionPlanResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (DetectionPlanResDTO resDTO : list) {
                resDTO.setManageOrg(organizationMapper.getOrgById(resDTO.getManageOrg()));
                resDTO.setSecOrg(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
                resDTO.setEditDeptCode(organizationMapper.getExtraOrgByAreaId(resDTO.getEditDeptCode()));
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
        resDTO.setManageOrg(organizationMapper.getOrgById(resDTO.getManageOrg()));
        resDTO.setSecOrg(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
        resDTO.setEditDeptCode(organizationMapper.getExtraOrgByAreaId(resDTO.getEditDeptCode()));
        return resDTO;
    }

    @Override
    public void addDetectionPlan(DetectionPlanReqDTO specialEquipReqDTO) {
        SimpleDateFormat min = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        specialEquipReqDTO.setRecId(TokenUtil.getUuId());
        specialEquipReqDTO.setArchiveFlag("0");
        specialEquipReqDTO.setPlanStatus("10");
        specialEquipReqDTO.setEditDeptCode(TokenUtil.getCurrentPerson().getOfficeAreaId());
        specialEquipReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        specialEquipReqDTO.setRecCreateTime(min.format(System.currentTimeMillis()));
        String instrmPlanNo = detectionPlanMapper.getMaxCode();
        if (instrmPlanNo == null || "".equals(instrmPlanNo) || !("20" + instrmPlanNo).substring(0, 8).equals(day.format(System.currentTimeMillis()))) {
            instrmPlanNo = "TP" + day.format(System.currentTimeMillis()).substring(2) + "00001";
        } else {
            instrmPlanNo = CodeUtils.getNextCode(instrmPlanNo, 8);
        }
        specialEquipReqDTO.setInstrmPlanNo(instrmPlanNo);
        if ("".equals(specialEquipReqDTO.getPlanPeriodMark())) {
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
        if (!"10".equals(resDTO.getPlanStatus())) {
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
                if (!"10".equals(resDTO.getPlanStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                detectionPlanMapper.deleteDetectionPlanDetail(resDTO.getInstrmPlanNo(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                detectionPlanMapper.deleteDetectionPlan(id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void submitDetectionPlan(String id) {
        // todo ServiceDMSE0001 submit
    }

    @Override
    public void exportDetectionPlan(String instrmPlanNo, String planStatus, String editDeptCode,
                                    String assetKindCode, String planPeriodMark, HttpServletResponse response) {
        List<String> listName = Arrays.asList("检测计划号", "特种设备分类", "年月", "检测委托人", "检测委托人电话", "管理部门", "维管部门", "编制部门", "检测单位", "计划状态", "备注");
        List<DetectionPlanResDTO> detectionPlanResDTOList = detectionPlanMapper.listDetectionPlan(instrmPlanNo, planStatus, editDeptCode, assetKindCode, planPeriodMark);
        List<Map<String, String>> list = new ArrayList<>();
        if (detectionPlanResDTOList != null && !detectionPlanResDTOList.isEmpty()) {
            for (DetectionPlanResDTO resDTO : detectionPlanResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("检测计划号", resDTO.getInstrmPlanNo());
                map.put("特种设备分类", resDTO.getInstrmPlanType());
                map.put("年月", resDTO.getPlanPeriodMark());
                map.put("检测委托人", resDTO.getSendConsignerName());
                map.put("检测委托人电话", resDTO.getSendConsignerTele());
                map.put("管理部门", organizationMapper.getExtraOrgByAreaId(resDTO.getManageOrg()));
                map.put("维管部门", organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
                map.put("编制部门", organizationMapper.getExtraOrgByAreaId(resDTO.getEditDeptCode()));
                map.put("检测单位", resDTO.getVerifyDept());
                map.put("计划状态", resDTO.getPlanStatus());
                map.put("备注", resDTO.getPlanNote());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("检测计划信息", listName, list, null, response);
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
        if (!"10".equals(resDTO.getPlanStatus())) {
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
        if (!"10".equals(resDTO.getPlanStatus())) {
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
                if (!"10".equals(resDTO.getPlanStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                detectionPlanMapper.deleteDetectionPlanDetail(resDTO.getInstrmPlanNo(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportDetectionPlanDetail(String instrmPlanNo, HttpServletResponse response) {
        List<String> listName = Arrays.asList("设备编码", "名称", "上次检测日期", "上次检测有效期", "位置一名称", "位置二名称", "型号规格");
        List<DetectionPlanDetailResDTO> detectionPlanDetailResDTOList = detectionPlanMapper.listDetectionPlanDetail(instrmPlanNo);
        List<Map<String, String>> list = new ArrayList<>();
        if (detectionPlanDetailResDTOList != null && !detectionPlanDetailResDTOList.isEmpty()) {
            for (DetectionPlanDetailResDTO resDTO : detectionPlanDetailResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("设备编码", resDTO.getEquipCode());
                map.put("名称", resDTO.getEquipName());
                map.put("上次检测日期", resDTO.getVerifyDate());
                map.put("上次检测有效期", resDTO.getVerifyValidityDate());
                map.put("位置一名称", resDTO.getPosition1Name());
                map.put("位置二名称", resDTO.getPosition2Name());
                map.put("型号规格", resDTO.getMatSpecifi());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("检测计划明细信息", listName, list, null, response);
    }
}
