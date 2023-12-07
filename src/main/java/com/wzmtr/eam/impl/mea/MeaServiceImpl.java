package com.wzmtr.eam.impl.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.mea.MeaListReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaReqDTO;
import com.wzmtr.eam.dto.req.specialEquip.SpecialEquipReqDTO;
import com.wzmtr.eam.dto.res.mea.MeaResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.dto.res.mea.excel.ExcelMeaResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.SysOffice;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.mea.MeaMapper;
import com.wzmtr.eam.service.mea.MeaService;
import com.wzmtr.eam.utils.EasyExcelUtils;
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
    public void exportMea(MeaListReqDTO meaListReqDTO, HttpServletResponse response) throws IOException {
        List<MeaResDTO> meaList = meaMapper.listMea(meaListReqDTO);
        if (meaList != null && !meaList.isEmpty()) {
            List<ExcelMeaResDTO> list = new ArrayList<>();
            for (MeaResDTO resDTO : meaList) {
                ExcelMeaResDTO res = new ExcelMeaResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setVerifyPeriod(String.valueOf(resDTO.getVerifyPeriod()));
                res.setLineNo(CommonConstants.LINE_CODE_ONE.equals(resDTO.getLineNo()) ? "S1线" : "S2线");
                list.add(res);
            }
            EasyExcelUtils.export(response, "计量器具台账信息", list);
        }
    }

    @Override
    public Page<SubmissionRecordDetailResDTO> pageMeaRecord(String equipCode, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return meaMapper.pageMeaRecord(pageReqDTO.of(), equipCode);
    }

}
