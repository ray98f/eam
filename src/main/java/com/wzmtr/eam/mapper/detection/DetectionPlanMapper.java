package com.wzmtr.eam.mapper.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.detection.DetectionPlanDetailReqDTO;
import com.wzmtr.eam.dto.req.detection.DetectionPlanReqDTO;
import com.wzmtr.eam.dto.res.detection.DetectionPlanDetailResDTO;
import com.wzmtr.eam.dto.res.detection.DetectionPlanResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface DetectionPlanMapper {
    
    Page<DetectionPlanResDTO> pageDetectionPlan(Page<DetectionPlanResDTO> page, String instrmPlanNo, String  planStatus, String  editDeptCode,
                                                String assetKindCode, String  planPeriodMark);

    DetectionPlanResDTO getDetectionPlanDetail(String id);

    String getMaxCode();

    void addDetectionPlan(DetectionPlanReqDTO detectionPlanReqDTO);

    List<String> hasDetail(String instrmPlanNo);

    void modifyDetectionPlan(DetectionPlanReqDTO specialEquipReqDTO);

    void deleteDetectionPlanDetail(String instrmPlanNo, String userId, String time);

    void deleteDetectionPlan(String id, String userId, String time);

    List<DetectionPlanResDTO> listDetectionPlan(String instrmPlanNo, String  planStatus, String  editDeptCode,
                                                String assetKindCode, String  planPeriodMark);

    Page<DetectionPlanDetailResDTO> pageDetectionPlanDetail(Page<DetectionPlanResDTO> page, String instrmPlanNo);

    DetectionPlanDetailResDTO getDetectionPlanDetailDetail(String id);

    void addDetectionPlanDetail(DetectionPlanDetailReqDTO detectionPlanDetailReqDTO);

    void modifyDetectionPlanDetail(DetectionPlanDetailReqDTO detectionPlanDetailReqDTO);

    List<DetectionPlanDetailResDTO> listDetectionPlanDetail(String instrmPlanNo);
}
