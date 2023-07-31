package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.WheelsetLathingReqDTO;
import com.wzmtr.eam.dto.res.WheelsetLathingResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.equipment.WheelsetLathingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/train/maintenance/wheelsetLathing")
@Api(tags = "设备管理-车辆维保台账-轮对镟修台账")
@Validated
public class WheelsetLathingController {

    @Resource
    private WheelsetLathingService wheelsetLathingService;

    @GetMapping("/page")
    @ApiOperation(value = "获取轮对镟修台账列表")
    public PageResponse<WheelsetLathingResDTO> pageWheelsetLathing(@RequestParam(required = false) @ApiParam("列车号") String trainNo,
                                                                   @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(wheelsetLathingService.pageWheelsetLathing(trainNo, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取轮对镟修台账详情")
    public DataResponse<WheelsetLathingResDTO> getWheelsetLathingDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(wheelsetLathingService.getWheelsetLathingDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增轮对镟修台账")
    public DataResponse<T> addWheelsetLathing(@RequestBody WheelsetLathingReqDTO wheelsetLathingReqDTO) {
        wheelsetLathingService.addWheelsetLathing(wheelsetLathingReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除轮对镟修台账")
    public DataResponse<T> deleteWheelsetLathing(@RequestBody BaseIdsEntity baseIdsEntity) {
        wheelsetLathingService.deleteWheelsetLathing(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入轮对镟修台账")
    public DataResponse<T> importWheelsetLathing(@RequestParam MultipartFile file) {
        wheelsetLathingService.importWheelsetLathing(file);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出轮对镟修台账")
    public void exportWheelsetLathing(@RequestParam(required = false) @ApiParam("列车号") String trainNo,
                                      HttpServletResponse response) {
        wheelsetLathingService.exportWheelsetLathing(trainNo, response);
    }

}
