package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;

/**
 * Author: Li.Wang
 * Date: 2023/8/9 14:47
 */
public interface FaultReportService {

    /**
     * 提报
     *
     * @param reqDTO
     */
    String addToEquip(FaultReportReqDTO reqDTO);

    /**
     * 已提报故障
     * @param reqDTO
     * @return
     */

    Page<FaultReportResDTO> list(FaultReportPageReqDTO reqDTO);
    Page<FaultReportResDTO> carReportList(FaultReportPageReqDTO reqDTO);

    void addToMajor(FaultReportToMajorReqDTO reqDTO);

    FaultDetailResDTO detail(FaultDetailReqDTO reqDTO);

    void delete(FaultCancelReqDTO reqDTO);

    void cancel(FaultCancelReqDTO reqDTO);

    void update(FaultReportReqDTO reqDTO);
}
