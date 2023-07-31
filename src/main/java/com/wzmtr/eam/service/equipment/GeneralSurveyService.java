package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.GeneralSurveyReqDTO;
import com.wzmtr.eam.dto.res.GeneralSurveyResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface GeneralSurveyService {

    Page<GeneralSurveyResDTO> pageGeneralSurvey(String trainNo, PageReqDTO pageReqDTO);

    GeneralSurveyResDTO getGeneralSurveyDetail(String id);

    void addGeneralSurvey(GeneralSurveyReqDTO generalSurveyReqDTO);

    void deleteGeneralSurvey(BaseIdsEntity baseIdsEntity);

    void importGeneralSurvey(MultipartFile file);

    void exportGeneralSurvey(String trainNo, HttpServletResponse response);

}
