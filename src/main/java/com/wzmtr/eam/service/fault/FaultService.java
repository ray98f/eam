package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.FaultReportPageReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;

/**
 * Author: Li.Wang
 * Date: 2023/8/17 16:21
 */
public interface FaultService {
    Page<FaultDetailResDTO> list(FaultReportPageReqDTO reqDTO);
}
