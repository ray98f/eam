package com.wzmtr.eam.service.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.statistic.CarFaultQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.CarFaultQueryResDTO;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:48
 */
public interface CarFaultService {

    Page<CarFaultQueryResDTO> query(CarFaultQueryReqDTO reqDTO);
}
