package com.wzmtr.eam.controller.mea;

import com.wzmtr.eam.dto.req.mea.SubmissionRecordDetailReqDTO;
import com.wzmtr.eam.dto.req.mea.SubmissionRecordReqDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordDetailResDTO;
import com.wzmtr.eam.dto.res.mea.SubmissionRecordResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.mea.SubmissionRecordService;
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
 * 计量器具管理-检定记录
 * @author  Ray
 * @version 1.0
 * @date 2023/08/18
 */
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
                                                                     @RequestParam(required = false) @ApiParam("检定记录状态") String recStatus,
                                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(submissionRecordService.pageSubmissionRecord(checkNo, recStatus, pageReqDTO));
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
    public DataResponse<T> submitSubmissionRecord(@RequestBody SubmissionRecordReqDTO submissionRecordReqDTO) throws Exception {
        submissionRecordService.submitSubmissionRecord(submissionRecordReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/examine")
    @ApiOperation(value = "审核检定记录")
    public DataResponse<T> examineSubmissionRecord(@RequestBody SubmissionRecordReqDTO submissionRecordReqDTO) {
        submissionRecordService.examineSubmissionRecord(submissionRecordReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出检定记录")
    public void exportSubmissionRecord(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        submissionRecordService.exportSubmissionRecord(baseIdsEntity.getIds(), response);
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

    @PostMapping("/detail/export")
    @ApiOperation(value = "导出检定记录明细")
    public void exportSubmissionRecordDetail(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        submissionRecordService.exportSubmissionRecordDetail(baseIdsEntity.getIds(), response);
    }
}
