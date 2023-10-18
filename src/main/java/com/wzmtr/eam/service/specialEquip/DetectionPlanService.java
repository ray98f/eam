package com.wzmtr.eam.service.specialEquip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.specialEquip.DetectionPlanDetailReqDTO;
import com.wzmtr.eam.dto.req.specialEquip.DetectionPlanReqDTO;
import com.wzmtr.eam.dto.res.specialEquip.DetectionPlanDetailResDTO;
import com.wzmtr.eam.dto.res.specialEquip.DetectionPlanResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;

public interface DetectionPlanService {
    
    Page<DetectionPlanResDTO> pageDetectionPlan(String instrmPlanNo, String planStatus, String editDeptCode,
                                                String assetKindCode, String planPeriodMark, PageReqDTO pageReqDTO);

    DetectionPlanResDTO getDetectionPlanDetail(String id);

    void addDetectionPlan(DetectionPlanReqDTO detectionPlanReqDTO);

    void modifyDetectionPlan(DetectionPlanReqDTO detectionPlanReqDTO);

    void deleteDetectionPlan(BaseIdsEntity baseIdsEntity);

    void submitDetectionPlan(String id, String comment) throws Exception;

    void exportDetectionPlan(String instrmPlanNo, String  planStatus, String  editDeptCode,
                             String assetKindCode, String  planPeriodMark, HttpServletResponse response);

    Page<DetectionPlanDetailResDTO> pageDetectionPlanDetail(String instrmPlanNo, PageReqDTO pageReqDTO);

    DetectionPlanDetailResDTO getDetectionPlanDetailDetail(String id);

    void addDetectionPlanDetail(DetectionPlanDetailReqDTO detectionPlanDetailReqDTO);

    void modifyDetectionPlanDetail(DetectionPlanDetailReqDTO detectionPlanDetailReqDTO);

    void deleteDetectionPlanDetail(BaseIdsEntity baseIdsEntity);

    void exportDetectionPlanDetail(String instrmPlanNo, HttpServletResponse response);
}
