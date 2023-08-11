package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.OverhaulMaterialReqDTO;
import com.wzmtr.eam.dto.req.OverhaulTplDetailReqDTO;
import com.wzmtr.eam.dto.req.OverhaulTplReqDTO;
import com.wzmtr.eam.dto.res.OverhaulMaterialResDTO;
import com.wzmtr.eam.dto.res.OverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.OverhaulTplResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OverhaulTplMapper {

    Page<OverhaulTplResDTO> pageOverhaulTpl(Page<OverhaulTplResDTO> page, String templateId, String templateName, String lineNo, String position1Code,
                                            String majorCode, String systemCode, String equipTypeCode, String trialStatus);

    OverhaulTplResDTO getOverhaulTplDetail(String id);

    List<String> getSubjectByUserId(String userId);

    String getMaxCode();

    void addOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    void modifyOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    void deleteOverhaulTplDetail(String id, String templateId, String userId, String time);

    void deleteOverhaulTpl(String id, String userId, String time);

    void changeOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    List<OverhaulTplDetailResDTO> listOverhaulTplDetail(String templateId);

    List<OverhaulTplResDTO> listOverhaulTpl(String templateId, String templateName, String lineNo, String position1Code,
                                            String majorCode, String systemCode, String equipTypeCode, String trialStatus);

    Page<OverhaulTplDetailResDTO> pageOverhaulDetailTpl(Page<OverhaulTplDetailResDTO> page, String templateId);

    OverhaulTplDetailResDTO getOverhaulTplDetailDetail(String id);

    void addOverhaulTplDetail(OverhaulTplDetailReqDTO overhaulTplDetailReqDTO);

    void modifyOverhaulTplDetail(OverhaulTplDetailReqDTO overhaulTplDetailReqDTO);

    Page<OverhaulMaterialResDTO> pageOverhaulMaterial(Page<OverhaulMaterialResDTO> page, String templateId);

    OverhaulMaterialResDTO getOverhaulMaterialDetail(String id);

    List<OverhaulTplResDTO> listOverhaulTplStatus(String templateId, String templateName);

    void addOverhaulMaterial(OverhaulMaterialReqDTO overhaulMaterialReqDTO);

    void modifyOverhaulMaterial(OverhaulMaterialReqDTO overhaulMaterialReqDTO);

    void deleteOverhaulMaterial(List<String> ids, String userId, String time);

    List<OverhaulMaterialResDTO> listOverhaulMaterial(String templateId);

    List<OverhaulTplDetailResDTO> queryTemplate(String lineNo, String subjectCode, String systemCode, String equipTypeCode, String trialStatus);

}
