package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.fault.TrackResDTO;
import com.wzmtr.eam.entity.SidEntity;

/**
 * Author: Li.Wang
 * Date: 2023/8/9 14:47
 */
public interface TrackService {

    Page<TrackResDTO> list(TrackReqDTO reqDTO);

    TrackResDTO detail(SidEntity reqDTO);

    void report(TrackReportReqDTO reqDTO);

    void close(TrackCloseReqDTO reqDTO);

    void repair(TrackRepairReqDTO reqDTO);
    void returns(FaultSubmitReqDTO reqDTO);

    void transmit(TrackTransmitReqDTO reqDTO);

    void submit(FaultSubmitReqDTO reqDTO);

    void export(TrackExportReqDTO reqDTO);
}
