package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.TrainMileReqDTO;
import com.wzmtr.eam.dto.res.TrainMileResDTO;
import com.wzmtr.eam.dto.res.TrainMileageResDTO;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface TrainService {

    Page<TrainMileResDTO> pageTrainMile(String equipCode, String equipName, String originLineNo, PageReqDTO pageReqDTO);

    TrainMileResDTO getTrainMileDetail(String id);

    void exportTrainMile(String equipCode, String equipName, String originLineNo, HttpServletResponse response);

    void modifyTrainMile(List<TrainMileReqDTO> list);

    Page<TrainMileageResDTO> pageTrainMileage(String startTime, String endTime, String equipCode, PageReqDTO pageReqDTO);

    TrainMileageResDTO getTrainMileageDetail(String id);

    void exportTrainMileage(String startTime, String endTime, String equipCode, HttpServletResponse response);

}
