package com.wzmtr.eam.impl.specialEquip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.DetectionDetailReqDTO;
import com.wzmtr.eam.dto.req.DetectionReqDTO;
import com.wzmtr.eam.dto.res.DetectionDetailResDTO;
import com.wzmtr.eam.dto.res.DetectionResDTO;
import com.wzmtr.eam.dto.res.DetectionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.specialEquip.DetectionMapper;
import com.wzmtr.eam.service.specialEquip.DetectionService;
import com.wzmtr.eam.utils.CodeUtils;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author frp
 */
@Service
@Slf4j
public class DetectionServiceImpl implements DetectionService {

    @Autowired
    private DetectionMapper detectionMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Page<DetectionResDTO> pageDetection(String checkNo, String sendVerifyNo, String editDeptCode, String recStatus, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<DetectionResDTO> page = detectionMapper.pageDetection(pageReqDTO.of(), checkNo, sendVerifyNo, editDeptCode, recStatus);
        List<DetectionResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (DetectionResDTO resDTO : list) {
                resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
                resDTO.setSecOrgName(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
                resDTO.setEditDeptName(organizationMapper.getExtraOrgByAreaId(resDTO.getEditDeptCode()));
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public DetectionResDTO getDetectionDetail(String id) {
        DetectionResDTO resDTO = detectionMapper.getDetectionDetail(id);
        if (Objects.isNull(resDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
        resDTO.setSecOrgName(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
        resDTO.setEditDeptName(organizationMapper.getExtraOrgByAreaId(resDTO.getEditDeptCode()));
        return resDTO;
    }

    @Override
    public void addDetection(DetectionReqDTO detectionReqDTO) {
        SimpleDateFormat min = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
        detectionReqDTO.setRecId(TokenUtil.getUuId());
        detectionReqDTO.setArchiveFlag("0");
        detectionReqDTO.setRecStatus("10");
        detectionReqDTO.setEditDeptCode(TokenUtil.getCurrentPerson().getOfficeAreaId());
        detectionReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        detectionReqDTO.setRecCreateTime(min.format(System.currentTimeMillis()));
        String checkNo = detectionMapper.getMaxCode();
        if (checkNo == null || "".equals(checkNo) || !("20" + checkNo).substring(0, 8).equals(day.format(System.currentTimeMillis()))) {
            checkNo = "TJ" + day.format(System.currentTimeMillis()).substring(2) + "00001";
        } else {
            checkNo = CodeUtils.getNextCode(checkNo, 8);
        }
        detectionReqDTO.setCheckNo(checkNo);
        detectionMapper.addDetection(detectionReqDTO);
    }

    @Override
    public void modifyDetection(DetectionReqDTO detectionReqDTO) {
        DetectionResDTO resDTO = detectionMapper.getDetectionDetail(detectionReqDTO.getRecId());
        if (Objects.isNull(resDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!"10".equals(resDTO.getRecStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        if (!detectionReqDTO.getAssetKindCode().equals(resDTO.getAssetKindCode())) {
            if (detectionMapper.hasDetail(detectionReqDTO.getRecId()).size() != 0) {
                throw new CommonException(ErrorCode.PLAN_HAS_DETAIL);
            }
        }
        detectionReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        detectionReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        detectionMapper.modifyDetection(detectionReqDTO);
    }

    @Override
    public void deleteDetection(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                DetectionResDTO resDTO = detectionMapper.getDetectionDetail(id);
                if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!"10".equals(resDTO.getRecStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                detectionMapper.deleteDetectionDetail(resDTO.getRecId(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                detectionMapper.deleteDetection(id, TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void submitDetection(String id) {
        // todo ServiceDMSE0301 submit
    }

    @Override
    public void exportDetection(String checkNo, String sendVerifyNo, String editDeptCode, String recStatus, HttpServletResponse response) {
        List<String> listName = Arrays.asList("检测单号", "特种设备分类", "管理部门", "维管部门", "编制部门", "检测单状态", "备注");
        List<DetectionResDTO> detectionResDTOList = detectionMapper.listDetection(null, checkNo, sendVerifyNo, editDeptCode, recStatus);
        List<Map<String, String>> list = new ArrayList<>();
        if (detectionResDTOList != null && !detectionResDTOList.isEmpty()) {
            for (DetectionResDTO resDTO : detectionResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("检测单号", resDTO.getCheckNo());
                map.put("特种设备分类", resDTO.getAssetKindCode());
                map.put("管理部门", organizationMapper.getOrgById(resDTO.getManageOrg()));
                map.put("维管部门", organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
                map.put("编制部门", organizationMapper.getExtraOrgByAreaId(resDTO.getEditDeptCode()));
                map.put("检测单状态", resDTO.getVerifyDept());
                map.put("备注", resDTO.getVerifyNote());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("检测记录信息", listName, list, null, response);
    }

    @Override
    public Page<DetectionDetailResDTO> pageDetectionDetail(String testRecId, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return detectionMapper.pageDetectionDetail(pageReqDTO.of(), testRecId);
    }

    @Override
    public DetectionDetailResDTO getDetectionDetailDetail(String id) {
        return detectionMapper.getDetectionDetailDetail(id);
    }

    @Override
    public void addDetectionDetail(DetectionDetailReqDTO detectionDetailReqDTO) throws ParseException {
        List<DetectionResDTO> list = detectionMapper.listDetection(detectionDetailReqDTO.getTestRecId(), null, null, null, null);
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        DetectionResDTO resDTO = list.get(0);
        if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!"10".equals(resDTO.getRecStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (sdf.parse(detectionDetailReqDTO.getVerifyValidityDate()).before(sdf.parse(detectionDetailReqDTO.getVerifyDate()))) {
            throw new CommonException(ErrorCode.VERIFY_DATE_ERROR);
        }
        detectionDetailReqDTO.setRecId(TokenUtil.getUuId());
        detectionDetailReqDTO.setArchiveFlag("0");
        detectionDetailReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        detectionDetailReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        detectionMapper.addDetectionDetail(detectionDetailReqDTO);
    }

    @Override
    public void modifyDetectionDetail(DetectionDetailReqDTO detectionDetailReqDTO) throws ParseException {
        List<DetectionResDTO> list = detectionMapper.listDetection(detectionDetailReqDTO.getTestRecId(), null, null, null, null);
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        DetectionResDTO resDTO = list.get(0);
        if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
            throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
        }
        if (!"10".equals(resDTO.getRecStatus())) {
            throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "修改");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (sdf.parse(detectionDetailReqDTO.getVerifyValidityDate()).before(sdf.parse(detectionDetailReqDTO.getVerifyDate()))) {
            throw new CommonException(ErrorCode.VERIFY_DATE_ERROR);
        }
        detectionDetailReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        detectionDetailReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        detectionMapper.modifyDetectionDetail(detectionDetailReqDTO);
    }

    @Override
    public void deleteDetectionDetail(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                DetectionDetailResDTO detailResDTO = detectionMapper.getDetectionDetailDetail(id);
                if (Objects.isNull(detailResDTO)) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                List<DetectionResDTO> list = detectionMapper.listDetection(detailResDTO.getTestRecId(), null, null, null, null);
                if (Objects.isNull(list) || list.isEmpty()) {
                    throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
                }
                DetectionResDTO resDTO = list.get(0);
                if (!resDTO.getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
                if (!"10".equals(resDTO.getRecStatus())) {
                    throw new CommonException(ErrorCode.CAN_NOT_MODIFY, "删除");
                }
                detectionMapper.deleteDetectionDetail(resDTO.getRecId(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
            }
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void exportDetectionDetail(String testRecId, HttpServletResponse response) {
        List<String> listName = Arrays.asList("设备编码", "名称", "上次检测日期", "上次检测有效期", "位置一名称", "位置二名称", "型号规格");
        List<DetectionDetailResDTO> detectionPlanDetailResDTOList = detectionMapper.listDetectionDetail(testRecId);
        List<Map<String, String>> list = new ArrayList<>();
        if (detectionPlanDetailResDTOList != null && !detectionPlanDetailResDTOList.isEmpty()) {
            for (DetectionDetailResDTO resDTO : detectionPlanDetailResDTOList) {
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
