package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.OverhaulObjectReqDTO;
import com.wzmtr.eam.dto.req.OverhaulPlanListReqDTO;
import com.wzmtr.eam.dto.req.OverhaulPlanReqDTO;
import com.wzmtr.eam.dto.res.OverhaulObjectResDTO;
import com.wzmtr.eam.dto.res.OverhaulPlanResDTO;
import com.wzmtr.eam.dto.res.OverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.WoRuleResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OverhaulPlanMapper {

    Page<OverhaulPlanResDTO> pageOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO);

    OverhaulPlanResDTO getOverhaulPlanDetail(String id, String objectFlag);

    List<String> getSubjectByUserId(String userId);

    List<OverhaulTplDetailResDTO> getOrderIsValid(String planCode);

    void updateTrigerTime(String beforeTriggerTime, String planCode);

    String getMaxCode();

    void addOverhaulPlan(OverhaulPlanReqDTO overhaulTplReqDTO);

    void modifyOverhaulPlan(OverhaulPlanReqDTO overhaulTplReqDTO);

    void deleteOverhaulPlanDetail(String id, String planCode, String userId, String time);

    void deleteOverhaulPlan(String id, String userId, String time);

    List<OverhaulPlanResDTO> listOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO);

    List<OverhaulObjectResDTO> listOverhaulObject(String planCode, String recId, String planName, String objectCode, String objectName, String templateId1);

    void updatePlanSta(OverhaulPlanReqDTO overhaulTplReqDTO);

    List<WoRuleResDTO.WoRuleDetail> queryAllRule(String planCodeAndIn);

    Page<OverhaulObjectResDTO> pageOverhaulObject(Page<OverhaulObjectResDTO> page, String planCode, String recId, String planName, String objectCode, String objectName, String templateId1);

    OverhaulObjectResDTO getOverhaulObjectDetail(String id);

    void addOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO);

    void modifyOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO);

    void deleteOverhaulObject(String id, String userId, String time);
}
