package com.wzmtr.eam.impl.specialEquip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.bpmn.ExamineReqDTO;
import com.wzmtr.eam.dto.req.specialEquip.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.req.specialEquip.excel.ExcelSpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.specialEquip.SpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.specialEquip.SpecialEquipResDTO;
import com.wzmtr.eam.dto.res.specialEquip.excel.ExcelSpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.specialEquip.excel.ExcelSpecialEquipResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.specialEquip.SpecialEquipMapper;
import com.wzmtr.eam.service.specialEquip.SpecialEquipService;
import com.wzmtr.eam.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.wzmtr.eam.constant.CommonConstants.XLS;
import static com.wzmtr.eam.constant.CommonConstants.XLSX;

/**
 * @author frp
 */
@Service
@Slf4j
public class SpecialEquipServiceImpl implements SpecialEquipService {

    @Autowired
    private SpecialEquipMapper specialEquipMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private DictionariesMapper dictionariesMapper;

    @Override
    public Page<SpecialEquipResDTO> pageSpecialEquip(String equipCode, String equipName, String specialEquipCode, String factNo,
                                                     String useLineNo, String position1Code, String specialEquipType, String equipStatus, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<SpecialEquipResDTO> page =  specialEquipMapper.pageSpecialEquip(pageReqDTO.of(), equipCode, equipName, specialEquipCode, factNo, useLineNo,
                position1Code, specialEquipType, equipStatus);
        List<SpecialEquipResDTO> list = page.getRecords();
        if (list != null && !list.isEmpty()) {
            for (SpecialEquipResDTO resDTO : list) {
                if (StringUtils.isNotEmpty(resDTO.getManageOrg())) {
                    resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
                }
                if (StringUtils.isNotEmpty(resDTO.getSecOrg())) {
                    resDTO.setSecOrgName(organizationMapper.getExtraOrgByAreaId(resDTO.getSecOrg()));
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
    public void importSpecialEquip(MultipartFile file) {
        try {
            List<ExcelSpecialEquipReqDTO> list = EasyExcelUtils.read(file, ExcelSpecialEquipReqDTO.class);
            for (ExcelSpecialEquipReqDTO reqDTO : list) {
                SpecialEquipReqDTO req = new SpecialEquipReqDTO();
                BeanUtils.copyProperties(reqDTO, req);
                req.setRecId(TokenUtil.getUuId());
                req.setRecCreator(TokenUtil.getCurrentPersonId());
                req.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                req.setRecRevisor(TokenUtil.getCurrentPersonId());
                req.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                if (StringUtils.isNotEmpty(reqDTO.getSpecialEquipType())) {
                    req.setSpecialEquipType("电梯".equals(reqDTO.getSpecialEquipType()) ? "10" : "起重机".equals(reqDTO.getSpecialEquipType()) ? "20" : "场（厂）内专用机动车辆".equals(reqDTO.getSpecialEquipType()) ? "30" : "40");
                }
                SysOffice manageOrg = organizationMapper.getByNames(reqDTO.getManageOrg());
                SysOffice secOrg = organizationMapper.getByNames(reqDTO.getSecOrg());
                if (Objects.isNull(manageOrg) || Objects.isNull(secOrg)) {
                    continue;
                }
                req.setManageOrg(manageOrg.getId());
                req.setSecOrg(secOrg.getId());
                specialEquipMapper.updateEquip(req);
                specialEquipMapper.modifySpecialEquip(req);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void modifySpecialEquip(SpecialEquipReqDTO specialEquipReqDTO) {
        specialEquipReqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
        specialEquipReqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        specialEquipMapper.modifySpecialEquip(specialEquipReqDTO);
    }

    @Override
    public void exportSpecialEquip(String equipCode, String equipName, String specialEquipCode, String factNo,
                                   String useLineNo, String position1Code, String specialEquipType, String equipStatus, HttpServletResponse response) throws IOException {
        List<SpecialEquipResDTO> specialEquipResDTOList = specialEquipMapper.listSpecialEquip(equipCode, equipName, specialEquipCode, factNo, useLineNo,
                position1Code, specialEquipType, equipStatus);
        if (specialEquipResDTOList != null && !specialEquipResDTOList.isEmpty()) {
            List<ExcelSpecialEquipResDTO> list = new ArrayList<>();
            for (SpecialEquipResDTO resDTO : specialEquipResDTOList) {
                ExcelSpecialEquipResDTO res = new ExcelSpecialEquipResDTO();
                BeanUtils.copyProperties(resDTO, res);
                List<Dictionaries> dicList = dictionariesMapper.list("dm.specialequiptype", resDTO.getSpecialEquipType() == null ? " " : resDTO.getSpecialEquipType(), null);
                if (dicList != null && !dicList.isEmpty()) {
                    res.setSpecialEquipType(dicList.get(0).getItemCname());
                } else {
                    res.setSpecialEquipType("");
                }
                res.setUseLineNo(CommonConstants.LINE_CODE_ONE.equals(resDTO.getUseLineNo()) ? "S1线" : "S2线");
                list.add(res);
            }
            EasyExcelUtils.export(response, "特种设备台账信息", list);
        }
    }

    @Override
    public Page<SpecialEquipHistoryResDTO> pageSpecialEquipHistory(String equipCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return specialEquipMapper.pageSpecialEquipHistory(pageReqDTO.of(), equipCode);
    }

    @Override
    public SpecialEquipHistoryResDTO getSpecialEquipHistoryDetail(String id) {
        return specialEquipMapper.getSpecialEquipHistoryDetail(id);
    }

    @Override
    public void exportSpecialEquipHistory(String equipCode, HttpServletResponse response) throws IOException {
        List<SpecialEquipHistoryResDTO> specialEquipHistoryResDTOList = specialEquipMapper.listSpecialEquiHistory(equipCode);
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
