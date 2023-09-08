package com.wzmtr.eam.mapper.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.mea.CheckPlanListReqDTO;
import com.wzmtr.eam.dto.req.mea.CheckPlanReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaInfoReqDTO;
import com.wzmtr.eam.dto.res.mea.CheckPlanResDTO;
import com.wzmtr.eam.dto.res.mea.MeaInfoResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface CheckPlanMapper {

    Page<CheckPlanResDTO> pageCheckPlan(Page<CheckPlanResDTO> page, CheckPlanListReqDTO req);

    CheckPlanResDTO getCheckPlanDetail(String id);

    String getMaxCode();

    void addCheckPlan(CheckPlanReqDTO checkPlanReqDTO);

    void modifyCheckPlan(CheckPlanReqDTO checkPlanReqDTO);

    void deleteCheckPlan(String id, String userId, String time);

    void deleteCheckPlanDetail(String id, String instrmPlanNo, String userId, String time);

    List<CheckPlanResDTO> listCheckPlan(CheckPlanListReqDTO checkPlanListReqDTO);

    Page<MeaInfoResDTO> pageInfo(Page<MeaInfoResDTO> page, String equipCode, String instrmPlanNo);

    MeaInfoResDTO getInfoDetail(String id);

    void addInfo(MeaInfoReqDTO meaInfoReqDTO);

    void modifyInfo(MeaInfoReqDTO meaInfoReqDTO);

    List<MeaInfoResDTO> listInfo(String equipCode, String instrmPlanNo);
}
