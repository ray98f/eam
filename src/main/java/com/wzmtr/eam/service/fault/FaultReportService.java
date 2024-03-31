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
     * @param reqDTO
     */
    String addToFault(FaultReportReqDTO reqDTO);

    /**
     * 转报
     * @param reqDTO
     */
    void changeReport(FaultReportReqDTO reqDTO);

    /**
     * 故障提报（到设备）-开放接口
     * @param reqDTO 入参
     * @return 故障编号
     */
    String addToFaultOpen(FaultReportOpenReqDTO reqDTO);

    /**
     * 已提报故障
     * @param reqDTO
     * @return
     */

    Page<FaultReportResDTO> list(FaultReportPageReqDTO reqDTO);

    /**
     * 已提报故障-开放接口
     * @param reqDTO 请求参数
     * @return 已提报故障列表
     */
    Page<FaultReportResDTO> openApiList(FaultReportPageReqDTO reqDTO);
    Page<FaultReportResDTO> carReportList(FaultReportPageReqDTO reqDTO);

    FaultDetailResDTO detail(FaultDetailReqDTO reqDTO);

    void delete(FaultCancelReqDTO reqDTO);

    void cancel(FaultCancelReqDTO reqDTO);

    void update(FaultReportReqDTO reqDTO);
}
