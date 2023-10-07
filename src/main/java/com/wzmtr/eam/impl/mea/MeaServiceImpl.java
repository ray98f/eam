package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.mea.MeaListReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaReqDTO;
import com.wzmtr.eam.dto.req.specialEquip.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.mea.MeaResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.mea.MeaMapper;
import com.wzmtr.eam.service.mea.MeaService;
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
public class MeaServiceImpl implements MeaService {

    @Autowired
    private MeaMapper meaMapper;

    @Override
    public Page<MeaResDTO> pageMea(MeaListReqDTO meaListReqDTO, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return meaMapper.pageMea(pageReqDTO.of(), meaListReqDTO);
    }

    @Override
    public MeaResDTO getMeaDetail(String id) {
        return meaMapper.getMeaDetail(id);
    }

    @Override
    public void importMea(MultipartFile file) {
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
            List<MeaReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                MeaReqDTO reqDTO = new MeaReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                reqDTO.setEquipCode(cells.getCell(0) == null ? "" : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setEquipName(cells.getCell(1) == null ? "" : cells.getCell(1).getStringCellValue());
                cells.getCell(2).setCellType(CellType.STRING);
                reqDTO.setMatSpecifi(cells.getCell(2) == null ? "" : cells.getCell(2).getStringCellValue());
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setCertificateNo(cells.getCell(3) == null ? "" : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setTransferDate(cells.getCell(4) == null ? "" : cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(CellType.STRING);
                reqDTO.setManufacture(cells.getCell(5) == null ? "" : cells.getCell(5).getStringCellValue());
                cells.getCell(6).setCellType(CellType.STRING);
                reqDTO.setSource(cells.getCell(6) == null ? "" : cells.getCell(6).getStringCellValue());
                cells.getCell(7).setCellType(CellType.STRING);
                reqDTO.setVerifyPeriod((int) cells.getCell(7).getNumericCellValue());
                cells.getCell(8).setCellType(CellType.STRING);
                reqDTO.setUseDeptCode(cells.getCell(8) == null ? "" : cells.getCell(8).getStringCellValue());
                cells.getCell(9).setCellType(CellType.STRING);
                reqDTO.setUseDeptCname(cells.getCell(9) == null ? "" : cells.getCell(9).getStringCellValue());
                cells.getCell(10).setCellType(CellType.STRING);
                reqDTO.setUseBeginDate(cells.getCell(10) == null ? "" : cells.getCell(10).getStringCellValue());
                cells.getCell(11).setCellType(CellType.STRING);
                reqDTO.setLastVerifyDate(cells.getCell(11) == null ? "" : cells.getCell(11).getStringCellValue());
                cells.getCell(12).setCellType(CellType.STRING);
                reqDTO.setExpirationDate(cells.getCell(12) == null ? "" : cells.getCell(12).getStringCellValue());
                cells.getCell(13).setCellType(CellType.STRING);
                reqDTO.setManufactureNo(cells.getCell(13) == null ? "" : cells.getCell(13).getStringCellValue());
                cells.getCell(14).setCellType(CellType.STRING);
                reqDTO.setPhoneNo(cells.getCell(14) == null ? "" : cells.getCell(14).getStringCellValue());
                cells.getCell(15).setCellType(CellType.STRING);
                reqDTO.setUseName(cells.getCell(15) == null ? "" : cells.getCell(15).getStringCellValue());
                cells.getCell(16).setCellType(CellType.STRING);
                reqDTO.setLineNo(cells.getCell(16) == null ? "" : cells.getCell(16).getStringCellValue());
                reqDTO.setRecId(TokenUtil.getUuId());
                reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                temp.add(reqDTO);
            }
            fileInputStream.close();
            if (temp.size() > 0) {
                meaMapper.importMea(temp);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void exportMea(MeaListReqDTO meaListReqDTO, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "计量器具编码", "计量器具名称", "型号规格", "证书编号", "移交日期", "制造单位",
                "条件代码", "检定/校准周期（月）", "使用公司", "使用单位", "开始使用日期", "上次检定/校准日期", "证书有效日期", "出厂编号",
                "使用保管人手机号", "使用保管人姓名", "线别");
        List<MeaResDTO> meaList = meaMapper.listMea(meaListReqDTO);
        List<Map<String, String>> list = new ArrayList<>();
        if (meaList != null && !meaList.isEmpty()) {
            for (MeaResDTO resDTO : meaList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("计量器具编码", resDTO.getEquipCode());
                map.put("计量器具名称", resDTO.getEquipName());
                map.put("型号规格", resDTO.getMatSpecifi());
                map.put("证书编号", resDTO.getCertificateNo());
                map.put("移交日期", resDTO.getTransferDate());
                map.put("制造单位", resDTO.getManufacture());
                map.put("条件代码", resDTO.getSource());
                map.put("检定/校准周期（月）", String.valueOf(resDTO.getVerifyPeriod()));
                map.put("使用公司", resDTO.getUseDeptCode());
                map.put("使用单位", resDTO.getUseDeptCname());
                map.put("开始使用日期", resDTO.getUseBeginDate());
                map.put("上次检定/校准日期", resDTO.getLastVerifyDate());
                map.put("证书有效日期", resDTO.getExpirationDate());
                map.put("出厂编号", resDTO.getManufactureNo());
                map.put("使用保管人手机号", resDTO.getPhoneNo());
                map.put("使用保管人姓名", resDTO.getUseName());
                map.put("线别", resDTO.getLineNo());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("计量器具台账信息", listName, list, null, response);
    }

    @Override
    public Page<SubmissionRecordDetailResDTO> pageMeaRecord(String equipCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return meaMapper.pageMeaRecord(pageReqDTO.of(), equipCode);
    }

}
