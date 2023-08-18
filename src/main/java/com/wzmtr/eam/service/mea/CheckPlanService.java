package com.wzmtr.eam.service.mea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.CheckPlanListReqDTO;
import com.wzmtr.eam.dto.req.CheckPlanReqDTO;
import com.wzmtr.eam.dto.req.MeaInfoReqDTO;
import com.wzmtr.eam.dto.res.CheckPlanResDTO;
import com.wzmtr.eam.dto.res.MeaInfoResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

public interface CheckPlanService {

    Page<CheckPlanResDTO> pageCheckPlan(CheckPlanListReqDTO checkPlanListReqDTO, PageReqDTO pageReqDTO);

    CheckPlanResDTO getCheckPlanDetail(String id);

    void addCheckPlan(CheckPlanReqDTO checkPlanReqDTO);

    void modifyCheckPlan(CheckPlanReqDTO checkPlanReqDTO);

    void deleteCheckPlan(BaseIdsEntity baseIdsEntity);

    void submitCheckPlan(CheckPlanReqDTO checkPlanReqDTO);

    void exportCheckPlan(CheckPlanListReqDTO checkPlanListReqDTO, HttpServletResponse response);

    Page<MeaInfoResDTO> pageCheckPlanInfo(String equipCode, String instrmPlanNo, PageReqDTO pageReqDTO);

    MeaInfoResDTO getCheckPlanInfoDetail(String id);

    void addCheckPlanInfo(MeaInfoReqDTO meaInfoReqDTO);

    void modifyCheckPlanInfo(MeaInfoReqDTO meaInfoReqDTO);

    void deleteCheckPlanInfo(BaseIdsEntity baseIdsEntity);

    void exportCheckPlanInfo(String equipCode, String instrmPlanNo, HttpServletResponse response);
    
}
