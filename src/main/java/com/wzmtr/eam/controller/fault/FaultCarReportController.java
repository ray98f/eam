package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportPageReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultReportReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.FaultReportResDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.FaultReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Author: Li.Wang
 * Date: 2023/8/15 17:04
 */
@RestController
@RequestMapping("/fault/car/report")
@Api(tags = "故障管理-车辆故障提报")
public class FaultCarReportController {
    @Autowired
    private FaultReportService reportService;

    @ApiOperation(value = "故障提报")
    @PostMapping("/add")
    public DataResponse<String> addToEquip(@RequestBody @Valid FaultReportReqDTO reqDTO) {
        return DataResponse.of(reportService.addToFault(reqDTO));
    }
    @ApiOperation(value = "已提报故障")
    @PostMapping("/list")
    public PageResponse<FaultReportResDTO> list(@RequestBody FaultReportPageReqDTO reqDTO) {
        return PageResponse.of(reportService.carReportList(reqDTO));
    }
    @ApiOperation(value = "故障编号详情")
    @PostMapping("/detail")
    public DataResponse<FaultDetailResDTO> detail(@RequestBody FaultDetailReqDTO reqDTO) {
        return DataResponse.of(reportService.detail(reqDTO));
    }

}
