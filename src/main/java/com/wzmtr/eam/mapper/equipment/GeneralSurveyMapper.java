package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.GeneralSurveyExportReqDTO;
import com.wzmtr.eam.dto.req.equipment.GeneralSurveyReqDTO;
import com.wzmtr.eam.dto.res.equipment.GeneralSurveyResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface GeneralSurveyMapper {

    Page<GeneralSurveyResDTO> pageGeneralSurvey(Page<GeneralSurveyResDTO> page, String trainNo, String recNotifyNo, String recDetail, String orgType);

    GeneralSurveyResDTO getGeneralSurveyDetail(String id);

    void addGeneralSurvey(GeneralSurveyReqDTO generalSurveyReqDTO);

    void modifyGeneralSurvey(GeneralSurveyReqDTO generalSurveyReqDTO);

    void deleteGeneralSurvey(List<String> ids, String userId, String time);

    void importGeneralSurvey(List<GeneralSurveyReqDTO> list);

    List<GeneralSurveyResDTO> listGeneralSurvey(GeneralSurveyExportReqDTO generalSurveyExportReqDTO);

}
