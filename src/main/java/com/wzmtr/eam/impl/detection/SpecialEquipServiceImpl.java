package com.wzmtr.eam.impl.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.detection.DetectionDetailReqDTO;
import com.wzmtr.eam.dto.req.detection.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.req.detection.excel.ExcelSpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.detection.SpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.detection.SpecialEquipResDTO;
import com.wzmtr.eam.dto.res.detection.SpecialEquipTypeResDTO;
import com.wzmtr.eam.dto.res.detection.excel.ExcelSpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.detection.excel.ExcelSpecialEquipResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.detection.SpecialEquipMapper;
import com.wzmtr.eam.mapper.detection.SpecialEquipTypeMapper;
import com.wzmtr.eam.service.detection.DetectionService;
import com.wzmtr.eam.service.detection.SpecialEquipService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class SpecialEquipServiceImpl implements SpecialEquipService {

    @Autowired
    private SpecialEquipMapper specialEquipMapper;

    @Autowired
    private SpecialEquipTypeMapper specialEquipTypeMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private DetectionService detectionService;

    @Override
    public Page<SpecialEquipResDTO> pageSpecialEquip(String equipCode, String equipName, String specialEquipCode, String factNo,
                                                     String useLineNo, String position1Code, String specialEquipType, String equipStatus, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<SpecialEquipResDTO> page =  specialEquipMapper.pageSpecialEquip(pageReqDTO.of(), equipCode, equipName, specialEquipCode, factNo, useLineNo,
                position1Code, specialEquipType, equipStatus);
        List<SpecialEquipResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (SpecialEquipResDTO resDTO : list) {
                if (StringUtils.isNotEmpty(resDTO.getManageOrg())) {
                    resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
                }
                if (StringUtils.isNotEmpty(resDTO.getSecOrg())) {
                    resDTO.setSecOrgName(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
                }
                if (StringUtils.isNotEmpty(resDTO.getVerifyValidityDate())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String day = DateUtils.getDayByMonth(3);
                    try {
                        resDTO.setIsWarn(sdf.parse(day).getTime() <= sdf.parse(resDTO.getVerifyValidityDate()).getTime() ? 0 : 1);
                    } catch (ParseException e) {
                        log.error("设备编码为" + resDTO.getEquipCode() + "的特种设备检测有效期时间异常");
                    }
                }
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public SpecialEquipResDTO getSpecialEquipDetail(String id) {
        SpecialEquipResDTO resDTO = specialEquipMapper.getSpecialEquipDetail(id);
        if (Objects.isNull(resDTO)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (StringUtils.isNotEmpty(resDTO.getManageOrg())) {
            resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
        }
        if (StringUtils.isNotEmpty(resDTO.getSecOrg())) {
            resDTO.setSecOrgName(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
        }
        return resDTO;
    }

    @Override
    public void importSpecialEquip(MultipartFile file) throws ParseException {
        List<String> specialCode = new ArrayList<>();
        List<ExcelSpecialEquipReqDTO> list = EasyExcelUtils.read(file, ExcelSpecialEquipReqDTO.class);
        for (ExcelSpecialEquipReqDTO reqDTO : list) {
            SpecialEquipReqDTO req = new SpecialEquipReqDTO();
            BeanUtils.copyProperties(reqDTO, req);
            req.setRecCreator(TokenUtils.getCurrentPersonId());
            req.setRecCreateTime(DateUtils.getCurrentTime());
            req.setRecRevisor(TokenUtils.getCurrentPersonId());
            req.setRecReviseTime(DateUtils.getCurrentTime());
            if (StringUtils.isNotEmpty(reqDTO.getSpecialEquipType())) {
                SpecialEquipTypeResDTO res = specialEquipTypeMapper.getSpecialEquipTypeDetailByType(null, reqDTO.getSpecialEquipType());
                if (Objects.isNull(res)) {
                    specialCode.add(req.getSpecialEquipCode());
                    continue;
                }
                req.setSpecialEquipType(res.getTypeCode());
            }
            SysOffice manageOrg = organizationMapper.getByNames(reqDTO.getManageOrg());
            SysOffice secOrg = organizationMapper.getByNames(reqDTO.getSecOrg());
            if (Objects.isNull(manageOrg) || Objects.isNull(secOrg)) {
                specialCode.add(req.getSpecialEquipCode());
                continue;
            }
            req.setManageOrg(manageOrg.getId());
            req.setSecOrg(secOrg.getId());
            specialEquipMapper.updateEquip(req);
            if (specialEquipMapper.selectSpecialEquipIsExist(req) > 0) {
                specialEquipMapper.modifySpecialEquip(req);
            } else {
                req.setRecId(TokenUtils.getUuId());
                specialEquipMapper.addSpecialEquip(req);
                DetectionDetailReqDTO detectionDetailReqDTO = buildDetectionDetail(req);
                detectionService.addNormalDetectionDetail(detectionDetailReqDTO);
            }
        }
        if (StringUtils.isNotEmpty(specialCode)) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "特种设备编号为" + String.join("、", specialCode) + "的特种设备导入失败，请重试");
        }
    }

    /**
     * 检测记录拼装
     * @param req 特种设备参数
     * @return 检测记录
     */
    @NotNull
    private static DetectionDetailReqDTO buildDetectionDetail(SpecialEquipReqDTO req) {
        DetectionDetailReqDTO detectionDetailReqDTO = new DetectionDetailReqDTO();
        detectionDetailReqDTO.setEquipCode(req.getEquipCode());
        detectionDetailReqDTO.setEquipName(req.getEquipName());
        detectionDetailReqDTO.setVerifyDate(req.getVerifyDate());
        detectionDetailReqDTO.setVerifyValidityDate(req.getVerifyValidityDate());
        detectionDetailReqDTO.setVerifyResult(req.getVerifyResult());
        detectionDetailReqDTO.setVerifyConclusion(req.getVerifyConclusion());
        return detectionDetailReqDTO;
    }

    @Override
    public void addSpecialEquip(SpecialEquipReqDTO specialEquipReqDTO) {
        specialEquipReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        specialEquipReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        specialEquipMapper.addSpecialEquip(specialEquipReqDTO);
    }

    @Override
    public void modifySpecialEquip(SpecialEquipReqDTO specialEquipReqDTO) {
        specialEquipReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        specialEquipReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        specialEquipMapper.modifySpecialEquip(specialEquipReqDTO);
    }

    @Override
    public void exportSpecialEquip(List<String> ids, HttpServletResponse response) throws IOException {
        List<SpecialEquipResDTO> specialEquipResDTOList = specialEquipMapper.listSpecialEquip(ids);
        if (specialEquipResDTOList != null && !specialEquipResDTOList.isEmpty()) {
            List<ExcelSpecialEquipResDTO> list = new ArrayList<>();
            for (SpecialEquipResDTO resDTO : specialEquipResDTOList) {
                ExcelSpecialEquipResDTO res = new ExcelSpecialEquipResDTO();
                BeanUtils.copyProperties(resDTO, res);
                SpecialEquipTypeResDTO specialEquipType = specialEquipTypeMapper.getSpecialEquipTypeDetailByType(resDTO.getSpecialEquipType(), null);
                if (!Objects.isNull(specialEquipType)) {
                    res.setSpecialEquipType(specialEquipType.getTypeName());
                }
                res.setUseLineNo(CommonConstants.LINE_CODE_ONE.equals(resDTO.getUseLineNo()) ? "S1线" : "S2线");
                list.add(res);
            }
            EasyExcelUtils.export(response, "特种设备台账信息", list);
        }
    }

    @Override
    public Page<SpecialEquipHistoryResDTO> pageSpecialEquipHistory(String equipCode, PageReqDTO pageReqDTO) {
        PageMethod.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return specialEquipMapper.pageSpecialEquipHistory(pageReqDTO.of(), equipCode);
    }

    @Override
    public SpecialEquipHistoryResDTO getSpecialEquipHistoryDetail(String id) {
        return specialEquipMapper.getSpecialEquipHistoryDetail(id);
    }

    @Override
    public void exportSpecialEquipHistory(String equipCode, HttpServletResponse response) throws IOException {
        List<SpecialEquipHistoryResDTO> specialEquipHistoryResDTOList = specialEquipMapper.listSpecialEquipHistory(equipCode);
        if (specialEquipHistoryResDTOList != null && !specialEquipHistoryResDTOList.isEmpty()) {
            List<ExcelSpecialEquipHistoryResDTO> list = new ArrayList<>();
            for (SpecialEquipHistoryResDTO resDTO : specialEquipHistoryResDTOList) {
                ExcelSpecialEquipHistoryResDTO res = new ExcelSpecialEquipHistoryResDTO();
                BeanUtils.copyProperties(resDTO, res);
                list.add(res);
            }
            EasyExcelUtils.export(response, "特种设备检测记录历史信息", list);
        }
    }

}
