package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.GearboxChangeOilReqDTO;
import com.wzmtr.eam.dto.res.equipment.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelGearboxChangeOilResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.GearboxChangeOilMapper;
import com.wzmtr.eam.service.equipment.GearboxChangeOilService;
import com.wzmtr.eam.utils.EasyExcelUtils;
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
public class GearboxChangeOilServiceImpl implements GearboxChangeOilService {

    @Autowired
    private GearboxChangeOilMapper gearboxChangeOilMapper;

    @Override
    public Page<GearboxChangeOilResDTO> pageGearboxChangeOil(String trainNo, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return gearboxChangeOilMapper.pageGearboxChangeOil(pageReqDTO.of(), trainNo);
    }

    @Override
    public GearboxChangeOilResDTO getGearboxChangeOilDetail(String id) {
        return gearboxChangeOilMapper.getGearboxChangeOilDetail(id);
    }

    @Override
    public void addGearboxChangeOil(GearboxChangeOilReqDTO gearboxChangeOilReqDTO) {
        gearboxChangeOilReqDTO.setRecId(TokenUtil.getUuId());
        gearboxChangeOilReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        gearboxChangeOilReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        gearboxChangeOilMapper.addGearboxChangeOil(gearboxChangeOilReqDTO);
    }

    @Override
    public void deleteGearboxChangeOil(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                if (!gearboxChangeOilMapper.getRecCreator(id).equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
            }
            gearboxChangeOilMapper.deleteGearboxChangeOil(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importGearboxChangeOil(MultipartFile file) {
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
            List<GearboxChangeOilReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                GearboxChangeOilReqDTO reqDTO = new GearboxChangeOilReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                reqDTO.setTrainNo(cells.getCell(0) == null ? "" : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setCompleteDate(cells.getCell(1) == null ? "" : cells.getCell(1).getStringCellValue());
                cells.getCell(2).setCellType(CellType.STRING);
                reqDTO.setOrgType(cells.getCell(2) == null ? "" : "维保".equals(cells.getCell(2).getStringCellValue()) ? "10" : "20");
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setOperator(cells.getCell(3) == null ? "" : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setConfirmor(cells.getCell(4) == null ? "" : cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(CellType.STRING);
                reqDTO.setRemark(cells.getCell(5) == null ? "" : cells.getCell(5).getStringCellValue());
                reqDTO.setRecId(TokenUtil.getUuId());
                reqDTO.setDeleteFlag("0");
                reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                temp.add(reqDTO);
            }
            fileInputStream.close();
            if (temp.size() > 0) {
                gearboxChangeOilMapper.importGearboxChangeOil(temp);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void exportGearboxChangeOil(String trainNo, HttpServletResponse response) throws IOException {
        List<GearboxChangeOilResDTO> gearboxChangeOilResDTOList = gearboxChangeOilMapper.listGearboxChangeOil(trainNo);
        if (gearboxChangeOilResDTOList != null && !gearboxChangeOilResDTOList.isEmpty()) {
            List<ExcelGearboxChangeOilResDTO> list = new ArrayList<>();
            for (GearboxChangeOilResDTO resDTO : gearboxChangeOilResDTOList) {
                ExcelGearboxChangeOilResDTO res = new ExcelGearboxChangeOilResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setTotalMiles(String.valueOf(resDTO.getTotalMiles()));
                res.setOrgType(CommonConstants.TEN_STRING.equals(resDTO.getOrgType()) ? "维保" : CommonConstants.TWENTY_STRING.equals(resDTO.getOrgType()) ? "售后服务站" : CommonConstants.THIRTY_STRING.equals(resDTO.getOrgType()) ? "一级修工班" : "二级修工班");
                list.add(res);
            }
            EasyExcelUtils.export(response, "", list);
        }
    }

}
