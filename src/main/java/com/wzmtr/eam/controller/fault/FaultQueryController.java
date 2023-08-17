package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.FaultReportPageReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.FaultQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Li.Wang
 * Date: 2023/8/16 20:36
 */
@RestController
@RequestMapping("/fault/car/xxxxx")
@Api(tags = "故障管理-故障查询")
public class FaultQueryController {
    @Autowired
    private FaultQueryService faultQueryService;

    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public PageResponse<FaultDetailResDTO> list(@RequestBody FaultReportPageReqDTO reqDTO) {
        return PageResponse.of(faultQueryService.list(reqDTO));
    }
}
