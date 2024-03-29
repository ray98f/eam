package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.overhaul.OverhaulMaterialReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulTplDetailReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulTplReqDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulMaterialResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OverhaulTplMapper {

    Page<OverhaulTplResDTO> pageOverhaulTpl(Page<OverhaulTplResDTO> page, String templateId, String templateName, String lineCode, String position1Code,
                                            String subjectCode, String systemCode, String equipTypeCode, String trialStatus,List<String> majors);

    OverhaulTplResDTO getOverhaulTplDetail(String id);

    List<String> getSubjectByUserId(String userId);

    String getMaxCode();

    /**
     * 判断检修计划是否已存在
     * @param overhaulTplReqDTO 检修计划信息
     * @return 检修计划是否已存在
     */
    Integer selectOverhaulTplIsExist(OverhaulTplReqDTO overhaulTplReqDTO);

    void addOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    void modifyOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    void deleteOverhaulTplDetail(String id, String templateId, String userId, String time);

    void deleteOverhaulTpl(String id, String userId, String time);

    /**
     * 检修模板导入
     * @param list 检修模板列表
     */
    void importOverhaulTpl(List<OverhaulTplReqDTO> list);

    void changeOverhaulTpl(OverhaulTplReqDTO overhaulTplReqDTO);

    List<OverhaulTplDetailResDTO> listOverhaulTplDetail(String templateId);

    List<OverhaulTplResDTO> listOverhaulTpl(String templateId, String templateName, String lineNo, String position1Code,
                                            String majorCode, String systemCode, String equipTypeCode, String trialStatus);

    /**
     * 导出搜索检修模板列表
     * @param ids ids
     * @return 检修模板列表
     */
    List<OverhaulTplResDTO> exportOverhaulTpl(List<String> ids);

    Page<OverhaulTplDetailResDTO> pageOverhaulDetailTpl(Page<OverhaulTplDetailResDTO> page, String templateId);

    /**
     * 根据信息获取模板编号
     * @param templateName 模板名称
     * @param lineName 线路名称
     * @param subjectName 专业名称
     * @param systemName 系统名称
     * @param equipTypeName 设备类别名称
     * @return 模板编号
     */
    String getTemplateId(String templateName, String lineName, String subjectName, String systemName, String equipTypeName);

    OverhaulTplDetailResDTO getOverhaulTplDetailDetail(String id);

    void addOverhaulTplDetail(OverhaulTplDetailReqDTO overhaulTplDetailReqDTO);

    void modifyOverhaulTplDetail(OverhaulTplDetailReqDTO overhaulTplDetailReqDTO);

    /**
     * 检修模板检修项导入
     * @param list 检修模板检修项列表
     */
    void importOverhaulTplDetail(List<OverhaulTplDetailReqDTO> list);

    Page<OverhaulMaterialResDTO> pageOverhaulMaterial(Page<OverhaulMaterialResDTO> page, String templateId);

    OverhaulMaterialResDTO getOverhaulMaterialDetail(String id);

    List<OverhaulTplResDTO> listOverhaulTplStatus(String templateId, String templateName);

    void addOverhaulMaterial(OverhaulMaterialReqDTO overhaulMaterialReqDTO);

    void modifyOverhaulMaterial(OverhaulMaterialReqDTO overhaulMaterialReqDTO);

    void deleteOverhaulMaterial(List<String> ids, String userId, String time);

    /**
     * 检修模板物料导入
     * @param list 检修模板物料列表
     */
    void importOverhaulMaterial(List<OverhaulMaterialReqDTO> list);

    List<OverhaulMaterialResDTO> listOverhaulMaterial(String templateId);

    List<OverhaulTplDetailResDTO> queryTemplate(String lineNo, String subjectCode, String systemCode, String equipTypeCode, String trialStatus);

}
