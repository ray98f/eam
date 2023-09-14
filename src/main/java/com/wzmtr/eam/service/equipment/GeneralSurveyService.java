package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.GeneralSurveyReqDTO;
import com.wzmtr.eam.dto.res.equipment.GeneralSurveyResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface GeneralSurveyService {

    Page<GeneralSurveyResDTO> pageGeneralSurvey(String trainNo, String recNotifyNo, String recDetail, String orgType, PageReqDTO pageReqDTO);

    GeneralSurveyResDTO getGeneralSurveyDetail(String id);

    void addGeneralSurvey(GeneralSurveyReqDTO generalSurveyReqDTO);

    void deleteGeneralSurvey(BaseIdsEntity baseIdsEntity);

    void importGeneralSurvey(MultipartFile file);

    void exportGeneralSurvey(String trainNo, String recNotifyNo, String recDetail, String orgType, HttpServletResponse response);

}
