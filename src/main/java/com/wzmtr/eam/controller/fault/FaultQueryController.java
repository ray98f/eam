package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.*;
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

    @ApiOperation(value = "派工")
    @PostMapping("/send/work")
    public DataResponse<String> sendWork(@RequestBody FaultSendWorkReqDTO reqDTO) {
        // faultWorkNo
        faultQueryService.sendWork(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "设调确认")
    @PostMapping("/eqCheck")
    public DataResponse<String> eqCheck(@RequestBody FaultEqCheckReqDTO reqDTO) throws Exception {
        // faultWorkNo
        faultQueryService.eqCheck(reqDTO);
        return DataResponse.success();
    }


    @ApiOperation(value = "导出")
    @GetMapping("/export")
    public void export(@RequestParam FaultExportReqDTO reqDTO, HttpServletResponse response) {
        // faultWorkNo
        faultQueryService.export(reqDTO, response);
    }

    @ApiOperation(value = "完工确认")
    @PostMapping("/finishConfirm")
    public DataResponse<String> finishConfirm(@RequestBody FaultFinishConfirmReqDTO reqDTO) {
        // faultWorkNo
        faultQueryService.finishConfirm(reqDTO);
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

    @ApiOperation(value = "故障跟踪表单提交")
    @PostMapping("/fault/track/submit")
    public DataResponse<String> submit(@RequestBody FaultSubmitReqDTO reqDTO) {
        // faultWorkNo
        faultQueryService.submit(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "操作前对选中的作前置校验")
    @PostMapping("/fault/track/compareRows")
    public DataResponse<Boolean> compareRows(@RequestBody CompareRowsReqDTO reqDTO) {
        // faultWorkNo
        return DataResponse.of(faultQueryService.compareRows(reqDTO));
    }

    @ApiOperation(value = "故障跟踪表单驳回")
    @PostMapping("/fault/track/cancel")
    public DataResponse<String> cancel(@RequestBody FaultSubmitReqDTO reqDTO) {
        // faultWorkNo
        faultQueryService.returns(reqDTO);
        return DataResponse.success();
    }
    // @ApiOperation(value = "故障对象确认")
    // @PostMapping("/fault/track/cancel")
    // public DataResponse<String> cancel(@RequestBody FaultSubmitReqDTO reqDTO) {
    //     // faultWorkNo
    //     faultQueryService.returns(reqDTO);
    //     return DataResponse.success();
    // }


}
