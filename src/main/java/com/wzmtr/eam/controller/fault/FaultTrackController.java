package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.TrackCloseReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackRepairReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackReportReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackReqDTO;
import com.wzmtr.eam.dto.res.fault.TrackResDTO;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.TrackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/report")
    public DataResponse<TrackResDTO> report(@RequestBody TrackReportReqDTO reqDTO) {
        return DataResponse.of(trackService.report(reqDTO));
    }
    @ApiOperation(value = "关闭")
    @GetMapping("/close")
    public DataResponse<TrackResDTO> close(@RequestBody TrackCloseReqDTO reqDTO) {
        return DataResponse.of(trackService.close(reqDTO));
    }

    @ApiOperation(value = "派工")
    @PostMapping("/repair")
    public DataResponse<TrackResDTO> repair(@RequestBody TrackRepairReqDTO reqDTO) {
        trackService.repair(reqDTO);
        return DataResponse.success();
    }
    // @ApiOperation(value = "安全/质量/消防/-检查问题单删除")
    // @PostMapping("/record/delete")
    // public DataResponse<SecureCheckRecordListResDTO> delete(@RequestBody BaseIdsEntity ids) {
    //     trackService.delete(ids);
    //     return DataResponse.success();
    // }


}
