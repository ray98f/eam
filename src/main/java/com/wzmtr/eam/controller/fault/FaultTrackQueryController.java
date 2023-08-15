package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.entity.SidEntity;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.TrackQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 16:25
 */
@RestController
@RequestMapping("/fault/track/query")
@Api(tags = "故障管理-故障跟踪查询")
public class FaultTrackQueryController {
    @Autowired
    private TrackQueryService trackQueryService;

    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public PageResponse<TrackQueryResDTO> list(@RequestBody TrackQueryReqDTO reqDTO) {
        return PageResponse.of(trackQueryService.list(reqDTO));
    }
    @ApiOperation(value = "故障编号详情")
    @PostMapping("/fault/detail")
    public DataResponse<FaultDetailResDTO> faultDetail(@RequestBody FaultDetailReqDTO reqDTO) {
        return DataResponse.of(trackQueryService.faultDetail(reqDTO));
    }

    @ApiOperation(value = "跟踪单详情")
    @PostMapping("/track/detail")
    public DataResponse<TrackQueryResDTO> detail(@RequestBody SidEntity reqDTO ) {
        return DataResponse.of(trackQueryService.trackDetail(reqDTO));
    }
}
