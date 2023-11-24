package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.bizobject.FaultTrackBO;
import com.wzmtr.eam.dto.req.fault.FaultBaseNoReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Li.Wang
 * Date: 2023/8/9 14:47
 */
public interface TrackQueryService {

    Page<TrackQueryResDTO> list(TrackQueryReqDTO reqDTO);

    TrackQueryResDTO trackDetail(FaultBaseNoReqDTO reqDTO);

    FaultDetailResDTO faultDetail(FaultDetailReqDTO reqDTO);

    void cancellGenZ(BaseIdsEntity reqDTO);

    void export(TrackQueryReqDTO reqDTO, HttpServletResponse response);

    void save(FaultTrackBO bo);


    // TrackResDTO detail(SidEntity reqDTO);
    //
    //
    // TrackResDTO report(TrackReportReqDTO reqDTO);
    //
    // TrackResDTO close(TrackCloseReqDTO reqDTO);
    //
    // void repair(TrackRepairReqDTO reqDTO);
}
