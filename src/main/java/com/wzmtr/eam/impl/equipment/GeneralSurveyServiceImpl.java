package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.dto.req.equipment.GeneralSurveyReqDTO;
import com.wzmtr.eam.dto.res.equipment.GeneralSurveyResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.GeneralSurveyMapper;
import com.wzmtr.eam.service.equipment.GeneralSurveyService;
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
public class GeneralSurveyServiceImpl implements GeneralSurveyService {

    @Autowired
    private GeneralSurveyMapper generalSurveyMapper;

    @Override
    public Page<GeneralSurveyResDTO> pageGeneralSurvey(String trainNo, String recNotifyNo, String recDetail, String orgType, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        return generalSurveyMapper.pageGeneralSurvey(pageReqDTO.of(), trainNo, recNotifyNo, recDetail, orgType);
    }

    @Override
    public GeneralSurveyResDTO getGeneralSurveyDetail(String id) {
        return generalSurveyMapper.getGeneralSurveyDetail(id);
    }

    @Override
    public void addGeneralSurvey(GeneralSurveyReqDTO generalSurveyReqDTO) {
        generalSurveyReqDTO.setRecId(TokenUtil.getUuId());
        generalSurveyReqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
        generalSurveyReqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        generalSurveyMapper.addGeneralSurvey(generalSurveyReqDTO);
    }

    @Override
    public void deleteGeneralSurvey(BaseIdsEntity baseIdsEntity) {
        if (baseIdsEntity.getIds() != null && !baseIdsEntity.getIds().isEmpty()) {
            for (String id : baseIdsEntity.getIds()) {
                if (!generalSurveyMapper.getGeneralSurveyDetail(id).getRecCreator().equals(TokenUtil.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
            }
            generalSurveyMapper.deleteGeneralSurvey(baseIdsEntity.getIds(), TokenUtil.getCurrentPersonId(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importGeneralSurvey(MultipartFile file) {
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
            List<GeneralSurveyReqDTO> temp = new ArrayList<>();
            for (Row cells : sheet) {
                if (cells.getRowNum() < 1) {
                    continue;
                }
                GeneralSurveyReqDTO reqDTO = new GeneralSurveyReqDTO();
                cells.getCell(0).setCellType(1);
                reqDTO.setTrainNo(cells.getCell(0) == null ? "" : cells.getCell(0).getStringCellValue());
                cells.getCell(1).setCellType(1);
                reqDTO.setRecType(cells.getCell(1) == null ? "" : "普查".equals(cells.getCell(1).getStringCellValue()) ? "10" : "20");
                cells.getCell(2).setCellType(1);
                reqDTO.setRecNotifyNo(cells.getCell(2) == null ? "" : cells.getCell(2).getStringCellValue());
                cells.getCell(3).setCellType(1);
                reqDTO.setRecDetail(cells.getCell(3) == null ? "" : cells.getCell(3).getStringCellValue());
                cells.getCell(4).setCellType(1);
                reqDTO.setCompleteDate(cells.getCell(4) == null ? "" : cells.getCell(4).getStringCellValue());
                cells.getCell(5).setCellType(1);
                reqDTO.setOrgType(cells.getCell(5) == null ? "" : "维保".equals(cells.getCell(5).getStringCellValue()) ? "10" : "20");
                cells.getCell(6).setCellType(1);
                reqDTO.setRemark(cells.getCell(6) == null ? "" : cells.getCell(6).getStringCellValue());
                reqDTO.setRecId(TokenUtil.getUuId());
                reqDTO.setDeleteFlag("0");
                reqDTO.setRecCreator(TokenUtil.getCurrentPersonId());
                reqDTO.setRecCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
                temp.add(reqDTO);
            }
            fileInputStream.close();
            if (temp.size() > 0) {
                generalSurveyMapper.importGeneralSurvey(temp);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.IMPORT_ERROR);
        }
    }

    @Override
    public void exportGeneralSurvey(String trainNo, String recNotifyNo, String recDetail, String orgType, HttpServletResponse response) {
        List<String> listName = Arrays.asList("记录编号", "列车号", "类别", "技术通知单编号", "项目内容", "完成时间", "作业单位", "备注", "附件编号", "创建者", "创建时间");
        List<GeneralSurveyResDTO> generalSurveyResDTOList = generalSurveyMapper.listGeneralSurvey(trainNo, recNotifyNo, recDetail, orgType);
        List<Map<String, String>> list = new ArrayList<>();
        if (generalSurveyResDTOList != null && !generalSurveyResDTOList.isEmpty()) {
            for (GeneralSurveyResDTO resDTO : generalSurveyResDTOList) {
                Map<String, String> map = new HashMap<>();
                map.put("记录编号", resDTO.getRecId());
                map.put("列车号", resDTO.getTrainNo());
                map.put("类别", resDTO.getRecType());
                map.put("技术通知单编号", resDTO.getRecNotifyNo());
                map.put("项目内容", resDTO.getRecDetail());
                map.put("完成时间", resDTO.getCompleteDate());
                map.put("作业单位", resDTO.getOrgType());
                map.put("备注", resDTO.getRemark());
                map.put("附件编号", resDTO.getDocId());
                map.put("创建者", resDTO.getRecCreator());
                map.put("创建时间", resDTO.getRecCreateTime());
                list.add(map);
            }
        }
        ExcelPortUtil.excelPort("普查与技改台账信息", listName, list, null, response);
    }

}
