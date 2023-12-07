package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.PartReplaceReqDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceBomResDTO;
import com.wzmtr.eam.dto.res.equipment.PartReplaceResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelPartReplaceResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.PartReplaceMapper;
import com.wzmtr.eam.service.equipment.PartReplaceService;
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
public class PartReplaceServiceImpl implements PartReplaceService {

    @Autowired
    private PartReplaceMapper partReplaceMapper;

    @Override
    public Page<PartReplaceResDTO> pagePartReplace(String equipName, String replacementName, String faultWorkNo, String orgType, String replaceReason, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return partReplaceMapper.pagePartReplace(pageReqDTO.of(), equipName, replacementName, faultWorkNo, orgType, replaceReason);
    }

    @Override
    public PartReplaceResDTO getPartReplaceDetail(String id) {
        return partReplaceMapper.getPartReplaceDetail(id);
    }

    @Override
    public List<PartReplaceBomResDTO> getBom(String equipCode, String node) {
        if (StringUtils.isNotEmpty(equipCode)) {
            node = partReplaceMapper.selectBomCode(equipCode);
            if (StringUtils.isEmpty(node)) {
                throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
            }
        }
        return partReplaceMapper.getBom(node);
    }

    @Override
    public void addPartReplace(PartReplaceReqDTO partReplaceReqDTO) {
        partReplaceReqDTO.setRecId(TokenUtil.getUuId());
        partReplaceReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        partReplaceReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        partReplaceMapper.addPartReplace(partReplaceReqDTO);
    }

    @Override
    public void deletePartReplace(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                if (!partReplaceMapper.getPartReplaceDetail(id).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
            }
            partReplaceMapper.deletePartReplace(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importPartReplace(MultipartFile file) {
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
            List<PartReplaceReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                PartReplaceReqDTO reqDTO = new PartReplaceReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                reqDTO.setEquipCode(cells.getCell(0) == null ? "" : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setEquipName(cells.getCell(1) == null ? "" : cells.getCell(1).getStringCellValue());
                cells.getCell(2).setCellType(CellType.STRING);
                reqDTO.setReplacementNo(cells.getCell(2) == null ? "" : cells.getCell(2).getStringCellValue());
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setReplacementName(cells.getCell(3) == null ? "" : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setFaultWorkNo(cells.getCell(4) == null ? "" : cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(CellType.STRING);
                reqDTO.setOrgType(cells.getCell(5) == null ? "" : "维保".equals(cells.getCell(5).getStringCellValue()) ? "10" : "20");
                cells.getCell(6).setCellType(CellType.STRING);
                reqDTO.setOperator(cells.getCell(6) == null ? "" : cells.getCell(6).getStringCellValue());
                cells.getCell(7).setCellType(CellType.STRING);
                reqDTO.setReplaceReason(cells.getCell(7) == null ? "" : cells.getCell(7).getStringCellValue());
                cells.getCell(8).setCellType(CellType.STRING);
                reqDTO.setExt1(cells.getCell(8) == null ? "" : cells.getCell(8).getStringCellValue());
                cells.getCell(9).setCellType(CellType.STRING);
                reqDTO.setOldRepNo(cells.getCell(9) == null ? "" : cells.getCell(9).getStringCellValue());
                cells.getCell(10).setCellType(CellType.STRING);
                reqDTO.setNewRepNo(cells.getCell(10) == null ? "" : cells.getCell(10).getStringCellValue());
                cells.getCell(11).setCellType(CellType.STRING);
                reqDTO.setOperateCostTime(cells.getCell(11) == null ? "" : cells.getCell(11).getStringCellValue());
                cells.getCell(12).setCellType(CellType.STRING);
                reqDTO.setReplaceDate(cells.getCell(12) == null ? "" : cells.getCell(12).getStringCellValue());
                cells.getCell(13).setCellType(CellType.STRING);
                reqDTO.setRemark(cells.getCell(13) == null ? "" : cells.getCell(13).getStringCellValue());
                reqDTO.setRecId(TokenUtil.getUuId());
                reqDTO.setDeleteFlag("0");
                reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                temp.add(reqDTO);
            }
            fileInputStream.close();
            if (temp.size() > 0) {
                partReplaceMapper.importPartReplace(temp);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void exportPartReplace(String equipName, String replacementName, String faultWorkNo, String orgType, String replaceReason, HttpServletResponse response) throws IOException {
        List<PartReplaceResDTO> partReplaceResDTOList = partReplaceMapper.listPartReplace(equipName, replacementName, faultWorkNo, orgType, replaceReason);
        if (partReplaceResDTOList != null && !partReplaceResDTOList.isEmpty()) {
            List<ExcelPartReplaceResDTO> list = new ArrayList<>();
            for (PartReplaceResDTO resDTO : partReplaceResDTOList) {
                ExcelPartReplaceResDTO res = new ExcelPartReplaceResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setOrgType(CommonConstants.TEN_STRING.equals(resDTO.getOrgType()) ? "维保" : CommonConstants.TWENTY_STRING.equals(resDTO.getOrgType()) ? "一级修工班" : CommonConstants.THIRTY_STRING.equals(resDTO.getOrgType()) ? "二级修工班" : "售后服务站");
                list.add(res);
            }
            EasyExcelUtils.export(response, "部件更换台账信息", list);
        }
    }

}
