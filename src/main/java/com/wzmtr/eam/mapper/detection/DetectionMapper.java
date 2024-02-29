package com.wzmtr.eam.mapper.detection;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.detection.DetectionDetailReqDTO;
import com.wzmtr.eam.dto.req.detection.DetectionReqDTO;
import com.wzmtr.eam.dto.res.detection.DetectionDetailResDTO;
import com.wzmtr.eam.dto.res.detection.DetectionResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface DetectionMapper {
    
    Page<DetectionResDTO> pageDetection(Page<DetectionResDTO> page, String checkNo, String sendVerifyNo, String editDeptCode, String recStatus);

    DetectionResDTO getDetectionDetail(String id);

    String getMaxCode();

    void addDetection(DetectionReqDTO detectionReqDTO);

    List<String> hasDetail(String testRecId);

    void modifyDetection(DetectionReqDTO detectionReqDTO);

    void deleteDetectionDetail(String testRecId, String recId, String userId, String time);

    void deleteDetection(String id, String userId, String time);

    List<DetectionResDTO> listDetection(String recId, String checkNo, String sendVerifyNo, String editDeptCode, String recStatus);

    Page<DetectionDetailResDTO> pageDetectionDetail(Page<DetectionResDTO> page, String testRecId);

    DetectionDetailResDTO getDetectionDetailDetail(String id);

    void addDetectionDetail(DetectionDetailReqDTO detectionDetailReqDTO);

    void modifyDetectionDetail(DetectionDetailReqDTO detectionDetailReqDTO);

    List<DetectionDetailResDTO> listDetectionDetail(String testRecId);

    List<DetectionDetailResDTO> queryMsg(String testRecId);

    void updateEquip(DetectionDetailResDTO detectionDetailResDTO);

}
