package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.AnalyzeReqDTO;
import com.wzmtr.eam.dto.res.fault.AnalyzeResDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.fault.AnalyzeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/fault/analyze")
@Api(tags = "故障管理-故障分析")
public class FaultAnalyzeController {

    @Autowired
    private AnalyzeService analyzeService;

    @ApiOperation(value = "列表")
    @PostMapping("/list")
    public PageResponse<AnalyzeResDTO> list(@RequestBody AnalyzeReqDTO reqDTO) {
        return PageResponse.of(analyzeService.list(reqDTO));
    }
    // @ApiOperation(value = "详情")
    // @PostMapping("/detail")
    // public DataResponse<TrackResDTO> detail(@RequestBody SidEntity reqDTO ) {
    //     return DataResponse.of(trackService.detail(reqDTO));
    // }


    @ApiOperation(value = "导出")
    @GetMapping("/export")
    public DataResponse<AnalyzeResDTO> export(@RequestParam(required = false) @ApiParam("故障分析编号") String faultAnalysisNo,
                                              @RequestParam(required = false) @ApiParam("故障编号") String faultNo,
                                              @RequestParam(required = false) @ApiParam("故障工单编号") String faultWorkNo,
                                              HttpServletResponse response) {
        analyzeService.export(faultAnalysisNo, faultNo, faultWorkNo, response);
        return DataResponse.success();
    }


}
