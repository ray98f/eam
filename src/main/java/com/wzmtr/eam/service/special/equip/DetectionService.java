package com.wzmtr.eam.service.special.equip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.special.equip.DetectionDetailReqDTO;
import com.wzmtr.eam.dto.req.special.equip.DetectionReqDTO;
import com.wzmtr.eam.dto.res.special.equip.DetectionDetailResDTO;
import com.wzmtr.eam.dto.res.special.equip.DetectionResDTO;
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

    Page<DetectionDetailResDTO> pageDetectionDetail(String testRecId, PageReqDTO pageReqDTO);

    DetectionDetailResDTO getDetectionDetailDetail(String id);

    void addDetectionDetail(DetectionDetailReqDTO detectionDetailReqDTO) throws ParseException;

    void modifyDetectionDetail(DetectionDetailReqDTO detectionDetailReqDTO) throws ParseException;

    void deleteDetectionDetail(BaseIdsEntity baseIdsEntity);

    void exportDetectionDetail(String testRecId, HttpServletResponse response) throws IOException;
}
