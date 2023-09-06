package com.wzmtr.eam.impl.equipment;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.EquipmentReqDTO;
import com.wzmtr.eam.dto.req.UnitCodeReqDTO;
import com.wzmtr.eam.dto.res.EquipmentQrResDTO;
import com.wzmtr.eam.dto.res.EquipmentResDTO;
import com.wzmtr.eam.dto.res.EquipmentTreeResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.CurrentLoginUser;
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

import javax.servlet.http.HttpServletResponse;
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
    public List<RegionResDTO> listTrainRegion() {
        return equipmentMapper.listTrainRegion();
    }

    @Override
    public EquipmentTreeResDTO listEquipmentTree(String lineCode, String regionCode, String recId, String parentNodeRecId, String equipmentCategoryCode) {
        EquipmentTreeResDTO res = new EquipmentTreeResDTO();
        if (lineCode == null || "".equals(lineCode)) {
            res.setLine(equipmentMapper.listLine());
        } else {
            List<RegionResDTO> region = equipmentMapper.listRegion(lineCode, regionCode, recId);
            if ((equipmentCategoryCode == null || "".equals(equipmentCategoryCode)) && region != null && !region.isEmpty()) {
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
    public void exportEquipment(String equipCode, String equipName, String useLineNo, String useSegNo, String position1Code, String majorCode,
                                String systemCode, String equipTypeCode, String brand, String startTime, String endTime, String manufacture, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "公司代码", "公司名称", "部门代码", "部门名称", "设备编码", "设备名称", "专业代码", "专业名称",
                "系统代码", "系统名称", "设备分类代码", "设备分类名称", "特种设备标识", "BOM类型", "生产厂家", "合同号", "合同名称", "型号规格", "品牌",
                "出厂日期", "出厂编号", "开始使用日期", "数量", "进设备台帐时间", "设备状态", "来源单号", "来源明细号", "来源记录编号", "安装单位", "附件编号",
                "来源线别代码", "来源线别名称", "来源线段代码", "来源线段名称", "应用线别代码", "应用线别", "应用线段代码", "应用线段", "位置一", "位置一名称",
                "位置二", "位置二名称", "位置三", "位置补充说明", "走行里程", "备注", "审批状态", "特种设备检测日期", "特种设备检测有效期", "创建者", "创建时间",
                "修改者", "修改时间", "删除者", "删除时间", "删除标志", "归档标记", "记录状态");
        List<EquipmentResDTO> equipmentResDTOList = equipmentMapper.listEquipment(equipCode, equipName, useLineNo, useSegNo, position1Code, majorCode,
                systemCode, equipTypeCode, brand, startTime, endTime, manufacture);
        List<Map<String, String>> list = new ArrayList<>();
        if (equipmentResDTOList != null && !equipmentResDTOList.isEmpty()) {
            for (EquipmentResDTO equipmentResDTO : equipmentResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", equipmentResDTO.getRecId());
                map.put("公司代码", equipmentResDTO.getCompanyCode());
                map.put("公司名称", equipmentResDTO.getCompanyName());
                map.put("部门代码", equipmentResDTO.getDeptCode());
                map.put("部门名称", equipmentResDTO.getDeptName());
                map.put("设备编码", equipmentResDTO.getEquipCode());
                map.put("设备名称", equipmentResDTO.getEquipName());
                map.put("专业代码", equipmentResDTO.getMajorCode());
                map.put("专业名称", equipmentResDTO.getMajorName());
                map.put("系统代码", equipmentResDTO.getSystemCode());
                map.put("系统名称", equipmentResDTO.getSystemName());
                map.put("设备分类代码", equipmentResDTO.getEquipTypeCode());
                map.put("设备分类名称", equipmentResDTO.getEquipTypeName());
                map.put("特种设备标识", equipmentResDTO.getSpecialEquipFlag());
                map.put("BOM类型", equipmentResDTO.getBomType());
                map.put("生产厂家", equipmentResDTO.getManufacture());
                map.put("合同号", equipmentResDTO.getOrderNo());
                map.put("合同名称", equipmentResDTO.getOrderName());
                map.put("型号规格", equipmentResDTO.getMatSpecifi());
                map.put("品牌", equipmentResDTO.getBrand());
                map.put("出厂日期", equipmentResDTO.getManufactureDate());
                map.put("出厂编号", equipmentResDTO.getManufactureNo());
                map.put("开始使用日期", equipmentResDTO.getStartUseDate());
                map.put("数量", String.valueOf(equipmentResDTO.getQuantity()));
                map.put("进设备台帐时间", equipmentResDTO.getManufactureNo());
                map.put("设备状态", equipmentResDTO.getEquipStatus());
                map.put("来源单号", equipmentResDTO.getSourceAppNo());
                map.put("来源明细号", equipmentResDTO.getSourceSubNo());
                map.put("来源记录编号", equipmentResDTO.getSourceRecId());
                map.put("安装单位", equipmentResDTO.getInstallDealer());
                map.put("附件编号", equipmentResDTO.getDocId());
                map.put("来源线别代码", equipmentResDTO.getOriginLineNo());
                map.put("来源线别名称", equipmentResDTO.getOriginLineName());
                map.put("来源线段代码", equipmentResDTO.getOriginSegNo());
                map.put("来源线段名称", equipmentResDTO.getOriginSegName());
                map.put("应用线别代码", equipmentResDTO.getUseLineNo());
                map.put("应用线别", equipmentResDTO.getUseLineName());
                map.put("应用线段代码", equipmentResDTO.getUseSegNo());
                map.put("应用线段", equipmentResDTO.getUseSegName());
                map.put("位置一", equipmentResDTO.getPosition1Code());
                map.put("位置一名称", equipmentResDTO.getPosition1Name());
                map.put("位置二", equipmentResDTO.getPosition2Code());
                map.put("位置二名称", equipmentResDTO.getPosition2Name());
                map.put("位置三", equipmentResDTO.getPosition3());
                map.put("位置补充说明", equipmentResDTO.getPositionRemark());
                map.put("走行里程", String.valueOf(equipmentResDTO.getTotalMiles()));
                map.put("备注", equipmentResDTO.getRemark());
                map.put("审批状态", equipmentResDTO.getApprovalStatus());
                map.put("特种设备检测日期", equipmentResDTO.getVerifyDate());
                map.put("特种设备检测有效期", equipmentResDTO.getVerifyValidityDate());
                map.put("创建者", equipmentResDTO.getRecCreator());
                map.put("创建时间", equipmentResDTO.getRecCreateTime());
                map.put("修改者", equipmentResDTO.getRecRevisor());
                map.put("修改时间", equipmentResDTO.getRecReviseTime());
                map.put("删除者", equipmentResDTO.getRecDeletor());
                map.put("删除时间", equipmentResDTO.getRecDeleteTime());
                map.put("删除标志", equipmentResDTO.getDeleteFlag());
                map.put("归档标记", equipmentResDTO.getArchiveFlag());
                map.put("记录状态", equipmentResDTO.getRecStatus());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("设备台账信息", listName, list, null, response);
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

}
