package com.wzmtr.eam.controller.mea;

import com.wzmtr.eam.dto.req.bpmn.ExamineReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordDetailReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordReqDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.mea.SubmissionRecordService;
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
@RequestMapping("/mea/submission/record")
@Api(tags = "计量器具管理-检定记录")
@Validated
public class SubmissionRecordController {

    @Resource
    private SubmissionRecordService submissionRecordService;

    @GetMapping("/page")
    @ApiOperation(value = "获取检定记录列表")
    public PageResponse<SubmissionRecordResDTO> listSubmissionRecord(@RequestParam(required = false) @ApiParam("检测单号") String checkNo,
                                                                     @RequestParam(required = false) @ApiParam("计量器具检验计划号") String instrmPlanNo,
                                                                     @RequestParam(required = false) @ApiParam("记录状态") String recStatus,
                                                                     @RequestParam(required = false) @ApiParam("工作流实例ID") String workFlowInstId,
                                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(submissionRecordService.pageSubmissionRecord(checkNo, instrmPlanNo, recStatus, workFlowInstId, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取检定记录详情")
    public DataResponse<SubmissionRecordResDTO> getSubmissionRecordDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(submissionRecordService.getSubmissionRecordDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增检定记录")
    public DataResponse<T> addSubmissionRecord(@RequestBody SubmissionRecordReqDTO submissionRecordReqDTO) {
        submissionRecordService.addSubmissionRecord(submissionRecordReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "编辑检定记录")
    public DataResponse<T> modifySubmissionRecord(@RequestBody SubmissionRecordReqDTO submissionRecordReqDTO) {
        submissionRecordService.modifySubmissionRecord(submissionRecordReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除检定记录")
    public DataResponse<T> deleteSubmissionRecord(@RequestBody BaseIdsEntity baseIdsEntity) {
        submissionRecordService.deleteSubmissionRecord(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/submit")
    @ApiOperation(value = "提交检定记录")
    public DataResponse<T> submitSubmissionRecord(@RequestBody ExamineReqDTO examineReqDTO) throws Exception {
        submissionRecordService.submitSubmissionRecord(examineReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/examine")
    @ApiOperation(value = "审核检定记录")
    public DataResponse<T> examineSubmissionRecord(@RequestBody ExamineReqDTO examineReqDTO) {
        submissionRecordService.examineSubmissionRecord(examineReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出检定记录")
    public void exportSubmissionRecord(@RequestParam(required = false) @ApiParam("检测单号") String checkNo,
                                       @RequestParam(required = false) @ApiParam("计量器具检验计划号") String instrmPlanNo,
                                       @RequestParam(required = false) @ApiParam("记录状态") String recStatus,
                                       @RequestParam(required = false) @ApiParam("工作流实例ID") String workFlowInstId,
                                       HttpServletResponse response) {
        submissionRecordService.exportSubmissionRecord(checkNo, instrmPlanNo, recStatus, workFlowInstId, response);
    }

    @GetMapping("/detail/page")
    @ApiOperation(value = "获取检定记录明细列表")
    public PageResponse<SubmissionRecordDetailResDTO> pageSubmissionRecordDetail(@RequestParam(required = false) @ApiParam("检测记录表REC_ID") String testRecId,
                                                                                 @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(submissionRecordService.pageSubmissionRecordDetail(testRecId, pageReqDTO));
    }

    @GetMapping("/detail/detail")
    @ApiOperation(value = "获取检定记录明细详情")
    public DataResponse<SubmissionRecordDetailResDTO> getSubmissionRecordDetailDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(submissionRecordService.getSubmissionRecordDetailDetail(id));
    }

    @PostMapping("/detail/add")
    @ApiOperation(value = "新增检定记录明细")
    public DataResponse<T> addSubmissionRecordDetail(@RequestBody SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO) {
        submissionRecordService.addSubmissionRecordDetail(submissionRecordDetailReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/detail/modify")
    @ApiOperation(value = "编辑检定记录明细")
    public DataResponse<T> modifySubmissionRecordDetail(@RequestBody SubmissionRecordDetailReqDTO submissionRecordDetailReqDTO) {
        submissionRecordService.modifySubmissionRecordDetail(submissionRecordDetailReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/detail/delete")
    @ApiOperation(value = "删除检定记录明细")
    public DataResponse<T> deleteSubmissionRecordDetail(@RequestBody BaseIdsEntity baseIdsEntity) {
        submissionRecordService.deleteSubmissionRecordDetail(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/detail/export")
    @ApiOperation(value = "导出检定记录明细")
    public void exportSubmissionRecordDetail(@RequestParam(required = false) @ApiParam("检测记录表REC_ID") String testRecId,
                                             HttpServletResponse response) {
        submissionRecordService.exportSubmissionRecordDetail(testRecId, response);
    }
}
