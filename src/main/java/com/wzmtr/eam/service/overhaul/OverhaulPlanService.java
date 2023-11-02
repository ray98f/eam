package com.wzmtr.eam.service.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.overhaul.OverhaulObjectReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulPlanListReqDTO;
import com.wzmtr.eam.dto.req.overhaul.OverhaulPlanReqDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulPlanResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplDetailResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

public interface OverhaulPlanService {

    Page<OverhaulPlanResDTO> pageOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, PageReqDTO pageReqDTO);

    OverhaulPlanResDTO getOverhaulPlanDetail(String id);

    void addOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) throws ParseException;

    void modifyOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO);

    void deleteOverhaulPlan(BaseIdsEntity baseIdsEntity);

    void triggerOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO);

    void submitOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) throws Exception;

    void examineOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO);

    void relationOverhaulPlan(List<OverhaulPlanReqDTO> list);

    void switchsOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO);

    void importOverhaulPlan(MultipartFile file);

    void exportOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, HttpServletResponse response);

    List<OverhaulTplDetailResDTO> getTemplates(String planCode);

    Page<OverhaulObjectResDTO> pageOverhaulObject(String planCode, String planName, String objectCode, String objectName, PageReqDTO pageReqDTO);

    OverhaulObjectResDTO getOverhaulObjectDetail(String id);

    void addOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO);

    void modifyOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO);

    void deleteOverhaulObject(BaseIdsEntity baseIdsEntity);

    void exportOverhaulObject(String planCode, String planName, String objectCode, String objectName, HttpServletResponse response);
}
