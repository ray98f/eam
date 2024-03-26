package com.wzmtr.eam.service.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.detection.DetectionDetailExportReqDTO;
import com.wzmtr.eam.dto.req.detection.DetectionDetailReqDTO;
import com.wzmtr.eam.dto.req.detection.DetectionReqDTO;
import com.wzmtr.eam.dto.res.detection.DetectionDetailResDTO;
import com.wzmtr.eam.dto.res.detection.DetectionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

public interface DetectionService {
    
    Page<DetectionResDTO> pageDetection(String checkNo, String sendVerifyNo, String editDeptCode, String recStatus, PageReqDTO pageReqDTO);

    DetectionResDTO getDetectionDetail(String id);

    void addDetection(DetectionReqDTO detectionReqDTO);

    void modifyDetection(DetectionReqDTO detectionReqDTO);

    void deleteDetection(BaseIdsEntity baseIdsEntity);

    void submitDetection(DetectionReqDTO detectionReqDTO) throws Exception;

    void examineDetection(DetectionReqDTO detectionReqDTO);

    void exportDetection(String checkNo, String sendVerifyNo, String editDeptCode, String recStatus, HttpServletResponse response) throws IOException;

    Page<DetectionDetailResDTO> pageDetectionDetail(String equipCode, String testRecId, PageReqDTO pageReqDTO);

    DetectionDetailResDTO getDetectionDetailDetail(String id);

    void addDetectionDetail(DetectionDetailReqDTO detectionDetailReqDTO) throws ParseException;

    void modifyDetectionDetail(DetectionDetailReqDTO detectionDetailReqDTO) throws ParseException;

    void deleteDetectionDetail(BaseIdsEntity baseIdsEntity);

    void exportDetectionDetail(DetectionDetailExportReqDTO detectionDetailExportReqDTO, HttpServletResponse response) throws IOException;
}
