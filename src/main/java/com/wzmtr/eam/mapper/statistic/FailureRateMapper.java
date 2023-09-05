package com.wzmtr.eam.mapper.statistic;

import com.wzmtr.eam.dto.req.statistic.FailreRateQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.FailureRateResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:03
 */
@Repository
@Mapper
public interface FailureRateMapper {

    List<FailureRateResDTO>  exitingRate(FailreRateQueryReqDTO reqDTO);

    List<FailureRateResDTO> vehicleRate(FailreRateQueryReqDTO reqDTO);

    List<FailureRateResDTO>  signalRate(FailreRateQueryReqDTO reqDTO);
    List<FailureRateResDTO>  powerRate(FailreRateQueryReqDTO reqDTO);
    List<FailureRateResDTO>  PSDrate(FailreRateQueryReqDTO reqDTO);
}
