package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.FaultDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultBaseNoReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultTrackSaveReqDTO;
import com.wzmtr.eam.dto.req.fault.TrackQueryReqDTO;
import com.wzmtr.eam.dto.res.fault.AnalyzeResDTO;
import com.wzmtr.eam.dto.res.fault.FaultDetailResDTO;
import com.wzmtr.eam.dto.res.fault.TrackQueryResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
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

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Li.Wang
 * Date: 2023/8/11 16:25
 */
@RestController
@RequestMapping("/fault/track/query")
@Api(tags = "故障管理-故障跟踪查询")
public class FaultTrackController {
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
    public DataResponse<TrackQueryResDTO> detail(@RequestBody FaultBaseNoReqDTO reqDTO) {
        return DataResponse.of(trackQueryService.trackDetail(reqDTO));
    }

    @ApiOperation(value = "跟踪单保存")
    @PostMapping("/save")
    public DataResponse<Void> save(@RequestBody FaultTrackSaveReqDTO reqDTO) {
       trackQueryService.save(reqDTO);
       return DataResponse.success();
    }

    @ApiOperation(value = "作废")
    @PostMapping("/cancellGenZ")
    public DataResponse<AnalyzeResDTO> cancellGenZ(@RequestBody BaseIdsEntity reqDTO) {
        // 		FAULT_TRACK_NO = #faultTrackNo# 故障跟踪查询 作废
        trackQueryService.cancellGenZ(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "导出")
    @PostMapping("/record/export")
    public void export(@RequestBody TrackQueryReqDTO reqDTO,
                       HttpServletResponse response) {
        // 		FAULT_TRACK_NO = #faultTrackNo#
        trackQueryService.export(reqDTO, response);
    }


}
