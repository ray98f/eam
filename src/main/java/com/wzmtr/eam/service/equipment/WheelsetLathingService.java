package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.WheelsetLathingReqDTO;
import com.wzmtr.eam.dto.res.equipment.WheelsetLathingResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface WheelsetLathingService {

    Page<WheelsetLathingResDTO> pageWheelsetLathing(String trainNo, String carriageNo, String axleNo, String wheelNo, PageReqDTO pageReqDTO);

    WheelsetLathingResDTO getWheelsetLathingDetail(String id);

    void addWheelsetLathing(WheelsetLathingReqDTO wheelsetLathingReqDTO);

    void deleteWheelsetLathing(BaseIdsEntity baseIdsEntity);

    void importWheelsetLathing(MultipartFile file);

    void exportWheelsetLathing(String trainNo, String carriageNo, String axleNo, String wheelNo, HttpServletResponse response);

}
