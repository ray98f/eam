package com.wzmtr.eam.service.statistic;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.statistic.CarFaultQueryReqDTO;
import com.wzmtr.eam.dto.req.statistic.MaterialQueryReqDTO;
import com.wzmtr.eam.dto.res.statistic.CarFaultQueryResDTO;
import com.wzmtr.eam.dto.res.statistic.MaterialResDTO;

/**
 * Author: Li.Wang
 * Date: 2023/8/18 10:48
 */
public interface MaterialService {
    Page<MaterialResDTO> query(MaterialQueryReqDTO reqDTO);
}
