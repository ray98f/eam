package com.wzmtr.eam.service.impl.equipment;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.EquipmentReqDTO;
import com.wzmtr.eam.dto.res.EquipmentCategoryResDTO;
import com.wzmtr.eam.dto.res.EquipmentResDTO;
import com.wzmtr.eam.dto.res.EquipmentTreeResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.EquipmentMapper;
import com.wzmtr.eam.service.equipment.EquipmentService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.FileUtils;
import com.wzmtr.eam.utils.QrUtils;
import com.wzmtr.eam.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
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

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public EquipmentTreeResDTO listEquipmentTree(String lineCode, String regionCode, String recId, String parentNodeRecId, String equipmentCategoryCode) {
        EquipmentTreeResDTO res = new EquipmentTreeResDTO();
        if (lineCode == null || "".equals(lineCode)) {
            res.setLine(equipmentMapper.listLine());
        } else {
            List<RegionResDTO> region = equipmentMapper.listRegion(lineCode, regionCode, recId);
            if (region != null && !region.isEmpty()) {
                if (regionCode == null || "".equals(regionCode)) {
                    RegionResDTO regionResDTO = new RegionResDTO();
                    regionResDTO.setRecId("E" + ("01".equals(lineCode) ? "S1" : "S2"));
                    regionResDTO.setParentNodeRecId(lineCode);
                    regionResDTO.setNodeCode("E" + ("01".equals(lineCode) ? "S1" : "S2"));
                    regionResDTO.setNodeName(("01".equals(lineCode) ? "S1线" : "S2线") + "车辆");
                    regionResDTO.setLineCode(lineCode);
                    region.add(regionResDTO);
                } else if ("ES1".equals(regionCode) || "ES2".equals(regionCode)) {
                    region = equipmentMapper.listCarRegion(regionCode, recId);
                }
                res.setRegion(region);
            } else if (!"ES1".equals(parentNodeRecId) && !"ES2".equals(parentNodeRecId)) {
                res.setEquipment(equipmentMapper.listEquipmentCategory(equipmentCategoryCode, lineCode, recId));
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
                cells.getCell(0).setCellType(1);
                reqDTO.setEquipName(cells.getCell(0) == null ? "" : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(1);
                reqDTO.setUseLineName(cells.getCell(1) == null ? "" : cells.getCell(1).getStringCellValue());
                reqDTO.setUseLineNo(cells.getCell(1) == null ? "" : "S1线".equals(cells.getCell(1).getStringCellValue()) ? "01" : "02");
                cells.getCell(2).setCellType(1);
                reqDTO.setUseSegName(cells.getCell(2) == null ? "" : cells.getCell(2).getStringCellValue());
                reqDTO.setUseSegNo(cells.getCell(2) == null ? "" : "一期".equals(cells.getCell(2).getStringCellValue()) ? "01" : "二期".equals(cells.getCell(2).getStringCellValue()) ? "二期" : "三期");
                cells.getCell(3).setCellType(1);
                reqDTO.setStartUseDate(cells.getCell(3) == null ? "" : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(1);
                reqDTO.setMajorName(cells.getCell(4) == null ? "" : cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(1);
                reqDTO.setSystemName(cells.getCell(5) == null ? "" : cells.getCell(5).getStringCellValue());
                cells.getCell(6).setCellType(1);
                reqDTO.setEquipTypeName(cells.getCell(6) == null ? "" : cells.getCell(6).getStringCellValue());
                cells.getCell(7).setCellType(1);
                reqDTO.setSpecialEquipFlag(cells.getCell(7) == null ? "" : "否".equals(cells.getCell(7).getStringCellValue()) ? "10" : "20");
                cells.getCell(8).setCellType(1);
                reqDTO.setManufacture(cells.getCell(8) == null ? "" : cells.getCell(8).getStringCellValue());
                cells.getCell(9).setCellType(1);
                reqDTO.setMatSpecifi(cells.getCell(9) == null ? "" : cells.getCell(9).getStringCellValue());
                cells.getCell(10).setCellType(1);
                reqDTO.setBrand(cells.getCell(10) == null ? "" : cells.getCell(10).getStringCellValue());
                cells.getCell(11).setCellType(1);
                reqDTO.setManufactureDate(cells.getCell(11) == null ? "" : cells.getCell(11).getStringCellValue());
                cells.getCell(12).setCellType(1);
                reqDTO.setManufactureNo(cells.getCell(12) == null ? "" : cells.getCell(12).getStringCellValue());
                cells.getCell(13).setCellType(1);
                reqDTO.setInstallDealer(cells.getCell(13) == null ? "" : cells.getCell(13).getStringCellValue());
                cells.getCell(14).setCellType(1);
                reqDTO.setPosition1Name(cells.getCell(14) == null ? "" : cells.getCell(14).getStringCellValue());
                cells.getCell(15).setCellType(1);
                reqDTO.setPosition2Name(cells.getCell(15) == null ? "" : cells.getCell(15).getStringCellValue());
                cells.getCell(16).setCellType(1);
                reqDTO.setPosition3(cells.getCell(16) == null ? "" : cells.getCell(16).getStringCellValue());
                cells.getCell(17).setCellType(1);
                reqDTO.setPositionRemark(cells.getCell(17) == null ? "" : cells.getCell(17).getStringCellValue());
                cells.getCell(18).setCellType(1);
                reqDTO.setRemark(cells.getCell(18) == null ? "" : cells.getCell(18).getStringCellValue());
                reqDTO.setRecId(TokenUtil.getUuId());
                reqDTO.setApprovalStatus("30");
                reqDTO.setQuantity(new BigDecimal("1"));
                reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                reqDTO.setCompanyCode(TokenUtil.getCurrentPersonId());
                reqDTO.setDeptCode(TokenUtil.getCurrentPersonId());
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
    public void exportEquipment(String equipCode, String equipName, String useLineNo, String useSegNo, String position1Code, String majorCode,
                                String systemCode, String equipTypeCode, String brand, String startTime, String endTime, String manufacture) {
        List<String> listName = Arrays.asList("记录编号", "节点编号", "节点名称", "父节点记录编号", "记录状态", "备注", "创建者", "创建时间");
        List<EquipmentResDTO> equipmentResDTOList = equipmentMapper.listEquipment(equipCode, equipName, useLineNo, useSegNo, position1Code, majorCode,
                systemCode, equipTypeCode, brand, startTime, endTime, manufacture);
        List<Map<String, String>> list = new ArrayList<>();
//        if (equipmentResDTOList != null && !equipmentResDTOList.isEmpty()) {
//            for (EquipmentResDTO equipmentResDTO : equipmentResDTOList) {
//                Map<String, String> map = new HashMap<>();
//                map.put("记录编号", equipmentResDTO.getRecId());
//                map.put("节点编号", equipmentResDTO.getNodeCode());
//                map.put("节点名称", equipmentResDTO.getNodeName());
//                map.put("父节点记录编号", equipmentResDTO.getParentNodeRecId());
//                map.put("记录状态", "10".equals(equipmentResDTO.getRecStatus()) ? "启用" : "禁用");
//                map.put("备注", equipmentResDTO.getRemark());
//                map.put("创建者", equipmentResDTO.getRecCreator());
//                map.put("创建时间", equipmentResDTO.getRecCreateTime());
//                list.add(map);
//            }
//        }
//        ExcelPortUtil.excelPort("设备分类信息", listName, list, null, response);
    }

    @Override
    public String generateQr(String id) throws ParseException {
        String qr = "";
        EquipmentResDTO resDTO = equipmentMapper.getEquipmentDetail(id);
        if (!Objects.isNull(resDTO)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
            qr = resDTO.getEquipCode() + "\n设备名称：" + resDTO.getEquipName() + "\n开始使用时间：" + sdf1.format(sdf.parse(resDTO.getStartUseDate()));
        }
        return QrCodeUtil.generateAsBase64(qr, QrUtils.initQrConfig(), "png");
    }

}
