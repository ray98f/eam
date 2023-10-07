package com.wzmtr.eam.impl.specialEquip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.specialEquip.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.specialEquip.SpecialEquipHistoryResDTO;
import com.wzmtr.eam.dto.res.specialEquip.SpecialEquipResDTO;
import com.wzmtr.eam.entity.Dictionaries;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.common.OrganizationMapper;
import com.wzmtr.eam.mapper.dict.DictionariesMapper;
import com.wzmtr.eam.mapper.specialEquip.SpecialEquipMapper;
import com.wzmtr.eam.service.specialEquip.SpecialEquipService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.FileUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
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
                if (resDTO.getManageOrg() != null && !"".equals(resDTO.getManageOrg())) {
                    resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
                }
                if (resDTO.getSecOrg() != null && !"".equals(resDTO.getSecOrg())) {
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
        if (resDTO.getManageOrg() != null && !"".equals(resDTO.getManageOrg())) {
            resDTO.setManageOrgName(organizationMapper.getOrgById(resDTO.getManageOrg()));
        }
        if (resDTO.getSecOrg() != null && !"".equals(resDTO.getSecOrg())) {
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
                if (!"".equals(reqDTO.getSpecialEquipType())) {
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
                                   String useLineNo, String position1Code, String specialEquipType, String equipStatus, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "设备编码", "特种设备代码", "设备名称", "特种设备类别", "应用线别代码", "位置一编号", "位置一名称",
                "特种设备检测日期", "特种设备检测有效期", "使用登记机构", "登记证编号", "出厂编号", "设备内部编号", "设备所在地点", "设备详细地址", "设备主要参数",
                "管理部门名称", "安管人员", "安管人员电话", "安管人员手机", "维管部门名称");
        List<SpecialEquipResDTO> specialEquipResDTOList = specialEquipMapper.listSpecialEquip(equipCode, equipName, specialEquipCode, factNo, useLineNo,
                position1Code, specialEquipType, equipStatus);
        List<Map<String, String>> list = new ArrayList<>();
        if (specialEquipResDTOList != null && !specialEquipResDTOList.isEmpty()) {
            for (SpecialEquipResDTO resDTO : specialEquipResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("设备编码", resDTO.getEquipCode());
                map.put("特种设备代码", resDTO.getSpecialEquipCode());
                map.put("设备名称", resDTO.getEquipName());
                List<Dictionaries> dicList = dictionariesMapper.list("dm.specialequiptype", resDTO.getSpecialEquipType() == null ? " " : resDTO.getSpecialEquipType(), null);
                if (dicList != null && !dicList.isEmpty()) {
                    map.put("特种设备类别", dicList.get(0).getItemCname());
                } else {
                    map.put("特种设备类别", "");
                }
                map.put("应用线别代码", "01".equals(resDTO.getUseLineNo()) ? "S1线" : "S2线");
                map.put("位置一编号", resDTO.getPosition1Code());
                map.put("位置一名称", resDTO.getPosition1Name());
                map.put("特种设备检测日期", resDTO.getVerifyDate());
                map.put("特种设备检测有效期", resDTO.getVerifyValidityDate());
                map.put("使用登记机构", resDTO.getRegOrg());
                map.put("登记证编号", resDTO.getRegNo());
                map.put("出厂编号", resDTO.getFactNo());
                map.put("设备内部编号", resDTO.getEquipInnerNo());
                map.put("设备所在地点", resDTO.getEquipPosition());
                map.put("设备详细地址", resDTO.getEquipDetailedPosition());
                map.put("设备主要参数", resDTO.getEquipParameter());
                map.put("管理部门名称", resDTO.getManageOrgName());
                map.put("安管人员", resDTO.getSecStaffName());
                map.put("安管人员电话", resDTO.getSecStaffPhone());
                map.put("安管人员手机", resDTO.getSecStaffMobile());
                map.put("维管部门名称", resDTO.getSecOrgName());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("特种设备台账信息", listName, list, null, response);
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
    public void exportSpecialEquipHistory(String equipCode, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "设备编码", "设备名称", "编制部门", "检测结果", "检测结果说明", "检测日期", "检测有效期", "检测单状态", "附件");
        List<SpecialEquipHistoryResDTO> specialEquipHistoryResDTOList = specialEquipMapper.listSpecialEquiHistory(equipCode);
        List<Map<String, String>> list = new ArrayList<>();
        if (specialEquipHistoryResDTOList != null && !specialEquipHistoryResDTOList.isEmpty()) {
            for (SpecialEquipHistoryResDTO resDTO : specialEquipHistoryResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("设备编码", resDTO.getEquipCode());
                map.put("设备名称", resDTO.getEquipName());
                map.put("编制部门", resDTO.getUseDeptCname());
                map.put("检测结果", resDTO.getVerifyResult());
                map.put("检测结果说明", resDTO.getVerifyConclusion());
                map.put("检测日期", resDTO.getLastVerifyDate());
                map.put("检测有效期", resDTO.getVerifyValidityDate());
                map.put("检测单状态", resDTO.getRecStatus());
                map.put("附件", resDTO.getDocId());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("特种设备检测记录历史信息", listName, list, null, response);
    }

}
