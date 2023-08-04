package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.PartReplaceReqDTO;
import com.wzmtr.eam.dto.req.WheelsetLathingReqDTO;
import com.wzmtr.eam.dto.res.WheelsetLathingResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.WheelsetLathingMapper;
import com.wzmtr.eam.service.equipment.WheelsetLathingService;
import com.wzmtr.eam.utils.ExcelPortUtil;
import com.wzmtr.eam.utils.FileUtils;
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
    public Page<WheelsetLathingResDTO> pageWheelsetLathing(String trainNo, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return wheelsetLathingMapper.pageWheelsetLathing(pageReqDTO.of(), trainNo);
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
                cells.getCell(0).setCellType(1);
                reqDTO.setTrainNo(cells.getCell(0) == null ? "" : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(1);
                reqDTO.setCarriageNo(cells.getCell(1) == null ? "" : cells.getCell(1).getStringCellValue());
                cells.getCell(2).setCellType(1);
                String axleNo = cells.getCell(2) == null ? "" : cells.getCell(2).getStringCellValue();
                if (!"".equals(axleNo)) {
                    reqDTO.setAxleNo("一轴".equals(axleNo) ? "01" : "二轴".equals(axleNo) ? "02" : "三轴".equals(axleNo) ? "03" : "04");
                } else {
                    reqDTO.setAxleNo(axleNo);
                }
                cells.getCell(3).setCellType(1);
                reqDTO.setRepairDetail(cells.getCell(3) == null ? "" : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(1);
                reqDTO.setStartDate(cells.getCell(4) == null ? "" : cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(1);
                reqDTO.setCompleteDate(cells.getCell(5) == null ? "" : cells.getCell(5).getStringCellValue());
                cells.getCell(6).setCellType(1);
                reqDTO.setRespPeople(cells.getCell(6) == null ? "" : cells.getCell(6).getStringCellValue());
                cells.getCell(7).setCellType(1);
                reqDTO.setRemark(cells.getCell(7) == null ? "" : cells.getCell(7).getStringCellValue());
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
    public void exportWheelsetLathing(String trainNo, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "列车号", "车厢号", "镟修轮对车轴", "镟修详情", "开始日期", "完成日期", "负责人", "备注", "附件编号", "创建者", "创建时间");
        List<WheelsetLathingResDTO> wheelsetLathingResDTOList = wheelsetLathingMapper.listWheelsetLathing(trainNo);
        List<Map<String, String>> list = new ArrayList<>();
        if (wheelsetLathingResDTOList != null && !wheelsetLathingResDTOList.isEmpty()) {
            for (WheelsetLathingResDTO resDTO : wheelsetLathingResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("列车号", resDTO.getTrainNo());
                map.put("车厢号", resDTO.getCarriageNo());
                map.put("镟修轮对车轴", resDTO.getAxleNo());
                map.put("镟修详情", resDTO.getRepairDetail());
                map.put("开始日期", resDTO.getStartDate());
                map.put("完成日期", resDTO.getCompleteDate());
                map.put("负责人", resDTO.getRespPeople());
                map.put("备注", resDTO.getRemark());
                map.put("附件编号", resDTO.getDocId());
                map.put("创建者", resDTO.getRecCreator());
                map.put("创建时间", resDTO.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("轮对镟修台账信息", listName, list, null, response);
    }

}
