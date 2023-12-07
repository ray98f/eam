package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.WheelsetLathingReqDTO;
import com.wzmtr.eam.dto.res.equipment.WheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelWheelsetLathingResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.WheelsetLathingMapper;
import com.wzmtr.eam.service.equipment.WheelsetLathingService;
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
public class WheelsetLathingServiceImpl implements WheelsetLathingService {

    @Autowired
    private WheelsetLathingMapper wheelsetLathingMapper;

    @Override
    public Page<WheelsetLathingResDTO> pageWheelsetLathing(String trainNo, String carriageNo, String axleNo, String wheelNo, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return wheelsetLathingMapper.pageWheelsetLathing(pageReqDTO.of(), trainNo, carriageNo, axleNo, wheelNo);
    }

    @Override
    public WheelsetLathingResDTO getWheelsetLathingDetail(String id) {
        return wheelsetLathingMapper.getWheelsetLathingDetail(id);
    }

    @Override
    public void addWheelsetLathing(WheelsetLathingReqDTO wheelsetLathingReqDTO) {
        wheelsetLathingReqDTO.setRecId(TokenUtil.getUuId());
        wheelsetLathingReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        wheelsetLathingReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        wheelsetLathingMapper.addWheelsetLathing(wheelsetLathingReqDTO);
    }

    @Override
    public void deleteWheelsetLathing(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                if (!wheelsetLathingMapper.getWheelsetLathingDetail(id).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
            }
            wheelsetLathingMapper.deleteWheelsetLathing(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importWheelsetLathing(MultipartFile file) {
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
            List<WheelsetLathingReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                WheelsetLathingReqDTO reqDTO = new WheelsetLathingReqDTO();
                cells.getCell(0).setCellType(CellType.STRING);
                reqDTO.setTrainNo(cells.getCell(0) == null ? "" : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(CellType.STRING);
                reqDTO.setCarriageNo(cells.getCell(1) == null ? "" : cells.getCell(1).getStringCellValue());
                cells.getCell(2).setCellType(CellType.STRING);
                String axleNo = cells.getCell(2) == null ? "" : cells.getCell(2).getStringCellValue();
                if (StringUtils.isNotEmpty(axleNo)) {
                    reqDTO.setAxleNo("一轴".equals(axleNo) ? "01" : "二轴".equals(axleNo) ? "02" : "三轴".equals(axleNo) ? "03" : "04");
                } else {
                    reqDTO.setAxleNo(axleNo);
                }
                cells.getCell(3).setCellType(CellType.STRING);
                reqDTO.setWheelNo(cells.getCell(3) == null ? "" : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(CellType.STRING);
                reqDTO.setWheelHeight(cells.getCell(4) == null ? "" : cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(CellType.STRING);
                reqDTO.setWheelThick(cells.getCell(5) == null ? "" : cells.getCell(5).getStringCellValue());
                cells.getCell(6).setCellType(CellType.STRING);
                reqDTO.setWheelDiameter(cells.getCell(6) == null ? "" : cells.getCell(6).getStringCellValue());
                cells.getCell(7).setCellType(CellType.STRING);
                reqDTO.setRepairDetail(cells.getCell(7) == null ? "" : cells.getCell(7).getStringCellValue());
                cells.getCell(8).setCellType(CellType.STRING);
                reqDTO.setStartDate(cells.getCell(8) == null ? "" : cells.getCell(8).getStringCellValue());
                cells.getCell(9).setCellType(CellType.STRING);
                reqDTO.setCompleteDate(cells.getCell(9) == null ? "" : cells.getCell(9).getStringCellValue());
                cells.getCell(10).setCellType(CellType.STRING);
                reqDTO.setRespPeople(cells.getCell(10) == null ? "" : cells.getCell(10).getStringCellValue());
                cells.getCell(11).setCellType(CellType.STRING);
                reqDTO.setRemark(cells.getCell(11) == null ? "" : cells.getCell(11).getStringCellValue());
                reqDTO.setRecId(TokenUtil.getUuId());
                reqDTO.setDeleteFlag("0");
                reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                temp.add(reqDTO);
            }
            fileInputStream.close();
            if (temp.size() > 0) {
                wheelsetLathingMapper.importWheelsetLathing(temp);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void exportWheelsetLathing(String trainNo, String carriageNo, String axleNo, String wheelNo, HttpServletResponse response) throws IOException {
        List<WheelsetLathingResDTO> wheelsetLathingResDTOList = wheelsetLathingMapper.listWheelsetLathing(trainNo, carriageNo, axleNo, wheelNo);
        if (wheelsetLathingResDTOList != null && !wheelsetLathingResDTOList.isEmpty()) {
            List<ExcelWheelsetLathingResDTO> list = new ArrayList<>();
            for (WheelsetLathingResDTO resDTO : wheelsetLathingResDTOList) {
                ExcelWheelsetLathingResDTO res = new ExcelWheelsetLathingResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setAxleNo(CommonConstants.LINE_CODE_ONE.equals(resDTO.getAxleNo()) ? "一轴" : CommonConstants.LINE_CODE_TWO.equals(resDTO.getAxleNo()) ? "二轴" : "03".equals(resDTO.getAxleNo()) ? "三轴" : "四轴");
                list.add(res);
            }
            EasyExcelUtils.export(response, "轮对镟修台账信息", list);
        }
    }

}
