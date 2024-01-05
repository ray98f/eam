package com.wzmtr.eam.service.special.equip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.special.equip.DetectionPlanDetailReqDTO;
import com.wzmtr.eam.dto.req.special.equip.DetectionPlanReqDTO;
import com.wzmtr.eam.dto.res.special.equip.DetectionPlanDetailResDTO;
import com.wzmtr.eam.dto.res.special.equip.DetectionPlanResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface DetectionPlanService {
    
    Page<DetectionPlanResDTO> pageDetectionPlan(String instrmPlanNo, String planStatus, String editDeptCode,
                                                String assetKindCode, String planPeriodMark, PageReqDTO pageReqDTO);

    DetectionPlanResDTO getDetectionPlanDetail(String id);

    void addDetectionPlan(DetectionPlanReqDTO detectionPlanReqDTO);

    void modifyDetectionPlan(DetectionPlanReqDTO detectionPlanReqDTO);

    void deleteDetectionPlan(BaseIdsEntity baseIdsEntity);

    void submitDetectionPlan(DetectionPlanReqDTO detectionPlanReqDTO) throws Exception;

    void examineDetectionPlan(DetectionPlanReqDTO detectionPlanReqDTO);

    void exportDetectionPlan(String instrmPlanNo, String  planStatus, String  editDeptCode,
                             String assetKindCode, String  planPeriodMark, HttpServletResponse response) throws IOException;

    Page<DetectionPlanDetailResDTO> pageDetectionPlanDetail(String instrmPlanNo, PageReqDTO pageReqDTO);

    DetectionPlanDetailResDTO getDetectionPlanDetailDetail(String id);

    void addDetectionPlanDetail(DetectionPlanDetailReqDTO detectionPlanDetailReqDTO);

    void modifyDetectionPlanDetail(DetectionPlanDetailReqDTO detectionPlanDetailReqDTO);

    void deleteDetectionPlanDetail(BaseIdsEntity baseIdsEntity);

    void exportDetectionPlanDetail(String instrmPlanNo, HttpServletResponse response) throws IOException;
}
