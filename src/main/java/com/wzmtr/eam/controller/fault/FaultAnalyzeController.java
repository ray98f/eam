package com.wzmtr.eam.controller.fault;

import com.wzmtr.eam.dto.req.fault.AnalyzeReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultAnalyzeDetailReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultAnalyzeUploadReqDTO;
import com.wzmtr.eam.dto.req.fault.FaultExamineReqDTO;
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

/**
 * 故障管理-故障分析
 * @author  Li.Wang
 * @version 1.0
 * @date 2023/08/15
 */
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

    @ApiOperation(value = "详情")
    @PostMapping("/detail")
    public DataResponse<AnalyzeResDTO> detail(@RequestBody FaultAnalyzeDetailReqDTO reqDTO) {
        return DataResponse.of(analyzeService.detail(reqDTO));
    }

    @ApiOperation(value = "附件上传")
    @PostMapping("/upload")
    public DataResponse<Void> update(@RequestBody FaultAnalyzeUploadReqDTO reqDTO) {
        // reqDTO
        analyzeService.upload(reqDTO);
        return DataResponse.success();
    }



    @ApiOperation(value = "导出")
    @GetMapping("/export")
    public DataResponse<AnalyzeResDTO> export(@RequestParam(required = false) @ApiParam("故障分析编号") String faultAnalysisNo,
                                              @RequestParam(required = false) @ApiParam("故障编号") String faultNo,
                                              @RequestParam(required = false) @ApiParam("故障工单编号") String faultWorkNo,
                                              HttpServletResponse response) {
        analyzeService.export(faultAnalysisNo, faultNo, faultWorkNo, response);
        return DataResponse.success();
    }

    @ApiOperation(value = "故障分析流程提交送审")
    @PostMapping("/submit")
    public DataResponse<String> submit(@RequestBody FaultExamineReqDTO reqDTO) {
        //com.baosight.wzplat.dm.fm.service.ServiceDMFM0008#submit
        analyzeService.submit(reqDTO);
        return DataResponse.success();
    }

    @ApiOperation(value = "故障分析流程审核")
    @PostMapping("/examine")
    public DataResponse<String> pass(@RequestBody FaultExamineReqDTO reqDTO) {
        //com.baosight.wzplat.dm.fm.service.ServiceDMFM0008#submit
        analyzeService.pass(reqDTO);
        return DataResponse.success();
    }
    @ApiOperation(value = "故障分析流程驳回")
    @PostMapping("/reject")
    public DataResponse<String> reject(@RequestBody FaultExamineReqDTO reqDTO) {
        //com.baosight.wzplat.dm.fm.service.ServiceDMFM0008#submit
        analyzeService.reject(reqDTO);
        return DataResponse.success();
    }
}
