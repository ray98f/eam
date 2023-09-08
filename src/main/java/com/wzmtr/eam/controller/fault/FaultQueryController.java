package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.ConstructionResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.FaultQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Li.Wang
 * Date: 2023/8/16 20:36
 */
@RestController
@RequestMapping("/fault/query")
@Api(tags = "故障管理-故障查询")
public class FaultQueryController {
    @Autowired
    private FaultQueryService faultQueryService;

    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public PageResponse<FaultDetailResDTO> list(@RequestBody FaultQueryReqDTO reqDTO) {
        return PageResponse.of(faultQueryService.list(reqDTO));
    }

    @ApiOperation(value = "查询订单状态")
    @PostMapping("/queryOrderStatus")
    public DataResponse<String> queryOrderStatus(@RequestBody SidEntity reqDTO) {
        return DataResponse.of(faultQueryService.queryOrderStatus(reqDTO));
    }

    @ApiOperation(value = "下发")
    @PostMapping("/issue")
    public DataResponse<String> issue(@RequestBody FaultDetailReqDTO reqDTO) {
        // faultWorkNo
        faultQueryService.issue(reqDTO);
        return DataResponse.success();
    }
    @ApiOperation(value = "导出")
    @GetMapping("/issue")
    public DataResponse<String> export(@RequestBody FaultQueryReqDTO reqDTO,HttpServletResponse response) {
        // faultWorkNo
        faultQueryService.export(reqDTO,response);
        return DataResponse.success();
    }
        @ApiOperation(value = "施工计划")
    @PostMapping("/construction")
    public PageResponse<ConstructionResDTO> construction(@RequestBody FaultQueryReqDTO reqDTO) {
        // faultWorkNo
        return PageResponse.of(faultQueryService.construction(reqDTO));
    }
    @ApiOperation(value = "请销点")
    @PostMapping("/cancellation")
    public PageResponse<ConstructionResDTO> cancellation(@RequestBody FaultQueryReqDTO reqDTO) {
        // faultWorkNo
        return PageResponse.of(faultQueryService.cancellation(reqDTO));
    }

    @ApiOperation(value = "故障跟踪下达")
    @PostMapping("/fault/track/transmit")
    public DataResponse<String> transmit(@RequestBody FaultQueryReqDTO reqDTO) {
        // faultWorkNo
        faultQueryService.transmit(reqDTO);
        return DataResponse.success();
    }
    @ApiOperation(value = "故障跟踪送审")
    @PostMapping("/fault/track/submit")
    public DataResponse<String> submit(@RequestBody FaultQueryReqDTO reqDTO) {
        // faultWorkNo
        faultQueryService.submit(reqDTO);
        return DataResponse.success();
    }
}
