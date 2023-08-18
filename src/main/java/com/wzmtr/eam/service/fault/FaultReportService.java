package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.req.secure.SecureCheckAddReqDTO;
import com.wzmtr.eam.dto.res.fault.AnalyzeResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Li.Wang
 * Date: 2023/8/9 14:47
 */
public interface FaultReportService {

    /**
     * 提报
     * @param reqDTO
     */
    void addToEquip(FaultReportReqDTO reqDTO);

    Page<FaultReportResDTO> list(FaultReportPageReqDTO reqDTO);

    void addToMajor(FaultReportToMajorReqDTO reqDTO);

    FaultDetailResDTO detail(FaultDetailReqDTO reqDTO);
}
