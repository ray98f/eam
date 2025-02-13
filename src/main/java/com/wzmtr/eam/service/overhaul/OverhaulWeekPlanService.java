package com.wzmtr.eam.service.overhaul;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.overhaul.*;
import com.wzmtr.eam.dto.res.basic.FaultRepairDeptResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulObjectResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulPlanResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulTplDetailResDTO;
import com.wzmtr.eam.dto.res.overhaul.OverhaulWeekPlanResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface OverhaulWeekPlanService {

    Page<OverhaulWeekPlanResDTO> pageOverhaulWeekPlan(OverhaulWeekPlanListReqDTO overhaulWeekPlanListReqDTO, PageReqDTO pageReqDTO);

    OverhaulWeekPlanResDTO getOverhaulWeekPlanDetail(String id);

    /**
     * 检修周计划（中铁通）获取作业工班
     * @param lineNo 线路编号
     * @param subjectCode 系统编号
     * @return 作业工班列表
     */
    List<FaultRepairDeptResDTO> queryDept(String lineNo, String subjectCode);

    void addOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws ParseException;

    void modifyOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO);

    void deleteOverhaulWeekPlan(BaseIdsEntity baseIdsEntity);

    void triggerOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws Exception;

    void submitOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws Exception;

    void examineOverhaulWeekPlan(OverhaulWeekPlanReqDTO overhaulWeekPlanReqDTO) throws Exception;

    void exportOverhaulWeekPlan(List<String> ids, HttpServletResponse response) throws IOException;

    Page<OverhaulPlanResDTO> pageOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, PageReqDTO pageReqDTO);

    OverhaulPlanResDTO getOverhaulPlanDetail(String id);

    void addOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO) throws ParseException;

    void modifyOverhaulPlan(OverhaulPlanReqDTO overhaulPlanReqDTO);

    void deleteOverhaulPlan(BaseIdsEntity baseIdsEntity);

    void importOverhaulPlan(MultipartFile file);

    void exportOverhaulPlan(OverhaulPlanListReqDTO overhaulPlanListReqDTO, HttpServletResponse response) throws IOException;

    List<OverhaulTplDetailResDTO> getTemplates(String planCode);

    Page<OverhaulObjectResDTO> pageOverhaulObject(String planCode, String planName, String objectCode, String objectName, PageReqDTO pageReqDTO);

    OverhaulObjectResDTO getOverhaulObjectDetail(String id);

    void addOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO);

    void modifyOverhaulObject(OverhaulObjectReqDTO overhaulObjectReqDTO);

    void deleteOverhaulObject(BaseIdsEntity baseIdsEntity);

    void exportOverhaulObject(String planCode, String planName, String objectCode, String objectName, HttpServletResponse response) throws IOException;
}
