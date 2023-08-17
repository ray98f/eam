package com.wzmtr.eam.controller.mea;

import com.wzmtr.eam.dto.req.SubmissionListReqDTO;
import com.wzmtr.eam.dto.req.SubmissionReqDTO;
import com.wzmtr.eam.dto.res.SubmissionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.mea.SubmissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/mea/submission")
@Api(tags = "计量器具管理-送检单")
@Validated
public class SubmissionController {

    @Resource
    private SubmissionService submissionService;

    @GetMapping("/page")
    @ApiOperation(value = "获取送检单列表")
    public PageResponse<SubmissionResDTO> listSubmission(SubmissionListReqDTO submissionListReqDTO, @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(submissionService.pageSubmission(submissionListReqDTO, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取送检单详情")
    public DataResponse<SubmissionResDTO> getSubmissionDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(submissionService.getSubmissionDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增送检单")
    public DataResponse<T> addSubmission(@RequestBody SubmissionReqDTO submissionReqDTO) {
        submissionService.addSubmission(submissionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "编辑送检单")
    public DataResponse<T> modifySubmission(@RequestBody SubmissionReqDTO submissionReqDTO) {
        submissionService.modifySubmission(submissionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除送检单")
    public DataResponse<T> deleteSubmission(@RequestBody BaseIdsEntity baseIdsEntity) {
        submissionService.deleteSubmission(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/submit")
    @ApiOperation(value = "提交送检单")
    public DataResponse<T> submitSubmission(@RequestBody SubmissionReqDTO submissionReqDTO) {
        submissionService.submitSubmission(submissionReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出送检单")
    public void exportSubmission(SubmissionListReqDTO submissionListReqDTO, HttpServletResponse response) {
        submissionService.exportSubmission(submissionListReqDTO, response);
    }
}
