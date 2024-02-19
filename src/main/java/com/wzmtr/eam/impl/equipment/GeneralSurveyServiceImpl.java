package com.wzmtr.eam.impl.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.wzmtr.eam.constant.CommonConstants;
import com.wzmtr.eam.dto.req.equipment.GeneralSurveyReqDTO;
import com.wzmtr.eam.dto.req.equipment.excel.ExcelGeneralSurveyReqDTO;
import com.wzmtr.eam.dto.res.equipment.GeneralSurveyResDTO;
import com.wzmtr.eam.dto.res.equipment.excel.ExcelGeneralSurveyResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.mapper.equipment.GeneralSurveyMapper;
import com.wzmtr.eam.mapper.file.FileMapper;
import com.wzmtr.eam.service.equipment.GeneralSurveyService;
import com.wzmtr.eam.utils.DateUtils;
import com.wzmtr.eam.utils.EasyExcelUtils;
import com.wzmtr.eam.utils.StringUtils;
import com.wzmtr.eam.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author frp
 */
@Service
@Slf4j
public class GeneralSurveyServiceImpl implements GeneralSurveyService {

    @Autowired
    private GeneralSurveyMapper generalSurveyMapper;

    @Autowired
    private FileMapper fileMapper;

    @Override
    public Page<GeneralSurveyResDTO> pageGeneralSurvey(String trainNo, String recNotifyNo, String recDetail, String orgType, PageReqDTO pageReqDTO) {
        PageHelper.startPage(pageReqDTO.getPageNo(), pageReqDTO.getPageSize());
        Page<GeneralSurveyResDTO> page = generalSurveyMapper.pageGeneralSurvey(pageReqDTO.of(), trainNo, recNotifyNo, recDetail, orgType);
        List<GeneralSurveyResDTO> list = page.getRecords();
        if (StringUtils.isNotEmpty(list)) {
            for (GeneralSurveyResDTO res : list) {
                if (StringUtils.isNotEmpty(res.getDocId())) {
                    res.setDocFile(fileMapper.selectFileInfo(Arrays.asList(res.getDocId().split(","))));
                }
                if (StringUtils.isNotEmpty(res.getRecordId())) {
                    res.setRecordFiles(fileMapper.selectFileInfo(Arrays.asList(res.getRecordId().split(","))));
                }
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public GeneralSurveyResDTO getGeneralSurveyDetail(String id) {
        GeneralSurveyResDTO res = generalSurveyMapper.getGeneralSurveyDetail(id);
        if (Objects.isNull(res)) {
            throw new CommonException(ErrorCode.RESOURCE_NOT_EXIST);
        }
        if (StringUtils.isNotEmpty(res.getDocId())) {
            res.setDocFile(fileMapper.selectFileInfo(Arrays.asList(res.getDocId().split(","))));
        }
        if (StringUtils.isNotEmpty(res.getRecordId())) {
            res.setRecordFiles(fileMapper.selectFileInfo(Arrays.asList(res.getRecordId().split(","))));
        }
        return res;
    }

    @Override
    public void addGeneralSurvey(GeneralSurveyReqDTO generalSurveyReqDTO) {
        generalSurveyReqDTO.setRecId(TokenUtils.getUuId());
        generalSurveyReqDTO.setRecCreator(TokenUtils.getCurrentPersonId());
        generalSurveyReqDTO.setRecCreateTime(DateUtils.getCurrentTime());
        generalSurveyMapper.addGeneralSurvey(generalSurveyReqDTO);
    }

    @Override
    public void modifyGeneralSurvey(GeneralSurveyReqDTO generalSurveyReqDTO) {
        generalSurveyReqDTO.setRecRevisor(TokenUtils.getCurrentPersonId());
        generalSurveyReqDTO.setRecReviseTime(DateUtils.getCurrentTime());
        generalSurveyMapper.modifyGeneralSurvey(generalSurveyReqDTO);
    }

    @Override
    public void deleteGeneralSurvey(BaseIdsEntity baseIdsEntity) {
        if (StringUtils.isNotEmpty(baseIdsEntity.getIds())) {
            for (String id : baseIdsEntity.getIds()) {
                if (!generalSurveyMapper.getGeneralSurveyDetail(id).getRecCreator().equals(TokenUtils.getCurrentPersonId())) {
                    throw new CommonException(ErrorCode.CREATOR_USER_ERROR);
                }
            }
            generalSurveyMapper.deleteGeneralSurvey(baseIdsEntity.getIds(), TokenUtils.getCurrentPersonId(), DateUtils.getCurrentTime());
        } else {
            throw new CommonException(ErrorCode.SELECT_NOTHING);
        }
    }

    @Override
    public void importGeneralSurvey(MultipartFile file) {
        List<ExcelGeneralSurveyReqDTO> list = EasyExcelUtils.read(file, ExcelGeneralSurveyReqDTO.class);
        List<GeneralSurveyReqDTO> temp = new ArrayList<>();
        for (ExcelGeneralSurveyReqDTO reqDTO : list) {
            GeneralSurveyReqDTO req = new GeneralSurveyReqDTO();
            BeanUtils.copyProperties(reqDTO, req);
            req.setRecType(Objects.isNull(reqDTO.getRecType()) ? "" : "普查".equals(reqDTO.getRecType()) ? "10" : "20");
            req.setOrgType(Objects.isNull(reqDTO.getOrgType()) ? "" : "维保".equals(reqDTO.getOrgType()) ? "10" : "20");
            req.setRecId(TokenUtils.getUuId());
            req.setDeleteFlag("0");
            req.setRecCreator(TokenUtils.getCurrentPersonId());
            req.setRecCreateTime(DateUtils.getCurrentTime());
            temp.add(req);
        }
        if (!temp.isEmpty()) {
            generalSurveyMapper.importGeneralSurvey(temp);
        }
    }

    @Override
    public void exportGeneralSurvey(String trainNo, String recNotifyNo, String recDetail, String orgType, HttpServletResponse response) throws IOException {
        List<GeneralSurveyResDTO> generalSurveyResDTOList = generalSurveyMapper.listGeneralSurvey(trainNo, recNotifyNo, recDetail, orgType);
        if (generalSurveyResDTOList != null && !generalSurveyResDTOList.isEmpty()) {
            List<ExcelGeneralSurveyResDTO> list = new ArrayList<>();
            for (GeneralSurveyResDTO resDTO : generalSurveyResDTOList) {
                ExcelGeneralSurveyResDTO res = new ExcelGeneralSurveyResDTO();
                BeanUtils.copyProperties(resDTO, res);
                res.setRecType(CommonConstants.TEN_STRING.equals(resDTO.getRecType()) ? "普查" : "技改");
                res.setOrgType(CommonConstants.TEN_STRING.equals(resDTO.getOrgType()) ? "维保" : CommonConstants.TWENTY_STRING.equals(resDTO.getOrgType()) ? "一级修工班" : CommonConstants.THIRTY_STRING.equals(resDTO.getOrgType()) ? "二级修工班" : "售后服务站");
                list.add(res);
            }
            EasyExcelUtils.export(response, "普查与技改台账信息", list);
        }
    }

}
