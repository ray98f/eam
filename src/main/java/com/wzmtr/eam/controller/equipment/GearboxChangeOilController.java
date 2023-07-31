package com.wzmtr.eam.controller.equipment;

import com.wzmtr.eam.dto.req.GearboxChangeOilReqDTO;
import com.wzmtr.eam.dto.res.GearboxChangeOilResDTO;
import com.wzmtr.eam.dto.res.RegionResDTO;
import com.wzmtr.eam.entity.BaseIdsEntity;
import com.wzmtr.eam.entity.PageReqDTO;
import com.wzmtr.eam.entity.response.DataResponse;
import com.wzmtr.eam.entity.response.PageResponse;
import com.wzmtr.eam.service.equipment.GearboxChangeOilService;
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
@RequestMapping("/train/maintenance/gearboxChangeOil")
@Api(tags = "设备管理-车辆维保台账-齿轮箱换油台账")
@Validated
public class GearboxChangeOilController {

    @Resource
    private GearboxChangeOilService gearboxChangeOilService;

    @GetMapping("/page")
    @ApiOperation(value = "获取齿轮箱换油台账列表")
    public PageResponse<GearboxChangeOilResDTO> pageGearboxChangeOil(@RequestParam(required = false) @ApiParam("列车号") String trainNo,
                                                                     @Valid PageReqDTO pageReqDTO) {
        return PageResponse.of(gearboxChangeOilService.pageGearboxChangeOil(trainNo, pageReqDTO));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取齿轮箱换油台账详情")
    public DataResponse<GearboxChangeOilResDTO> getGearboxChangeOilDetail(@RequestParam @ApiParam("id") String id) {
        return DataResponse.of(gearboxChangeOilService.getGearboxChangeOilDetail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增齿轮箱换油台账")
    public DataResponse<T> addGearboxChangeOil(@RequestBody GearboxChangeOilReqDTO gearboxChangeOilReqDTO) {
        gearboxChangeOilService.addGearboxChangeOil(gearboxChangeOilReqDTO);
        return DataResponse.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除齿轮箱换油台账")
    public DataResponse<T> deleteGearboxChangeOil(@RequestBody BaseIdsEntity baseIdsEntity) {
        gearboxChangeOilService.deleteGearboxChangeOil(baseIdsEntity);
        return DataResponse.success();
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入齿轮箱换油台账")
    public DataResponse<T> importGearboxChangeOil(@RequestParam MultipartFile file) {
        gearboxChangeOilService.importGearboxChangeOil(file);
        return DataResponse.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出齿轮箱换油台账")
    public void exportGearboxChangeOil(@RequestParam(required = false) @ApiParam("列车号") String trainNo,
                                       HttpServletResponse response) {
        gearboxChangeOilService.exportGearboxChangeOil(trainNo, response);
    }

}
