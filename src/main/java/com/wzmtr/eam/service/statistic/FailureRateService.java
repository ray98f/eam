package com.wzmtr.eam.service.statistic;

import com.wzmtr.eam.dto.req.statistic.FailreRateQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.FailureRateResDTO;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:48
 */
public interface FailureRateService {
    List<FailureRateResDTO> query(FailreRateQueryReqDTO reqDTO);
}
