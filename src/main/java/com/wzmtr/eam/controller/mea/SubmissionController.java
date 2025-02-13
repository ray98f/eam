package com.wzmtr.eam.controller.mea;

import com.wzmtr.eam.dto.req.mea.SubmissionDetailReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionListReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionReqDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionDetailResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.mea.SubmissionService;
import com.wzmtr.eam.utils.StringUtils;
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
import java.io.IOException;
import java.util.Objects;

/**
 * 预防性检修管理-送检单
 * @author  Ray
 * @version 1.0
 * @date 2023/08/17
 */
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
    public DataResponse<T> submitSubmission(@RequestBody SubmissionReqDTO submissionReqDTO) throws Exception {
        submissionService.submitSubmission(submissionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/examine")
    @ApiOperation(value = "审核送检单")
    public DataResponse<T> examineSubmission(@RequestBody SubmissionReqDTO submissionReqDTO) {
        submissionService.examineSubmission(submissionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出送检单")
    public void exportSubmission(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        submissionService.exportSubmission(baseIdsEntity.getIds(), response);
    }

    @GetMapping("/detail/page")
    @ApiOperation(value = "获取送检单明细列表")
    public PageResponse<SubmissionDetailResDTO> pageSubmissionDetail(@RequestParam(required = false) @ApiParam("检测单号") String sendVerifyNo,
                                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(submissionService.pageSubmissionDetail(sendVerifyNo, pageReqDTO));
    }

    @GetMapping("/detail/detail")
    @ApiOperation(value = "获取送检单明细详情")
    public DataResponse<SubmissionDetailResDTO> getSubmissionDetailDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(submissionService.getSubmissionDetailDetail(id));
    }

    @PostMapping("/detail/add")
    @ApiOperation(value = "新增送检单明细")
    public DataResponse<T> addSubmissionDetail(@RequestBody SubmissionDetailReqDTO submissionDetailReqDTO) {
        submissionService.addSubmissionDetail(submissionDetailReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/detail/modify")
    @ApiOperation(value = "编辑送检单明细")
    public DataResponse<T> modifySubmissionDetail(@RequestBody SubmissionDetailReqDTO submissionDetailReqDTO) {
        submissionService.modifySubmissionDetail(submissionDetailReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/detail/delete")
    @ApiOperation(value = "删除送检单明细")
    public DataResponse<T> deleteSubmissionDetail(@RequestBody BaseIdsEntity baseIdsEntity) {
        submissionService.deleteSubmissionDetail(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/detail/export")
    @ApiOperation(value = "导出送检单明细")
    public void exportSubmissionDetail(@RequestParam(required = false) @ApiParam("检测单号") String sendVerifyNo,
                                       HttpServletResponse response) throws IOException {
        submissionService.exportSubmissionDetail(sendVerifyNo, response);
    }
}
