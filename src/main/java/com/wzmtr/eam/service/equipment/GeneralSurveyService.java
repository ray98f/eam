package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.GeneralSurveyExportReqDTO;
import com.wzmtr.eam.dto.req.equipment.GeneralSurveyReqDTO;
import com.wzmtr.eam.dto.res.equipment.GeneralSurveyResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface GeneralSurveyService {

    Page<GeneralSurveyResDTO> pageGeneralSurvey(String trainNo, String recNotifyNo, String recDetail, String orgType, PageReqDTO pageReqDTO);

    GeneralSurveyResDTO getGeneralSurveyDetail(String id);

    void addGeneralSurvey(GeneralSurveyReqDTO generalSurveyReqDTO);

    void modifyGeneralSurvey(GeneralSurveyReqDTO generalSurveyReqDTO);

    void deleteGeneralSurvey(BaseIdsEntity baseIdsEntity);

    void importGeneralSurvey(MultipartFile file);

    void exportGeneralSurvey(GeneralSurveyExportReqDTO generalSurveyExportReqDTO, HttpServletResponse response) throws IOException;

}
