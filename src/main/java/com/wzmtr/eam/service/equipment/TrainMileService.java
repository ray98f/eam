package com.wzmtr.eam.service.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.equipment.TrainMileReqDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileResDTO;
import com.wzmtr.eam.dto.res.equipment.TrainMileageResDTO;
import com.wzmtr.eam.entity.PageReqDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface TrainMileService {

    Page<TrainMileResDTO> pageTrainMile(String equipCode, String equipName, String originLineNo, PageReqDTO pageReqDTO);

    TrainMileResDTO getTrainMileDetail(String id);

    void exportTrainMile(String equipCode, String equipName, String originLineNo, HttpServletResponse response) throws IOException;

    void modifyTrainMile(TrainMileReqDTO trainMileReqDTO);

    Page<TrainMileageResDTO> pageTrainMileage(String startTime, String endTime, String equipCode, PageReqDTO pageReqDTO);

    TrainMileageResDTO getTrainMileageDetail(String id);

    void exportTrainMileage(String startTime, String endTime, String equipCode, HttpServletResponse response) throws IOException;

}
