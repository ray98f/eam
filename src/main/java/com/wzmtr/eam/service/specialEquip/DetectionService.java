package com.wzmtr.eam.service.specialEquip;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.DetectionDetailReqDTO;
import com.wzmtr.eam.dto.req.DetectionReqDTO;
import com.wzmtr.eam.dto.res.DetectionDetailResDTO;
import com.wzmtr.eam.dto.res.DetectionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

public interface DetectionService {
    
    Page<DetectionResDTO> pageDetection(String checkNo, String sendVerifyNo, String editDeptCode, String recStatus, PageReqDTO pageReqDTO);

    DetectionResDTO getDetectionDetail(String id);

    void addDetection(DetectionReqDTO detectionReqDTO);

    void modifyDetection(DetectionReqDTO detectionReqDTO);

    void deleteDetection(BaseIdsEntity baseIdsEntity);

    void submitDetection(String id);

    void exportDetection(String checkNo, String sendVerifyNo, String editDeptCode, String recStatus, HttpServletResponse response);

    Page<DetectionDetailResDTO> pageDetectionDetail(String testRecId, PageReqDTO pageReqDTO);

    DetectionDetailResDTO getDetectionDetailDetail(String id);

    void addDetectionDetail(DetectionDetailReqDTO detectionDetailReqDTO) throws ParseException;

    void modifyDetectionDetail(DetectionDetailReqDTO detectionDetailReqDTO) throws ParseException;

    void deleteDetectionDetail(BaseIdsEntity baseIdsEntity);

    void exportDetectionDetail(String testRecId, HttpServletResponse response);
}
