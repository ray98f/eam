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
import java.io.IOException;
import java.util.List;

public interface CheckPlanService {

    Page<CheckPlanResDTO> pageCheckPlan(CheckPlanListReqDTO checkPlanListReqDTO, PageReqDTO pageReqDTO);

    CheckPlanResDTO getCheckPlanDetail(String id);

    void addCheckPlan(CheckPlanReqDTO checkPlanReqDTO);

    void modifyCheckPlan(CheckPlanReqDTO checkPlanReqDTO);

    void deleteCheckPlan(BaseIdsEntity baseIdsEntity);

    void submitCheckPlan(CheckPlanReqDTO checkPlanReqDTO) throws Exception;

    void examineCheckPlan(CheckPlanReqDTO checkPlanReqDTO);

    void exportCheckPlan(List<String> ids, HttpServletResponse response) throws IOException;

    Page<MeaInfoResDTO> pageCheckPlanInfo(String equipCode, String instrmPlanNo, PageReqDTO pageReqDTO);

    MeaInfoResDTO getCheckPlanInfoDetail(String id);

    void addCheckPlanInfo(MeaInfoReqDTO meaInfoReqDTO);

    void modifyCheckPlanInfo(MeaInfoReqDTO meaInfoReqDTO);

    void deleteCheckPlanInfo(BaseIdsEntity baseIdsEntity);

    void exportCheckPlanInfo(List<String> ids, HttpServletResponse response) throws IOException;

    Page<MeaInfoResDTO> queryCheckPlanInfo(MeaInfoQueryReqDTO meaInfoQueryReqDTO, PageReqDTO pageReqDTO);
    
}
