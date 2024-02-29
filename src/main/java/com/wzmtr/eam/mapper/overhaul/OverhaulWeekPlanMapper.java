package com.wzmtr.eam.mapper.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.overhaul.OverhaulWeekPlanListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulWeekPlanReqDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulWeekPlanResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface OverhaulWeekPlanMapper {

    Page<OverhaulWeekPlanResDTO> pageOverhaulWeekPlan(Page<OverhaulWeekPlanResDTO> page, OverhaulWeekPlanListReqDTO req);

    OverhaulWeekPlanResDTO getOverhaulWeekPlanDetail(String id);

    List<String> getSubjectByUserId(String userId);

    String getMaxCode();

    void addOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO);

    void modifyOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO);

    void deleteOverhaulWeekPlan(String id, String userId, String time);

    void deleteOverhaulPlan(String weekPlanCode, String userId, String time);

    void deleteOverhaulObject(String weekPlanCode, String userId, String time);

    List<OverhaulWeekPlanResDTO> listOverhaulWeekPlan(OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO);

    List<OverhaulWeekPlanResDTO> getOverhaulWeekPlanByIds(List<String> ids);
}
