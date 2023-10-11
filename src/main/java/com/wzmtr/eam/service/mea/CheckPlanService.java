package com.wzmtr.eam.service.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.mea.CheckPlanListReqDTO;
import com.wzmtr.eam.dto.req.mea.CheckPlanReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaInfoQueryReqDTO;
import com.wzmtr.eam.dto.req.mea.MeaInfoReqDTO;
import com.wzmtr.eam.dto.res.mea.CheckPlanResDTO;
import com.wzmtr.eam.dto.res.mea.MeaInfoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

public interface CheckPlanService {

    Page<CheckPlanResDTO> pageCheckPlan(CheckPlanListReqDTO checkPlanListReqDTO, PageReqDTO pageReqDTO);

    CheckPlanResDTO getCheckPlanDetail(String id);

    void addCheckPlan(CheckPlanReqDTO checkPlanReqDTO);

    void modifyCheckPlan(CheckPlanReqDTO checkPlanReqDTO);

    void deleteCheckPlan(BaseIdsEntity baseIdsEntity);

    void submitCheckPlan(CheckPlanReqDTO checkPlanReqDTO) throws Exception;

    void exportCheckPlan(CheckPlanListReqDTO checkPlanListReqDTO, HttpServletResponse response);

    Page<MeaInfoResDTO> pageCheckPlanInfo(String equipCode, String instrmPlanNo, PageReqDTO pageReqDTO);

    MeaInfoResDTO getCheckPlanInfoDetail(String id);

    void addCheckPlanInfo(MeaInfoReqDTO meaInfoReqDTO);

    void modifyCheckPlanInfo(MeaInfoReqDTO meaInfoReqDTO);

    void deleteCheckPlanInfo(BaseIdsEntity baseIdsEntity);

    void exportCheckPlanInfo(String equipCode, String instrmPlanNo, HttpServletResponse response);

    Page<MeaInfoResDTO> queryCheckPlanInfo(MeaInfoQueryReqDTO meaInfoQueryReqDTO, PageReqDTO pageReqDTO);
    
}
