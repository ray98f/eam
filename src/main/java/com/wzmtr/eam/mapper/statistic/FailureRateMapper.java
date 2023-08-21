package com.wzmtr.eam.mapper.statistic;

import com.wzmtr.eam.dto.req.statistic.FailreRateQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.FailureRateResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 11:03
 */
@Repository
@Mapper
public interface FailureRateMapper {

    FailureRateResDTO exitingRate(FailreRateQueryReqDTO reqDTO);

    FailureRateResDTO vehicleRate(FailreRateQueryReqDTO reqDTO);

    FailureRateResDTO signalRate(FailreRateQueryReqDTO reqDTO);
    FailureRateResDTO powerRate(FailreRateQueryReqDTO reqDTO);
    FailureRateResDTO PSDrate(FailreRateQueryReqDTO reqDTO);
}
