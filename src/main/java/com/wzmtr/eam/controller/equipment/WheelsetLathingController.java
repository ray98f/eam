package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.equipment.WheelsetLathingReqDTO;
import com.wzmtr.eam.dto.res.equipment.WheelsetLathingResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.enums.ErrorCode;
import com.wzmtr.eam.exception.CommonException;
import com.wzmtr.eam.service.equipment.WheelsetLathingService;
import com.wzmtr.eam.utils.StringUtils;
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
import java.io.IOException;
import java.util.Objects;

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
                                                                   @RequestParam(required = false) @ApiParam("车厢号") String carriageNo,
                                                                   @RequestParam(required = false) @ApiParam("镟修轮对车轴") String axleNo,
                                                                   @RequestParam(required = false) @ApiParam("镟修轮对号") String wheelNo,
                                                                   @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(wheelsetLathingService.pageWheelsetLathing(trainNo, carriageNo, axleNo, wheelNo, pageReqDTO));
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

    @PostMapping("/export")
    @ApiOperation(value = "导出轮对镟修台账")
    public void exportWheelsetLathing(@RequestBody BaseIdsEntity baseIdsEntity, HttpServletResponse response) throws IOException {
        if (Objects.isNull(baseIdsEntity) || StringUtils.isEmpty(baseIdsEntity.getIds())) {
            throw new CommonException(ErrorCode.NORMAL_ERROR, "请先勾选后导出");
        }
        wheelsetLathingService.exportWheelsetLathing(baseIdsEntity.getIds(), response);
    }

}
