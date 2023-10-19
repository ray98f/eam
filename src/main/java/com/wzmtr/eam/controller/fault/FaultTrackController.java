package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.*;
import com.wzmtr.eam.dto.res.fault.TrackResDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.TrackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fault/track")
@Api(tags = "故障管理-故障跟踪工单")
public class FaultTrackController {

    @Autowired
    private TrackService trackService;

    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public PageResponse<TrackResDTO> list(@RequestBody TrackReqDTO reqDTO) {
        return PageResponse.of(trackService.list(reqDTO));
    }
    // @ApiOperation(value = "详情")
    // @PostMapping("/detail")
    // public DataResponse<TrackResDTO> detail(@RequestBody SidEntity reqDTO ) {
    //     return DataResponse.of(trackService.detail(reqDTO));
    // }

    @ApiOperation(value = "报告")
    @PostMapping("/report")
    public DataResponse<TrackResDTO> report(@RequestBody TrackReportReqDTO reqDTO) {
        trackService.report(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "关闭")
    @PostMapping("/close")
    public DataResponse<TrackResDTO> close(@RequestBody TrackCloseReqDTO reqDTO) {
        trackService.close(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "派工")
    @PostMapping("/repair")
    public DataResponse<TrackResDTO> repair(@RequestBody TrackRepairReqDTO reqDTO) {
        trackService.repair(reqDTO);
        return DataResponse.success();
    }


    @ApiOperation(value = "故障跟踪表单驳回")
    @PostMapping("/cancel")
    public DataResponse<String> cancel(@RequestBody FaultSubmitReqDTO reqDTO) {
        // faultWorkNo
        trackService.returns(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "故障跟踪下达")
    @PostMapping("/fault/track/transmit")
    public DataResponse<String> transmit(@RequestBody FaultQueryReqDTO reqDTO) {
        // faultWorkNo
        trackService.transmit(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "故障跟踪表单提交")
    @PostMapping("/fault/track/submit")
    public DataResponse<String> submit(@RequestBody FaultSubmitReqDTO reqDTO) {
        // faultWorkNo
        trackService.submit(reqDTO);
        return DataResponse.success();
    }
}
