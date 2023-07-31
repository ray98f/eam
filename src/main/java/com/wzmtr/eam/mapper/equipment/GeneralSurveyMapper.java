package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.GeneralSurveyReqDTO;
import com.wzmtr.eam.dto.res.GeneralSurveyResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface GeneralSurveyMapper {

    Page<GeneralSurveyResDTO> pageGeneralSurvey(Page<GeneralSurveyResDTO> page, String equipName);

    GeneralSurveyResDTO getGeneralSurveyDetail(String id);

    void addGeneralSurvey(GeneralSurveyReqDTO generalSurveyReqDTO);

    void deleteGeneralSurvey(List<String> ids, String userId, String time);

    List<GeneralSurveyResDTO> listGeneralSurvey(String equipName);

}
