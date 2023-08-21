package com.wzmtr.eam.service.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.statistic.CarFaultQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.FailreRateQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.MaterialQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.OneCarOneGearQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.*;

import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:48
 */
public interface StatisticService {

    List<FailureRateResDTO> query(FailreRateQueryReqDTO reqDTO);

    Page<MaterialResDTO> query(MaterialQueryReqDTO reqDTO);

    Page<CarFaultQueryResDTO> query(CarFaultQueryReqDTO reqDTO);

    List<ReliabilityResDTO> reliabilityQuery(FailreRateQueryReqDTO reqDTO);

    OneCarOneGearResDTO oneCarOneGearQuery(OneCarOneGearQueryReqDTO reqDTO);

    InspectionJobListResDTO querydmer3(OneCarOneGearQueryReqDTO reqDTO);

    InspectionJobListResDTO queryER4(OneCarOneGearQueryReqDTO reqDTO);
}
