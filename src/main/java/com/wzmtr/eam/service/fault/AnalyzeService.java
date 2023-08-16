package com.wzmtr.eam.service.fault;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.fault.AnalyzeResDTO;
import com.wzmtr.eam.dto.res.fault.TrackResDTO;
import com.wzmtr.eam.entity.SidEntity;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Li.Wang
 * Date: 2023/8/9 14:47
 */
public interface AnalyzeService {

    Page<AnalyzeResDTO> list(AnalyzeReqDTO reqDTO);

    void export(String faultAnalysisNo, String faultNo, String faultWorkNo, HttpServletResponse response);

    AnalyzeResDTO detail(FaultAnalyzeDetailReqDTO reqDTO);

}
