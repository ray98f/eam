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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 故障管理-故障跟踪工单
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/12/06
 */
@RestController
@RequestMapping("/fault/track")
@Api(tags = "故障管理-故障跟踪工单")
public class FaultTrackWorkController {

    @Autowired
    private TrackService trackService;

    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public PageResponse<TrackResDTO> list(@RequestBody TrackReqDTO reqDTO) {
        return PageResponse.of(trackService.list(reqDTO));
    }

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

    @ApiOperation(value = "故障跟踪下达")
    @PostMapping("/fault/track/transmit")
    public DataResponse<String> transmit(@RequestBody TrackTransmitReqDTO reqDTO) {
        trackService.transmit(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "故障跟踪送审")
    @PostMapping("/fault/track/commit")
    public DataResponse<String> commit(@RequestBody FaultExamineReqDTO reqDTO) {
        trackService.commit(reqDTO);
        return DataResponse.success();
    }


    @ApiOperation(value = "审核通过")
    @PostMapping("/fault/track/examine")
    public DataResponse<String> pass(@RequestBody FaultExamineReqDTO reqDTO) {
        trackService.pass(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "故障跟踪驳回")
    @PostMapping("/reject")
    public DataResponse<String> cancel(@RequestBody FaultExamineReqDTO reqDTO) {
        trackService.returns(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "故障跟踪工单导出")
    @PostMapping("/fault/track/export")
    public void export(@RequestBody TrackExportReqDTO reqDTO, HttpServletResponse response) throws IOException {
        trackService.export(reqDTO,response);
    }
}
