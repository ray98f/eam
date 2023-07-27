package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.TrainMileReqDTO;
import com.wzmtr.eam.dto.res.EquipmentResDTO;
import com.wzmtr.eam.dto.res.TrainMileResDTO;
import com.wzmtr.eam.dto.res.TrainMileageResDTO;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.equipment.TrainService;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/train")
@Api(tags = "设备管理-车辆行走里程台账")
@Validated
public class TrainController {

    @Resource
    private TrainService trainService;

    @GetMapping("/mile/page")
    @ApiOperation(value = "获取车辆行走里程台账列表")
    public PageResponse<TrainMileResDTO> pageTrainMile(@RequestParam(required = false) @ApiParam("设备编码") String equipCode,
                                                       @RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                                       @RequestParam(required = false) @ApiParam("线路编号") String originLineNo,
                                                       @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(trainService.pageTrainMile(equipCode, equipName, originLineNo, pageReqDTO));
    }

    @GetMapping("/mile/detail")
    @ApiOperation(value = "获取车辆行走里程台账详情")
    public DataResponse<TrainMileResDTO> getTrainMileDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(trainService.getTrainMileDetail(id));
    }

    @GetMapping("/mile/export")
    @ApiOperation(value = "导出车辆行走里程台账")
    public void exportTrainMile(@RequestParam(required = false) @ApiParam("设备编码") String equipCode,
                                @RequestParam(required = false) @ApiParam("设备名称") String equipName,
                                @RequestParam(required = false) @ApiParam("线路编号") String originLineNo,
                                HttpServletResponse response) {
        trainService.exportTrainMile(equipCode, equipName, originLineNo, response);
    }

    @PostMapping("/mile/modify")
    @ApiOperation(value = "更新车辆行走里程台账")
    public DataResponse<T> modifyTrainMile(@RequestBody List<TrainMileReqDTO> list) {
        trainService.modifyTrainMile(list);
        return DataResponse.success();
    }

    @GetMapping("/mileage/page")
    @ApiOperation(value = "获取车辆行走里程历史列表")
    public PageResponse<TrainMileageResDTO> pageTrainMileage(@RequestParam(required = false) @ApiParam("开始时间") String startTime,
                                                             @RequestParam(required = false) @ApiParam("结束时间") String endTime,
                                                             @RequestParam(required = false) @ApiParam("设备编号") String equipCode,
                                                             @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(trainService.pageTrainMileage(startTime, endTime, equipCode, pageReqDTO));
    }

    @GetMapping("/mileage/detail")
    @ApiOperation(value = "获取车辆行走里程历史详情")
    public DataResponse<TrainMileageResDTO> getTrainMileageDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(trainService.getTrainMileageDetail(id));
    }

    @GetMapping("/mileage/export")
    @ApiOperation(value = "导出车辆行走里程历史列表")
    public void exportTrainMileage(@RequestParam(required = false) @ApiParam("开始时间") String startTime,
                                   @RequestParam(required = false) @ApiParam("结束时间") String endTime,
                                   @RequestParam(required = false) @ApiParam("设备编号") String equipCode,
                                   HttpServletResponse response) {
        trainService.exportTrainMileage(startTime, endTime, equipCode, response);
    }

}
