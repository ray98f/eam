package com.wzmtr.eam.mapper.equipment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.TrainMileReqDTO;
import com.wzmtr.eam.dto.req.TrainMileageReqDTO;
import com.wzmtr.eam.dto.res.TrainMileResDTO;
import com.wzmtr.eam.dto.res.TrainMileageResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface TrainMileMapper {

    Page<TrainMileResDTO> pageTrainMile(Page<TrainMileResDTO> page, String equipCode, String equipName, String originLineNo);

    TrainMileResDTO getTrainMileDetail(String id);

    List<TrainMileResDTO> listTrainMile(String equipCode, String equipName, String originLineNo);

    void updateTrainMile(TrainMileReqDTO trainMileReqDTO);

    void insertTrainMileage(TrainMileageReqDTO trainMileageReqDTO);

    Page<TrainMileageResDTO> pageTrainMileage(Page<TrainMileageResDTO> page, String startTime, String endTime, String equipCode);

    TrainMileageResDTO getTrainMileageDetail(String id);

    List<TrainMileageResDTO> listTrainMileage(String startTime, String endTime, String equipCode);

}
