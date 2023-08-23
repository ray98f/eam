package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.dto.res.fault.TrackResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.SidEntity;

/**
 * Author: Li.Wang
 * Date: 2023/8/9 14:47
 */
public interface TrackQueryService {

    Page<TrackQueryResDTO> list(TrackQueryReqDTO reqDTO);

    TrackQueryResDTO trackDetail(SidEntity reqDTO);

    FaultDetailResDTO faultDetail(FaultDetailReqDTO reqDTO);

    void cancellGenZ(BaseIdsEntity reqDTO);

    // TrackResDTO detail(SidEntity reqDTO);
    //
    //
    // TrackResDTO report(TrackReportReqDTO reqDTO);
    //
    // TrackResDTO close(TrackCloseReqDTO reqDTO);
    //
    // void repair(TrackRepairReqDTO reqDTO);
}
