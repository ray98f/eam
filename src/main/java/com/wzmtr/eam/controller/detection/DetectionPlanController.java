package com.wzmtr.eam.controller.detection;

import com.wzmtr.eam.dto.req.detection.DetectionPlanDetailReqDTO;
import com.wzmtr.eam.dto.req.detection.DetectionPlanReqDTO;
import com.wzmtr.eam.dto.res.detection.DetectionPlanDetailResDTO;
import com.wzmtr.eam.dto.res.detection.DetectionPlanResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.detection.DetectionPlanService;
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

/**
 * 特种设备管理-检测计划
 * @author  Ray
 * @version 1.0
 * @date 2023/08/02
 */
@Slf4j
@RestController
@RequestMapping("/detectionPlan")
@Api(tags = "特种设备管理-检测计划")
@Validated
public class DetectionPlanController {

    @Resource
    private DetectionPlanService detectionPlanService;

    @GetMapping("/page")
    @ApiOperation(value = "获取检测计划列表")
    public PageResponse<DetectionPlanResDTO> pageDetectionPlan(@RequestParam(required = false) @ApiParam("特种设备定检计划号") String instrmPlanNo,
                                                               @RequestParam(required = false) @ApiParam("计划状态") String planStatus,
                                                               @RequestParam(required = false) @ApiParam("编制部门") String editDeptCode,
                                                               @RequestParam(required = false) @ApiParam("特种设备分类代码") String assetKindCode,
                                                               @RequestParam(required = false) @ApiParam("定检计划时期") String planPeriodMark,
                                                               @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(detectionPlanService.pageDetectionPlan(instrmPlanNo, planStatus, editDeptCode, assetKindCode, planPeriodMark, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取检测计划详情")
    public DataResponse<DetectionPlanResDTO> getDetectionPlanDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(detectionPlanService.getDetectionPlanDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增检测计划")
    public DataResponse<T> addDetectionPlan(@RequestBody DetectionPlanReqDTO detectionPlanReqDTO) {
        detectionPlanService.addDetectionPlan(detectionPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "编辑检测计划")
    public DataResponse<T> modifyDetectionPlan(@RequestBody DetectionPlanReqDTO detectionPlanReqDTO) {
        detectionPlanService.modifyDetectionPlan(detectionPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除检测计划")
    public DataResponse<T> deleteDetectionPlan(@RequestBody BaseIdsEntity baseIdsEntity) {
        detectionPlanService.deleteDetectionPlan(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/submit")
    @ApiOperation(value = "提交检测计划")
    public DataResponse<T> submitDetectionPlan(@RequestBody DetectionPlanReqDTO detectionPlanReqDTO) throws Exception {
        detectionPlanService.submitDetectionPlan(detectionPlanReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/examine")
    @ApiOperation(value = "审核检测计划")
    public DataResponse<T> examineDetectionPlan(@RequestBody DetectionPlanReqDTO detectionPlanReqDTO) {
        detectionPlanService.examineDetectionPlan(detectionPlanReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出检测计划")
    public void exportDetectionPlan(@RequestParam(required = false) @ApiParam("特种设备定检计划号") String instrmPlanNo,
                                    @RequestParam(required = false) @ApiParam("计划状态") String planStatus,
                                    @RequestParam(required = false) @ApiParam("编制部门") String editDeptCode,
                                    @RequestParam(required = false) @ApiParam("特种设备分类代码") String assetKindCode,
                                    @RequestParam(required = false) @ApiParam("定检计划时期") String planPeriodMark,
                                    HttpServletResponse response) throws IOException {
        detectionPlanService.exportDetectionPlan(instrmPlanNo, planStatus, editDeptCode, assetKindCode, planPeriodMark, response);
    }

    @GetMapping("/detail/page")
    @ApiOperation(value = "获取检测计划明细列表")
    public PageResponse<DetectionPlanDetailResDTO> pageDetectionPlanDetail(@RequestParam(required = false) @ApiParam("特种设备定检计划号") String instrmPlanNo,
                                                                           @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(detectionPlanService.pageDetectionPlanDetail(instrmPlanNo, pageReqDTO));
    }

    @GetMapping("/detail/detail")
    @ApiOperation(value = "获取检测计划明细详情")
    public DataResponse<DetectionPlanDetailResDTO> getDetectionPlanDetailDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(detectionPlanService.getDetectionPlanDetailDetail(id));
    }

    @PostMapping("/detail/add")
    @ApiOperation(value = "新增检测计划明细")
    public DataResponse<T> addDetectionPlanDetail(@RequestBody DetectionPlanDetailReqDTO detectionPlanDetailReqDTO) {
        detectionPlanService.addDetectionPlanDetail(detectionPlanDetailReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/detail/modify")
    @ApiOperation(value = "编辑检测计划明细")
    public DataResponse<T> modifyDetectionPlanDetail(@RequestBody DetectionPlanDetailReqDTO detectionPlanDetailReqDTO) {
        detectionPlanService.modifyDetectionPlanDetail(detectionPlanDetailReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/detail/delete")
    @ApiOperation(value = "删除检测计划明细")
    public DataResponse<T> deleteDetectionPlanDetail(@RequestBody BaseIdsEntity baseIdsEntity) {
        detectionPlanService.deleteDetectionPlanDetail(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/detail/export")
    @ApiOperation(value = "导出检测计划明细")
    public void exportDetectionPlanDetail(@RequestParam(required = false) @ApiParam("特种设备定检计划号") String instrmPlanNo,
                                          HttpServletResponse response) throws IOException {
        detectionPlanService.exportDetectionPlanDetail(instrmPlanNo, response);
    }
}
