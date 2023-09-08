package com.wzmtr.eam.service.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.overhaul.*;
import com.wzmtr.eam.dto.res.overhaul.OverhaulObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulPlanResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulWeekPlanResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

public interface OverhaulWeekPlanService {

    Page<OverhaulWeekPlanResDTO> pageOverhaulWeekPlan(OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO, PageReqDTO pageReqDTO);

    OverhaulWeekPlanResDTO getOverhaulWeekPlanDetail(String id);

    void addOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws ParseException;

    void modifyOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO);

    void deleteOverhaulWeekPlan(BaseIdsEntity baseIdsEntity);

    void triggerOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws Exception;

    void submitOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws Exception;

    void exportOverhaulWeekPlan(OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO, HttpServletResponse response);

    Page<OverhaulPlanResDTO> pageOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, PageReqDTO pageReqDTO);

    OverhaulPlanResDTO getOverhaulPlanDetail(String id);

    void addOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) throws ParseException;

    void modifyOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO);

    void deleteOverhaulPlan(BaseIdsEntity baseIdsEntity);

    void importOverhaulPlan(MultipartFile file);

    void exportOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, HttpServletResponse response);

    Page<OverhaulObjectResDTO> pageOverhaulObject(String planCode, String planName, String objectCode, String objectName, PageReqDTO pageReqDTO);

    OverhaulObjectResDTO getOverhaulObjectDetail(String id);

    void addOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO);

    void modifyOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO);

    void deleteOverhaulObject(BaseIdsEntity baseIdsEntity);

    void exportOverhaulObject(String planCode, String planName, String objectCode, String objectName, HttpServletResponse response);
}
