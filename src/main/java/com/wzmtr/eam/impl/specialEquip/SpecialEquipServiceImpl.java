package com.wzmtr.eam.impl.specialEquip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.specialEquip.SpecialEquipReqDTO;
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
            Workbook workbook;
            String fileName = file.getOriginalFilename();
            FileInputStream fileInputStream = new FileInputStream(FileUtils.transferToFile(file));
            if (Objects.requireNonNull(fileName).endsWith(XLS)) {
                workbook = new HSSFWorkbook(fileInputStream);
            } else if (fileName.endsWith(XLSX)) {
                workbook = new XSSFWorkbook(fileInputStream);
            } else {
                throw new CommonException(ErrorCode.PARAM_NULL_ERROR);
            }
            Sheet sheet = workbook.getSheetAt(0);
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                SpecialEquipReqDTO reqDTO = new SpecialEquipReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                reqDTO.setEquipCode(cells.getCell(0) == null ? "" : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setSpecialEquipCode(cells.getCell(1) == null ? "" : cells.getCell(1).getStringCellValue());
                cells.getCell(2).setCellType(CellType.STRING);
                reqDTO.setEquipName(cells.getCell(2) == null ? "" : cells.getCell(2).getStringCellValue());
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setSpecialEquipType(cells.getCell(3) == null ? "" : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setVerifyDate(cells.getCell(4) == null ? "" : cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(CellType.STRING);
                reqDTO.setVerifyValidityDate(cells.getCell(5) == null ? "" : cells.getCell(5).getStringCellValue());
                cells.getCell(6).setCellType(CellType.STRING);
                reqDTO.setRegOrg(cells.getCell(6) == null ? "" : cells.getCell(6).getStringCellValue());
                cells.getCell(7).setCellType(CellType.STRING);
                reqDTO.setRegNo(cells.getCell(7) == null ? "" : cells.getCell(7).getStringCellValue());
                cells.getCell(8).setCellType(CellType.STRING);
                reqDTO.setFactNo(cells.getCell(8) == null ? "" : cells.getCell(8).getStringCellValue());
                cells.getCell(9).setCellType(CellType.STRING);
                reqDTO.setEquipInnerNo(cells.getCell(9) == null ? "" : cells.getCell(9).getStringCellValue());
                cells.getCell(10).setCellType(CellType.STRING);
                reqDTO.setEquipPosition(cells.getCell(10) == null ? "" : cells.getCell(10).getStringCellValue());
                cells.getCell(11).setCellType(CellType.STRING);
                reqDTO.setEquipDetailedPosition(cells.getCell(11) == null ? "" : cells.getCell(11).getStringCellValue());
                cells.getCell(12).setCellType(CellType.STRING);
                reqDTO.setEquipParameter(cells.getCell(12) == null ? "" : cells.getCell(12).getStringCellValue());
                cells.getCell(13).setCellType(CellType.STRING);
                reqDTO.setManageOrg(cells.getCell(13) == null ? "" : cells.getCell(13).getStringCellValue());
                cells.getCell(14).setCellType(CellType.STRING);
                reqDTO.setSecStaffName(cells.getCell(14) == null ? "" : cells.getCell(14).getStringCellValue());
                cells.getCell(15).setCellType(CellType.STRING);
                reqDTO.setSecStaffPhone(cells.getCell(15) == null ? "" : cells.getCell(15).getStringCellValue());
                cells.getCell(16).setCellType(CellType.STRING);
                reqDTO.setSecStaffMobile(cells.getCell(16) == null ? "" : cells.getCell(16).getStringCellValue());
                cells.getCell(17).setCellType(CellType.STRING);
                reqDTO.setSecOrg(cells.getCell(17) == null ? "" : cells.getCell(17).getStringCellValue());
                reqDTO.setRecId(TokenUtil.getUuId());
                reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                reqDTO.setRecRevisor(TokenUtil.getCurrentPersonId());
                reqDTO.setRecReviseTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                if (StringUtils.isNotEmpty(reqDTO.getSpecialEquipType())) {
                    reqDTO.setSpecialEquipType("电梯".equals(reqDTO.getSpecialEquipType()) ? "10" : "起重机".equals(reqDTO.getSpecialEquipType()) ? "20" : "场（厂）内专用机动车辆".equals(reqDTO.getSpecialEquipType()) ? "30" : "40");
                }
                SysOffice manageOrg = organizationMapper.getByNames(reqDTO.getManageOrg());
                SysOffice secOrg = organizationMapper.getByNames(reqDTO.getSecOrg());
                if (Objects.isNull(manageOrg) || Objects.isNull(secOrg)) {
                    continue;
                }
                reqDTO.setManageOrg(manageOrg.getId());
                reqDTO.setSecOrg(secOrg.getId());
                specialEquipMapper.updateEquip(reqDTO);
                specialEquipMapper.modifySpecialEquip(reqDTO);
            }
            fileInputStream.close();
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
