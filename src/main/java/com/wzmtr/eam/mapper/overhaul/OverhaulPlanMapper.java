package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.overhaul.OverhaulObjectReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulPlanListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulPlanReqDTO;
import com.wzmtr.eam.dto.res.basic.WoRuleResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulPlanResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplDetailResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OverhaulPlanMapper {

    Page<OverhaulPlanResDTO> pageOverhaulPlan(Page<OverhaulPlanResDTO> page, OverhaulPlanListReqDTO req);

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

    List<OverhaulPlanResDTO> queryWeekObj(String weekPlanCode);

    List<OverhaulObjectResDTO> listOverhaulObject(String planCode, String recId, String planName,
                                                  String objectCode, String objectName, String templateId1);

    List<OverhaulObjectResDTO> queryObject(String planCode);

    void updatePlanSta(OverhaulPlanReqDTO overhaulTplReqDTO);

    List<WoRuleResDTO.WoRuleDetail> queryAllRule(String planCodeAndIn);

    Page<OverhaulObjectResDTO> pageOverhaulObject(Page<OverhaulObjectResDTO> page, String planCode, String recId,
                                                  String planName, String objectCode, String objectName, String templateId1);

    OverhaulObjectResDTO getOverhaulObjectDetail(String id);

    void addOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO);

    void modifyOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO);

    void deleteOverhaulObject(String id, String userId, String time);

    /**
     * 获取需要触发的检修计划
     * @param nowDay 当前日期，如0206
     * @return 需要自动触发的检修计划id
     */
    List<OverhaulPlanResDTO> getTriggerOverhaulPlan(String nowDay);
}
