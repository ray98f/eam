package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.SidEntity;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 16:21
 */
public interface FaultQueryService {
    Page<FaultDetailResDTO> list(FaultQueryReqDTO reqDTO);

    String queryOrderStatus(SidEntity reqDTO);

    void issue(FaultDetailReqDTO reqDTO);
}
