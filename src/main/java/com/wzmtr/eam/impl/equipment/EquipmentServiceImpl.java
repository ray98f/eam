package com.wzmtr.eam.impl.equipment;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.EquipmentReqDTO;
import com.wzmtr.eam.dto.req.equipment.PartFaultReqDTO;
import com.wzmtr.eam.dto.req.equipment.UnitCodeReqDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentQrResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentResDTO;
import com.wzmtr.eam.dto.res.equipment.EquipmentTreeResDTO;
import com.wzmtr.eam.dto.res.basic.RegionResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelEquipmentResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulOrderDetailResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.CurrentLoginUser;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.service.equipment.EquipmentService;
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
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.wzmtr.eam.constant.CommonConstants.XLS;
import static com.wzmtr.eam.constant.CommonConstants.XLSX;

/**
 * @author frp
 */
@Service
@Slf4j
public class EquipmentServiceImpl implements EquipmentService {

    private static final String REGION_CODE_ES1 = "ES1";
    private static final String REGION_CODE_ES2 = "ES2";

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public List<RegionResDTO> listTrainRegion() {
        return equipmentMapper.listTrainRegion();
    }

    @Override
    public EquipmentTreeResDTO listEquipmentTree(String lineCode, String regionCode, String recId, String parentNodeRecId, String equipmentCategoryCode) {
        EquipmentTreeResDTO res = new EquipmentTreeResDTO();
        if (StringUtils.isEmpty(lineCode)) {
            res.setLine(equipmentMapper.listLine());
        } else {
            List<RegionResDTO> region = equipmentMapper.listRegion(lineCode, regionCode, recId);
            boolean bool = StringUtils.isEmpty(equipmentCategoryCode) && region != null && !region.isEmpty();
            if (bool) {
                if (StringUtils.isEmpty(regionCode)) {
                    RegionResDTO regionResDTO = new RegionResDTO();
                    String name = "E" + (CommonConstants.LINE_CODE_ONE.equals(lineCode) ? "S1" : "S2");
                    regionResDTO.setRecId(name);
                    regionResDTO.setParentNodeRecId(lineCode);
                    regionResDTO.setNodeCode(name);
                    regionResDTO.setNodeName((CommonConstants.LINE_CODE_ONE.equals(lineCode) ? "S1线" : "S2线") + "车辆");
                    regionResDTO.setLineCode(lineCode);
                    region.add(regionResDTO);
                } else if (REGION_CODE_ES1.equals(regionCode) || REGION_CODE_ES2.equals(regionCode)) {
                    region = equipmentMapper.listCarRegion(lineCode, recId);
                }
                res.setRegion(region);
            } else if (CommonConstants.LINE_CODE_ONE.equals(parentNodeRecId) && REGION_CODE_ES1.equals(recId) && CommonConstants.LINE_CODE_ONE.equals(lineCode)) {
                res.setRegion(equipmentMapper.listCarRegion(lineCode, recId));
            } else if (!REGION_CODE_ES1.equals(parentNodeRecId) && !REGION_CODE_ES2.equals(parentNodeRecId)) {
                res.setEquipment(equipmentMapper.listEquipmentCategory(equipmentCategoryCode, lineCode, recId, regionCode));
            }
        }
        return res;
    }

    @Override
    public Page<EquipmentResDTO> pageEquipment(String equipCode, String equipName, String useLineNo, String useSegNo, String position1Code, String majorCode,
                                               String systemCode, String equipTypeCode, String brand, String startTime, String endTime, String manufacture, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentMapper.pageEquipment(pageReqDTO.of(), equipCode, equipName, useLineNo, useSegNo, position1Code, majorCode,
                systemCode, equipTypeCode, brand, startTime, endTime, manufacture);
    }

    @Override
    public EquipmentResDTO getEquipmentDetail(String id) {
        return equipmentMapper.getEquipmentDetail(id);
    }

    @Override
    public void importEquipment(MultipartFile file) {
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
            List<EquipmentReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                EquipmentReqDTO reqDTO = new EquipmentReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                reqDTO.setEquipName(cells.getCell(0) == null ? "" : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setUseLineName(cells.getCell(1) == null ? "" : cells.getCell(1).getStringCellValue());
                reqDTO.setUseLineNo(cells.getCell(1) == null ? "" : "S1线".equals(cells.getCell(1).getStringCellValue()) ? "01" : "02");
                cells.getCell(2).setCellType(CellType.STRING);
                reqDTO.setUseSegName(cells.getCell(2) == null ? "" : cells.getCell(2).getStringCellValue());
                reqDTO.setUseSegNo(cells.getCell(2) == null ? "" : "一期".equals(cells.getCell(2).getStringCellValue()) ? "01" : "二期".equals(cells.getCell(2).getStringCellValue()) ? "二期" : "三期");
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setStartUseDate(cells.getCell(3) == null ? "" : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setMajorName(cells.getCell(4) == null ? "" : cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(CellType.STRING);
                reqDTO.setSystemName(cells.getCell(5) == null ? "" : cells.getCell(5).getStringCellValue());
                cells.getCell(6).setCellType(CellType.STRING);
                reqDTO.setEquipTypeName(cells.getCell(6) == null ? "" : cells.getCell(6).getStringCellValue());
                cells.getCell(7).setCellType(CellType.STRING);
                reqDTO.setSpecialEquipFlag(cells.getCell(7) == null ? "" : "否".equals(cells.getCell(7).getStringCellValue()) ? "10" : "20");
                cells.getCell(8).setCellType(CellType.STRING);
                reqDTO.setManufacture(cells.getCell(8) == null ? "" : cells.getCell(8).getStringCellValue());
                cells.getCell(9).setCellType(CellType.STRING);
                reqDTO.setMatSpecifi(cells.getCell(9) == null ? "" : cells.getCell(9).getStringCellValue());
                cells.getCell(10).setCellType(CellType.STRING);
                reqDTO.setBrand(cells.getCell(10) == null ? "" : cells.getCell(10).getStringCellValue());
                cells.getCell(11).setCellType(CellType.STRING);
                reqDTO.setManufactureDate(cells.getCell(11) == null ? "" : cells.getCell(11).getStringCellValue());
                cells.getCell(12).setCellType(CellType.STRING);
                reqDTO.setManufactureNo(cells.getCell(12) == null ? "" : cells.getCell(12).getStringCellValue());
                cells.getCell(13).setCellType(CellType.STRING);
                reqDTO.setInstallDealer(cells.getCell(13) == null ? "" : cells.getCell(13).getStringCellValue());
                cells.getCell(14).setCellType(CellType.STRING);
                reqDTO.setPosition1Name(cells.getCell(14) == null ? "" : cells.getCell(14).getStringCellValue());
                cells.getCell(15).setCellType(CellType.STRING);
                reqDTO.setPosition2Name(cells.getCell(15) == null ? "" : cells.getCell(15).getStringCellValue());
                cells.getCell(16).setCellType(CellType.STRING);
                reqDTO.setPosition3(cells.getCell(16) == null ? "" : cells.getCell(16).getStringCellValue());
                cells.getCell(17).setCellType(CellType.STRING);
                reqDTO.setPositionRemark(cells.getCell(17) == null ? "" : cells.getCell(17).getStringCellValue());
                cells.getCell(18).setCellType(CellType.STRING);
                reqDTO.setRemark(cells.getCell(18) == null ? "" : cells.getCell(18).getStringCellValue());
                reqDTO.setRecId(TokenUtil.getUuId());
                reqDTO.setApprovalStatus("30");
                reqDTO.setQuantity(new BigDecimal("1"));
                reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                CurrentLoginUser user = TokenUtil.getCurrentPerson();
                reqDTO.setCompanyCode(user.getCompanyAreaId() == null ? user.getCompanyId() : user.getCompanyAreaId());
                reqDTO.setCompanyName(user.getCompanyName());
                reqDTO.setDeptCode(user.getOfficeAreaId() == null ? user.getOfficeId() : user.getOfficeAreaId());
                reqDTO.setDeptName(user.getOfficeName());
                String unitNo = String.valueOf(Long.parseLong(equipmentMapper.getMaxCode(1)) + 1);
                String equipCode = String.valueOf(Long.parseLong(equipmentMapper.getMaxCode(4)) + 1);
                UnitCodeReqDTO unitCodeReqDTO = new UnitCodeReqDTO();
                unitCodeReqDTO.setRecId(TokenUtil.getUuId());
                unitCodeReqDTO.setUnitNo(unitNo);
                unitCodeReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                unitCodeReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                unitCodeReqDTO.setDevNo(equipCode);
                unitCodeReqDTO.setBatchNo("");
                unitCodeReqDTO.setAssetNo("");
                unitCodeReqDTO.setProCode("");
                unitCodeReqDTO.setProName("");
                unitCodeReqDTO.setOrderNo(reqDTO.getOrderNo());
                unitCodeReqDTO.setOrderName(reqDTO.getOrderName());
                unitCodeReqDTO.setSupplierId(user.getOfficeId());
                unitCodeReqDTO.setSupplierName(user.getOfficeAreaId() == null ? user.getOfficeId() : user.getOfficeAreaId());
                unitCodeReqDTO.setMatSpecifi(reqDTO.getMatSpecifi());
                unitCodeReqDTO.setBrand(reqDTO.getBrand());
                equipmentMapper.insertUnitCode(unitCodeReqDTO);
                reqDTO.setEquipCode(unitNo);
                temp.add(reqDTO);
            }
            fileInputStream.close();
            if (temp.size() > 0) {
                equipmentMapper.importEquipment(temp);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void exportEquipment(List<String> ids, HttpServletResponse response) throws IOException {
        List<EquipmentResDTO> equipmentResDTOList = equipmentMapper.listEquipment(ids);
        if (equipmentResDTOList != null && !equipmentResDTOList.isEmpty()) {
            List<ExcelEquipmentResDTO> list = new ArrayList<>();
            for (EquipmentResDTO resDTO : equipmentResDTOList) {
                ExcelEquipmentResDTO res = new ExcelEquipmentResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setQuantity(String.valueOf(resDTO.getQuantity()));
                res.setTotalMiles(String.valueOf(resDTO.getTotalMiles()));
                list.add(res);
            }
            EasyExcelUtils.export(response, "设备台账信息", list);
        }
    }

    @Override
    public List<EquipmentQrResDTO> generateQr(BaseIdsEntity baseIdsEntity) throws ParseException {
        List<EquipmentQrResDTO> list = new ArrayList<>();
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                EquipmentQrResDTO res = new EquipmentQrResDTO();
                EquipmentResDTO resDTO = equipmentMapper.getEquipmentDetail(id);
                if (!Objects.isNull(resDTO)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
                    String qr = resDTO.getEquipCode() + "\n设备名称：" + resDTO.getEquipName() + "\n开始使用时间：" + sdf1.format(sdf.parse(resDTO.getStartUseDate()));
                    res.setRecId(id);
                    res.setCompanyName(resDTO.getCompanyName());
                    res.setDeptName(resDTO.getDeptName());
                    res.setEquipCode(resDTO.getEquipCode());
                    res.setEquipName(resDTO.getEquipName());
                    res.setStartUseDate(sdf1.format(sdf.parse(resDTO.getStartUseDate())));
                    res.setQr(QrCodeUtil.generateAsBase64(qr, QrUtils.initQrConfig(), "png"));
                    list.add(res);
                }
            }
        }
        return list;
    }

    @Override
    public Page<OverhaulOrderDetailResDTO> listOverhaul(String equipCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentMapper.listOverhaul(pageReqDTO.of(), equipCode);
    }

    @Override
    public Page<FaultDetailResDTO> listFault(String equipCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentMapper.listFault(pageReqDTO.of(), equipCode);
    }

    @Override
    public Page<PartReplaceResDTO> listPartReplace(String equipCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return equipmentMapper.listPartReplace(pageReqDTO.of(), equipCode);
    }

}
