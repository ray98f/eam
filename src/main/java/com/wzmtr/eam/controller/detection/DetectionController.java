package com.wzmtr.eam.controller.detection;

import com.wzmtr.eam.dto.req.detection.DetectionDetailReqDTO;
import com.wzmtr.eam.dto.req.detection.DetectionReqDTO;
import com.wzmtr.eam.dto.res.detection.DetectionDetailResDTO;
import com.wzmtr.eam.dto.res.detection.DetectionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.detection.DetectionService;
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
import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/detection")
@Api(tags = "特种设备管理-检测记录")
@Validated
public class DetectionController {

    @Resource
    private DetectionService detectionService;

    @GetMapping("/page")
    @ApiOperation(value = "获取检测记录列表")
    public PageResponse<DetectionResDTO> pageDetection(@RequestParam(required = false) @ApiParam("检测单号") String checkNo,
                                                       @RequestParam(required = false) @ApiParam("委托单号") String sendVerifyNo,
                                                       @RequestParam(required = false) @ApiParam("编制部门") String editDeptCode,
                                                       @RequestParam(required = false) @ApiParam("检测单状态") String recStatus,
                                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(detectionService.pageDetection(checkNo, sendVerifyNo, editDeptCode, recStatus, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取检测记录详情")
    public DataResponse<DetectionResDTO> getDetectionDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(detectionService.getDetectionDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增检测记录")
    public DataResponse<T> addDetection(@RequestBody DetectionReqDTO detectionReqDTO) {
        detectionService.addDetection(detectionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/modify")
    @ApiOperation(value = "编辑检测记录")
    public DataResponse<T> modifyDetection(@RequestBody DetectionReqDTO detectionReqDTO) {
        detectionService.modifyDetection(detectionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除检测记录")
    public DataResponse<T> deleteDetection(@RequestBody BaseIdsEntity baseIdsEntity) {
        detectionService.deleteDetection(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/submit")
    @ApiOperation(value = "提交检测记录")
    public DataResponse<T> submitDetection(@RequestBody DetectionReqDTO detectionReqDTO) throws Exception {
        detectionService.submitDetection(detectionReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/examine")
    @ApiOperation(value = "审核检测记录")
    public DataResponse<T> examineDetection(@RequestBody DetectionReqDTO detectionReqDTO) {
        detectionService.examineDetection(detectionReqDTO);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出检测记录")
    public void exportDetection(@RequestParam(required = false) @ApiParam("检测单号") String checkNo,
                                @RequestParam(required = false) @ApiParam("委托单号") String sendVerifyNo,
                                @RequestParam(required = false) @ApiParam("编制部门") String editDeptCode,
                                @RequestParam(required = false) @ApiParam("检测单状态") String recStatus,
                                HttpServletResponse response) throws IOException {
        detectionService.exportDetection(checkNo, sendVerifyNo, editDeptCode, recStatus, response);
    }

    @GetMapping("/detail/page")
    @ApiOperation(value = "获取检测记录明细列表")
    public PageResponse<DetectionDetailResDTO> pageDetectionDetail(@RequestParam(required = false) @ApiParam("检测记录表REC_ID") String testRecId,
                                                                   @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(detectionService.pageDetectionDetail(testRecId, pageReqDTO));
    }

    @GetMapping("/detail/detail")
    @ApiOperation(value = "获取检测记录明细详情")
    public DataResponse<DetectionDetailResDTO> getDetectionDetailDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(detectionService.getDetectionDetailDetail(id));
    }

    @PostMapping("/detail/add")
    @ApiOperation(value = "新增检测记录明细")
    public DataResponse<T> addDetectionDetail(@RequestBody DetectionDetailReqDTO detectionDetailReqDTO) throws ParseException {
        detectionService.addDetectionDetail(detectionDetailReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/detail/modify")
    @ApiOperation(value = "编辑检测记录明细")
    public DataResponse<T> modifyDetectionDetail(@RequestBody DetectionDetailReqDTO detectionDetailReqDTO) throws ParseException {
        detectionService.modifyDetectionDetail(detectionDetailReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/detail/delete")
    @ApiOperation(value = "删除检测记录明细")
    public DataResponse<T> deleteDetectionDetail(@RequestBody BaseIdsEntity baseIdsEntity) {
        detectionService.deleteDetectionDetail(baseIdsEntity);
        return DataResponse.success();
    }

    @GetMapping("/detail/export")
    @ApiOperation(value = "导出检测记录明细")
    public void exportDetectionDetail(@RequestParam(required = false) @ApiParam("检测记录表REC_ID") String testRecId,
                                      HttpServletResponse response) throws IOException {
        detectionService.exportDetectionDetail(testRecId, response);
    }
}
