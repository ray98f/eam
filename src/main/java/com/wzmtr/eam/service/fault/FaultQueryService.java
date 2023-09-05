package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.SidEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 16:21
 */
public interface FaultQueryService {
    Page<FaultDetailResDTO> list(FaultQueryReqDTO reqDTO);
    List<FaultDetailResDTO> statisticList(FaultQueryReqDTO reqDTO);

    String queryOrderStatus(SidEntity reqDTO);

    void issue(FaultDetailReqDTO reqDTO);

    void export(FaultQueryReqDTO reqDTO, HttpServletResponse response);

    Page<ConstructionResDTO> construction(FaultQueryReqDTO reqDTO);

    Page<ConstructionResDTO> cancellation(FaultQueryReqDTO reqDTO);

    void transmit(FaultQueryReqDTO reqDTO);

    void submit(FaultQueryReqDTO reqDTO);
}
